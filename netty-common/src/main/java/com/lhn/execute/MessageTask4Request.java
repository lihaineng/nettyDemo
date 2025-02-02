package com.lhn.execute;

import com.lhn.common.protobuf.MessageBuilder;
import com.lhn.common.protobuf.MessageModule;
import com.lhn.common.protobuf.Result;
import com.lhn.scanner.Invoker;
import com.lhn.scanner.InvokerTable;
import io.netty.channel.ChannelHandlerContext;

public class MessageTask4Request implements Runnable {

    private MessageModule.Message message;

    private ChannelHandlerContext ctx;

    private final static String RETURN = "-return";

    public MessageTask4Request(MessageModule.Message message, ChannelHandlerContext ctx) {
        this.message = message;
        this.ctx = ctx;
    }

    @Override
    public void run() {
        String module = message.getModule();
        String cmd = message.getCmd();
        byte[] data = message.getBody().toByteArray();
        Invoker invoker = InvokerTable.getInvoker(module, cmd);
        Result<?> result = (Result<?>) invoker.invoke(data);

        ctx.writeAndFlush(MessageBuilder
                .getResponseMessage(module + RETURN,
                        cmd + RETURN,
                        result.getResultType(),
                        result.getContent()));
    }
}
