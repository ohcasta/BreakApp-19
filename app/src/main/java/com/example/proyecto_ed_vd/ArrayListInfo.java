package com.example.proyecto_ed_vd;

import com.example.proyecto_ed_vd.models.ModelInfo;
import com.example.proyecto_ed_vd.models.ModelUser;

public class ArrayListInfo {

    private ModelInfo[] arr;
    private int capacity;
    private int current;


    public ArrayListInfo()
    {
        arr = new ModelInfo[1];
        capacity = 1;
        current = 0;
    }

    public void add(ModelInfo data) {
        if (current == capacity) {
            ModelInfo temp[] = new ModelInfo[2 * capacity];

            for (int i = 0; i < capacity; i++)
                temp[i] = arr[i];

            capacity *= 2;
            arr = temp;
        }

        arr[current] = data;
        current++;
    }

    public void add(ModelInfo data, int index) {
        if (index == capacity)
            add(data);
        else
            arr[index] = data;
    }

    public ModelInfo get(int index) {

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
