package com.czh.service;

import com.czh.bo.LoginBo;
import com.czh.dao.GroupMapper;
import com.czh.po.common.Group;
import com.czh.po.common.ReturnInfo;
import com.czh.po.common.StatusCode;
import com.czh.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;

/**
 * @author chenzhuohong
 */
public class GroupService {

    /**
     * 新建群组
     * @param loginBo 群主的登录凭证
     * @return Group
     */
    public static Group establishGroup(LoginBo loginBo){
        String uid = loginBo.getLoginUid();
        Group group = new Group(uid);
        SqlSession session = MybatisUtils.getSqlSession();
        GroupMapper mapper = session.getMapper(GroupMapper.class);
        if(mapper.addGroup(group)==1){
            System.out.println(uid+"创建群组："+group.getGid());
        }
        return group;
    }

    /**
     * 群主更新群组信息
     * @param loginBo 群主的登录凭证
     * @param group 更新信息后的群组
     * @return 更新操作的返回信息
     */
    public static ReturnInfo updateGroup(LoginBo loginBo, Group group){
        if(!loginBo.getLoginUid().equals(group.getOwnerId())){
            return new ReturnInfo(StatusCode.NO_PERMISSION_CODE,"只有群主可以更新群组信息");
        }
        SqlSession session = MybatisUtils.getSqlSession();
        GroupMapper mapper = session.getMapper(GroupMapper.class);
        if(mapper.updateGroup(group) != 1) {
            return new ReturnInfo(StatusCode.ERROR_CODE, "更新群组信息失败");
        }
        return new ReturnInfo(StatusCode.SUCCESS_CODE,"更新群组信息成功");
    }

    /**
     * 群主解散群组
     * 该方法首先通过gid查询登录id是否为群组id
     * 确认操作者身份后再执行删除操作
     * @param loginBo 群主登录凭证
     * @param gid 群组id
     * @return 删除操作的返回信息
     */
    public static ReturnInfo deleteGroup(LoginBo loginBo, String gid){
        SqlSession session = MybatisUtils.getSqlSession();
        GroupMapper mapper = session.getMapper(GroupMapper.class);
        if(!loginBo.getLoginUid().equals(mapper.queryGroupById(gid).getOwnerId())){
            return new ReturnInfo(StatusCode.NO_PERMISSION_CODE,"只有群主可以删除群组");
        }
        if(mapper.deleteGroup(gid) != 1){
            return new ReturnInfo(StatusCode.ERROR_CODE, "删除群组失败");
        }
        return new ReturnInfo(StatusCode.SUCCESS_CODE,"删除群组成功");
    }

    /**
     * 通过id查询群组
     * @param gid 群组id
     * @return 查询的结果
     */
    public static Group queryGroupById(String gid){
        GroupMapper mapper = MybatisUtils.getSqlSession().getMapper(GroupMapper.class);
        return mapper.queryGroupById(gid);
    }
}
