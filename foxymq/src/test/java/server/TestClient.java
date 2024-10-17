package server;

import org.foxymq.connection.Connection;
import org.foxymq.connection.ConnectionBuilder;
import org.foxymq.connection.Mode;
import org.junit.jupiter.api.Test;

import template.MsgHeader;
import template.MsgTest;

public class TestClient {

    private static final int TEST_SOCK_PORT = 9998;

    @Test
    public void TestCreateClientSocket() {
        Connection client = new ConnectionBuilder(Mode.CLIENT)
                .header(new MsgHeader())
                .host("0.0.0.0")
                .port(TEST_SOCK_PORT)
                .build();

        client.start();

        MsgTest msgTest = new MsgTest();
        msgTest.setMessageHeader(new MsgHeader(MsgTest.messageId));
        msgTest.intData = 7;
        byte[] actualData = msgTest.encodeMessage();
        System.out.println(msgTest);

        client.sendMessageBroadcast(msgTest);

        try {

            System.in.read();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
