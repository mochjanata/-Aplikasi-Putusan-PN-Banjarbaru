package com.kelompok.pnbanjarbaru.service;

import com.kelompok.pnbanjarbaru.structure.Stack;

public class StackTest {

    public static void main(String[] args) {

        Stack<Integer> stack = new Stack<>();

        System.out.println("=== PENGUJIAN STACK ===");

        // 1. Menguji push()
        System.out.println("\n1. Pengujian push()");

        stack.push(10);
        stack.push(20);
        stack.push(30);

        System.out.println("Jumlah data: " + stack.size());
        System.out.println("Stack valid: " + stack.validateInvariant());

        // 2. Menguji peek()
        System.out.println("\n2. Pengujian peek()");

        System.out.println("Data teratas: " + stack.peek());
        System.out.println("Jumlah data: " + stack.size());

        // 3. Menguji searchPreserve()
        System.out.println("\n3. Pengujian searchPreserve()");

        System.out.println("Posisi data 20: " + stack.searchPreserve(20));
        System.out.println("Posisi data 99: " + stack.searchPreserve(99));

        System.out.println("Data teratas setelah pencarian: " + stack.peek());
        System.out.println("Stack valid: " + stack.validateInvariant());

        // 4. Menguji pop()
        System.out.println("\n4. Pengujian pop()");

        System.out.println("Data yang dihapus: " + stack.pop());
        System.out.println("Data teratas sekarang: " + stack.peek());

        // 5. Menguji clear()
        System.out.println("\n5. Pengujian clear()");

        stack.clear();

        System.out.println("Jumlah data setelah clear: " + stack.size());
        System.out.println("Apakah Stack kosong? " + stack.isEmpty());
        System.out.println("Stack valid: " + stack.validateInvariant());

        // 5. Menguji resize()
        System.out.println("\n5. Pengujian resize()");

        for (int i = 1; i <= 15; i++) {
            stack.push(i);
        }

        System.out.println("Jumlah data: " + stack.size());
        System.out.println("Stack valid: " + stack.validateInvariant());
    }
}
