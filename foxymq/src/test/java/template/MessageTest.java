package template;

import org.foxymq.annotation.MessageId;
import org.foxymq.message.Message;

public class MessageTest extends Message {
    @MessageId
    public static final int messageId = 0xAB;
    boolean booleanData = false;
    byte byteData = 2;
    short shortData = 3;
    int intData = 4;
    long longData = 5;
    float floatData = 6;
    double doubleData =7;
    char charData = 'A';
}
