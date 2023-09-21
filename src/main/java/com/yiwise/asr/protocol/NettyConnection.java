package com.yiwise.asr.protocol;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by 昌夜 on 2023/6/25.
 */
public class NettyConnection implements Connection {
    static Logger logger = LoggerFactory.getLogger(NettyConnection.class);
    Channel channel;

    public NettyConnection(Channel channel) {
        this.channel = channel;
    }

    @Override
    public String getId() {
        if (channel != null) {
            return channel.id().toString();
        }
        return null;

    }

    @Override
    public boolean isActive() {
        if(channel!=null && channel.isActive()){
            return true;
        }
        return  false;
    }

    @Override
    public void close() {
        channel.close();
    }

    @Override
    public void sendText(final String payload) {
        if (channel != null && channel.isActive()) {
            logger.debug("thread:{},send:{}", Thread.currentThread().getId(), payload);
            TextWebSocketFrame frame = new TextWebSocketFrame(payload);
            channel.writeAndFlush(frame);
        }

    }

    @Override
    public void sendBinary(byte[] payload) {
        if (channel != null && channel.isActive()) {
            BinaryWebSocketFrame frame = new BinaryWebSocketFrame(Unpooled.wrappedBuffer(payload));
            channel.writeAndFlush(frame);
        }

    }

    @Override
    public void sendPing(){
        PingWebSocketFrame frame=new PingWebSocketFrame();
        if (channel != null && channel.isActive()) {
            channel.writeAndFlush(frame);
        }
    }
}