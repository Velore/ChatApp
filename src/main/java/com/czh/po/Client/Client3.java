package com.czh.po.Client;

/**
 * 客户端主启动类3
 * 功能与客户端2相同
 * @author chenzhuohong
 */
public class Client3 extends Client{
    public Client3(String host, int port) {
        super(host, port);
    }

    public static void main(String[] args) {
        Client client3 = clientOnline("127.0.0.1", 6666);
    }
}
