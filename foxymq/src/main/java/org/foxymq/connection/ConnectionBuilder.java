package org.foxymq.connection;

import org.foxymq.message.IMessageHeader;

public class ConnectionBuilder {
    private Mode mode;
    private String host;
    private int port;

    private IMessageHeader header;
    private IMessageReceive messageReceive;
    private IConnection connectionInterface;

    public ConnectionBuilder(Mode mode) {
        this.mode = mode;
        this.host = "";
        this.port = 0;
    }

    public ConnectionBuilder host(String host) {
        this.host = host;
        return this;
    }

    public ConnectionBuilder port(int port) {
        this.port = port;
        return this;
    }

    public ConnectionBuilder header(IMessageHeader header) {
        this.header = header;
        return this;
    }

    public ConnectionBuilder callback(IMessageReceive messageReceive) {
        this.messageReceive = messageReceive;
        return this;
    }

    public ConnectionBuilder callback(IConnection connectionInterface) {
        this.connectionInterface = connectionInterface;
        return this;
    }

    public Connection build() {
        Connection c = new Connection(mode, host, port);
        c.setHeader(header);
        c.setMessageReceiveInterface(messageReceive);
        c.setConnectionInterface(connectionInterface);

        return c;
    }
}
