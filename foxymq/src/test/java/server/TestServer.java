package server;

import org.foxymq.connection.ConnectionFactory;
import org.foxymq.connection.IConnection;
import org.foxymq.connection.Mode;
import org.junit.jupiter.api.Test;

public class TestServer {
    @Test
    public void TestCreateServerSocket(){
        IConnection con = ConnectionFactory.createConnection("localhost", 8080);
        con.setMode(Mode.SERVER);
        con.start();

        // check socket
        System.out.println(String.format("Connection created %s:%d", con.getHost(), con.getPort()));
    }
}
