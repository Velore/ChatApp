package Po.Common;

import Po.Server.Server;
import Utils.RandomUtils;
import Utils.StorageUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

/**
 * 用户类
 * 内置用户信息如下
 * 帐号 姓名 密码 所在群组列表 好友列表
 * uid name pwd groupList friendList
 * u1 t1 p1 [10001,12345] [u2,u3]
 * u2 n2 p2 [10001] [u1,u6]
 * u3 n3 p3 [10001] [u1]
 * u4 n4 p4 [10001, 10002] []
 * u5 n5 p5 [10002] []
 * u6 n6 p6 [12345] [u2]
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
     * 暂时没有用的字段，代码全部使用uid
     */
    private String name;

    /**
     * 用户密码
     */
    private String pwd;

    /**
     * 用户所在的群组的列表
     */
    private final ArrayList<String> groupList;

    /**
     * 用户的好友列表
     * 目前该字段没有用，若有空会新建Friend类的列表取代
     */
    private final ArrayList<String> friendList;

    public User(){
        this.groupList = new ArrayList<>();
        this.friendList = new ArrayList<>();
    }

    public User(String uid, String name, String pwd){
        this.uid = uid;
        this.name = name;
        this.pwd = pwd;
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

    /**
     * 用户申请进入群组，若群组不存在，则新建一个该id的群组
     * @param gid 要申请或建立的群组id
     */
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
            System.out.println("群组"+gid+"不存在");
        }
    }

    public ArrayList<String> getFriendList(){
        return this.friendList;
    }

    /**
     * 添加好友
     * 不重复添加
     * @param uid 添加的好友帐号
     */
    public void addFriend(String uid){
        if(!this.friendList.contains(uid)){
            this.friendList.add(uid);
        }
    }

    /**
     * 删除好友
     * 该方法暂时有问题
     * @param uid 要删除的好友帐号
     */
    public void delFriend(String uid){
        if(this.getFriendList().contains(uid)){
            this.friendList.remove(uid);
        }
    }

    public static void main(String[] args){

//        ArrayList<User> userList = StorageUtils.objToUser(StorageUtils.read(Server.USER_FILE_PATH));
        ArrayList<User> userList = new ArrayList<>();
        System.out.println(userList);
        User u1 = new User("u1", "n1", "p1");
        u1.getGroupList().add("10001");
        u1.addFriend("u2");
        u1.addFriend("u3");
        userList.add(u1);
        User u2 = new User("u2", "n2", "p2");

        u2.getGroupList().add("10001");
        u2.addFriend("u1");
        userList.add(u2);
        User u3 = new User("u3", "n3", "p3");

        u3.getGroupList().add("10001");
        u3.addFriend("u1");
        userList.add(u3);
        User u4 = new User("u4", "n4", "p4");

        u4.getGroupList().add("10001");
        u4.getGroupList().add("10002");
        userList.add(u4);
        User u5 = new User("u5", "n5", "p5");

        u5.getGroupList().add("10002");

        userList.add(u5);
        User u6 = new User("u6", "n6", "p6");
        userList.add(u6);
        StorageUtils.write(StorageUtils.userToObj(userList), Server.USER_FILE_PATH, false);
        System.out.println(userList);
    }
}
