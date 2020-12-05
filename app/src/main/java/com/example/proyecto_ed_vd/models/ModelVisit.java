package com.example.proyecto_ed_vd.models;

public class ModelVisit {

    public ModelUser usuario;
    public int element;

    public ModelVisit(ModelUser user, int likes) {
        usuario = user;
        element = likes;
    }
}
