package com.lhn.client;

import com.google.protobuf.GeneratedMessageV3;
import com.lhn.common.protobuf.MessageBuilder;
import com.lhn.common.protobuf.MessageModule;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class Client {

    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    private EventLoopGroup group = new NioEventLoopGroup(2);

    public static final String VIP_HOST = "192.168.203.1";
    public static final int VIP_PORT = 8888;

    private Channel channel;

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

    private void connect(String host, int port) throws InterruptedException {

        try {
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
            // 发起同步连接操作
            ChannelFuture future = b.connect(host, port).sync();
            this.channel = future.channel();
            System.out.println("Client Start.. ");
            this.channel.closeFuture().sync();
        }finally {
            // 	所有资源释放完成之后，清空资源，再次发起重连操作
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        TimeUnit.SECONDS.sleep(1);
                        try {
                            connect(host, port);// 发起重连操作
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    /**
     * 	$sendMessage
     * 发送数据的方法
     * @param module 模块
     * @param cmd 指令
     * @param messageData 数据内容
     */
    public void sendMessage(String module, String cmd , GeneratedMessageV3 messageData) {
        this.channel.writeAndFlush(MessageBuilder.getRequestMessage(module, cmd, messageData));
    }
}
