package template;

import org.foxymq.message.IMessageHeader;
import org.foxymq.message.Message;

public class MsgHeader extends IMessageHeader {
    public int messageId;

    @Override
    protected Message createMessage() {
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
