package com.lhn.execute;

import com.lhn.common.protobuf.MessageModule;
import com.lhn.common.protobuf.MessageModule.ResultType;
import com.lhn.scanner.Invoker;
import com.lhn.scanner.InvokerTable;
import io.netty.channel.ChannelHandlerContext;

public class MessageTask4Response implements Runnable{

    private MessageModule.Message message;
    private ChannelHandlerContext ctx;

    public MessageTask4Response(MessageModule.Message message, ChannelHandlerContext ctx) {
        this.message = message;
        this.ctx = ctx;
    }


    @Override
    public void run() {
        //	user-return
        String module = this.message.getModule();
        //	save-return
        String cmd = this.message.getCmd();
        //	响应的结果
        ResultType resultType = this.message.getResultType();
        //	响应的内容
        byte[] data = this.message.getBody().toByteArray();

        // 查询要执行的方法
        Invoker invoker = InvokerTable.getInvoker(module, cmd);
        invoker.invoke(resultType, data);
    }
}
