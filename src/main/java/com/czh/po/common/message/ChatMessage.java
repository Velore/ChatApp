package com.czh.po.common.message;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 在客户端和服务器端之间传递的聊天信息
 * 客户端发送给服务器
 * 服务器再发送给其他的客户端
 * @author chenzhuohong
 */
@Getter
@Setter
@NoArgsConstructor
public class ChatMessage extends Message {

    /**
     * 发送信息的用户id
     */
    private String senderId;

    private String gid;

    private String msgStr;

    public ChatMessage(String gid, String msgStr) {
        this.setSendTime(LocalDateTime.now());
        this.gid = gid;
        this.setMsgType('c');
        this.msgStr = msgStr;
    }

    @Override
    public String getInfo(){
        return this.msgStr;
    }

    @Override
    public String toString() {
        return "[" + gid + "](" +
                getSendTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + ")" +"\t"+
                getSenderId() +
                ":{" +this.msgStr + "}";
    }

    public static void main(String[] args) {
        ChatMessage cm = new ChatMessage("testGid", "testMsg");
        cm.setSenderId("testLoginId");
        System.out.println(cm);
    }
}
