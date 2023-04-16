package com.neutron.openapiclientsdk.client;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.http.HttpRequest;
import com.google.gson.Gson;
import com.neutron.openapiclientsdk.model.Request;

import java.util.HashMap;
import java.util.Map;

/**
 * 用于为用户提供调用第三方接口的客户端
 *
 * @author zzs
 * @date 2023/4/10 15:42
 */
public class OpenApiClient {

    /**
     * 用户标识
     */
    private final String accessKey;

    /**
     * 用户秘钥
     */
    private final String secretKey;

    public OpenApiClient(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    /**
     * 根据秘钥生成签名
     *
     * @param userAccount    用户账号
     * @param secretKey 秘钥
     * @return 签名
     */
    public static String generateSign(String userAccount, String secretKey) {
        String content = userAccount + "." + secretKey;
        return DigestUtil.md5Hex(content);
    }

    /**
     * 用户调用线上接口的方法
     *
     * @param request 用户请求
     * @return 响应
     */
    public String sendRequest(Request request) {
        Gson gson = new Gson();
        String json = gson.toJson(request);

        if (compareMethod("GET", request.getMethod())) {
            return HttpRequest.get(request.getUrl())
                    .header("Accept","application/json;charset=UTF-8")
                    .addHeaders(setHeaders(request, accessKey, secretKey))
                    .charset("UTF-8")
                    .body(json)
                    .execute().body();
        } else {
            return HttpRequest.post(request.getUrl())
                    .header("Accept","application/json;charset=UTF-8")
                    .addHeaders(setHeaders(request, accessKey, secretKey))
                    .charset("UTF-8")
                    .body(json)
                    .execute().body();
        }
    }

    /**
     * 设置请求头
     *
     * @param request   请求
     * @param accessKey 用户标识
     * @param secretKey 秘钥
     * @return 请求头的map集合
     */
    private Map<String, String> setHeaders(Request request, String accessKey, String secretKey) {
        Gson gson = new Gson();
        Map<String, String> map = new HashMap<String, String>(5);
        map.put("accessKey", accessKey);
        map.put("sign", generateSign(request.getUserAccount(), secretKey));
        map.put("nonce", RandomUtil.randomNumbers(4));
        map.put("body", gson.toJson(request.getBody()));
        map.put("timestamp", String.valueOf(System.currentTimeMillis()));
        return map;
    }

    /**
     * 比较请求方法
     * @param method1
     * @param method2
     * @return
     */
    private boolean compareMethod(String method1, String method2) {
        return method1.equalsIgnoreCase(method2);
    }

}
