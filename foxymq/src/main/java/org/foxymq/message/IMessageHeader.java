package org.foxymq.message;

public abstract class IMessageHeader extends MessageEncoder {
    public abstract Message createMessage();
}
