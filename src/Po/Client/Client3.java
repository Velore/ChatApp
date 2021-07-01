package Po.Client;

public class Client3 extends Client{
    public Client3(String host, int port) {
        super(host, port);
    }

    public static void main(String[] args) {
        Client client3 = clientOnline("127.0.0.1", 6666);
    }
}
