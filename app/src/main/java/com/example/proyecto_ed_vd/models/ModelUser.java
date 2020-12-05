package com.example.proyecto_ed_vd.models;

import java.io.Serializable;

public class ModelUser implements Serializable {
    public String name,email, phone, Tipo_usuario, image,cover,uid;

    public ModelUser() {
    }

    public ModelUser(String name, String email, String phone, String tipo_usuario, String image, String cover, String uid) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.Tipo_usuario = tipo_usuario;
        this.image = image;
        this.cover = cover;
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getTipo_usuario() {
        return Tipo_usuario;
    }

    public String getImage() {
        return image;
    }

    public String getCover() {
        return cover;
    }

    public String getUid() {
        return uid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setTipo_usuario(String Tipo_usuario) {
        this.Tipo_usuario = Tipo_usuario;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int CodeHash() {

        int sum = 0;

        for (int i=0; i<uid.length(); i++) {
            sum += ((int) uid.charAt(i)*Math.pow(31, uid.length()-1-i));
        }

        return sum;
    }

    public boolean compareUser(ModelUser otherUser) {
        if (this.uid.equals(otherUser.uid)) {
            return true;
        }

        return false;
    }
}
