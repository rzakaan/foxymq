package org.foxymq.connection;

import org.foxymq.message.Message;

public interface MessageReceive {
    void onMessageReceived(Message message);
}
