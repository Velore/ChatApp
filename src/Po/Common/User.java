package Po.Common;

import Po.Server.Server;
import Utils.RandomUtils;
import Utils.StorageUtils;
import Utils.UserUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

/**
 * @author chenzhuohong
 */
public class User implements Serializable {

    /**
     * 序列化版本号
     */
    private static final long serialVersionUID = 101010L;

    /**
     * 用户id的长度
     */
    private final static int UID_LENGTH = 5;

    /**
     * 用户id(帐号)
     */
    private String uid;

    /**
     * 用户名字
     */
    private String name;

    /**
     * 用户密码
     */
    private String pwd;

    /**
     * 用户所在的群组的列表
     */
    private ArrayList<String> groupList;

    /**
     * 用户的好友列表
     */
    private ArrayList<String> friendList;

    public User(){
        this.groupList = new ArrayList<>();
        this.friendList = new ArrayList<>();
    }

    /**
     * 用户名初始化为账号
     * @param pwd 密码
     */
    public User(String pwd){
        this.uid = RandomUtils.intString(UID_LENGTH);
        this.name = this.uid;
        this.pwd = pwd;
        this.groupList = new ArrayList<>();
        this.friendList = new ArrayList<>();
    }

    /**
     * 用户名自定义
     * @param name 用户名
     * @param pwd 密码
     */
    public User(String name, String pwd){
        this.uid = RandomUtils.intString(UID_LENGTH);
        this.name = name;
        this.pwd = pwd;
        this.groupList = new ArrayList<>();
        this.friendList = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "User{" +
                "uid='" + uid + '\'' +
                ", name='" + name + '\'' +
                ", pwd='" + pwd + '\'' +
                ", groupList=" + groupList +
                ", friendList=" + friendList +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return uid.equals(user.uid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uid);
    }

    public String getUid(){
        return this.uid;
    }

    public void setUid(String uid){
        this.uid = uid;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getPwd(){
        return this.pwd;
    }

    public void setPwd(String pwd){
        this.pwd = pwd;
    }

    public ArrayList<String> getGroupList(){
        return this.groupList;
    }

    public void setGroupList(ArrayList<String> groupList){
        this.groupList = groupList;
    }

    public void addGroup(String gid){
        if(Group.findGroup(gid, Server.groupList)){
            if(!this.groupList.contains(gid)){
                this.groupList.add(gid);
            }
            Group group = null;
            for(Group g : Server.groupList){
                if(g.getGid().equals(gid)){
                    group = g;
                }
            }
            if(group == null){
                //在这一步，群组必定存在，该判断只是防止编译器警告，
                return;
            }
            if(!group.getMemberList().contains(this.uid)){
                group.addMember(this.uid);
            }
        }else{
            System.out.println("群组不存在");
        }
    }

    public ArrayList<String> getFriendList(){
        return this.friendList;
    }

    public void setFriendList(ArrayList<String> friendList){
        this.friendList = friendList;
    }

    public void addFriend(String uid){
        if(!this.friendList.contains(uid)){
            this.friendList.add(uid);
        }
    }

    public void delFriend(String uid){
        this.friendList.remove(uid);
    }

    public static void main(String[] args){

        ArrayList<User> userList = StorageUtils.objToUser(StorageUtils.read(Server.USER_FILE_PATH));
//        ArrayList<User> userList = new ArrayList<>();
//        System.out.println(userList);
//        User u1 = new User();
//        u1.uid = "u1";
//        u1.name = "n1";
//        u1.pwd = "p1";
//        userList.add(u1);
//        User u2 = new User();
//        u2.uid = "u2";
//        u2.name = "n2";
//        u2.pwd = "p2";
//        userList.add(u2);
//        User u3 = new User();
//        u3.uid = "u3";
//        u3.name = "n3";
//        u3.pwd = "p3";
//        userList.add(u3);
//        User u4 = new User();
//        u4.uid = "u4";
//        u4.name = "n4";
//        u4.pwd = "p4";
//        userList.add(u4);
//        User u5 = new User();
//        u5.uid = "u5";
//        u5.name = "n5";
//        u5.pwd = "p5";
//        userList.add(u5);
//        User u6 = new User();
//        u6.uid = "u6";
//        u6.name = "n6";
//        u6.pwd = "p6";
//        userList.add(u6);
//        StorageUtils.write(StorageUtils.userToObj(userList), Server.USER_FILE_PATH, false);
        System.out.println(userList);
    }
}
