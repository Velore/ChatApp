package Po.Client;

/**
 * 客户端主启动类2
 * 主要是测试多用户登录和聊天
 * 直接运行
 * @author chenzhuohong
 */
public class Client2 extends Client{
    public Client2(String host, int port) {
        super(host, port);
    }

    public static void main(String[] args) {
        Client client2 = clientOnline("127.0.0.1", 6666);
    }
}
