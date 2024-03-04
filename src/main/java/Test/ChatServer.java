package Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ChatServer {
    private static final int PORT = 8000;
    private static final Map<Integer, ChatRoom> auctionRooms = new ConcurrentHashMap<>();

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Chat Server is running on port " + PORT);

        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                new Thread(new ClientHandler(clientSocket)).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static class ChatRoom {
        private final Set<ClientHandler> clients = ConcurrentHashMap.newKeySet();

        synchronized void broadcastMessage(String message, ClientHandler sender) {
            for (ClientHandler client : clients) {
                if (client != sender) {
                    client.sendMessage(message);
                }
            }
        }

        void addClient(ClientHandler client) {
            clients.add(client);
        }

        void removeClient(ClientHandler client) {
            clients.remove(client);
        }
    }

    private static class ClientHandler implements Runnable {
        private final Socket socket;
        private PrintWriter out;
        private BufferedReader in;
        private ChatRoom currentRoom;
        private int auctionId;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);


                String fromClient;
                while ((fromClient = in.readLine()) != null) {
                    if (fromClient.startsWith("JOIN ")) {
                        handleJoinMessage(fromClient);
                    } else {
                        if (currentRoom != null) {
                            currentRoom.broadcastMessage(fromClient, this);
                        }
                        // Remove this line to prevent echoing the message back to the sender
                        // sendMessage(fromClient);
                    }
                }
            } catch (IOException e) {
                if (currentRoom != null) {
                    currentRoom.removeClient(this);
                }
                e.printStackTrace();
            } finally {
                closeConnections();
            }
        }

        private void handleJoinMessage(String message) {
            try {
                this.auctionId = Integer.parseInt(message.substring(5));
                currentRoom = auctionRooms.computeIfAbsent(auctionId, k -> new ChatRoom());
                currentRoom.addClient(this);
            } catch (NumberFormatException e) {
                System.out.println("Invalid auction ID received: " + message);
            }
        }

        void sendMessage(String message) {
            out.println(message);
        }

        private void closeConnections() {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}