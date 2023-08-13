package org.foxymq.connection;

import lombok.*;
import org.foxymq.message.Message;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

@Data
public abstract class IConnection {

    // server
    private ServerSocket serverSocket;
    private Map<Integer, Socket> clients;

    private String host;
    private int port;
    private boolean acceptClientAvailable;

    private MessageReceive messageReceive;
    private Mode mode;

    public IConnection() {
        mode = Mode.SERVER;
        acceptClientAvailable = true;
        clients = new HashMap<>();
    }

    public void start() {
        switch (mode) {
            case SERVER:
                startServer();
                break;
            case CLIENT:
                startClient();
                break;
        }
    }

    private void startServer() {
        try {
            serverSocket = new ServerSocket(port);
            acceptClient(serverSocket);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void acceptClient(ServerSocket server) {
        new Thread(()->{
            while (acceptClientAvailable) {
                try {
                    Socket socket = serverSocket.accept();
                    clients.put(socket.getPort(), socket);
                    readClient(socket);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void readClient(Socket socket) {
        new Thread(()-> {
            try {
                InputStream inputStream = socket.getInputStream();
                InputStreamReader inputStreamReader =  new InputStreamReader(inputStream);
                BufferedReader buffer = new BufferedReader(inputStreamReader);

                boolean bufferAvailable = true;
                while (bufferAvailable) {

                    // buffer.read();
                    // callback interface
                    Message message = new Message();
                    messageReceive.onMessageReceived(message);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void sendMessage(Message message, Socket socket) {

    }

    private void sendMessageBroadcast(Message message) {
        for(Integer port : clients.keySet()){
            try
            {
                Socket socket = clients.get(port);
                OutputStream outputStream = socket.getOutputStream();

                byte[] bytes = message.encodeMessage();
                outputStream.write(bytes);
                // outputStream.close();
            } catch (Exception e ) {
                e.printStackTrace();
            }

        }
    }


    private void startClient() {

    }
}
