package com.example.proyecto_ed_vd;

import com.example.proyecto_ed_vd.models.ModelInfo;
import com.example.proyecto_ed_vd.models.ModelUser;

import java.io.Serializable;

public class HashMapUsers implements Serializable {

    public ArrayListInfo[] A;
    public int n;

    public HashMapUsers(int num) {
        A = new ArrayListInfo[num];
        n = num;
    }

    public void Set(ModelUser O, int p, int l, int c) {

        int ref = O.CodeHash()%n;
        ArrayListInfo L = A[ref];

        if (L != null) {
            for (int i=0; i<L.size(); i++) {
                if (L.get(i).user.compareUser(O)) {
                    L.get(i).num_posts = p;
                    L.get(i).num_likes = l;
                    L.get(i).num_comments = c;
                    return;
                }
            }
            ModelInfo In = new ModelInfo(O, p, l, c);
            L.add(In);
        } else {
            A[ref] = new ArrayListInfo();
            L = A[ref];
            ModelInfo In = new ModelInfo(O, p, l, c);
            L.add(In);
        }
    }

    public boolean HasKey(ModelUser O) {
        ArrayListInfo L = A[O.CodeHash()%n];

        if (L != null) {
            for (int i=0; i<L.size(); i++) {
                if (L.get(i).user.compareUser(O)) {
                    return true;
                }
            }
        }

        return false;
    }

    public int[] Get(ModelUser O) {
        ArrayListInfo L = A[O.CodeHash()%n];

        if (L != null) {
            for (int i=0; i<L.size(); i++) {
                if (L.get(i).user.compareUser(O)) {
                    int[] x = new int[]{L.get(i).num_posts, L.get(i).num_likes, L.get(i).num_comments};

                    return x;
                }
            }
        }

        return null;
    }

}
