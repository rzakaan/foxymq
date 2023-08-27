package org.foxymq.connection;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
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
                    // readClient(socket);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void readClient(Socket socket) {
        new Thread(() -> {
            try {
                InputStream inputStream = socket.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader buffer = new BufferedReader(inputStreamReader);

                boolean bufferAvailable = true;
                while (bufferAvailable) {

                    // buffer.read();
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
