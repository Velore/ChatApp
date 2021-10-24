package com.czh.service;

import com.czh.bo.LoginBo;
import com.czh.po.client.Client;
import com.czh.po.common.*;
import com.czh.po.common.message.ChatMessage;
import com.czh.po.common.message.InfoMessage;
import com.czh.po.common.message.Message;
import com.czh.po.common.message.StatusMessage;
import com.czh.po.server.Server;
import com.czh.po.server.ServerThread;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 消息
 * @author chenzhuohong
 */
public class MsgService {

    /**
     * 向群组内在线成员发送信息
     * @param msg 要发送的信息
     * @return 发送的结果：
     */
    public static ReturnInfo sendChatMsg(ChatMessage msg){
        try{
            //所有的群组成员, 无论是否在线
            List<String> memberList = GroupMemberService
                    .queryAllMemberByGroupId(msg.getGid())
                    .stream()
                    .map(GroupMember::getUid)
                    .collect(Collectors.toList());
            for(ServerThread st : Server.threadList){
                //只有带有LoginBo的serverThread才是已登录的
                if(st.getLoginBo()!=null){
                    for(String s : memberList){
                        if(st.getLoginBo().getLoginUid().equals(s)){
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
     * 客户端处理消息
     * @param client 客户端
     */
    public static void handleMsg(Client client){
        switch (client.inputMsg.getMsgType()){
            case 's':
                //登录时loginBo为null
                if(client.loginBo==null){
                    //接收到服务器发送的登录信息，传递给Client，Client再传递给输出线程
                    client.loginBo = client.inputMsg.getLoginBo();
                    System.out.println("登录信息" + client.loginBo);
                }else if("0000".equals(client.loginBo.getLoginUid())){
                    try{
                        client.socket.close();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                break;
            case 'c':
                //接收到聊天信息，输出在屏幕上，输出格式：[群组gid](发送时间)成员uid:{聊天内容}
                System.out.println(client.inputMsg);
                break;
            case 'i':
                //输出用户信息，群组信息，或群组的聊天信息，或者通知信息
                InfoMessage im = (InfoMessage) client.inputMsg;
                for(String s : im.getInfo()){
                    System.out.println(s+"\n");
                }
                break;
            default:
                System.out.println("msgType无效:"+client.inputMsg.getMsgType());
        }
    }


    /**
     * 服务器处理接收到的msg
     * @param msg 接收的msg
     */
    public static void handleMsg(ServerThread serverThread, Message msg){
        switch (msg.getMsgType()) {
            case 's':
                handleStatusMsg(serverThread, msg);
                break;
            case 'u':
                handleUpdateMsg(serverThread, msg);
                break;
            case 'c':
                handleChatMsg(serverThread, msg);
                break;
            case 'i':
                handleInfoMsg(serverThread, msg);
                break;
            default:
                System.out.println("msgType无效:" + msg.getMsgType());
        }
    }

    /**
     * 处理状态信息，如注销
     * @param thread 要处理的服务器线程
     * @param msg 要处理的状态信息
     */
    private static void handleStatusMsg(ServerThread thread, Message msg){
        //移除本次登录信息
        Server.loginList.removeIf(loginBo -> msg.getLoginBo().equals(loginBo));
        try {
            //通知客户端，注销信息服务器端接收成功，准备注销
            thread.getOutput().writeObject(new StatusMessage(new LoginBo("0000")));
            if(thread.getOutput()!=null){
                thread.getOutput().close();
            }
            if(thread.getInput()!=null){
                thread.getInput().close();
            }
            if(thread.getSocket()!=null){
                thread.getSocket().close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("用户"+thread.getLoginBo().getLoginUid()+"注销");
        System.out.println(Server.loginList.toString());
        //通知注销用户的好友，该用户已下线
    }

    /**
     * 服务器端处理用户信息，如更新密码等
     * @param serverThread 要处理的服务器线程
     * @param msg 要处理的用户信息
     */
    private static void handleUpdateMsg(ServerThread serverThread, Message msg){
        try{
            User user = (User) msg.getInfo();
            System.out.println(user);
            //客户端修改用户信息
            if(UserService.userOnline(user, Server.loginList)){
                //防止同一用户在不同客户端登录造成修改用户信息的错误情况
                System.out.println("serverThread:"+serverThread.getLoginBo());
                System.out.println("msg:"+msg.getLoginBo());
                if(serverThread.getLoginBo().getLoginUid().equals(msg.getLoginBo().getLoginUid())){
                    serverThread.getOutput().writeObject(
                            new InfoMessage(
                                    UserService.updateUser(user, msg.getLoginBo()).getBriefInfo()));
                }else{
                    System.out.println("帐号"+user.getUid()+"重复登录");
                }
            }else{
                LoginBo loginBo = UserService.userLoginConfirm(user);
                //客户端登录,新建对应的登录凭证
                if(loginBo!=null){
                    //给自己带上登录凭证
                    serverThread.setLoginBo(loginBo);
                    //将登录凭证加入服务器登录列表
                    Server.loginList.add(loginBo);
                    //将登录凭证传给客户端
                    serverThread.getOutput().writeObject(new StatusMessage(loginBo));
                    System.out.println("登录凭证["+loginBo+"]已发送,目前User在线数量:"+Server.loginList.size());
                    for(LoginBo l : Server.loginList){
                        System.out.println("登录用户Uid[" +l.getLoginUid()+"]\n");
                    }
                    //向在线用户广播某用户已上线
                    NotificationService.notifyUserStatus(serverThread, user);
                }else{
                    serverThread.getOutput().writeObject(new InfoMessage("帐号或密码错误"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理聊天msg
     * @param thread 要处理聊天信息的线程
     * @param msg 要处理的聊天信息
     */
    private static void handleChatMsg(ServerThread thread, Message msg){
        try{
            //将聊天信息发给对应群组的每个成员
            StatusCode e = MsgService.sendChatMsg((ChatMessage) msg).getStatusCode();
            switch (e){
                case NOT_FOUND_CODE:
                    thread.getOutput().writeObject(new InfoMessage("没有找到对应群组"));
                    break;
                case NO_PERMISSION_CODE:
                    thread.getOutput().writeObject(new InfoMessage("请先加入该群组"));
                    break;
                case SUCCESS_CODE:
                    if(ChatMsgService.addChatMsg((ChatMessage) msg).getStatusCode().equals(StatusCode.SUCCESS_CODE)){
                        System.out.println("聊天信息"+ msg +"已保存");
                    }else {
                        System.out.println("聊天信息"+ msg +"保存失败");
                    }
                    break;
                default:
            }
            System.out.println((msg.toString()+"处理完成"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理查询msg
     * @param msg infoMessage
     */
    private static void handleInfoMsg(ServerThread serverThread, Message msg){
        try{
            InfoMessage im = (InfoMessage) msg;
            switch (im.getInfoType()){
                case 'u':
                    //查找对应uid的用户信息
                    System.out.println("用户"+msg.getLoginBo()+"查询用户信息"+im.getSpecType());
                    for(String s : im.getSpecType()){
                        User user = UserService.queryUserById(s);
                        if(user!=null){
                            im.addInfo(user.toString());
                        }
                    }
                    break;
                case 'g':
                    //查找对应gid的群组信息
                    System.out.println("用户"+msg.getLoginBo()+"查询群组信息"+im.getSpecType());
                    for(String s : im.getSpecType()){
                        Group group = GroupService.queryGroupById(s);
                        if(group!=null){
                            im.addInfo(group.toString());
                        }
                    }
                    break;
                case 'c':
                    //查找对应gid的聊天信息
                    System.out.println("用户"+msg.getLoginBo()+"查询"+im.getSpecType()+"聊天信息");
                    ArrayList<String> msgList = ChatMsgService
                            .queryChatMsgByGroupId(im.getSpecType().get(0))
                            .stream()
                            .map(ChatMessage::toString)
                            .collect(Collectors.toCollection(ArrayList::new));
                    im.setInfoList(msgList);
                    break;
                default:
                    im.addInfo("infoType无效"+im.getInfoType());
                    System.out.println("infoType无效"+im.getInfoType());
            }
            serverThread.getOutput().writeObject(im);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
