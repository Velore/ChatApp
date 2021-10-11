package com.czh.dao;

import com.czh.po.common.Group;

import java.util.List;

/**
 * @author chenzhuohong
 */
public interface GroupMapper {

    /**
     * 添加群组
     * @param group 群组
     * @return 操作返回值
     */
    int addGroup(Group group);

    /**
     * 更新群组信息
     * @param group Group
     * @return 操作返回值
     */
    int updateGroup(Group group);

    /**
     * 通过id删除群组
     * @param gid 群组id
     * @return 操作返回值
     */
    int deleteGroup(String gid);

    /**
     * 通过群组id查询群组
     * @param gid gid
     * @return Group
     */
    Group queryGroupById(String gid);

    /**
     * 通过群主id查询群组
     * @param uid uid
     * @return 群组list
     */
    List<Group> queryGroupByOwnerId(String uid);

    /**
     * 查询所有群组
     * @return 群组list
     */
    List<Group> queryAllGroup();

    /**
     * 通过名字模糊查询群组
     * @param gName 名字
     * @return 群组list
     */
    List<Group> queryGroupLikeName(String gName);

}
