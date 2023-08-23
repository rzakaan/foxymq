package org.foxymq.message;

import lombok.Getter;
import lombok.Setter;

public class Message extends MessageEncoder implements IMessage {
    @Setter
    @Getter
    private IMessageHeader messageHeader;
    private byte[] encoded;

    public Message() {
    }

    @Override
    public byte[] encodeMessage() {
        // set sizes
        int payloadSize = getSize();
        int headerSize = messageHeader.getSize();
        int totalSize = payloadSize + headerSize;

        // encode message header
        put(messageHeader.encode());
        put(encode());

        // store the last encoded data
        encoded = data();
        return encoded;
    }

    @Override
    public void decodeMessage(byte[] bytes) {

    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(String.format("%s\n", getClass().getName()));
        str.append(String.format("  |-> header size  : %d\n", messageHeader.getSize()));
        str.append(String.format("  |-> total size   : %d\n", getSize()));
        str.append(String.format("  |-> byte buffer  : "));

        if (encoded != null)
            for (byte b : encoded) {
                str.append(String.format("%X ", b));
            }

        str.append("\n");

        return str.toString();
    }
}
