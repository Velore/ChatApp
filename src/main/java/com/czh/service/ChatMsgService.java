package com.czh.service;

import com.czh.dao.ChatMsgMapper;
import com.czh.po.common.ReturnInfo;
import com.czh.po.common.StatusCode;
import com.czh.po.common.message.ChatMessage;
import com.czh.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

/**
 * @author chenzhuohong
 */
public class ChatMsgService {

    /**
     * 发送聊天信息
     * 由于聊天信息在生成时已经验证过LoginBo
     * 此处无需再次验证
     * @param message 要发送的聊天信息
     * @return 操作的返回信息
     */
    public static ReturnInfo addChatMsg(ChatMessage message){
        SqlSession session = MybatisUtils.getSqlSession();
        ChatMsgMapper mapper = session.getMapper(ChatMsgMapper.class);
        if(mapper.addChatMsg(message)!=1){
            return new ReturnInfo(StatusCode.ERROR_CODE, "聊天信息发送失败");
        }
        session.commit();
        //向在线成员发送聊天信息
        return new ReturnInfo(StatusCode.SUCCESS_CODE, "聊天信息发送成功");
    }

    /**
     * 根据群组id查询聊天信息
     * @param gid 群组id
     * @return 聊天信息list
     */
    public static List<ChatMessage> queryChatMsgByGroupId(String gid){
        ChatMsgMapper mapper = MybatisUtils.getSqlSession().getMapper(ChatMsgMapper.class);
        return mapper.queryChatMsgByGroupId(gid);
    }

    /**
     * 根据发送者id和群组id查询聊天信息
     * @param gid 群组id
     * @param senderId 发送者id
     * @return 聊天信息list
     */
    public static List<ChatMessage> queryChatMsgByGroupIdAndSenderId(String gid, String senderId){
        ChatMsgMapper mapper = MybatisUtils.getSqlSession().getMapper(ChatMsgMapper.class);
        return mapper.queryChatMsgByGroupIdAndSenderId(gid, senderId);
    }

    /**
     * 根据群组id和msgStr模糊查询聊天信息
     * @param gid 群组id
     * @param msgStr msgStr
     * @return 聊天信息list
     */
    public static List<ChatMessage> queryChatMsgLikeMsgStr(String gid, String msgStr){
        ChatMsgMapper mapper = MybatisUtils.getSqlSession().getMapper(ChatMsgMapper.class);
        return mapper.queryChatMsgLikeMsgStr(gid, msgStr);
    }
}
