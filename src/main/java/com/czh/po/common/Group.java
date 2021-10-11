package com.czh.po.common;

import com.czh.utils.RandomUtils;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 聊天室抽象为群组
 * 群组本身不记录用户状态
 * - 两人单独聊天的群组：用户列表只有两人
 * - 多人聊天的群组，用户列表包含多人
 * 内置聊天群组如下
 * gid memberList
 * 10001 [u1,u2,u3,u4]
 * 10002 [u4,u5]
 * 12345 [u1,u6]
 * @author chenzhuohong
 */
@Getter
public class Group implements Serializable {

    /**
     * 序列化版本号
     */
    private static final long serialVersionUID = 101010L;

    /**
     * 群组id的长度
     * 一般用不上
     */
    private final static int GID_LENGTH = 5;

    /**
     * 群组的id
     * 每个群组都有的唯一的id
     */
    private final String gid;

    /**
     * 群组名称
     */
    private String gName;

    /**
     * 群主
     */
    private String ownerId;

    /**
     * 建立群组的时间
     */
    private final LocalDateTime establishTime;

    public Group(){
        this.gid = RandomUtils.mixString(GID_LENGTH);
        this.establishTime = LocalDateTime.now();
    }

    public Group(String gid, String uid){
        this.gid = gid;
        this.gName = gid;
        this.ownerId = uid;
        this.establishTime = LocalDateTime.now();
    }

    public Group(String uid) {
        this.gid = RandomUtils.mixString(GID_LENGTH);
        this.ownerId = uid;
        this.gName = this.gid;
        this.establishTime = LocalDateTime.now();
    }

    public void setGroupName(String name){
        this.gName = name;
    }

    @Override
    public String toString() {
        return "Group{" +
                "gid='" + gid + '\'' +
                ", gName='" + gName + '\'' +
                ", ownerId='" + ownerId + '\'' +
                ", establishTime='" + establishTime + '\'' +
                '}';
    }

    public static void main(String[] args) {
    }
}