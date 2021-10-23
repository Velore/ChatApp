package com.czh.po.common.message;

import com.czh.bo.LoginBo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author chenzhuohong
 */
@Getter
@Setter
@NoArgsConstructor
public class StatusMessage extends Message{

    /**
     * 用户登录信息
     * 用户登录时，第一次发送UpdateMessage，服务器端返回新的LoginBo的StatusMessage
     * 用户注销时，发送StatusMessage，服务器端关闭用户对应serverThread的进程
     */
    public StatusMessage(LoginBo loginBo){
        this.setSendTime(LocalDateTime.now());
        this.setMsgType('s');
        this.setSenderId(loginBo.getLoginUid());
    }

    @Override
    public String getInfo(){
        return this.getSenderId();
    }

    @Override
    public String toString() {
        return "StatusMessage{" +
                "senderId=" + getSenderId() +
                ", msgType=" + getMsgType() +
                ", sendTime=" + getSendTime() +
                '}';
    }
}