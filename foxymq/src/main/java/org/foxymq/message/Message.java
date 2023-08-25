package org.foxymq.message;

import java.nio.ByteBuffer;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

public class Message extends MessageEncoder implements IMessage {
    @Setter
    @Getter
    private IMessageHeader messageHeader;

    private ByteBuffer encoded;
    private LocalDateTime encodedTime = LocalDateTime.MIN;
    private LocalDateTime decodedTime = LocalDateTime.MIN;

    private int headerSize = 0;
    private int payloadSize = 0;

    public Message() {
    }

    @Override
    public byte[] encodeMessage() {
        // set size and byte buffer for encoded data
        payloadSize = getSize();
        headerSize = messageHeader.getSize();

        encoded = ByteBuffer.allocate(headerSize + payloadSize);
        encoded.put(messageHeader.encode());
        encoded.put(encode());
        encodedTime = LocalDateTime.now();
        return encoded.array();
    }

    @Override
    public void decodeMessage(byte[] bytes) {
        decodedTime = LocalDateTime.now();
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(String.format("%s\n", getClass().getName()));
        str.append(String.format("  |-> header  size  : %d\n", headerSize));
        str.append(String.format("  |-> payload size   : %d\n", payloadSize));
        str.append(String.format("  |-> total   size   : %d\n", headerSize + payloadSize));

        if (encoded != null) {
            str.append(String.format("  |-> byte buffer  : "));
            for (byte b : encoded.array()) {
                str.append(String.format("%X ", b));
            }
        }
        str.append("\n");

        return str.toString();
    }

    @Override
    public LocalDateTime encodedTime() {
        return encodedTime;
    }

    @Override
    public LocalDateTime decodedTime() {
        return decodedTime;
    }
}
