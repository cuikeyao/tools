package com.cuikeyao.agent.constant;

/**
 * @author KEYAO
 */
public final class Constant {
    public static final String TRUE = "true";
    public static final String URL = "http://%s:%s/api/push/%s?status=up&msg=%s&ping=%s";
    public static final String SERVICE_NORMAL = "服务正常";
    public static final String SERVICE_ABNORMAL = "服务异常";
    public static final String ZERO_MEMORY_SIZE = "0";
    public static final String GET_PID = "ps -ef | grep '%s' | grep -v 'grep' | sed -n 1p | awk '{print($2)}'";
    public static final String GET_MEMORY_SIZE = "grep VmRSS /proc/%s/status | awk '{print($2)}'";

    private Constant() {
        throw new IllegalStateException("Constants Class");
    }
}
