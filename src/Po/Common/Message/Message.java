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
     * c:服务器端新的聊天记录，子类为ChatMessage
     * u:客户端修改用户信息，子类为UpdateMessage
     * s:客户端登录或注销，子类为StatusMessage
     * i:客户端查询信息，子类为InfoMessage
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
