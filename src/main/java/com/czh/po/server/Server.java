package com.czh.po.server;

import com.czh.bo.LoginBo;
import com.czh.po.common.Group;
import com.czh.po.common.message.ChatMessage;
import com.czh.po.common.User;
import com.czh.utils.StorageUtils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * 服务器端主启动类
 * 直接运行即可
 * @author chenzhuohong
 */
public class Server extends Thread{

    /**
     * 服务器保存文件的前缀
     */
    public final static String PREFIX = "src/main/java/com/czh";

    /**
     * 服务器保存用户的文件路径
     */
    public final static String USER_FILE_PATH = PREFIX + "/po/Server/user.txt";

    /**
     * 服务器保存聊天信息的文件路径
     */
    public final static String MSG_FILE_PATH = PREFIX + "/po/Server/msg.txt";

    /**
     * 服务器保存群组的文件路径
     */
    public final static String GROUP_FILE_PATH = PREFIX + "/po/Server/group.txt";

    public static ArrayList<User> userList;

    public static ArrayList<LoginBo> loginList;

    public static ArrayList<Group> groupList;

    private ServerSocket serverSocket;

    public static ArrayList<ServerThread> threadList;

    public Server(int port){
        System.out.println("服务器启动中");
        //初始化列表
        userList = new ArrayList<>();
        loginList = new ArrayList<>();
        groupList = new ArrayList<>();
        initList();
        threadList = new ArrayList<>();
        try{
            this.serverSocket = new ServerSocket(port);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 将文件中的信息加载到列表中
     */
    public static void initList(){
        System.out.println("准备加载持久化文件");
        userList = StorageUtils.objToUser(StorageUtils.read(USER_FILE_PATH));
        for(User u : userList){
            System.out.println(u.toString()+"\n");
        }
        groupList = StorageUtils.objToGroup(StorageUtils.read(GROUP_FILE_PATH));
        for(Group g : groupList){
            System.out.println(g.toString()+"\n");
        }
        ArrayList<ChatMessage> msgTmp = StorageUtils.readMsg(MSG_FILE_PATH);
        //把聊天记录加载到对应群组的聊天记录上
        for(Group g : groupList){
            for(ChatMessage cm : msgTmp){
                if(cm.getGid().equals(g.getGid())){
                    g.addMsg(cm);
                }
            }
        }
    }

    @Override
    public void run(){
//        建立监听socket
        Socket socket;
        try{
            while (true){
                //监听客户端的请求
                socket = this.serverSocket.accept();
                System.out.println(socket+"已连接");
                //为连接的socket开启一个处理线程ServerThread
                ServerThread st = new ServerThread(socket);
                threadList.add(st);
                //ServerThread开始运行
                st.start();
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try {if(this.serverSocket!=null){
                this.serverSocket.close();
            }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Server server = new Server(6666);
        server.start();
    }

}
