    package Models;

    public class ChatMessage {
        private String text;
        private boolean ownMessage;

        public ChatMessage(String text, boolean ownMessage) {
            this.text = text;
            this.ownMessage = ownMessage;
        }

        public String getText() {
            return text;
        }

        public boolean isOwnMessage() {
            return ownMessage;
        }
    }
