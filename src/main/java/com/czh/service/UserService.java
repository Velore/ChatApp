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
        System.out.println("请输入名字");
        String name = in.nextLine();
        System.out.println("请输入密码");
        String pwd = in.nextLine();
        System.out.println("用户注册成功，等待系统生成注册信息");
        return new User(name, pwd);
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
     * 添加关注
     * @param uid 要关注的帐号
     * @param loginBo 登录凭证
     */
    public static void addAttention(String uid, LoginBo loginBo){
    }

    /**
     * 取消关注
     * @param uid 要取消关注的帐号
     * @param loginBo 登录凭证
     */
    public static void deleteAttention(String uid, LoginBo loginBo){
    }

    /**
     * 查询某用户是否在线
     * @param user 查询用户
     * @param loginList 在线列表
     * @return 查询结果，在线返回true，离线返回false
     */
    public static boolean userOnline(User user, ArrayList<LoginBo> loginList){
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
