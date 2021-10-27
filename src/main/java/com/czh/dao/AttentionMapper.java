package com.czh.dao;

import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * attention目前无实体类
 * 只是作为User类的一个附加属性
 * @author chenzhuohong
 */
public interface AttentionMapper {

    /**
     * 添加(uid1 -> uid2)的关注
     * @param followerId 关注者
     * @param acceptId 被关注者
     * @param followTime 关注时间
     * @return 是否操作成功
     */
    int addAttention(@Param("followerId") String followerId,
                     @Param("acceptId") String acceptId,
                     @Param("followTime") LocalDateTime followTime);

    /**
     * 取消(uid1 -> uid2)的关注
     * @param followerId 关注者
     * @param acceptId 被关注者
     * @return 是否操作成功,成功返回1
     */
    int deleteAttention(String followerId, String acceptId);

    /**
     * 查询某个uid的关注者uid
     * @param uid uid
     * @return 关注者list
     */
    List<String> queryFollowerById(String uid);

    /**
     * 查询某个uid主动关注的uid
     * @param uid uid
     * @return 用户主动关注的list
     */
    List<String> queryAttentionById(String uid);

    /**
     * 根据传入的时间点查询在时间点之前的关注list
     * @param followerId 关注者
     * @param beforeTime 时间点
     * @return 关注list
     */
    List<String> queryAttentionBeforeTime(String followerId, LocalDateTime beforeTime);

    /**
     * 根据传入的时间点查询在时间点之后的关注list
     * @param followerId 关注者
     * @param afterTime 时间点
     * @return 关注list
     */
    List<String> queryAttentionAfterTime(String followerId, LocalDateTime afterTime);
}
