package com.czh.dao;

import com.czh.po.common.GroupMember;
import com.czh.po.common.User;

import java.util.List;

/**
 * @author chenzhuohong
 */
public interface GroupMemberMapper {

    /**
     * 添加群组成员
     * @param member 群组成员
     * @return 是否添加成功
     */
    int addMember(GroupMember member);

    /**
     * 移除群组成员
     * @param uid 成员id
     * @param gid 群组id
     * @return 是否移除成功
     */
    int deleteMember(String uid, String gid);

    /**
     * 通过用户id查询群组内成员
     * @param uid 成员id
     * @param gid 群组id
     * @return User
     */
    GroupMember queryMemberById(String uid, String gid);

    /**
     * 查询群组所有成员
     * @param gid 群组id
     * @return 成员List
     */
    List<GroupMember> queryAllMember(String gid);

    /**
     * 通过名字模糊查询群组成员
     * @param name 名字
     * @param gid 群组id
     * @return 成员list
     */
    List<GroupMember> queryMemberLikeName(String name, String gid);
}