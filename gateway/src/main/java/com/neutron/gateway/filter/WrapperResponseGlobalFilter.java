package com.neutron.gateway.filter;

import cn.hutool.core.text.CharSequenceUtil;
import com.alibaba.nacos.shaded.com.google.common.base.Joiner;
import com.alibaba.nacos.shaded.com.google.common.base.Throwables;
import com.alibaba.nacos.shaded.com.google.common.collect.Lists;
import com.neutron.common.exception.BusinessException;
import com.neutron.common.response.ErrorCode;
import com.neutron.common.service.DubboUserInterfaceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.ORIGINAL_RESPONSE_CONTENT_TYPE_ATTR;

/**
 * 全局响应日志过滤器
 * @author zzs
 * @date 2023/4/12 13:50
 */
@Slf4j
@Component
public class WrapperResponseGlobalFilter implements GlobalFilter, Ordered {
    private static final Joiner joiner = Joiner.on("");

    @DubboReference
    private DubboUserInterfaceService dubboUserInterfaceService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpResponse originalResponse = exchange.getResponse();
        DataBufferFactory bufferFactory = originalResponse.bufferFactory();
        ServerHttpResponseDecorator response = new ServerHttpResponseDecorator(originalResponse) {
            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                if (getStatusCode().equals(HttpStatus.OK) && body instanceof Flux) {
                    // 获取ContentType，判断是否返回JSON格式数据
                    String originalResponseContentType = exchange.getAttribute(ORIGINAL_RESPONSE_CONTENT_TYPE_ATTR);
                    if (CharSequenceUtil.isNotBlank(originalResponseContentType) && originalResponseContentType.contains("application/json")) {
                        Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                        //（返回数据内如果字符串过大，默认会切割）解决返回体分段传输
                        return super.writeWith(fluxBody.buffer().map(dataBuffers -> {
                            List<String> list = Lists.newArrayList();
                            dataBuffers.forEach(dataBuffer -> {
                                try {
                                    byte[] content = new byte[dataBuffer.readableByteCount()];
                                    dataBuffer.read(content);
                                    DataBufferUtils.release(dataBuffer);
                                    list.add(new String(content, StandardCharsets.UTF_8));
                                } catch (Exception e) {
                                    log.info("加载Response字节流异常，失败原因：{}", Throwables.getStackTraceAsString(e));
                                }
                            });
                            String userId = originalResponse.getHeaders().getFirst("userId");
                            String interfaceId = originalResponse.getHeaders().getFirst("interfaceId");
                            try {
                                if (userId == null || interfaceId == null) {
                                    throw new BusinessException(ErrorCode.SYSTEM_ERROR, "没有获取到用户id和接口id");
                                }
                                dubboUserInterfaceService.addInvokeNum(Long.parseLong(interfaceId), Long.parseLong(userId));
                            } catch (Exception e) {
                                log.error("远程调用增加接口调用次数的方法失败", e);
                            }
                            String responseData = joiner.join(list);
                            log.info("==========响应日志开始==========");
                            log.info("responseData：" + responseData);
                            log.info("==========响应日志结束==========");

                            byte[] uppedContent = new String(responseData.getBytes(), StandardCharsets.UTF_8).getBytes();
                            originalResponse.getHeaders().setContentLength(uppedContent.length);
                            return bufferFactory.wrap(uppedContent);
                        }));
                    }
                }
                return super.writeWith(body);
            }

            @Override
            public Mono<Void> writeAndFlushWith(Publisher<? extends Publisher<? extends DataBuffer>> body) {
                return writeWith(Flux.from(body).flatMapSequential(p -> p));
            }
        };
        return chain.filter(exchange.mutate().response(response).build());
    }

    @Override
    public int getOrder() {
        return -2;
    }
}
