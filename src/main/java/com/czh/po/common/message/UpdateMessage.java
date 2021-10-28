package com.czh.po.common.message;

import com.czh.po.common.User;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 客户端登录时发送一个只包含帐号和密码的用户信息;
 * 登录后，客户端发送给服务器端需要更新的用户信息;
 * @author chenzhuohong
 */
@NoArgsConstructor
public class UpdateMessage extends Message {

    private User updateUser;

    public UpdateMessage(User user){
        this.setSendTime(LocalDateTime.now());
        this.setMsgType(MessageType.UPDATE_TYPE);
        this.updateUser = user;
    }

    @Override
    public User getInfo() {
        return this.updateUser;
    }

    @Override
    public String toString() {
        return "UpdateMessage{" +
                "loginBo=" + getLoginBo() +
                ", msgType=" + getMsgType() +
                ", sendTime=" + getSendTime() +
                ", updateUser=" + updateUser +
                '}';
    }
}