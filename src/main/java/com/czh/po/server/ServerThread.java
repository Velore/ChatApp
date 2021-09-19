package com.czh.po.server;

import com.czh.bo.LoginBo;
import com.czh.po.common.Group;
import com.czh.po.common.message.ChatMessage;
import com.czh.po.common.message.InfoMessage;
import com.czh.po.common.message.Message;
import com.czh.po.common.message.StatusMessage;
import com.czh.po.common.User;
import com.czh.utils.MsgUtils;
import com.czh.utils.StorageUtils;
import com.czh.utils.UserUtils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * 服务器端为每一个连接的客户端新建一个处理线程ServerThread
 * 功能包括接收服务器发送的Message和发送对应的结果Message
 * @author chenzhuohong
 */
public class ServerThread extends Thread{

    private final Socket socket;

    private LoginBo loginBo;

    private ObjectInputStream input;

    private ObjectOutputStream output;

    public String getUid(){
        return this.loginBo.getLoginUid();
    }

    public ObjectOutputStream getOutput(){
        return this.output;
    }

    public ServerThread(Socket socket) {
        this.socket = socket;
        try{
            this.input = new ObjectInputStream(this.socket.getInputStream());
            this.output = new ObjectOutputStream(this.socket.getOutputStream());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 处理接收到的msg
     * @param msg 接收的msg
     */
    private void handle(Message msg){
        switch (msg.getMsgType()) {
            case 's':
                handleStatusMsg(this, msg);
                break;
            case 'u':
                handleUpdateMsg(this, msg);
                break;
            case 'c':
                handleChatMsg(this, msg);
                break;
            case 'i':
                handleInfoMsg(msg);
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
    private void handleStatusMsg(ServerThread thread, Message msg){
        //移除本次登录信息
        Server.loginList.remove(msg.loginBo);
        try {
            thread.output.close();
            thread.input.close();
            thread.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("用户"+this.loginBo.getLoginUid()+"注销");
        System.out.println(Server.loginList.toString());
        //通知注销用户的好友，该用户已下线
    }

    /**
     * 处理用户信息，如更新密码等
     * @param thread 要处理的服务器线程
     * @param msg 要处理的用户信息
     */
    private void handleUpdateMsg(ServerThread thread, Message msg){
        try{
            User user = (User) msg.getInfo();
            //客户端修改用户信息
            if(UserUtils.isOnline(user, Server.loginList)){
                //防止同一用户在不同客户端登录造成修改用户信息的错误情况
                if(thread.loginBo.getLoginUid().equals(msg.loginBo.getLoginUid())){
                    UserUtils.updateUser(user, msg.loginBo);
                    //将用户和群组的修改保存进文件
                    StorageUtils.write(StorageUtils.userToObj(Server.userList), Server.USER_FILE_PATH, false);
                    StorageUtils.write(StorageUtils.groupToObj(Server.groupList), Server.GROUP_FILE_PATH, false);
                }else{
                    System.out.println("帐号重复登录");
                }
            }else{
                //客户端登录,新建对应的登录凭证
                LoginBo loginBo = UserUtils.userLoginConfirm(user);
                if(loginBo!=null){
                    //给自己带上登录凭证
                    this.loginBo = loginBo;
                    //将登录凭证加入服务器登录列表
                    Server.loginList.add(loginBo);
                    //将登录凭证传给客户端
                    thread.output.writeObject(new StatusMessage(loginBo));
                    System.out.println("登录凭证["+loginBo+"]已发送,目前User在线数量:"+Server.loginList.size());
                    for(LoginBo l : Server.loginList){
                        System.out.println("登录用户Uid[" +l.getLoginUid()+"]\n");
                    }
                    //向在线用户广播某用户已上线
                    for(ServerThread st : Server.threadList){
                        if(!st.loginBo.equals(thread.loginBo)){
                            st.output.writeObject(new InfoMessage(user.getUid()+"已上线"));
                        }
                    }
                }else{
                    thread.output.writeObject(new InfoMessage("帐号或密码错误"));
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
    private void handleChatMsg(ServerThread thread, Message msg){
        try{
            //将聊天信息发给对应群组的每个成员
            int code = Group.sendMsg(msg);
            if(code == -1){
                System.out.println("没有找到对应群组");
                thread.output.writeObject(new InfoMessage("没有找到对应群组"));
            }else if(code == -2){
                System.out.println("请先加入该群组");
                thread.output.writeObject(new InfoMessage("请先加入该群组"));
            }else{
                //将聊天信息保存入聊天记录的文件
                StorageUtils.writeMsg((ChatMessage) msg, Server.MSG_FILE_PATH);
                System.out.println("聊天信息"+ msg +"已发送");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理查询msg
     * @param msg infoMessage
     */
    private void handleInfoMsg(Message msg){
        try{
            InfoMessage im = (InfoMessage) msg;
            switch (im.getInfoType()){
                case 'u':
                    //查找对应uid的用户信息
                    System.out.println("用户"+msg.loginBo.getLoginUid()+"查询用户信息"+im.getSpecType());
                    for(User u : Server.userList){
                        if(im.getSpecType().get(0).equals(u.getUid())){
                            im.addInfo(u.toString());
                            break;
                        }
                    }
                    break;
                case 'g':
                    //查找对应gid的群组信息
                    System.out.println("用户"+msg.loginBo.getLoginUid()+"查询群组信息"+im.getSpecType());
                    for(String s : im.getSpecType()){
                        if(Group.findGroup(s, Server.groupList)){
                            for(Group g : Server.groupList){
                                if(g.getGid().equals(s)){
                                    im.addInfo(g.toString());
                                    break;
                                }
                            }
                        }else{
                            im.addInfo("没有找到群组"+s);
                        }
                    }
                    break;
                case 'c':
                    //查找对应gid的聊天信息
                    System.out.println("用户"+msg.loginBo.getLoginUid()+"查询"+im.getSpecType()+"聊天信息");
                    if(Group.findGroup(im.getSpecType().get(0), Server.groupList)){
                        for(Group g : Server.groupList){
                            if(g.getGid().equals(im.getSpecType().get(0))){
                                im.setInfoList(MsgUtils.chatMsgToString(g.getMsgList()));
                            }
                        }
                    }else{
                        im.addInfo("没有找到群组"+im.getSpecType().get(0));
                    }
                    break;
                default:
                    im.addInfo("infoType无效"+im.getInfoType());
                    System.out.println("infoType无效"+im.getInfoType());
            }
            this.output.writeObject(im);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try{
            while(!this.socket.isClosed()){
                Message msgTemp = (Message) this.input.readObject();
                handle(msgTemp);
            }
        }catch (Exception e){
            e.printStackTrace();
//            System.out.println("线程异常");
        }
    }
}
