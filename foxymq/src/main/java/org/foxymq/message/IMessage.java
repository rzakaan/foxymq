package org.foxymq.message;

import lombok.Data;

import java.lang.reflect.Field;
import java.nio.ByteOrder;
import java.util.Enumeration;

/**
 * @author Riza Kaan Ucak
 * @apiNote This class usage for MessageBody and MessageHeader
 * @since 13.08.2023
 * */
@Data
public abstract class IMessage {

    protected ByteOrder byteOrder;

    protected abstract byte[] encode();

    protected abstract void decode(byte[] buffer);

    public IMessage() {
        this.byteOrder = ByteOrder.BIG_ENDIAN;
    }

    public int getSize() {
        int size = 0;
        for (Field field : this.getClass().getDeclaredFields()) {
            try {
                field.setAccessible(true);
                Object obj = field.get(this);
                if (obj instanceof Byte || obj instanceof Boolean || obj instanceof Character ) {
                    size++;
                } else if (obj instanceof Short) {
                    size += 2;
                } else if (obj instanceof Integer || obj instanceof Float) {
                    size += 4;
                } else if (obj instanceof Long || obj instanceof Double) {
                    size += 8;
                } else if (obj instanceof String) {
                    // +1 because \0 escape character
                    size += ((String) obj).length() + 1;
                } else if (obj instanceof Enumeration) {
                    size++;
                } else if (obj instanceof Object[]) {
                    // array found
                    size += ((Object[]) obj).length;
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        return size;
    }

}
