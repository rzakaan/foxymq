package org.foxymq.message;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;

public abstract class IMessageHeader extends IMessage {
    protected abstract Message createMessage();

    @Override
    protected byte[] encode() {
        int size = getSize();
        System.out.println(String.format("%s size:%d", getClass().getName(), size));
        byte[] bytes = new byte[size];
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        buffer.order(byteOrder);

        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                Object obj = field.get(this);
                if (obj instanceof Byte ) buffer.put((Byte) obj);
                if (obj instanceof Short ) buffer.putShort((Short) obj);
                if (obj instanceof Integer ) buffer.putInt((Integer) obj);
                if (obj instanceof Long ) buffer.putLong((Long) obj);
                if (obj instanceof Float ) buffer.putFloat((Float) obj);
                if (obj instanceof Double ) buffer.putDouble((Double) obj);
                if (obj instanceof Character ) buffer.putChar((Character) obj);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return  bytes;
    }

    @Override
    protected void decode(byte[] bytes) {}
}
