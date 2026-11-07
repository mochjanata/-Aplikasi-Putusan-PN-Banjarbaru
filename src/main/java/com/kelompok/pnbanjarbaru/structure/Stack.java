
package com.kelompok.pnbanjarbaru.structure;

import java.util.Objects;

public class Stack<T> {

    // 1. Array untuk menyimpan data Stack
    private Object[] data;
    private int top;
    private int capacity;

    // 2. Constructor
    public Stack() {
        capacity = 10;
        data = new Object[capacity];
        top = -1;
    }

    // 3. Menambahkan data ke Stack
    public void push(T value) {

        Objects.requireNonNull(
                value,
                "Data Stack tidak boleh null!"
        );

        if (top == capacity - 1) {
            resize();
        }

        data[++top] = value;
    }

    // 4. Menghapus dan mengambil data teratas
    @SuppressWarnings("unchecked")
    public T pop() {

        if (isEmpty()) {
            throw new IllegalStateException("Stack kosong!");
        }

        T value = (T) data[top];

        data[top] = null;
        top--;

        return value;
    }

    // 5. Melihat data teratas tanpa menghapus
    @SuppressWarnings("unchecked")
    public T peek() {

        if (isEmpty()) {
            throw new IllegalStateException("Stack kosong!");
        }

        return (T) data[top];
    }

    // 6. Mengembalikan data dari Stack sementara
    public void restore(Stack<T> temporary) {

        Objects.requireNonNull(
                temporary,
                "Stack sementara tidak boleh null!"
        );

        if (temporary == this) {
            throw new IllegalArgumentException(
                    "Stack tidak dapat memulihkan dirinya sendiri!"
            );
        }

        while (!temporary.isEmpty()) {
            push(temporary.pop());
        }
    }

    // 7. Mencari data tanpa mengubah urutan Stack
    public int searchPreserve(T value) {

        Stack<T> temporary = new Stack<>();

        int position = 0;
        int foundPosition = -1;

        while (!isEmpty()) {

            T current = pop();

            if (foundPosition == -1
                    && Objects.equals(current, value)) {

                foundPosition = position;
            }

            temporary.push(current);
            position++;
        }

        // Mengembalikan data ke Stack utama
        restore(temporary);

        return foundPosition;
    }

    // 8. Memeriksa apakah Stack kosong
    public boolean isEmpty() {
        return top == -1;
    }

    // 9. Menghitung jumlah data
    public int size() {
        return top + 1;
    }

    // 10. Menghapus seluruh data
    public void clear() {

        for (int i = 0; i <= top; i++) {
            data[i] = null;
        }

        top = -1;
    }

    // 11. Memperbesar kapasitas array
    private void resize() {

        capacity *= 2;

        Object[] newData = new Object[capacity];

        for (int i = 0; i <= top; i++) {
            newData[i] = data[i];
        }

        data = newData;
    }

    // 12. Memeriksa konsistensi Stack
    public boolean validateInvariant() {

        if (data == null || capacity <= 0) {
            return false;
        }

        if (data.length != capacity) {
            return false;
        }

        if (top < -1 || top >= capacity) {
            return false;
        }

        // Memeriksa bagian yang berisi data
        for (int i = 0; i <= top; i++) {

            if (data[i] == null) {
                return false;
            }
        }

        // Memeriksa bagian kosong
        for (int i = top + 1; i < capacity; i++) {

            if (data[i] != null) {
                return false;
            }
        }

        return true;
    }
}
