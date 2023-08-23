package org.foxymq.message;

public interface IMessage {
    public byte[] encodeMessage();

    public void decodeMessage(byte[] bytes);
}
