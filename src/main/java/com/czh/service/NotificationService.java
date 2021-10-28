package com.czh.service;

import com.czh.po.common.message.InfoMessage;
import com.czh.po.server.Server;
import com.czh.po.server.ServerCallable;

/**
 * 通知服务
 * @author chenzhuohong
 */
public class NotificationService {

    /**
     * 用户登录或注销时发送的通知
     * @param uid 登录或注销的用户uid
     * @param status 状态：登录/注销
     */
    public static void notifyUserStatus(String uid, String status){
        try{
            //向在线用户广播某用户已上线
            for(ServerCallable st : Server.threadList){
                if(!st.getUid().equals(uid)){
                    st.getOutput().writeObject(new InfoMessage(uid+status));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void notifyFollower(){

    }
}
