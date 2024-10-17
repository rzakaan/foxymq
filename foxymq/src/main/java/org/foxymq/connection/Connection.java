package org.foxymq.connection;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import org.foxymq.message.IMessageHeader;
import org.foxymq.message.Message;
import lombok.Data;

@Data
public class Connection {
    // general
    private Mode mode;
    private String host;
    private int port;

    // server
    private ServerSocket serverSocket;
    private Map<Integer, Socket> clients;
    private boolean acceptClientAvailable;

    // interfaces
    private IConnection connectionInterface;
    private IMessageReceive messageReceiveInterface;

    // data
    private IMessageHeader header;

    public void init() {
        acceptClientAvailable = true;
        clients = new HashMap<>();
    }

    public Connection(Mode mode, int port) {
        this.host = "";
        this.mode = mode;
        this.port = port;
        init();
    }

    public Connection(Mode mode, String host, int port) {
        this.mode = mode;
        this.host = host;
        this.port = port;
        init();
    }

    private InetAddress getAddress() {
        try {
            if (host.isEmpty()) {
                return InetAddress.getLocalHost();
            } else {
                return InetAddress.getByName(host);
            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void start() {
        switch (mode) {
            case SERVER:
                startServer();
                break;
            case CLIENT:
                startClient();
                break;
            case BROADCAST:
                System.err.println("not implemented");
        }
    }

    private void startServer() {
        try {
            InetAddress address = getAddress();

            int backlog = 5;
            serverSocket = new ServerSocket(port, backlog, address);
            acceptClient(serverSocket);

            System.out.println(String.format("Server started on %s:%d", address.getHostAddress(), port));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void startClient() {
        try {
            InetAddress address = getAddress();
            Socket socket = new Socket(address, port);

            OutputStream outputStream = socket.getOutputStream();
            InputStream inputStream = socket.getInputStream();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // server methods
    private void acceptClient(ServerSocket server) {
        new Thread(() -> {
            while (acceptClientAvailable) {
                try {
                    Socket socket = serverSocket.accept();

                    // callback function with thread
                    new Thread(() -> {
                        connectionInterface.onClientConnected(socket);
                    }).start();

                    // save the socket for broadcast send message
                    clients.put(socket.getPort(), socket);

                    // read byte from socket
                    readClient(socket);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void readClient(Socket socket) {
        new Thread(() -> {
            try {
                InputStream is = socket.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(is);

                boolean bufferAvailable = true;
                while (bufferAvailable) {

                    IMessageHeader header = parseHeader(is);
                    Message m = parseMessage(is, header);

                    // callback interface
                    Message message = new Message();
                    message.setMessageHeader(header);
                    messageReceiveInterface.onMessageReceived(message);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private IMessageHeader parseHeader(InputStream stream) throws Exception {
        // not implemented -> header.getSize();
        int length = 4;
        byte[] bytes = new byte[8];
        stream.read(bytes, 0, 4);

        ByteBuffer buf = ByteBuffer.wrap(bytes);
        header.setBuffer(buf);
        // header.decode();
        return header;
    }

    private Message parseMessage(InputStream stream, IMessageHeader header) throws Exception {
        // parse message and decoding bytes
        Message msg = header.createMessage();
        Class clazz = msg.getClass();
        short size = 0;
        byte[] bytes;
        try {
            Method method = clazz.getDeclaredMethod("getSize");
            size = (short) method.invoke(msg);
            bytes = stream.readNBytes(size);
            msg.decodeMessage(bytes);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return msg;
    }

    public void sendMessage(Message message, Socket socket) {

    }

    public void sendMessageBroadcast(Message message) {
        for (Integer port : clients.keySet()) {
            try {
                Socket socket = clients.get(port);
                OutputStream outputStream = socket.getOutputStream();

                byte[] bytes = message.encodeMessage();
                outputStream.write(bytes);
                // outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    // client methods
}
