package Utils;

import Bo.LoginBo;
import Po.Common.Group;
import Po.Common.User;
import Po.Server.Server;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

/**
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
        //更新用户群组
//        if(!user.getGroupList().isEmpty()){
//            for(String ug : user.getGroupList()){
//                if(!Group.findGroup(ug, Server.groupList)){
//                    Server.groupList.add(new Group(user.getUid()));
//                }else{
//
//                }
//            }
//        }
        //将修改保存到服务器的用户列表
        Server.userList.set(index, user);
    }

}
