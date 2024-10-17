package template;

import org.foxymq.annotation.TotalSize;
import org.foxymq.message.IMessageHeader;
import org.foxymq.message.Message;

public class MsgHeader extends IMessageHeader {
    public int messageId;

    @TotalSize
    public static final int headerSize = 4;

    public MsgHeader() {
    }

    public MsgHeader(int messageId) {
        this.messageId = messageId;
    }

    @Override
    public Message createMessage() {
        switch (messageId) {
            case MsgTest.messageId:
                return new MsgTest();
            case MsgArray.messageId:
                return new MsgArray();
            default:
                return null;
        }
    }
}
