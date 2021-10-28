package com.czh.po.common;

import com.czh.utils.RandomUtils;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * 用户实体类
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
    private LocalDateTime registerTime;

    /**
     * 最后登录时间
     */
    private LocalDateTime lastOnlineTime;

    public User(){
        this.registerTime = LocalDateTime.now();
        this.lastOnlineTime = this.registerTime;
    }

    /**
     * 自定义id,名字和密码
     * @param uid 帐号id
     * @param name 名字
     * @param pwd 密码
     */
    public User(String uid, String name, String pwd){
        this.uid = uid;
        this.name = name;
        this.pwd = pwd;
        this.registerTime = LocalDateTime.now();
        this.lastOnlineTime = this.registerTime;
    }

    /**
     * 用户名初始化为id
     * id为随机生成的5位字符
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
     * 帐号为随机生成的5位字符
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
                ", registerTime='" + registerTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + '\'' +
                ", lastOnlineTime='" + lastOnlineTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + '\'' +
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