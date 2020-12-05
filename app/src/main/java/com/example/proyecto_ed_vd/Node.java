package com.example.proyecto_ed_vd;

import com.example.proyecto_ed_vd.models.ModelUser;

public class Node {
    public ModelUser user;
    public Node next;

    Node(ModelUser user){
        this.user = user;
        next = null;
    }
}
