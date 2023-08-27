package org.foxymq.connection;

import java.net.Socket;

public interface IConnection {
    void onClientConnected(Socket socket);

    void onClientDisconnected(Socket socket);
}