package org.foxymq.connection;

public class ConnectionFactory {
    public static Connection createConnection(String host, int port) {
        return new Connection();
    }
}
