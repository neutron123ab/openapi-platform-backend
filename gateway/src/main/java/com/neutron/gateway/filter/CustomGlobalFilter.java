package com.neutron.gateway.filter;

import cn.hutool.core.text.AntPathMatcher;
import cn.hutool.core.util.StrUtil;
import com.neutron.common.model.entity.InterfaceInfo;
import com.neutron.common.model.entity.User;
import com.neutron.common.service.DubboInterfaceService;
import com.neutron.common.service.DubboUserInterfaceService;
import com.neutron.common.service.DubboUserService;
import com.neutron.openapiclientsdk.client.OpenApiClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

/**
 * 全局请求过滤器
 * @author zzs
 * @date 2023/4/10 20:25
 */
@Slf4j
@Component
public class CustomGlobalFilter implements GlobalFilter, Ordered {

    @DubboReference
    private DubboUserService dubboUserService;

    @DubboReference
    private DubboInterfaceService dubboInterfaceService;

    @DubboReference
    private DubboUserInterfaceService dubboUserInterfaceService;

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        boolean permit = handlePaths(exchange);
        if (permit) {
            return chain.filter(exchange);
        }
        ServerHttpRequest request = exchange.getRequest();
        //打印请求日志
        log.info("请求唯一标识：{}", request.getId());
        log.info("请求uri：{}", request.getURI());
        log.info("请求路径：{}", request.getPath().value());
        log.info("请求方法：{}", request.getMethodValue());
        log.info("请求参数：{}", request.getQueryParams());
        log.info("请求来源地址：{}", request.getRemoteAddress());
        log.info("请求地址：{}", request.getLocalAddress());
        log.info("请求头信息：{}", request.getHeaders());

        //取出请求头信息，校验签名
        HttpHeaders headers = request.getHeaders();
        String accessKey = headers.getFirst("accessKey");
        String sign = headers.getFirst("sign");
        String nonce = headers.getFirst("nonce");
        String body = headers.getFirst("body");
        String timestamp = headers.getFirst("timestamp");

        ServerHttpResponse response = exchange.getResponse();
        if (accessKey == null || sign == null || nonce == null || body == null || timestamp == null) {
            return handleNoAuth(response);
        }
        User user = null;
        try {
            user = dubboUserService.queryUser(accessKey);
        } catch (Exception e) {
            log.error("远程调用查询用户接口失败", e);
        }
        //1. 用户不存在，拦截请求
        if (user == null) {
            return handleNoAuth(response);
        }
        //2. 校验签名
        String secretKey = user.getSecretKey();
        //秘钥不存在，拦截请求
        if (StrUtil.isEmptyIfStr(secretKey)) {
            return handleNoAuth(response);
        }
        //生成签名
        String generateSign = OpenApiClient.generateSign(user.getAccount(), secretKey);
        if (!sign.equals(generateSign)) {
            return handleNoAuth(response);
        }

        //3. nonce防重放
        Boolean addNonce = dubboUserService.addNonce(nonce);
        if (Boolean.FALSE.equals(addNonce)) {
            return handleNoAuth(response);
        }

        //4. 校验时间戳，签发时的时间与当前时间不能大于5分钟
        long currentTimeMillis = System.currentTimeMillis();
        long fiveMinutes = 60 * 5 * 1000L;
        if (currentTimeMillis - Long.parseLong(timestamp) > fiveMinutes) {
            return handleNoAuth(response);
        }

        //5. 校验接口是否存在
        String url = request.getURI().toString();
        String method = request.getMethodValue();
        InterfaceInfo interfaceInfo = null;
        try {
            interfaceInfo = dubboInterfaceService.getInterfaceInfo(url, method);
        } catch (Exception e) {
            log.error("远程调用查询接口信息方法失败", e);
        }
        if (interfaceInfo == null) {
            return handleNoAuth(response);
        }

        //6. 校验用户是否还有调用次数
        Long userId = user.getId();
        Long interfaceId = interfaceInfo.getId();
        Boolean canInvoke = null;
        try {
            canInvoke = dubboUserInterfaceService.getLeftInvokeNums(interfaceId, userId);
        } catch (Exception e) {
            log.error("远程调用接口计数服务失败", e);
        }
        if (Boolean.FALSE.equals(canInvoke)) {
            return handleNoAuth(response);
        }
        response.getHeaders().set("userId", userId.toString());
        response.getHeaders().set("interfaceId", interfaceId.toString());
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }

    private Mono<Void> handleNoAuth(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.FORBIDDEN);
        return response.setComplete();
    }

    /**
     * 校验请求路径是否需要放行
     *
     * @param exchange 交换机
     * @return true：放行
     */
    private boolean handlePaths(ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        List<String> ignorePaths = new ArrayList<>();
        ignorePaths.add("/**/user/**");
        ignorePaths.add("/**/interface/**");
        ignorePaths.add("/**/manager/**");

        for (String ignorePath : ignorePaths) {
            if (antPathMatcher.match(ignorePath, path)) {
                return true;
            }
        }
        return false;
    }
}
