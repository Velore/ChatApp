package com.czh.service;

import com.czh.bo.LoginBo;
import com.czh.po.client.Client;
import com.czh.po.common.Group;
import com.czh.po.common.StatusCode;
import com.czh.po.common.User;
import com.czh.po.common.message.ChatMessage;
import com.czh.po.common.message.InfoMessage;
import com.czh.po.common.message.Message;
import com.czh.po.common.message.StatusMessage;
import com.czh.po.server.Server;
import com.czh.po.server.ServerCallable;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * 处理消息的服务
 * @author chenzhuohong
 */
public class MsgService {

    /**
     * 服务器端接收到客户端发送的注销Message后
     * 发送带有该uid的LoginBo给客户端表示服务器端收到注销信息
     */
    public static String SIGN_OUT_UID = "0000";

    /**
     * 客户端处理接收到的Message;
     * @param client 客户端
     */
    public static void handleMsg(Client client){
        System.out.println("接收到服务器消息,类型为"+client.inputMsg.getMsgType());
        switch (client.inputMsg.getMsgType()){
            case STATUS_TYPE:
                //登录时loginBo为null
                if(client.loginBo==null){
                    //接收到服务器发送的登录信息，传递给Client，Client再传递给输出线程
                    client.loginBo = client.inputMsg.getLoginBo();
                    System.out.println("登录信息" + client.loginBo);
                }else if(SIGN_OUT_UID.equals(client.loginBo.getLoginUid())){
                    try{
                        client.socket.close();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                break;
            case CHAT_TYPE:
                //接收到聊天信息，输出在屏幕上，输出格式：[群组gid](发送时间)成员uid:{聊天内容}
                System.out.println(client.inputMsg);
                break;
            case INFO_TYPE:
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
     * 服务器处理接收到的Message;
     * @param msg 接收的msg
     */
    public static void handleMsg(ServerCallable serverCallable, Message msg){
        System.out.println("接收到客户端["+msg.getLoginBo()+"]消息,类型:"+msg.getMsgType());
        switch (msg.getMsgType()) {
            case STATUS_TYPE:
                handleStatusMsg(serverCallable, msg);
                break;
            case UPDATE_TYPE:
                handleUpdateMsg(serverCallable, msg);
                break;
            case CHAT_TYPE:
                handleChatMsg(serverCallable, msg);
                break;
            case INFO_TYPE:
                handleInfoMsg(serverCallable, msg);
                break;
            default:
                System.out.println("msgType无效:" + msg.getMsgType());
        }
    }

    /**
     * 处理状态信息，如注销;
     * @param thread 要处理的服务器线程
     * @param msg 要处理的状态信息
     */
    private static void handleStatusMsg(ServerCallable thread, Message msg){
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
        System.out.println();
        System.out.println("用户"+thread.getLoginBo().getLoginUid()+"注销");
        //移除本次登录信息
        Server.loginList.removeIf(loginBo -> msg.getLoginBo().equals(loginBo));
        Server.threadList.removeIf(serverCallable -> serverCallable.getSocket().isClosed());
        System.out.println(Server.loginList.toString());
        //通知注销用户的好友，该用户已下线
        NotificationService.notifyUserStatus(thread.getUid(), "已注销");
    }

    /**
     * 服务器端处理用户信息，如更新密码等;
     * @param thread 要处理的服务器线程
     * @param msg 要处理的用户信息
     */
    private static void handleUpdateMsg(ServerCallable thread, Message msg){
        try{
            User user = (User) msg.getInfo();
            System.out.println(user);
            //msg的loginBo为空,还没登录
            if(msg.getLoginBo()==null){
                //服务器端 -> 验证客户端的登录信息, 若信息正确则生成登录凭证
                LoginBo loginBo = UserService.userLoginConfirm(user);
                if(loginBo==null){
                    //loginBo为空说明帐号或密码错误
                    System.out.println(thread.getSocket()+", 登录:帐号或密码错误");
                    thread.getOutput().writeObject(new InfoMessage("帐号或密码错误"));
                } else if(UserService.userOnline(user.getUid(), Server.loginList)){
                    //若该用户已登录，则返回重复登录
                    //防止同一用户在不同客户端登录可能造成修改用户信息的错误情况
                    System.out.println(thread.getSocket()+", 帐号"+user.getUid()+"重复登录");
                    thread.getOutput().writeObject(new InfoMessage("帐号"+user.getUid()+"重复登录"));
                }else{
                    //更新用户最新登录时间
                    UserService.updateUser(new User(), loginBo);
                    //给自己带上登录凭证
                    thread.setLoginBo(loginBo);
                    //将登录凭证加入服务器登录列表
                    Server.loginList.add(loginBo);
                    //将登录凭证传给客户端
                    thread.getOutput().writeObject(new StatusMessage(loginBo));
                    System.out.println("登录凭证["+loginBo+"]已发送,目前User在线数量:"+Server.loginList.size());
                    System.out.print("登录用户：");
                    for(LoginBo l : Server.loginList){
                        System.out.print("Uid[" +l.getLoginUid()+"]\t");
                    }
                    //向在线用户广播某用户已上线
                    NotificationService.notifyUserStatus(thread.getUid(), "已上线");
                }
            } else {
                //客户端修改用户信息
                System.out.println("serverThread:"+ thread.getLoginBo());
                System.out.println("msg:"+msg.getLoginBo());
                if(thread.getLoginBo().getLoginUid().equals(msg.getLoginBo().getLoginUid())){
                    thread.getOutput().writeObject(
                            new InfoMessage(
                                    UserService.updateUser(user, msg.getLoginBo()).getBriefInfo()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理聊天msg;
     * @param thread 要处理聊天信息的线程
     * @param msg 要处理的聊天信息
     */
    private static void handleChatMsg(ServerCallable thread, Message msg){
        try{
            //将聊天信息发给对应群组的每个成员
            StatusCode e = ChatMsgService.sendChatMsg((ChatMessage) msg).getStatusCode();
            switch (e){
                case NOT_FOUND_CODE:
                    thread.getOutput().writeObject(new InfoMessage("没有找到对应群组"));
                    break;
                case NO_PERMISSION_CODE:
                    thread.getOutput().writeObject(new InfoMessage("请先加入该群组"));
                    break;
                case SUCCESS_CODE:
                    if(ChatMsgService.addChatMsg((ChatMessage) msg).getStatusCode()
                            .equals(StatusCode.SUCCESS_CODE)){
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
     * 处理查询msg;
     * @param msg infoMessage
     */
    private static void handleInfoMsg(ServerCallable serverCallable, Message msg){
        try{
            InfoMessage im = (InfoMessage) msg;
            switch (im.getInfoType()){
                case 'u':
                    //查找对应uid的用户信息
                    System.out.println("用户"+msg.getLoginBo()+"查询用户信息"+im.getSpecType());
                    for(String s : im.getSpecType()){
                        User user = UserService.queryUserById(s);
                        //若用户存在, 返回[用户信息], 否则返回["用户不存在"]
                        im.addInfo((user!=null)?user.toString():"用户不存在");
                    }
                    break;
                case 'g':
                    //查找对应gid的群组信息
                    System.out.println("用户"+msg.getLoginBo()+"查询群组信息"+im.getSpecType());
                    for(String s : im.getSpecType()){
                        Group group = GroupService.queryGroupById(s);
                        //若群组存在, 返回[群组信息], 否则返回["群组不存在"]
                        im.addInfo((group!=null)?group.toString():"群组不存在");
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
            serverCallable.getOutput().writeObject(im);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
