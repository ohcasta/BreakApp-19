package com.example.proyecto_ed_vd.models;

public class ModelInfo {

    public ModelUser user;
    public int num_posts, num_likes, num_comments;

    public ModelInfo(ModelUser us, int np, int nl, int nc) {
        user = us;
        num_posts = np;
        num_likes = nl;
        num_comments = nc;
    }
}
