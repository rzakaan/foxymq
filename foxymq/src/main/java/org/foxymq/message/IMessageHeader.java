package org.foxymq.message;

public abstract class IMessageHeader extends MessageEncoder {
    protected abstract Message createMessage();
}
