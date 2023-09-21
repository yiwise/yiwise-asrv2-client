package com.yiwise.asr.protocol;

/**
 * Created by 昌夜 on 2023/6/25.
 */
public interface Connection {
    public void close();

    public void sendText(final String payload);

    public void sendBinary(byte[] payload);

    public void sendPing();

    public String getId();

    public boolean isActive();
}
