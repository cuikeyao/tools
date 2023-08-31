package com.cuikeyao.agent.util;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.http.HttpUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import static com.cuikeyao.agent.constant.Constant.SERVICE_NORMAL;
import static com.cuikeyao.agent.constant.Constant.URL;

class HttpUtilsTest {

    String host = "127.0.0.1";
    String port = "1234";
    String key = "abcdefghij";
    String processName = "Nginx";
    String memorySize = "111";
    String url = String.format(URL, host, port, key, processName + SERVICE_NORMAL, memorySize);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void sendMessage() {
        // init初始化方法测试
        HttpUtils httpUtils = new HttpUtils();
        ReflectionTestUtils.setField(httpUtils,"kumaHost",host);
        ReflectionTestUtils.setField(httpUtils,"kumaPort",port);
        ReflectionTestUtils.setField(httpUtils,"kumaKey",key);
        Assertions.assertDoesNotThrow(() -> httpUtils.init());

        MockedStatic<HttpUtil> httpUtilMockedStatic = Mockito.mockStatic(HttpUtil.class);

        // 调用接口成功测试
        httpUtilMockedStatic.when(() -> HttpUtil.get(url , CharsetUtil.CHARSET_UTF_8))
            .thenReturn("{\"ok\":true}");
        Assertions.assertDoesNotThrow(() -> HttpUtils.sendMessage(processName, memorySize));

        // 调用接口失败测试
        httpUtilMockedStatic.when(() -> HttpUtil.get(url , CharsetUtil.CHARSET_UTF_8))
                .thenReturn("{\"ok\":false}");
        Assertions.assertDoesNotThrow(() -> HttpUtils.sendMessage(processName, memorySize));

    }
}