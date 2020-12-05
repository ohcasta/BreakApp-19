package com.example.proyecto_ed_vd;

import com.example.proyecto_ed_vd.models.ModelChatlist;

public class ArrayListChatList {

    private ModelChatlist[] arr;
    private int capacity;
    private int current;


    public ArrayListChatList()
    {
        arr = new ModelChatlist[1];
        capacity = 1;
        current = 0;
    }

    public void add(ModelChatlist data) {
        if (current == capacity) {
            ModelChatlist temp[] = new ModelChatlist[2 * capacity];

            for (int i = 0; i < capacity; i++)
                temp[i] = arr[i];

            capacity *= 2;
            arr = temp;
        }

        arr[current] = data;
        current++;
    }

    public void add(ModelChatlist data, int index) {
        if (index == capacity)
            add(data);
        else
            arr[index] = data;
    }

    public ModelChatlist get(int index) {

        if (index < current) {
            return arr[index];
        }
        return null;
    }

    public void pop() {
        current--;
    }

    public int size() {
        return current;
    }

    public int getcapacity() {
        return capacity;
    }

    public void clear() {
        current=0;
    }

    public void print() {
        for (int i = 0; i < current; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.println();
    }
}
