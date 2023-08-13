package org.foxymq.message;

import lombok.Getter;
import lombok.Setter;
import org.foxymq.annotation.MessageId;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;

public class Message extends IMessage {
    @Setter
    @Getter
    private IMessageHeader messageHeader;
    private ByteBuffer buffer;

    public Message() {
    }

    public byte[] encodeMessage() {
        // set sizes
        int payloadSize = getSize();
        int headerSize = messageHeader.getSize();
        int totalSize = payloadSize + headerSize;

        // buffer and message header
        buffer = ByteBuffer.allocate(totalSize);
        buffer.order(byteOrder);

        // encode message header
        buffer.put(messageHeader.encode());
        buffer.put(encode());

        return buffer.array();
    }

    public void decodeMessage(byte[] buffer) {

    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(String.format("%s\n",getClass().getName()));
        str.append(String.format("  |-> header size  : %d\n", messageHeader.getSize()));
        str.append(String.format("  |-> total size   : %d\n", getSize()));
        str.append(String.format("  |-> byte buffer  : "));
        for(byte b : buffer.array()) {
            str.append(String.format("%X ", b));
        }
        str.append("\n");

        return str.toString();
    }
}
