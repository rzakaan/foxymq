package message;

import org.junit.jupiter.api.Test;
import template.MsgHeader;
import template.MsgTest;

public class TestMessage {
    @Test
    public void TestMessageEncode() {
        // message header
        MsgHeader header = new MsgHeader();
        header.messageId = 7;

        // message body
        MsgTest msg = new MsgTest();
        msg.setMessageHeader(header);
        byte[] bytes = msg.encodeMessage();
        System.out.println(msg);
    }
}
