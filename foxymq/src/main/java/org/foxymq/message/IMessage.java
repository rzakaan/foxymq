package org.foxymq.message;

import java.time.LocalDateTime;

public interface IMessage {
    public byte[] encodeMessage();

    public void decodeMessage(byte[] bytes);

    public LocalDateTime encodedTime();

    public LocalDateTime decodedTime();
}
