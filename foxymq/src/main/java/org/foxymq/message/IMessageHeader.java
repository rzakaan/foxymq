package org.foxymq.message;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;

public abstract class IMessageHeader extends IMessage {
    protected abstract Message createMessage();
}
