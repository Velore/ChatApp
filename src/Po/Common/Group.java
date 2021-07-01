package Po.Common;

import Bo.LoginBo;
import Po.Common.Message.ChatMessage;
import Po.Common.Message.Message;
import Po.Server.Server;
import Po.Server.ServerThread;
import Utils.RandomUtils;
import Utils.StorageUtils;

import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * 聊天室抽象为群组
 * 群组本身不记录用户状态
 * - 两人单独聊天的群组：用户列表只有两人
 * - 多人聊天的群组，用户列表包含多人
 * @author chenzhuohong
 */
public class Group implements Serializable {

    /**
     * 序列化版本号
     */
    private static final long serialVersionUID = 101010L;

    /**
     * 群组id的长度
     */
    private final static int GID_LENGTH = 5;

    /**
     * 群组的id
     */
    private final String gid;

    /**
     * 存放用户帐号
     */
    private ArrayList<String> memberList;

    /**
     * 聊天记录
     */
    private ArrayList<ChatMessage> msgList;

    public Group(String uid) {
        this.gid = RandomUtils.intString(GID_LENGTH);
        this.memberList = new ArrayList<>();
        this.memberList.add(uid);
        this.msgList = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Group{" +
                "gid='" + gid + '\'' +
                ", memberList=" + memberList +
                ", msgList=" + msgList +
                '}';
    }

    public String getGid() {
        return gid;
    }

    public ArrayList<String> getMemberList() {
        return memberList;
    }

    public void setMemberList(ArrayList<String> memberList) {
        this.memberList = memberList;
    }

    public void addMember(String uid){
        this.memberList.add(uid);
    }

    public void deleteMember(String uid){
        this.memberList.remove(uid);
    }

    public ArrayList<ChatMessage> getMsgList() {
        return msgList;
    }

    public void setMsgList(ArrayList<ChatMessage> msgList) {
        this.msgList = msgList;
    }

    public void addMsg(ChatMessage msg){
        this.msgList.add(msg);
    }

    /**
     * 某用户是否为该群组的成员
     * @param uid 用户uid
     * @return 是该群组成员返回true，否则返回false
     */
    public boolean containsMember(String uid){
        for(String u : this.memberList){
            if(u.equals(uid)){
                return true;
            }
        }
        return false;
    }

    /**
     * 通过gid查找群组列表的某个群组
     * @param gid 群组id
     * @param groupList 群组列表
     * @return 查找成功返回true，否则返回false
     */
    public static boolean findGroup(String gid, ArrayList<Group> groupList){
        for(Group sg : groupList){
            if(sg.getGid().equals(gid)){
                return true;
            }
        }
        return false;
    }

    public static int sendMsg(Message msg){
        ChatMessage cm = (ChatMessage) msg;
        Group sendGroup = null;
        for(Group g : Server.groupList){
            if(g.gid.equals(cm.getGid())){
                sendGroup = g;
            }
        }
        if(sendGroup == null){
            //没有找到对应的群组
            return -1;
        }
        if(!sendGroup.containsMember(msg.loginBo.getLoginUid())){
            //发送消息的用户不是该群组的成员
            return -2;
        }
        //对群组内成员一一判断是否在线
        for(LoginBo loginBo : Server.loginList){
            for(String memberId : sendGroup.memberList){
                if(loginBo.getLoginUid().equals(memberId)){
                    //如果登录列表中存在该成员的登录信息
                    try{
                        for(ServerThread st : Server.threadList){
                            if(st.getUid().equals(memberId)){
                                st.getOutput().writeObject(msg);
                                break;
                            }
                        }
                        System.out.println("成员"+memberId+"在线，聊天信息已发送");
                        break;
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else{
                    System.out.println("成员"+memberId+"离线，聊天信息不发送");
                }
            }
        }
        //消息发送成功
        //将聊天信息加入群组聊天记录
        sendGroup.msgList.add(cm);
        return 0;
    }

    public static void main(String[] args) {
        ArrayList<Group> groupList = StorageUtils.objToGroup(StorageUtils.read(Server.GROUP_FILE_PATH));
        Group g1 = new Group("u1");
        g1.addMember("u2");
        g1.addMember("u3");
        g1.addMember("u4");
        groupList.add(g1);
        Group g2 = new Group("u4");
        g2.addMember("u5");
        groupList.add(g2);
        StorageUtils.write(StorageUtils.groupToObj(groupList), Server.GROUP_FILE_PATH, false);
        System.out.println(groupList);
    }
}
