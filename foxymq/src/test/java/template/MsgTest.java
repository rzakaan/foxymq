package template;

import org.foxymq.annotation.ByPass;
import org.foxymq.annotation.MessageId;
import org.foxymq.annotation.TotalSize;
import org.foxymq.message.Message;

public class MsgTest extends Message {
    @MessageId
    public static final int messageId = 0xAB;

    @TotalSize
    public static final int messageSize = 5;

    @ByPass
    int skipData = 0xff;

    boolean booleanData = true;
    byte byteData = 0x7F;
    short shortData = 3;
    int intData = 4;
    long longData = 5;
    float floatData = 6;
    double doubleData = 7;
    char charData = 'A';
}
