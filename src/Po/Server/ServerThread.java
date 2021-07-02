package Po.Server;

import Bo.LoginBo;
import Po.Common.Group;
import Po.Common.Message.ChatMessage;
import Po.Common.Message.InfoMessage;
import Po.Common.Message.Message;
import Po.Common.Message.StatusMessage;
import Po.Common.User;
import Utils.MsgUtils;
import Utils.StorageUtils;
import Utils.UserUtils;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
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

    @Override
    public void run() {
        try{
            while(!this.socket.isClosed()){
                Message msgTemp = (Message) this.input.readObject();
                switch (msgTemp.msgType){
                    //客户端注销信息
                    case 's':
                        //移除本次登录信息
                        Server.loginList.remove(msgTemp.loginBo);
                        this.output.close();
                        this.input.close();
                        this.socket.close();
                        System.out.println("用户"+this.loginBo.getLoginUid()+"注销");
                        System.out.println(Server.loginList.toString());
                        //通知注销用户的好友，该用户已下线
                        break;
                    case 'u':
                        User user = (User) msgTemp.getInfo();
                        //客户端修改用户信息
                        if(UserUtils.isOnline(user, Server.loginList)){
                            //防止同一用户在不同客户端登录造成修改用户信息的错误情况
                            if(this.loginBo.getLoginUid().equals(msgTemp.loginBo.getLoginUid())){
                                UserUtils.updateUser(user, msgTemp.loginBo);
                                //将用户和群组的修改保存进文件
                                StorageUtils.write(StorageUtils.userToObj(Server.userList), Server.USER_FILE_PATH, false);
                                StorageUtils.write(StorageUtils.groupToObj(Server.groupList), Server.GROUP_FILE_PATH, false);
                            }else{
                                System.out.println("帐号重复登录");
                            }
                        }else{
                            //客户端登录,新建登录信息
                            LoginBo loginBo = UserUtils.userLoginConfirm(user);
                            if(loginBo!=null){
                                //给自己带上登录信息
                                this.loginBo = loginBo;
                                //将登录信息加入服务器登录列表
                                Server.loginList.add(loginBo);
                                //将登录信息传给客户端
                                this.output.writeObject(new StatusMessage(loginBo));
                                System.out.println("登录信息["+loginBo+"]已发送,目前User在线数量:"+Server.loginList.size());
                                for(LoginBo l : Server.loginList){
                                    System.out.println("登录用户Uid[" +l.getLoginUid()+"]\n");
                                }
                                for(ServerThread st : Server.threadList){
                                    if(!st.loginBo.equals(this.loginBo)){
                                        st.output.writeObject(new InfoMessage(user.getUid()+"已上线"));
                                    }
                                }
                            }else{
                                this.output.writeObject(new InfoMessage("帐号或密码错误"));
                            }
                        }
                        break;
                    case 'c':
                        //将聊天信息发给对应群组的每个成员
                        int code = Group.sendMsg(msgTemp);
                        if(code == -1){
                            System.out.println("没有找到对应群组");
                            this.output.writeObject(new InfoMessage("没有找到对应群组"));
                        }else if(code == -2){
                            System.out.println("请先加入该群组");
                            this.output.writeObject(new InfoMessage("请先加入该群组"));
                        }else{
                            //将聊天信息保存入聊天记录的文件
                            StorageUtils.writeMsg((ChatMessage) msgTemp, Server.MSG_FILE_PATH);
                            System.out.println("聊天信息"+ msgTemp +"已发送");
                        }
                        break;
                    case 'i':
                        InfoMessage im = (InfoMessage) msgTemp;
                        switch (im.getInfoType()){
                            case 'u':
                                //查找对应uid的用户信息
                                System.out.println("用户"+msgTemp.loginBo.getLoginUid()+"查询用户信息"+im.getSpecType());
                                for(User u : Server.userList){
                                    if(im.getSpecType().get(0).equals(u.getUid())){
                                        im.addInfo(u.toString());
                                        break;
                                    }
                                }
                                break;
                            case 'g':
                                //查找对应gid的群组信息
                                System.out.println("用户"+msgTemp.loginBo.getLoginUid()+"查询群组信息"+im.getSpecType());
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
                                System.out.println("用户"+msgTemp.loginBo.getLoginUid()+"查询"+im.getSpecType()+"聊天信息");
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
                        break;
                    default:
                        System.out.println("msgType无效:"+msgTemp.msgType);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
//            System.out.println("线程异常");
        }
    }
}
