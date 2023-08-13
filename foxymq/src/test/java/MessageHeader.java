import org.foxymq.message.IMessageHeader;
import org.foxymq.message.Message;

public class MessageHeader extends IMessageHeader {
    public int messageId;

    @Override
    protected Message createMessage() {
        switch (messageId) {
            case MessageTest.messageId: return new MessageTest();
            case MessageArray.messageId: return new MessageArray();
            default: return null;
        }
    }
}
