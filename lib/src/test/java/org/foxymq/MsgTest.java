package org.foxymq.message;

public class MsgTest extends IMessage {
    private static final int ID = 1;
    private byte age;
    
    public setAge(byte age) {
        this.age = age;
    }

    public byte getAge() {
        return this.age;
    }

    @java.lang.Override
    public byte[] encode() {
        return new byte[0];
    }

    @java.lang.Override
    public byte[] decode() {
        return new byte[0];
    }
}
