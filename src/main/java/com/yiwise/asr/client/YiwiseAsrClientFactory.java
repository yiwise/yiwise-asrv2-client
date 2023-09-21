package com.yiwise.asr.client;

/**
 * @author lgy
 * @date 2023/1/29
 */
public class YiwiseAsrClientFactory {
    static YiwiseAsrClient asrClient;

    static Object lock = new Object();

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
