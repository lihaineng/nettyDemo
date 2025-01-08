package com.lhn.client;

import com.lhn.common.protobuf.MessageModule;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

import java.util.concurrent.atomic.AtomicBoolean;

public class Client {

    private EventLoopGroup group = new NioEventLoopGroup(2);

    public static final String VIP_HOST = "192.168.203.1";
    public static final int VIP_PORT = 8888;

    // 是否已连接标识
    private AtomicBoolean isConnect = new AtomicBoolean(false);

    private Client()  {
    }

    private static class SingletonHolder {
        private static final Client INSTANCE = new Client();
    }

    public static final Client getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public synchronized void init() {

        if (!isConnect.get()) {
            // 如果没有连接就建立连接
            try {
                this.connect(VIP_HOST, VIP_PORT);
                isConnect.set(true);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void connect(String host, int port) {

        // 配置netty客户端
        Bootstrap b = new Bootstrap();
        b.group(group).channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true) // TCP_NODELAY就是用于启用或关闭Nagle算法。如果要求高实时性，有数据发送时就马上发送，就将该选项设置为true关闭Nagle算法
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new ProtobufVarint32FrameDecoder());
                        ch.pipeline().addLast(new ProtobufDecoder(MessageModule.Message.getDefaultInstance()));
                        ch.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());
                        ch.pipeline().addLast(new ProtobufEncoder());
                        ch.pipeline().addLast(new ClientHandler());
                    }
                });

    }
}
