import org.foxymq.connection.ConnectionFactory;
import org.foxymq.connection.IConnection;
import org.foxymq.connection.MessageReceive;
import org.foxymq.connection.Mode;
import org.foxymq.message.Message;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;

public class TestCase {

    @Test
    public void TestCase(){
        IConnection con = ConnectionFactory.createConnection("localhost", 8080);
        con.start();
        con.setMode(Mode.SERVER);
        con.setMessageReceive(message -> {
            System.out.println("message received !");
        });

        // System.out.println(String.format("Connection %s:%d", con.getHost(), con.getPort()));
    }

    @Test
    public void TestMessageEncode() {
        MessageHeader header = new MessageHeader();
        header.messageId = 7;
        MessageTest messageTest = new MessageTest();
        messageTest.setMessageHeader(header);

        // ByteBuffer buffer = ByteBuffer.allocate(bytes);
        byte[] bytes = messageTest.encode();
        System.out.printf("Bytes:0x ");
        for(byte b : bytes) {
            System.out.printf("%x ", b);
        }
    }
}
