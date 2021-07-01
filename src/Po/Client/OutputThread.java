package Po.Client;

import Po.Common.Message.StatusMessage;
import Utils.MsgUtils;

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
            //发送登录信息
            oos.writeObject(this.client.outputMsg);
            oos.flush();
            oos.reset();
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
                    this.client.outputMsg = MsgUtils.msgAnalyse(MsgUtils.msgSplit(input), this.client.user);
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
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
