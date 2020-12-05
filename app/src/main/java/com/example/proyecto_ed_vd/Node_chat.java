package com.example.proyecto_ed_vd;

import com.example.proyecto_ed_vd.models.ModelChat;

public class Node_chat {

    public ModelChat message;
    public Node_chat next;

    public Node_chat(ModelChat message) {
        this.message = message;
        next = null;
    }
}
