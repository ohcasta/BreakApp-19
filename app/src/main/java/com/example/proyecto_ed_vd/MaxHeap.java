package com.example.proyecto_ed_vd;

import com.example.proyecto_ed_vd.models.ModelVisit;

public class MaxHeap {
    private ModelVisit[] Heap;
    private int size;
    private int maxsize;

    public MaxHeap(int maxsize) {
        this.maxsize = maxsize;
        this.size = 0;
        Heap = new ModelVisit[this.maxsize + 1];
        Heap[0] = new ModelVisit(null, Integer.MAX_VALUE);
    }

    private int parent(int pos) {
        return pos / 2;
    }

    private int leftChild(int pos) {
        return (2 * pos);
    }

    private int rightChild(int pos) {
        return (2 * pos) + 1;
    }

    private boolean isLeaf(int pos) {
        if (pos >= (size / 2) && pos <= size) {
            return true;
        }
        return false;
    }

    private void swap(int fpos, int spos) {
        ModelVisit tmp;
        tmp = Heap[fpos];
        Heap[fpos] = Heap[spos];
        Heap[spos] = tmp;
    }


    private void maxHeapify(int pos) {
        if (isLeaf(pos))
            return;

        if (Heap[pos].element < Heap[leftChild(pos)].element || Heap[pos].element < Heap[rightChild(pos)].element) {

            if (Heap[leftChild(pos)].element > Heap[rightChild(pos)].element) {
                swap(pos, leftChild(pos));
                maxHeapify(leftChild(pos));
            } else {
                swap(pos, rightChild(pos));
                maxHeapify(rightChild(pos));
            }
        }
    }

    public ModelVisit extractMax()
    {
        ModelVisit popped = Heap[1];
        Heap[1] = Heap[size--];
        maxHeapify(1);
        return popped;
    }

    public void insert(ModelVisit user) {
        Heap[++size] = user;

        int current = size;
        while (Heap[current].element > Heap[parent(current)].element) {
            swap(current, parent(current));
            current = parent(current);
        }
    }
}
