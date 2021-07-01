package Po.Server;

import Bo.LoginBo;
import Po.Common.Group;
import Po.Common.Message.ChatMessage;
import Po.Common.User;
import Utils.StorageUtils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * @author chenzhuohong
 */
public class Server extends Thread{

    /**
     * 服务器保存用户的文件路径
     */
    public final static String USER_FILE_PATH = "src/Po/Server/user.txt";

    /**
     * 服务器保存聊天信息的文件路径
     */
    public final static String MSG_FILE_PATH = "src/Po/Server/msg.txt";

    /**
     * 服务器保存群组的文件路径
     */
    public final static String GROUP_FILE_PATH = "src/Po/Server/group.txt";

    public static ArrayList<User> userList;

    public static ArrayList<LoginBo> loginList;

    public static ArrayList<Group> groupList;

    private ServerSocket serverSocket;

    public static ArrayList<ServerThread> threadList;

    public Server(int port){
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
        userList = StorageUtils.objToUser(StorageUtils.read(USER_FILE_PATH));
        System.out.println(userList);
        groupList = StorageUtils.objToGroup(StorageUtils.read(GROUP_FILE_PATH));
        System.out.println(groupList);
        ArrayList<ChatMessage> msgTmp = StorageUtils.objToMsg(StorageUtils.read(MSG_FILE_PATH));
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
//                监听客户端的请求
                socket = this.serverSocket.accept();
                System.out.println(socket+"已连接");
                ServerThread st = new ServerThread(socket);
                threadList.add(st);
                st.start();
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            StorageUtils.write(StorageUtils.groupToObj(groupList), GROUP_FILE_PATH, false);
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
