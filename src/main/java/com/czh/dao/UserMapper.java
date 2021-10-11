package com.czh.dao;

import com.czh.po.common.User;

import java.util.List;

/**
 * @author chenzhuohong
 */
public interface UserMapper {

    /**
     * 添加用户
     * @param user 用户
     * @return 操作返回值
     */
    int addUser(User user);

    /**
     * 更新用户信息
     * @param user 用户
     * @return 操作返回值
     */
    int updateUser(User user);

    /**
     * 通过uid删除用户
     * @param uid uid
     * @return 操作返回值
     */
    int deleteUser(String uid);

    /**
     * 查询所有的用户
     * @return 用户list
     */
    List<User> queryAllUser();

    /**
     * 通过uid查询一个用户
     * @param uid uid
     * @return User
     */
    User queryUserById(String uid);

    /**
     * 通过名字模糊查询用户
     * @param name 名字
     * @return User
     */
    List<User> queryUserLikeName(String name);

}
