package com.example.proyecto_ed_vd;

import com.example.proyecto_ed_vd.models.ModelPost;

public class BinarySearchTreePost {

    class Node {
        int key;
        Node left, right;
        ModelPost data;

        public Node(ModelPost post) {
            key = Integer.parseInt(post.getpLikes());
            data = post;
            left = right = null;
        }
    }

    Node root;

    BinarySearchTreePost() {
        root = null;
    }

    void insert(ModelPost user) {
        root = insertRec(root, user);
    }

    Node insertRec(Node root, ModelPost user) {

        if (root == null) {
            root = new Node(user);
            return root;
        }

        if (Integer.parseInt(user.getpLikes()) < root.key)
            root.left = insertRec(root.left, user);
        else if (Integer.parseInt(user.getpLikes()) > root.key)
            root.right = insertRec(root.right, user);

        return root;
    }

    public ModelPost search(int num) {
        return search_help(root, num).data;
    }

    private Node search_help(Node root, int key)
    {
        if (root==null || root.key==key)
            return root;

        if (root.key < key)
            return search_help(root.right, key);

        return search_help(root.left, key);
    }

    public int size() {
        return (size(root));
    }

    private int size(Node node) {
        if (node == null) return (0);
        else {
            return (size(node.left) + 1 + size(node.right));
        }
    }
}
