package com.czh.service;

import com.czh.dao.ChatMsgMapper;
import com.czh.po.common.GroupMember;
import com.czh.po.common.ReturnInfo;
import com.czh.po.common.StatusCode;
import com.czh.po.common.message.ChatMessage;
import com.czh.po.server.Server;
import com.czh.po.server.ServerCallable;
import com.czh.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author chenzhuohong
 */
public class ChatMsgService {

    /**
     * 向群组内在线成员发送信息;
     * @param msg 要发送的信息
     * @return 发送的结果：
     */
    public static ReturnInfo sendChatMsg(ChatMessage msg){
        try{
            if(GroupService.queryGroupById(msg.getGid())==null){
                return new ReturnInfo(StatusCode.NOT_FOUND_CODE, "群组不存在");
            }
            //所有的群组成员, 无论是否在线
            List<String> memberList = GroupMemberService
                    .queryAllMemberByGroupId(msg.getGid())
                    .stream()
                    .map(GroupMember::getUid)
                    .collect(Collectors.toList());
            if(!memberList.contains(msg.getSenderId())){
                return new ReturnInfo(StatusCode.NO_PERMISSION_CODE, "请先加入群组");
            }
            for(ServerCallable st : Server.threadList){
                //只有带有LoginBo的serverThread才是已登录的
                if(st.getLoginBo()!=null){
                    for(String s : memberList){
                        if(st.getUid().equals(s)){
                            System.out.println("成员"+s+"在线");
                            st.getOutput().writeObject(msg);
                        }
                    }
                }
            }
            return new ReturnInfo(StatusCode.SUCCESS_CODE, "发送信息成功");
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ReturnInfo(StatusCode.ERROR_CODE, "发送信息失败");
    }

    /**
     * 将聊天信息保存至数据库;
     * 由于聊天信息在生成时已经验证过LoginBo, 此处无需再次验证;
     * @param msg 要保存的聊天信息
     * @return 操作的返回信息
     */
    public static ReturnInfo addChatMsg(ChatMessage msg){
        SqlSession session = MybatisUtils.getSqlSession();
        ChatMsgMapper mapper = session.getMapper(ChatMsgMapper.class);
        if(mapper.addChatMsg(msg)!=1){
            return new ReturnInfo(StatusCode.ERROR_CODE, "聊天信息保存至数据库失败");
        }
        session.commit();
        //向在线成员发送聊天信息
        return new ReturnInfo(StatusCode.SUCCESS_CODE, "聊天信息已保存至数据库");
    }

    /**
     * 根据群组id查询聊天信息;
     * @param gid 群组id
     * @return 聊天信息list
     */
    public static List<ChatMessage> queryChatMsgByGroupId(String gid){
        ChatMsgMapper mapper = MybatisUtils.getSqlSession().getMapper(ChatMsgMapper.class);
        return mapper.queryChatMsgByGroupId(gid);
    }

    /**
     * 根据发送者id和群组id查询聊天信息;
     * @param gid 群组id
     * @param senderId 发送者id
     * @return 聊天信息list
     */
    public static List<ChatMessage> queryChatMsgByGroupIdAndSenderId(String gid, String senderId){
        ChatMsgMapper mapper = MybatisUtils.getSqlSession().getMapper(ChatMsgMapper.class);
        return mapper.queryChatMsgByGroupIdAndSenderId(gid, senderId);
    }

    /**
     * 根据群组id和msgStr模糊查询聊天信息;
     * @param gid 群组id
     * @param msgStr msgStr
     * @return 聊天信息list
     */
    public static List<ChatMessage> queryChatMsgLikeMsgStr(String gid, String msgStr){
        ChatMsgMapper mapper = MybatisUtils.getSqlSession().getMapper(ChatMsgMapper.class);
        return mapper.queryChatMsgLikeMsgStr(gid, msgStr);
    }
}
