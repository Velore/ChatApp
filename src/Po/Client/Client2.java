package Po.Client;

public class Client2 extends Client{
    public Client2(String host, int port) {
        super(host, port);
    }

    public static void main(String[] args) {
        Client client2 = clientOnline("127.0.0.1", 6666);
    }
}
