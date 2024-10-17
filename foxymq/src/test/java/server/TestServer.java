package server;

import java.net.Socket;
import org.foxymq.connection.Connection;
import org.foxymq.connection.ConnectionBuilder;
import org.foxymq.connection.IConnection;
import org.foxymq.connection.IMessageReceive;
import org.foxymq.connection.Mode;
import org.foxymq.message.Message;
import org.junit.jupiter.api.Test;

import template.MsgHeader;
import template.MsgTest;

public class TestServer {
    private static final int TEST_SOCK_PORT = 9998;
    IMessageReceive messageReceive = new IMessageReceive() {
        @Override
        public void onMessageReceived(Message message) {
            System.out.println(String.format("%s message received", message.toString()));
        }
    };

    IConnection connectionRecevie = new IConnection() {
        @Override
        public void onClientConnected(Socket socket) {
            System.out.println(String.format("Client connect from (:%d)", socket.getPort()));
        }

        @Override
        public void onClientDisconnected(Socket socket) {
            System.out.println(String.format("Client(%d) disconnect", socket.getPort()));
        }
    };

    public void wait(int mseconds) {
        try {
            Thread.sleep(mseconds);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void TestCreateServerSocket() {

        Connection server = new ConnectionBuilder(Mode.SERVER)
                .port(TEST_SOCK_PORT)
                .header(new MsgHeader())
                .callback(connectionRecevie)
                .callback(messageReceive)
                .build();

        server.start();

        wait(1);

        try {

            System.in.read();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
