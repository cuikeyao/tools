package com.cuikeyao.agent.util;


import cn.hutool.core.util.CharsetUtil;
import cn.hutool.http.HttpUtil;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static com.cuikeyao.agent.constant.Constant.SERVICE_NORMAL;
import static com.cuikeyao.agent.constant.Constant.TRUE;
import static com.cuikeyao.agent.constant.Constant.URL;

/**
 * @author KEYAO
 */
@Log4j2
@Component
public final class HttpUtils {
    @Value("${kuma.host}")
    private String kumaHost;

    @Value("${kuma.port}")
    private String kumaPort;

    @Value("${nginx.key}")
    private String kumaKey;

    private static String host;

    private static String port;

    private static String key;

    @PostConstruct
    public void init() {
        host = kumaHost;
        port = kumaPort;
        key = kumaKey;
    }

    public static void sendMessage( String processName, String memorySize) {
        String url = String.format(URL, host, port, key, processName + SERVICE_NORMAL, memorySize);
        getRequest(url);
    }

    public static void getRequest(String url) {
        String response = HttpUtil.get(url, CharsetUtil.CHARSET_UTF_8);
        if (!response.contains(TRUE)) {
            log.error("调用kuma接口失败：" + url);
        }
    }
}
