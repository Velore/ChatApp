package com.czh.po.common.message;

import com.czh.bo.LoginBo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 在客户端和服务器端之间传递的聊天信息
 * 客户端发送给服务器
 * 服务器再发送给其他的客户端
 * @author chenzhuohong
 */
public class ChatMessage extends Message {

    private String gid;

    private String msgStr;

    public ChatMessage() {}

    public ChatMessage(String gid, String msgStr) {
        this.sendTime = LocalDateTime.now();
        this.gid = gid;
        this.setMsgType('c');
        this.msgStr = msgStr;
    }

    @Override
    public String getInfo(){
        return this.msgStr;
    }

    public void setMsgStr(String msgStr) {
        this.msgStr = msgStr;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    @Override
    public String toString() {
        return "[" + gid + "](" +
                sendTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + ")" +"\t"+
                loginBo.getLoginUid() +
                ":{" +this.msgStr + "}";
    }

    public static void main(String[] args) {
        ChatMessage cm = new ChatMessage("testGid", "testMsg");
        cm.loginBo = new LoginBo();
        cm.loginBo.setLoginUid("testLoginId");
        System.out.println(cm);
    }
}
