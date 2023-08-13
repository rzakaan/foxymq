package message;

import org.junit.jupiter.api.Test;
import template.MessageHeader;
import template.MessageTest;

public class TestMessage {
    @Test
    public void TestMessageEncode() {
        MessageHeader header = new MessageHeader();
        header.messageId = 7;
        MessageTest messageTest = new MessageTest();
        messageTest.setMessageHeader(header);

        // ByteBuffer buffer = ByteBuffer.allocate(bytes);
        byte[] bytes = messageTest.encodeMessage();
        System.out.println(messageTest);
    }
}
