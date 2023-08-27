package template;

import org.foxymq.annotation.ByPass;
import org.foxymq.annotation.MessageId;
import org.foxymq.annotation.TotalSize;
import org.foxymq.message.Message;

public class MsgTest extends Message {
    @MessageId
    public static final int messageId = 0xAA;

    @TotalSize
    public static final int messageSize = 8;

    @ByPass
    int skipData = 0xff;
    int intData = 4;
}
