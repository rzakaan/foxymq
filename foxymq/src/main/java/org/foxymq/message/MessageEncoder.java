package org.foxymq.message;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.AccessLevel;

import org.foxymq.annotation.MessageId;
import org.foxymq.annotation.PayloadSize;
import org.foxymq.annotation.TotalSize;
import org.foxymq.annotation.ByPass;

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
public abstract class MessageEncoder {

    @Getter(AccessLevel.PROTECTED)
    @Setter(AccessLevel.PROTECTED)
    private ByteOrder byteOrder;

    private ByteBuffer buffer;

    public MessageEncoder() {
        this.byteOrder = ByteOrder.BIG_ENDIAN;
    }

    protected void initBuffer(int size) {
        if (buffer == null) {
            buffer = ByteBuffer.allocate(size);
        }
    }

    protected byte[] encode() {
        // calculate size
        int size = getSize();
        initBuffer(size);

        Class clazz = getClass();
        Field[] fields = clazz.getDeclaredFields();
        parentLoop: for (Field field : fields) {
            try {
                field.setAccessible(true);
                Object obj = field.get(this);

                // System.out.println("Field: " + field.getName() + " = " + obj.toString());

                Annotation[] annotations = field.getDeclaredAnnotations();
                for (Annotation a : annotations) {
                    if (isPassField(a)) {
                        continue parentLoop;
                    }
                }

                if (obj instanceof Byte)
                    buffer.put((Byte) obj);
                else if (obj instanceof Short)
                    buffer.putShort((Short) obj);
                else if (obj instanceof Integer)
                    buffer.putInt((Integer) obj);
                else if (obj instanceof Long)
                    buffer.putLong((Long) obj);
                else if (obj instanceof Float)
                    buffer.putFloat((Float) obj);
                else if (obj instanceof Double)
                    buffer.putDouble((Double) obj);
                else if (obj instanceof Character)
                    buffer.putChar((Character) obj);
                else if (obj instanceof Boolean) {
                    byte v = (Boolean) obj ? (byte) 1 : 0;
                    buffer.put(v);
                } else if (obj instanceof Record) {
                    // not implemented
                }

            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        return buffer.array();
    }

    protected void decode(byte[] buffer) {

    }

    private static int getSize(Object object) {
        int size = 0;
        Class clazz = object.getClass();
        FieldLoop: for (Field field : clazz.getDeclaredFields()) {
            try {
                field.setAccessible(true);
                Object obj = field.get(object);

                Annotation[] annotations = field.getDeclaredAnnotations();
                for (Annotation a : annotations) {
                    // System.out.println("Annotation :" + a.annotationType() + " : " +
                    // a.toString());
                    if (isPassField(a)) {
                        continue FieldLoop;
                    }
                }

                if (obj instanceof Byte || obj instanceof Boolean) {
                    size++;
                } else if (obj instanceof Character) {
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
                } else if (obj instanceof IMessageHeader) {
                    continue FieldLoop;
                    // size += getSize(obj.getClass());
                    // change implement
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

    private static boolean isPassField(Annotation a) {
        if (a instanceof MessageId || a instanceof ByPass
                || a instanceof PayloadSize || a instanceof TotalSize) {
            return true;
        }
        return false;
    }

    public byte[] data() {
        return (buffer != null) ? buffer.array() : new byte[1];
    }

    protected int getSize() {
        return getSize(this);
    }

    protected void put(char val) {
        buffer.putChar(val);
    }

    protected void put(byte val) {
        buffer.put(val);
    }

    protected void put(short val) {
        buffer.putInt(val);
    }

    protected void put(int val) {
        buffer.putInt(val);
    }

    protected void put(long val) {
        buffer.putLong(val);
    }

    protected void put(float val) {
        buffer.putFloat(val);
    }

    protected void put(double val) {
        buffer.putDouble(val);
    }

    protected void put(byte[] val) {
        buffer.put(val);
    }
}
