package com.yiwise.asr.protocol;

import java.nio.ByteBuffer;

/**
 * Created by 昌夜 on 2023/6/25.
 */
public interface ConnectionListener {
    void onOpen();

    /**
     * Invoked after the connection was closed.
     *
     * @param closeCode the RFC 6455 status code
     * @param reason    a string description for the reason of the close
     */
    void onClose(int closeCode, String reason);

    /**
     * Invoked after an error.
     *
     * @param throwable the cause
     */
    void onError(Throwable throwable);

    /**
     * Invoded after fail
     *
     * @param status
     * @param reason
     */
    void onFail(int status, String reason);

    /**
     * Invoked on arrival of a text message.
     *
     * @param message the text message.
     */
    void onMessage(String message);

    /**
     * Invoked on arrival of a binary message.
     *
     * @param message the binary message.
     */
    void onMessage(ByteBuffer message);
}
