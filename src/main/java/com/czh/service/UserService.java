package com.czh.service;

import com.czh.bo.LoginBo;
import com.czh.dao.UserMapper;
import com.czh.po.common.ReturnInfo;
import com.czh.po.common.StatusCode;
import com.czh.po.common.User;
import com.czh.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * 用户工具类
 * 包括用户登录和用户注册
 * @author chenzhuohong
 */
public class UserService {

    /**
     * 客户端用户注册
     * @return 一个用户对象
     */
    public static User userRegister(){
        Scanner in = new Scanner(System.in);
        User newUser;
        System.out.println("请输入名字");
        String name = in.nextLine();
        System.out.println("请输入密码");
        String pwd = in.nextLine();
        System.out.println("信息填写完成, 等待系统生成注册信息");
        do{
            newUser = new User(name, pwd);
        }while (UserService.isNewUser(newUser.getUid()));
        System.out.println("注册信息生成完毕, 等待系统确认");
        return newUser;
    }

    /**
     * 客户端用户登录
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
     * 服务器端验证是否为新用户
     * @param uid 要验证的用户id
     * @return 是新用户返回true，否则返回false
     */
    public static boolean isNewUser(String uid){
        try{
            UserMapper userMapper = MybatisUtils.getSqlSession().getMapper(UserMapper.class);
            if(userMapper.queryUserById(uid) != null){
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 查询某用户是否在线
     * @param uid 查询用户uid
     * @param loginList 在线列表
     * @return 查询结果，在线返回true，离线返回false
     */
    public static boolean userOnline(String uid, ArrayList<LoginBo> loginList){
        if(!loginList.isEmpty()){
            for(LoginBo l : loginList){
                if(uid.equals(l.getLoginUid())){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 服务器端用户登录确认
     * @param user 客户端登录的用户
     * @return 服务器生成的用户登录信息
     */
    public static LoginBo userLoginConfirm(User user){
        try{
            User u = UserService.queryUserById(user.getUid());
            if(u.getUid().equals(user.getUid())&&u.getPwd().equals(user.getPwd())){
                return new LoginBo(u.getUid());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("帐号或密码错误");
        return null;
    }

    /**
     * 更新用户信息
     * @param user 要更新的用户
     * @param loginBo 更新信息需要先登录
     */
    public static ReturnInfo updateUser(User user, LoginBo loginBo){
        //查询修改之前的用户信息并对比修改的地方
        User oldUser = UserService.queryUserById(loginBo.getLoginUid());
        user.setUid(oldUser.getUid());
        if(user.getName()==null){
            user.setName(oldUser.getName());
        }
        if(user.getPwd()==null){
            user.setPwd(oldUser.getPwd());
        }
        user.setRegisterTime(oldUser.getRegisterTime());
        //准备修改用户信息
        if(!user.getUid().equals(loginBo.getLoginUid())){
            return new ReturnInfo(StatusCode.NO_PERMISSION_CODE, "没有更改权限");
        }
        SqlSession session = MybatisUtils.getSqlSession();
        UserMapper mapper = session.getMapper(UserMapper.class);
        if(mapper.updateUser(user)!=1){
            return new ReturnInfo(StatusCode.ERROR_CODE, "更新用户信息失败");
        }
        session.commit();
        return new ReturnInfo(StatusCode.SUCCESS_CODE, "更新用户信息成功");
    }

    /**
     * 通过id查询用户
     * @param uid 用户id
     * @return 用户
     */
    public static User queryUserById(String uid){
        UserMapper mapper = MybatisUtils.getSqlSession().getMapper(UserMapper.class);
        return mapper.queryUserById(uid);
    }

    /**
     * 通过名字模糊查询用户
     * @param name 名字
     * @return 用户
     */
    public static List<User> queryUserLikeName(String name){
        UserMapper mapper = MybatisUtils.getSqlSession().getMapper(UserMapper.class);
        return mapper.queryUserLikeName(name);
    }

}
