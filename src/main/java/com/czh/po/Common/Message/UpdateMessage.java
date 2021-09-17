package com.czh.po.Common.Message;


import com.czh.po.Common.User;

import java.time.LocalDateTime;

/**
 * 客户端登录时发送一个只包含帐号和密码的用户信息
 * 登录后，客户端发送给服务器端需要更新的用户信息
 * @author chenzhuohong
 */
public class UpdateMessage extends Message {

    private User updateUser;

    public UpdateMessage(){}

    public UpdateMessage(User user){
        this.sendTime = LocalDateTime.now();
        this.msgType = 'u';
        this.updateUser = user;
    }

    @Override
    public User getInfo() {
        return this.updateUser;
    }

    @Override
    public String toString() {
        return "UpdateMessage{" +
                "loginBo=" + loginBo +
                ", msgType=" + msgType +
                ", sendTime=" + sendTime +
                ", updateUser=" + updateUser +
                '}';
    }
}