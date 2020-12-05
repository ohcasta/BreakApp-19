package com.example.proyecto_ed_vd;

import com.example.proyecto_ed_vd.models.ModelUser;

class BinarySearchTreeUser {

    class Node {
        int key;
        Node left, right;
        ModelUser data;

        public Node(ModelUser usuario, int item) {
            key = item;
            data = usuario;
            left = right = null;
        }
    }

    Node root;

    BinarySearchTreeUser() {
        root = null;
    }

    void insert(ModelUser node, int key) {
        root = insertRec(root, node, key);
    }

    Node insertRec(Node root, ModelUser user, int key) {

        if (root == null) {
            root = new Node(user, key);
            return root;
        }

        if (key < root.key)
            root.left = insertRec(root.left, user, key);
        else if (key > root.key)
            root.right = insertRec(root.right, user, key);

        return root;
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

    public ArrayListUser toArray() {
        ArrayListUser result = new ArrayListUser();
        toArrayHelp(root, result);
        return result;
    }

    private void toArrayHelp(Node ref, ArrayListUser result) {
        if (ref == null) {
            return;
        }
        toArrayHelp(ref.left, result);
        result.add(ref.data);
        toArrayHelp(ref.right, result);
    }
}
