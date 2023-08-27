package org.foxymq.connection;

import org.foxymq.message.Message;

public interface IMessageReceive {
    void onMessageReceived(Message message);
}
