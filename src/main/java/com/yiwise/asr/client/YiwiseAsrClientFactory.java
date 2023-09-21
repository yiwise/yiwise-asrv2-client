package com.yiwise.asr.client;

/**
 * @author lgy
 */
public class YiwiseAsrClientFactory {
    static YiwiseAsrClient asrClient;

    static Object lock = new Object();

    /**
     *
     * @param asrGatewayUrl 语音识别网关地址
     * @return 语音识别客户端
     */
    public static YiwiseAsrClient getAsrClient(String asrGatewayUrl) {
        if (asrClient != null) {
            return asrClient;
        }
        synchronized (lock) {
            if (asrClient == null) {
                asrClient = new YiwiseAsrClient(asrGatewayUrl);
            }
        }
        return asrClient;
    }

}
