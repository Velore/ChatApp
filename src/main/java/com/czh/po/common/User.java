package com.czh.po.common;

import com.czh.utils.RandomUtils;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
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
@Getter
@Setter
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
     * 注册时间
     */
    private final LocalDateTime registerTime;

    /**
     * 最后登录时间
     */
    private LocalDateTime lastOnlineTime;

    public User(){
        this.registerTime = LocalDateTime.now();
        this.lastOnlineTime = this.registerTime;
    }

    public User(String uid, String name, String pwd){
        this.uid = uid;
        this.name = name;
        this.pwd = pwd;
        this.registerTime = LocalDateTime.now();
        this.lastOnlineTime = this.registerTime;
    }

    /**
     * 用户名初始化为账号
     * @param pwd 密码
     */
    public User(String pwd){
        this.uid = RandomUtils.mixString(UID_LENGTH);
        this.name = this.uid;
        this.pwd = pwd;
        this.registerTime = LocalDateTime.now();
        this.lastOnlineTime = this.registerTime;
    }

    /**
     * 用户名自定义
     * @param name 用户名
     * @param pwd 密码
     */
    public User(String name, String pwd){
        this.uid = RandomUtils.mixString(UID_LENGTH);
        this.name = name;
        this.pwd = pwd;
        this.registerTime = LocalDateTime.now();
        this.lastOnlineTime = this.registerTime;
    }

    @Override
    public String toString() {
        return "User{" +
                "uid='" + uid + '\'' +
                ", name='" + name + '\'' +
                ", pwd='" + pwd + '\'' +
                ", registerTime='" + registerTime.toString() + '\'' +
                ", lastOnlineTime='" + lastOnlineTime.toString() + '\'' +
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

    public static void main(String[] args){
    }
}