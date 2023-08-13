package org.foxymq.message;

import lombok.Data;
import org.foxymq.annotation.MessageId;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Enumeration;

/**
 * @author Riza Kaan Ucak
 * @apiNote This class usage for MessageBody and MessageHeader
 * @since 13.08.2023
 */
@Data
public abstract class IMessage {

    private ByteBuffer buffer;
    protected ByteOrder byteOrder;

    protected byte[] encode() {
        // calculate size
        Class clazz = getClass();
        int size = getSize();
        buffer = ByteBuffer.allocate(size);
        buffer.order(byteOrder);

        Field[] fields = clazz.getDeclaredFields();
        parentLoop:
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                Object obj = field.get(this);

                // System.out.println("Field: " + field.getName() + " = " + obj.toString());

                Annotation[] annotations = field.getDeclaredAnnotations();
                for (Annotation a : annotations) {
                    // System.out.println("Annotation :" + a.annotationType() + " : " + a.toString());
                    if (a instanceof MessageId)
                        continue parentLoop;
                }

                if (obj instanceof Byte) buffer.put((Byte) obj);
                else if (obj instanceof Short) buffer.putShort((Short) obj);
                else if (obj instanceof Integer) buffer.putInt((Integer) obj);
                else if (obj instanceof Long) buffer.putLong((Long) obj);
                else if (obj instanceof Float) buffer.putFloat((Float) obj);
                else if (obj instanceof Double) buffer.putDouble((Double) obj);
                else if (obj instanceof Character) buffer.putChar((Character) obj);
                else if (obj instanceof Boolean) {
                    byte v = (Boolean) obj ? (byte) 1 : 0;
                    buffer.put(v);
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        return buffer.array();
    }

    protected void decode(byte[] buffer) {

    }

    public IMessage() {
        this.byteOrder = ByteOrder.BIG_ENDIAN;
    }

    public int getSize() {
        int size = 0;
        FieldLoop:
        for (Field field : this.getClass().getDeclaredFields()) {
            try {
                field.setAccessible(true);
                Object obj = field.get(this);

                Annotation[] annotations = field.getDeclaredAnnotations();
                for (Annotation a : annotations) {
                    // System.out.println("Annotation :" + a.annotationType() + " : " + a.toString());
                    if (a instanceof MessageId)
                        continue FieldLoop;
                }

                if (obj instanceof Byte || obj instanceof Boolean) {
                    size++;
                }
                if (obj instanceof Character) {
                    size += 2;
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
