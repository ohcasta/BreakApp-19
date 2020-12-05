package com.example.proyecto_ed_vd;

import com.example.proyecto_ed_vd.models.ModelInfo;
import com.example.proyecto_ed_vd.models.ModelMessage;
import com.example.proyecto_ed_vd.models.ModelUser;

import java.io.Serializable;

public class HashMapMessage implements Serializable {

    public ArrayListMessage[] A;
    public int n;

    public HashMapMessage(int num) {
        A = new ArrayListMessage[num];
        n = num;
    }

    public void Set(String O, String val) {

        int ref = CodeHash(O)%n;
        ArrayListMessage L = A[ref];

        if (L != null) {
            for (int i=0; i<L.size(); i++) {
                if (L.get(i).user.equals(O)) {
                    L.get(i).mess = val;
                    return;
                }
            }
            ModelMessage In = new ModelMessage(O, val);
            L.add(In);
        } else {
            A[ref] = new ArrayListMessage();
            L = A[ref];
            ModelMessage In = new ModelMessage(O, val);
            L.add(In);
        }
    }

    public boolean HasKey(String O) {
        ArrayListMessage L = A[CodeHash(O)%n];

        if (L != null) {
            for (int i=0; i<L.size(); i++) {
                if (L.get(i).user.equals(O)) {
                    return true;
                }
            }
        }

        return false;
    }

    public String Get(String O) {
        ArrayListMessage L = A[CodeHash(O)%n];

        if (L != null) {
            for (int i=0; i<L.size(); i++) {
                if (L.get(i).user.equals(O)) {

                    return L.get(i).mess;
                }
            }
        }

        return null;
    }

    private int CodeHash(String O) {
        int sum = 0;

        for (int i=0; i<O.length(); i++) {
            sum += ((int) O.charAt(i)*Math.pow(31, O.length()-1-i));
        }

        return sum;
    }

}