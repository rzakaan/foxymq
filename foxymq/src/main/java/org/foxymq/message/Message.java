package org.foxymq.message;

import lombok.Getter;
import lombok.Setter;
import org.foxymq.annotation.MessageId;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;

public class Message extends IMessage {
    @Setter
    @Getter
    private IMessageHeader messageHeader;
    private ByteBuffer buffer;

    public Message() {
    }

    @Override
    public byte[] encode() {
        // set sizes
        Class clazz = getClass();
        int payloadSize = getSize();
        int headerSize = messageHeader.getSize();
        int messageSize = payloadSize + headerSize;

        // buffer and message header
        buffer = ByteBuffer.allocate(messageSize);
        buffer.order(byteOrder);

        // encode message header
        buffer.put(messageHeader.encode());

        System.out.println(String.format("%s size:%d", getClass().getName(), messageSize));

        Field[] fields = clazz.getDeclaredFields();
        parentLoop:
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                Object obj = field.get(this);

                // System.out.println("Field: " + field.getName() + " = " + obj.toString());

                Annotation[] annotations = field.getDeclaredAnnotations();
                for(Annotation a : annotations) {
                    // System.out.println("Annotation :" + a.annotationType() + " : " + a.toString());
                    if (a instanceof MessageId)
                        continue parentLoop;
                }

                if (obj instanceof Byte ) buffer.put((Byte) obj);
                else if (obj instanceof Short ) buffer.putShort((Short) obj);
                else if (obj instanceof Integer ) buffer.putInt((Integer) obj);
                else if (obj instanceof Long ) buffer.putLong((Long) obj);
                else if (obj instanceof Float ) buffer.putFloat((Float) obj);
                else if (obj instanceof Double ) buffer.putDouble((Double) obj);
                else if (obj instanceof Character ) buffer.putChar((Character) obj);
                else if (obj instanceof Boolean ) {
                    byte v = (Boolean) obj ? (byte) 1 : 0;
                    buffer.put(v);
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        return buffer.array();
    }

    @Override
    public void decode(byte[] buffer) {

    }
}
