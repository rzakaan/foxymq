package message;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.util.HexFormat;

import org.junit.jupiter.api.Test;
import template.MsgHeader;
import template.MsgTest;

public class TestMessage {
    @Test
    public void TestSimpleMessageEncode() {
        // message header
        // contains integer field for messageId (4 byte)
        MsgHeader header = new MsgHeader();
        header.messageId = 0x01;

        // message id AA
        // contains one integer field (4 byte)
        MsgTest msg = new MsgTest();
        msg.setMessageHeader(header);
        byte[] actual = msg.encodeMessage();

        // expected byte array
        byte[] expected = HexFormat.ofDelimiter(":").parseHex("00:00:00:01:00:00:00:04");

        assertArrayEquals(expected, actual);
    }
}
