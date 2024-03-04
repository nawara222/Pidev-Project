package Controllers.ChatClient;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import Models.ChatMessage;
import Test.ChatClient;

import java.io.IOException;

public class ChatClientController {
    @FXML
    private TextField inputField;
    @FXML
    private ListView<ChatMessage> messageListView;
    private ChatClient chatClient;
    private int auctionId;

    public void setAuctionId(int auctionId) {
        this.auctionId = auctionId;
        if (chatClient != null) {
            closeChatClient(); // Close existing connection if any
        }
        initializeChatClient(); // Initialize new chat client
    }

    @FXML
    public void initialize() {
        messageListView.setCellFactory(lv -> new ListCell<ChatMessage>() {
            @Override
            protected void updateItem(ChatMessage message, boolean empty) {
                super.updateItem(message, empty);
                if (empty || message == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(message.getText());
                    if (message.isOwnMessage()) {
                        setTextFill(Color.BLUE);
                        setStyle("-fx-alignment: center-right;");
                    } else {
                        setTextFill(Color.BLACK);
                        setStyle("-fx-alignment: center-left;");
                    }
                }
            }
        });
    }

    private void initializeChatClient() {
        try {
            chatClient = new ChatClient("localhost", 8000, auctionId, this::onMessageReceived);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle connection issues
        }
    }

    private void closeChatClient() {
        // Implement logic to close the chat client's connection
        chatClient.closeConnection();
    }

    @FXML
    private void handleSendMessage() {
        String message = inputField.getText().trim();
        if (!message.isEmpty()) {
            // Create a ChatMessage object for the sent message
            ChatMessage chatMessage = new ChatMessage(message, true);

            // Add the message to the ListView
            messageListView.getItems().add(chatMessage);

            // Send the message via the ChatClient
            if (chatClient != null) {
                chatClient.sendMessage(message);
            } else {
                System.out.println("ChatClient is null"); // For debugging
            }
            inputField.clear();
        }
    }

    private void onMessageReceived(String message) {
        Platform.runLater(() -> {
            // Create a ChatMessage object for the received message
            ChatMessage chatMessage = new ChatMessage(message, false);

            // Add the message to the ListView
            messageListView.getItems().add(chatMessage);

            // Scroll to the bottom of the ListView to show the latest message
            messageListView.scrollTo(messageListView.getItems().size() - 1);
        });
    }

    // Rest of your controller...
}
