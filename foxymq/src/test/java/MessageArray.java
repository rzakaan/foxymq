import org.foxymq.annotation.ArraySizeField;
import org.foxymq.annotation.MessageId;
import org.foxymq.message.Message;

import java.util.ArrayList;
import java.util.List;

public class MessageArray extends Message {
    @MessageId
    public static final int messageId = 2;

    @ArraySizeField("integerList")
    private byte listSize;
    private List<Integer> integerList = new ArrayList<>();
}
