package com.czh.service;

import com.czh.po.common.User;
import com.czh.po.common.message.InfoMessage;
import com.czh.po.server.Server;
import com.czh.po.server.ServerThread;

/**
 * 通知服务
 * @author chenzhuohong
 */
public class NotificationService {

    /**
     * 用户登录或注销时发送的通知
     */
    public static void notifyUserStatus(ServerThread serverThread, User user){
        try{
            //向在线用户广播某用户已上线
            for(ServerThread st : Server.threadList){
                if(!st.getLoginBo().equals(serverThread.getLoginBo())){
                    st.getOutput().writeObject(new InfoMessage(user.getUid()+"已上线"));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
