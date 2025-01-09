package com.lhn.server;

import com.lhn.common.protobuf.MessageModule;
import com.lhn.execute.MessageTask4Request;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ServerHandler extends ChannelInboundHandlerAdapter {

    ThreadPoolExecutor workerPool = new ThreadPoolExecutor(5,
            10,
            60L,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(4000),
            new ThreadPoolExecutor.DiscardPolicy());

    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        MessageModule.Message request = (MessageModule.Message) msg;
        workerPool.submit(new MessageTask4Request(request, ctx));
    }


}
