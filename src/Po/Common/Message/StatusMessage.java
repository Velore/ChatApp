package Po.Common.Message;

import Bo.LoginBo;

import java.time.LocalDateTime;

/**
 * @author chenzhuohong
 */
public class StatusMessage extends Message{

    /**
     * 用户登录信息
     * 用户登录时，第一次发送UpdateMessage，服务器端返回新的LoginBo的StatusMessage
     * 用户注销时，发送StatusMessage，服务器端关闭用户对应serverThread的进程
     */

    public StatusMessage(){}

    public StatusMessage(LoginBo loginBo){
        this.sendTime = LocalDateTime.now();
        this.msgType = 's';
        this.loginBo = loginBo;
    }

    @Override
    public LoginBo getInfo(){
        return this.loginBo;
    }

    @Override
    public String toString() {
        return "StatusMessage{" +
                "loginBo=" + loginBo +
                ", msgType=" + msgType +
                ", sendTime=" + sendTime +
                '}';
    }
}
