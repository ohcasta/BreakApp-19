package com.example.proyecto_ed_vd.models;

public class ModelMessage {
    public String user;
    public String mess;

    ModelMessage() {

    }

    public ModelMessage(String u, String m) {
        user = u;
        mess = m;
    }

    public int CodeHash() {

        int sum = 0;

        for (int i=0; i<user.length(); i++) {
            sum += ((int) user.charAt(i)*Math.pow(31, user.length()-1-i));
        }

        return sum;
    }
}
