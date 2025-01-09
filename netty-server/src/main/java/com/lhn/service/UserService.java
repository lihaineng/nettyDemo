package com.lhn.service;

import com.google.protobuf.InvalidProtocolBufferException;
import com.lhn.annotation.Cmd;
import com.lhn.annotation.Module;
import com.lhn.common.protobuf.Result;
import com.lhn.common.protobuf.UserModule;
import org.springframework.stereotype.Service;

@Service
@Module(module = "user")
public class UserService {

    //	自动注入相关的spring bean (Service)

    @Cmd(cmd = "save")
    public Result<?> save(byte[] data) {
        UserModule.User user = null;
        try {
            user = UserModule.User.parseFrom(data);
            System.err.println(" save ok , userId: " + user.getUserId() + " ,userName: " + user.getUserName());
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
            return Result.FAILURE();
        }
        return Result.SUCCESS(user);
    }

    @Cmd(cmd = "update")
    public Result<?> update(byte[] data) {
        UserModule.User user = null;
        try {
            user = UserModule.User.parseFrom(data);
            System.err.println(" update ok , userId: " + user.getUserId() + " ,userName: " + user.getUserName());
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
            return Result.FAILURE();
        }
        return Result.SUCCESS(user);
    }

}
