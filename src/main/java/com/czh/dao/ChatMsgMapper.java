package com.czh.dao;

import com.czh.po.common.message.ChatMessage;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author chenzhuohong
 */
public interface ChatMsgMapper {

    /**
     * 发送聊天信息
     * @param chatMessage 聊天信息
     * @return 是否发送成功
     */
    int addChatMsg(ChatMessage chatMessage);

//    /**
//     * 撤回聊天信息
//     * @param chatMessage 聊天信息
//     * @return 是否撤回成功
//     */
//    int deleteChatMsg(String gid);

    /**
     * 通过群组id查询该群组的全部聊天信息
     * @param gid 群组id
     * @return 聊天信息list
     */
    List<ChatMessage> queryChatMsgByGroupId(String gid);

    /**
     * 通过群组id和发送者id查询聊天信息
     * @param gid 群组id
     * @param senderId 发送者id
     * @return 聊天信息list
     */
    List<ChatMessage> queryChatMsgBySenderIdAndGroupId(String gid, String senderId);

    /**
     * 通过聊天信息部分str模糊查询聊天信息
     * @param gid 群组
     * @param msgStr 聊天信息部分str
     * @return ChatMessage
     */
    List<ChatMessage> queryChatMsgLikeMsgStr(String gid, String msgStr);

    /**
     * 通过群组id, 开始时间, 结束时间查询聊天信息
     * @param gid 群组id
     * @param beginTime 开始时间
     * @param endTime 结束时间
     * @return 聊天信息list
     */
    List<ChatMessage> queryChatMsgWithSendTime(String gid, Timestamp beginTime, Timestamp endTime);

}
