package com.example.proyecto_ed_vd;

import com.example.proyecto_ed_vd.models.ModelMessage;

public class ArrayListMessage {

    private ModelMessage[] arr;
    private int capacity;
    private int current;


    public ArrayListMessage()
    {
        arr = new ModelMessage[1];
        capacity = 1;
        current = 0;
    }

    public void add(ModelMessage data) {
        if (current == capacity) {
            ModelMessage temp[] = new ModelMessage[2 * capacity];

            for (int i = 0; i < capacity; i++)
                temp[i] = arr[i];

            capacity *= 2;
            arr = temp;
        }

        arr[current] = data;
        current++;
    }

    public void add(ModelMessage data, int index) {
        if (index == capacity)
            add(data);
        else
            arr[index] = data;
    }

    public ModelMessage get(int index) {

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
