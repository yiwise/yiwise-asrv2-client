package com.yiwise.asr.client;

import com.yiwise.asr.protocol.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class YiwiseAsrClient {
    private static final Logger logger = LoggerFactory.getLogger(YiwiseAsrClient.class);

    private final YiwiseNettyWebSocketClient webSocketClient;
    private static final int DEFAULT_CONNECTION_TIMEOUT = 5000;

    public YiwiseAsrClient(String gatewayUrl) {
        this.webSocketClient = new YiwiseNettyWebSocketClient(gatewayUrl);
    }

    public Connection connect(YiwiseSpeechTranscriberListener listener) throws Exception {
        int i = 0;

        while(true) {
            try {
                return this.webSocketClient.connect(listener, DEFAULT_CONNECTION_TIMEOUT);
            } catch (Exception var5) {
                if (i == 2) {
                    logger.error("failed to connect to server after 3 tries, error msg is : {}", var5.getMessage());
                    throw var5;
                }

                Thread.sleep(100L);
                logger.warn("failed to connect to server the {} time, error msg is : {}, try again", i, var5.getMessage());
                ++i;
            }
        }
    }

    public void shutdown() throws IOException {
        logger.debug("asr client shutdown");
        this.webSocketClient.shutdown();
    }
}
