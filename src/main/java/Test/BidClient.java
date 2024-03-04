package Test;

import Controllers.Client.ViewBidUsers;
import Models.Bid;
import com.google.gson.Gson;
import javafx.application.Platform;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.UUID;

public class BidClient {
    private String host;
    private int port;
    private int auctionId;
    private ViewBidUsers viewBidUsers;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private UUID clientId; // Unique identifier for this client

    public BidClient(String host, int port, int auctionId, ViewBidUsers viewBidUsers) {
        this.host = host;
        this.port = port;
        this.auctionId = auctionId;
        this.viewBidUsers = viewBidUsers;
        this.clientId = UUID.randomUUID(); // Generate a unique identifier
    }

    public void connectBidClient(int auctionId) throws IOException {
        this.auctionId = auctionId;
        socket = new Socket(host, port);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        sendJoinMessage(); // Send a join message including the client ID

        // Start a new thread to listen for messages from the server
        new Thread(() -> {
            try {
                String fromServer;
                while ((fromServer = in.readLine()) != null) {
                    System.out.println("Received bid message from server: " + fromServer);
                    Bid bid = convertToBid(fromServer);
                    Platform.runLater(() -> {
                        System.out.println("Processing bid message on JavaFX thread.");
                        viewBidUsers.updateTableView(bid);
                        viewBidUsers.refreshHighestBid();
                    });
                }
            } catch (IOException e) {
                // Handle exception
            } finally {
                closeConnections();
            }
        }).start();
    }

    private void sendJoinMessage() {
        // Send a join message with the auction ID and the client's unique identifier
        out.println("JOIN " + auctionId + " " + clientId);
    }

    public void sendBid(String bidMessage) {
        out.println("BID " + bidMessage);
    }

    private Bid convertToBid(String bidUpdate) {
        Gson gson = new Gson();
        String jsonPart = bidUpdate.substring(bidUpdate.indexOf("{"));
        return gson.fromJson(jsonPart, Bid.class);
    }

    private void closeConnections() {
        try {
            if (out != null) out.close();
            if (in != null) in.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
