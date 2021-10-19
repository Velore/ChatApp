package com.czh.po.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * @author chenzhuohong
 */
@Getter
@AllArgsConstructor
public class GroupMember {

    private final String uid;

    private final String gid;

    private String memberAuth;

    private final LocalDateTime joinTime;

    public GroupMember(String uid, String gid){
        this.uid = uid;
        this.gid = gid;
        this.memberAuth = "member";
        this.joinTime = LocalDateTime.now();
    }

    public GroupMember(String uid, String gid, String memberAuth){
        this.uid = uid;
        this.gid = gid;
        this.memberAuth = memberAuth;
        this.joinTime = LocalDateTime.now();
    }

    public void setMemberAuth(String memberAuth){
        this.memberAuth = memberAuth;
    }

    @Override
    public String toString() {
        return "GroupMember{" +
                "uid='" + uid + '\'' +
                ", gid='" + gid + '\'' +
                ", memberAuth='" + memberAuth + '\'' +
                ", joinTime=" + joinTime +
                '}';
    }
}
