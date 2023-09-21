package com.yiwise.asr.client;

import com.yiwise.asr.protocol.Connection;
import com.yiwise.asr.protocol.ConnectionListener;
import com.yiwise.asr.protocol.NettyConnection;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import java.net.URI;

public class YiwiseNettyWebSocketClient {
    private static final Logger logger = LoggerFactory.getLogger(YiwiseNettyWebSocketClient.class);

    private final URI websocketURI;
    EventLoopGroup group = new NioEventLoopGroup(0);
    Bootstrap bootstrap = new Bootstrap();

    /**
     * @param url gateway url
     */
    public YiwiseNettyWebSocketClient(String url) {
        try {
            this.websocketURI = new URI(url);
            bootstrap.option(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .group(group)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline p = socketChannel.pipeline();
                            if (websocketURI.toString().startsWith("wss")) {
                                SSLEngine sslEngine = SSLContext.getDefault().createSSLEngine();
                                sslEngine.setUseClientMode(true);
                                p.addLast("ssl", new SslHandler(sslEngine));
                            }
                            p.addLast(new HttpClientCodec(), new HttpObjectAggregator(1024 * 1024 * 10));
                            p.addLast("hookedHandler", new YiwiseWebSocketClientHandler());
                        }
                    });
        } catch (Exception e) {
            logger.error("初始化YiwiseNettyWebSocketClient出错", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 建立WebSocket连接
     * 预留了鉴权逻辑，可以参考腾讯ASR：
     * - 在URL中带上ASR参数及SecretId
     * - 用HMAC-SHA1算法和SecretKey对生成的URL进行签名，并进行Base64编码
     * - 服务端存有SecretId和SecretKey的键值对，收到后利用对称算法解密
     * @param listener listener
     * @param connectionTimeout connectionTimeout
     * @return Connection
     * @throws Exception Exception
     */
    public Connection connect(ConnectionListener listener, int connectionTimeout) throws Exception {
        bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectionTimeout);
        HttpHeaders httpHeaders = new DefaultHttpHeaders();
        // TODO 生成签名
//        httpHeaders.set("Authorization", SHA1.base64_hmac_sha1(websocketURI.getHost(), YiwiseAsrClientFactory.SECRET_KEY));
//        httpHeaders.set("Host", websocketURI.getHost());
//        httpHeaders.set("X-Task-Id", IdGen.genId());
        //进行握手
        WebSocketClientHandshaker handshaker = WebSocketClientHandshakerFactory.newHandshaker(websocketURI, WebSocketVersion.V13, null, true, httpHeaders);
        final Channel channel = bootstrap.connect(websocketURI.getHost(), websocketURI.getPort()).sync().channel();
        logger.debug("websocket channel is established after sync,connectionId:{}", channel.id());

        YiwiseWebSocketClientHandler handler = (YiwiseWebSocketClientHandler) channel.pipeline().get("hookedHandler");
        handler.setHandshaker(handshaker);
        handler.setListener(listener);
        handshaker.handshake(channel);
        //阻塞等待是否握手成功
        handler.handshakeFuture().sync();
        logger.debug("websocket connection is established after handshake,connectionId:{}", channel.id());

        return new NettyConnection(channel);
    }

    public void shutdown() {
        group.shutdownGracefully();
    }
}
