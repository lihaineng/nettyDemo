package com.lhn.common.protobuf;

import com.google.protobuf.ByteString;
import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.Message;

public class MessageBuilder {

    private static final int CRCCODE = 0xabef0101;

    /**
     * 	$getRequestMessage 请求封装
     * @param module
     * @param cmd
     * @param data
     * @return
     */
    public static Message getRequestMessage(String module,
                                            String cmd,
                                            GeneratedMessageV3 data) {

        return MessageModule.Message.newBuilder()
                .setCrcCode(CRCCODE)
                .setMessageType(MessageModule.MessageType.REQUEST)
                .setModule(module)
                .setCmd(cmd)
                .setBody(ByteString.copyFrom(data.toByteArray()))
                .build();
    }

    /**
     * 	$getResponseMessage 响应封装
     * @param module
     * @param cmd
     * @param resultType
     * @param data
     * @return
     */
    public static Message getResponseMessage(String module,
                                             String cmd,
                                             MessageModule.ResultType resultType,
                                             GeneratedMessageV3 data) {

        return MessageModule.Message.newBuilder()
                .setCrcCode(CRCCODE)
                .setMessageType(MessageModule.MessageType.RESPONSE)
                .setModule(module)
                .setCmd(cmd)
                .setResultType(resultType)
                .setBody(ByteString.copyFrom(data.toByteArray()))
                .build();
    }
}
