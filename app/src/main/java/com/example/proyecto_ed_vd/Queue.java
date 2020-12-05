package com.example.proyecto_ed_vd;

class Queue_node<T> {
    public T value;
    public Queue_node<T> previous;

    Queue_node(T value) {
        this.value = value;
    }

}

public class Queue<T> {
    private Queue_node<T> front;
    private Queue_node<T> back;

    public Queue() {}

    public void enqueue(T value) {

        Queue_node<T> new_node = new Queue_node<T>(value);

        if (this.isEmpty() == true) {
            this.front = this.back = new_node;
        } else {
            this.back.previous = new_node;
            this.back = new_node;
        }
    }

    public T desqueue() {
        if (this.isEmpty())
            throw new IllegalArgumentException("Stack vacio");

        T top = this.front.value;
        this.front = this.front.previous;

        return top;
    }

    public T peek() {
        return this.front.value;
    }

    public boolean isEmpty() {
        return this.front == null;
    }

    public int size() {

        if (this.isEmpty()==true) {
            return 0;
        } else {
            Queue_node<T> current = this.front;
            int c = 1;

            while (current.previous != null) {
                c = c+1;
                current = current.previous;
            }

            return c;
        }
        
    }

}
