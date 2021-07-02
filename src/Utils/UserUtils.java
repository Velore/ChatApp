package Utils;

import Bo.LoginBo;
import Po.Common.Group;
import Po.Common.Message.InfoMessage;
import Po.Common.User;
import Po.Server.Server;
import Po.Server.ServerThread;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * 用户工具类
 * 包括用户登录和用户注册
 * @author chenzhuohong
 */
public class UserUtils {

    /**
     * 用户注册
     * @return 一个用户对象
     */
    public static User userRegister(){
        Scanner in = new Scanner(System.in);
        System.out.println("请输入名字");
        String name = in.nextLine();
        System.out.println("请输入密码");
        String pwd = in.nextLine();
        System.out.println("用户注册成功，等待系统生成注册信息");
        return new User(name, pwd);
    }

    /**
     * 用户登录
     * @return 一个用户对象
     */
    public static User userLogin(){
        Scanner in = new Scanner(System.in);
        System.out.println("请输入帐号");
        String uid = in.nextLine();
        System.out.println("请输入密码");
        String pwd = in.nextLine();
        User user = new User();
        user.setUid(uid);
        user.setPwd(pwd);
        System.out.println("等待系统生成登录信息");
        return user;
    }

    /**
     * 是否为新用户
     * @param uid 要验证的用户id
     * @param userList 登录过的用户列表
     * @return 是新用户返回true，否则返回false
     */
    public static boolean containsUser(String uid, ArrayList<User> userList){
        for(User u : userList){
            if(uid.equals(u.getUid())){
                return true;
            }
        }
        return false;
    }

    /**
     * 查询某用户是否在线
     * @param user 查询用户
     * @param loginList 在线列表
     * @return 查询结果，在线返回true，离线返回false
     */
    public static boolean isOnline(User user, ArrayList<LoginBo> loginList){
        if(!loginList.isEmpty()){
            for(LoginBo l : loginList){
                if(user.getUid().equals(l.getLoginUid())){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 用户登录确认
     * @param user 客户端登录的用户
     * @return 服务器生成的用户登录信息
     */
    public static LoginBo userLoginConfirm(User user){
        for(User u : Server.userList){
            if(u.getUid().equals(user.getUid())&&u.getPwd().equals(user.getPwd())){
                return new LoginBo(u.getUid());
            }
        }
        System.out.println("帐号或密码错误");
        return null;
    }

    /**
     * 更新用户信息
     * @param user 要更新的用户，不需更新的属性为null
     * @param loginBo 更新信息需要先登录
     */
    public static void updateUser(User user, LoginBo loginBo){
        int index = 0;
        if(user==null){
            return;
        }
        for(User u : Server.userList){
            if(u.getUid().equals(loginBo.getLoginUid())){
                user.setUid(u.getUid());
                index = Server.userList.indexOf(u);
            }
        }
        if(user.getName()!=null){
            Server.userList.get(index).setName(user.getName());
        }
        if(user.getPwd()!=null){
            Server.userList.get(index).setPwd(user.getPwd());
        }
        //更新用户的群组列表和群组的成员列表
        if(!user.getGroupList().isEmpty()){
            for(String gid : user.getGroupList()){
                if(Group.findGroup(gid, Server.groupList)){
                    //对应群组存在则添加新成员
                    for(Group g : Server.groupList){
                        if(g.getGid().equals(gid) && !g.containsMember(user.getUid())){
                            Server.userList.get(index).addGroup(gid);
                            break;
                        }
                    }
                }else{
                    //没有找到对应群组则新建一个
                    Server.groupList.add(new Group(gid, user.getUid()));
                    //把新建的群组添加进服务器端的群组列表
                    Server.userList.get(index).getGroupList().add(gid);
                    System.out.println("群组"+gid+"已创建");
                    //向在线用户广播新建的群组
                    try{
                        for(ServerThread st : Server.threadList){
                            st.getOutput().writeObject(new InfoMessage("用户"+user.getUid()+"新建群组"+gid+"快来加入聊天吧"));
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }
        }
        //更新用户的好友列表和被添加好友的好友列表
        if(!user.getFriendList().isEmpty()){
            for(String uid : user.getFriendList()){
                for(User friend : Server.userList){
                    if(friend.getUid().equals(uid)){
                        Server.userList.get(index).addFriend(uid);
                        friend.addFriend(user.getUid());
                        break;
                    }
                }
            }
        }
    }

}
