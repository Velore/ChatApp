package Po.Common.Message;

import Bo.LoginBo;
import Po.Server.Server;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

/**
 * @author chenzhuohong
 */
public class ChatMessage extends Message {

    private String gid;

    private String msgStr;

    public ChatMessage() {}

    public ChatMessage(String gid, String msgStr) {
        this.sendTime = LocalDateTime.now();
        this.gid = gid;
        this.msgType = 'c';
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
