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
     * 用户登录时，第一次发送UpdateMessage，服务器端返回新的LoginBo的StatusMessage;
     * 用户注销时，发送StatusMessage，服务器端关闭用户对应serverThread的进程，并发送注销通知;
     * 用户端接收到服务器端发送的注销通知(LoginBo中[uid=0000]的StatusMessage)时，关闭全部线程;
     */
    public StatusMessage(LoginBo loginBo){
        this.setSendTime(LocalDateTime.now());
        this.setMsgType(MessageType.STATUS_TYPE);
        this.setLoginBo(loginBo);
    }

    @Override
    public String getInfo(){
        return this.getLoginBo().toString();
    }

    @Override
    public String toString() {
        return "StatusMessage{" +
                "loginBo=" + getLoginBo() +
                ", msgType=" + getMsgType() +
                ", sendTime=" + getSendTime() +
                '}';
    }
}