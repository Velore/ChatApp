package Po.Client;

import Po.Common.Message.StatusMessage;
import Po.Common.Message.UpdateMessage;
import Utils.MsgUtils;
import Utils.UserUtils;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Scanner;

/**
 * @author chenzhuohong
 */
public class OutputThread extends Thread{

    private final Client client;

    public OutputThread(Client client){
        this.client = client;
    }

    @Override
    public void run() {
        Scanner send = new Scanner(System.in);
        String input;
        try {
            ObjectOutputStream oos = new ObjectOutputStream(this.client.socket.getOutputStream());
            // 重新输入帐号和密码，直至服务器端传过来了一个loginBo，代表登录成功
            do{
                this.client.user = UserUtils.userLogin();
                this.client.outputMsg = new UpdateMessage(this.client.user);
                //发送登录信息
                oos.writeObject(this.client.outputMsg);
                oos.flush();
                synchronized (this){
                    this.wait(100);
                }
//                oos.reset();
            }while(this.client.inputMsg.loginBo==null);
            this.client.outputMsg.loginBo = this.client.loginBo;
            while (!this.client.socket.isClosed()){
                //客户端输入
                do {
                    System.out.println("请输入想发送的信息");
                    input = send.nextLine();
                }while (input == null);
                //判断是否输入注销命令
                if(!"end".equals(input)){
                    //非注销命令交给命令分析器处理
                    this.client.outputMsg = MsgUtils.inputAnalyse(MsgUtils.inputSplit(input), this.client.user);
                    //输入分析器将分析结果返回至交互对象
                    if(this.client.outputMsg == null){
                        System.out.println("输入分析失败");
                    }else{
                        //在交互对象中加入登录信息
                        this.client.outputMsg.loginBo = this.client.loginBo;
                        oos.writeObject(this.client.outputMsg);
                        oos.flush();
                        oos.reset();
                    }
                }else {
                    //客户端发送注销信息后离线
                    oos.writeObject(new StatusMessage(this.client.loginBo));
                    oos.flush();
                    oos.reset();
                    break;
                }
            }
            oos.close();
        } catch (IOException | InterruptedException e){
            e.printStackTrace();
        }
    }
}
