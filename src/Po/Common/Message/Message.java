package Po.Common.Message;

import Bo.LoginBo;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 客户端与服务器交互对象的父类
 * 子类包括
 * @author chenzhuohong
 */
public class Message implements Serializable {

    /**
     * 发送信息的用户的登录信息
     */
    public LoginBo loginBo;
    /**
     * 消息的类型
     * c:客户端发送给服务器端，或者服务器端发送给客户端[新的聊天记录]，子类为ChatMessage
     * u:客户端第一次登录时，登录后修改用户信息，子类为UpdateMessage
     * s:服务器端发送登录信息给客户端，或客户端注销时向服务器端发送，子类为StatusMessage
     * i:客户端查询信息，服务器端返回查询结果，子类为InfoMessage
     */
    public char msgType;
    /**
     * 消息发送的时间
     */
    public LocalDateTime sendTime;

    public Object getInfo(){
        return null;
    }

}
