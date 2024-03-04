package Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.function.Consumer;

public class ChatClient {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private Consumer<String> onMessageReceived;
    private int auctionId; // Add auction ID

    public ChatClient(String address, int port, int auctionId, Consumer<String> onMessageReceived) throws IOException {
        this.onMessageReceived = onMessageReceived;
        this.auctionId = auctionId; // Initialize auction ID
        socket = new Socket(address, port);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        new Thread(this::run).start();

        // Send auction ID to server immediately after connecting
        sendMessage("JOIN " + auctionId);
    }
    public void closeConnection() {
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void run() {
        try {
            String fromServer;
            while ((fromServer = in.readLine()) != null) {
                onMessageReceived.accept(fromServer);
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Handle disconnection or server issues here
        }
    }

    public void sendMessage(String msg) {
        out.println(msg);
    }
}
