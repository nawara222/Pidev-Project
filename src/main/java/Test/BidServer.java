package Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class BidServer {
    private static final int PORT = 8001;
    private static final Map<Integer, BidRoom> bidRooms = new ConcurrentHashMap<>();

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Bid Server is running on port " + PORT);
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                new Thread(new ClientHandler(clientSocket)).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static class BidRoom {
        private final Map<PrintWriter, UUID> clients = new ConcurrentHashMap<>();

        void broadcastBid(String bidMessage, UUID senderId) {
            clients.entrySet().stream()
                    .filter(entry -> !entry.getValue().equals(senderId))
                    .forEach(entry -> {
                        entry.getKey().println(bidMessage);
                        System.out.println("Sent bid message to a client: " + bidMessage);
                    });
        }

        void addClient(PrintWriter client, UUID clientId) {
            clients.put(client, clientId);
        }

        void removeClient(PrintWriter client) {
            clients.remove(client);
        }
    }

    private static class ClientHandler implements Runnable {
        private final Socket socket;
        private PrintWriter out;
        private BufferedReader in; // Add BufferedReader for reading from the client
        private BidRoom currentRoom;
        private int auctionId;
        private UUID clientId;
        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream())); // Initialize BufferedReader
                // Read join message from client
                String joinMessage = in.readLine(); // Read join message from client
                handleJoinMessage(joinMessage);

                String fromClient;
                while ((fromClient = in.readLine()) != null) {
                    if (fromClient.startsWith("BID ")) {
                        // Handle bid message from client
                        // Example: BID {"auctionId":123,"bidAmount":50,"userId":456}
                        handleBidMessage(fromClient);
                    }
                }
            } catch (IOException e) {
                // Handle exception
            } finally {
                // Close connections
                if (out != null) {
                    out.close();
                }
                if (in != null) { // Close BufferedReader
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                // Remove client from room
                if (currentRoom != null) {
                    currentRoom.removeClient(out);
                }
            }
        }
        private void handleJoinMessage(String joinMessage) {
            // Extract auctionId and clientId from join message
            // Example: JOIN 123 clientId
            String[] parts = joinMessage.split(" ");
            if (parts.length == 3) {
                try {
                    auctionId = Integer.parseInt(parts[1]);
                    clientId = UUID.fromString(parts[2]);
                    currentRoom = bidRooms.computeIfAbsent(auctionId, k -> new BidRoom());
                    currentRoom.addClient(out, clientId);
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid join message: " + joinMessage);
                }
            } else {
                System.out.println("Invalid join message format: " + joinMessage);
            }
        }

        private void handleBidMessage(String bidMessage) {
            // When broadcasting, pass the clientId
            if (currentRoom != null) {
                currentRoom.broadcastBid(bidMessage, this.clientId);
            } else {
                System.out.println("No room found for auction ID: " + auctionId);
            }
        }
    }
}