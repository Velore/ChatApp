package com.czh.service;

import com.czh.dao.GroupMemberMapper;
import com.czh.po.common.GroupMember;
import com.czh.po.common.ReturnInfo;
import com.czh.po.common.StatusCode;
import com.czh.utils.MybatisUtils;

import java.util.List;

/**
 * @author chenzhuohong
 */
public class GroupMemberService {

    /**
     * 用户申请进入群组
     * @param uid 要申请的用户id
     * @param gid 要申请进入的群组id
     * @return int 是否加入成功
     */
    public static ReturnInfo addGroupMember(String gid, String uid){
        GroupMemberMapper mapper = MybatisUtils.getSqlSession().getMapper(GroupMemberMapper.class);
        if(mapper.addMember(new GroupMember(gid, uid))!=1){
            return new ReturnInfo(StatusCode.ERROR_CODE, "申请加入群组["+gid+"]失败");
        }
        return new ReturnInfo(StatusCode.SUCCESS_CODE, "成功加入群组["+gid+"]");
    }

    /**
     * 用户退出群组
     * @param uid 用户uid
     * @param gid 群组id
     * @return 是否退出成功
     */
    public static ReturnInfo deleteGroupMember(String gid, String uid){
        GroupMemberMapper mapper = MybatisUtils.getSqlSession().getMapper(GroupMemberMapper.class);
        if(mapper.deleteMember(gid, uid)!=1){
            return new ReturnInfo(StatusCode.ERROR_CODE, "退出群组["+gid+"]失败");
        }
        return new ReturnInfo(StatusCode.SUCCESS_CODE, "成功退出群组["+gid+"]");
    }

    /**
     * 通过群组id和用户id查询群组成员
     * @param gid 群组id
     * @param uid 用户id
     * @return 群组成员
     */
    public static GroupMember queryMemberById(String gid, String uid){
        GroupMemberMapper mapper = MybatisUtils.getSqlSession().getMapper(GroupMemberMapper.class);
        return mapper.queryMemberById(gid, uid);
    }

    /**
     * 通过群组id查询群组内全部成员
     * @param gid 群组id
     * @return 群组成员list
     */
    public static List<GroupMember> queryAllMemberByGroupId(String gid){
        GroupMemberMapper mapper = MybatisUtils.getSqlSession().getMapper(GroupMemberMapper.class);
        return mapper.queryAllMemberByGroupId(gid);
    }

    /**
     * 通过群组id和成员名字模糊查询群组成员
     * @param gid 群组id
     * @param name 成员名字
     * @return 群组成员list
     */
    public static List<GroupMember> queryMemberLikeName(String gid, String name){
        GroupMemberMapper mapper = MybatisUtils.getSqlSession().getMapper(GroupMemberMapper.class);
        return mapper.queryMemberLikeName(gid, name);
    }
}
