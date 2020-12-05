package com.example.proyecto_ed_vd.models;

public class ModelChat {
    public String message, receiver, sender;

    public ModelChat() {
    }

    public ModelChat(String message, String receiver, String sender) {
        this.message = message;
        this.receiver = receiver;
        this.sender = sender;
    }

    public String getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }

    public String getReceiver() {
        return receiver;
    }
}
