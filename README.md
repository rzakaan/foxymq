# foxymq

## About

**foxymq** is a message queue library developed with java. For experimental purposes.

## Example

```java
/*
 * Simple message encoding to byte array
 * The required class creation procedures are as follows.
 */

public class MessageTest extends Message {
    @MessageId
    public static final int messageId = 0x1;
    int integerData = Integer.MAX_VALUE;
}

public class MessageHeader extends IMessageHeader {
    public int messageId;

    @Override
    protected Message createMessage() {
        switch (messageId) {
            case MessageTest.messageId: return new MessageTest();
            default: return null;
        }
    }
}

public class TestMessage {
    @Test
    public static void main(String args[]) {
        MessageHeader header = new MessageHeader();
        header.messageId = 7;
        MessageTest messageTest = new MessageTest();
        messageTest.setMessageHeader(header);

        byte[] bytes = messageTest.encodeMessage();
        System.out.println(messageTest);
    }
}

```
