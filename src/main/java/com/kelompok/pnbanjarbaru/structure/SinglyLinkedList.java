package com.kelompok.pnbanjarbaru.structure;

import java.util.Objects;

public class SinglyLinkedList<T> implements StrukturData<T> {

    // 1. Node untuk menyimpan data dan referensi node berikutnya
    private static class Node<T> {
        T data;
        Node<T> next;

        Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    // 2. Atribut utama linked list
    private Node<T> head;
    private Node<T> tail;
    private int size;

    // 3. Menambahkan data di akhir
    @Override
    public void add(T value) {
        addLast(value);
    }

    public void addLast(T value) {
        Node<T> newNode = new Node<>(value);

        if (isEmpty()) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }

        size++;
    }

    // 4. Menambahkan data di awal
    public void addFirst(T value) {
        Node<T> newNode = new Node<>(value);

        if (isEmpty()) {
            head = newNode;
            tail = newNode;
        } else {
            newNode.next = head;
            head = newNode;
        }

        size++;
    }

    // 5. Menyisipkan data pada posisi tertentu
    public void insert(int position, T value) {
        if (position < 0 || position > size) {
            throw new IndexOutOfBoundsException(
                    "Posisi penyisipan tidak valid: " + position
            );
        }

        if (position == 0) {
            addFirst(value);
            return;
        }

        if (position == size) {
            addLast(value);
            return;
        }

        Node<T> newNode = new Node<>(value);
        Node<T> current = head;

        for (int i = 0; i < position - 1; i++) {
            current = current.next;
        }

        newNode.next = current.next;
        current.next = newNode;
        size++;
    }

    // 6. Mengambil data berdasarkan posisi
    @Override
    public T get(int position) {
        checkPosition(position);
        return getNode(position).data;
    }

    // 7. Menghapus data berdasarkan posisi
    @Override
    public T remove(int position) {
        checkPosition(position);

        Node<T> removed;

        if (position == 0) {
            removed = head;
            head = head.next;
            size--;

            if (size == 0) {
                tail = null;
            }

            removed.next = null;
            return removed.data;
        }

        Node<T> previous = getNode(position - 1);

        removed = previous.next;
        previous.next = removed.next;

        if (removed == tail) {
            tail = previous;
        }

        size--;
        removed.next = null;

        return removed.data;
    }

    // 8. Mencari posisi data
    public int search(T value) {
        Node<T> current = head;
        int position = 0;

        while (current != null) {
            if (Objects.equals(current.data, value)) {
                return position;
            }

            current = current.next;
            position++;
        }

        return -1;
    }

    // 9. Memeriksa keberadaan data
    @Override
    public boolean contains(T value) {
        return search(value) != -1;
    }

    // 10. Menghitung jumlah data
    @Override
    public int size() {
        return size;
    }

    // 11. Memeriksa apakah linked list kosong
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // 12. Menghapus seluruh data
    @Override
    public void clear() {
        head = null;
        tail = null;
        size = 0;
    }

    // 13. Mengambil node berdasarkan posisi
    private Node<T> getNode(int position) {
        Node<T> current = head;

        for (int i = 0; i < position; i++) {
            current = current.next;
        }

        return current;
    }

    // 14. Memeriksa validitas posisi
    private void checkPosition(int position) {
        if (position < 0 || position >= size) {
            throw new IndexOutOfBoundsException(
                    "Posisi tidak valid: " + position
            );
        }
    }

    // 15. Memeriksa konsistensi linked list
    public boolean validateInvariant() {

        if (size < 0) {
            return false;
        }

        if (size == 0) {
            return head == null && tail == null;
        }

        if (head == null || tail == null) {
            return false;
        }

        if (tail.next != null) {
            return false;
        }

        int count = 0;
        Node<T> current = head;
        Node<T> last = null;

        while (current != null) {
            count++;
            last = current;
            current = current.next;

            if (count > size) {
                return false;
            }
        }

        return count == size && last == tail;
    }
}
