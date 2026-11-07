package com.kelompok.pnbanjarbaru.structure;

public class SinglyLinkedListTest {

    public static void main(String[] args) {

        SinglyLinkedList<Integer> list = new SinglyLinkedList<>();

        System.out.println("=== PENGUJIAN TAMBAHAN SINGLY LINKED LIST ===");

        // 1. Menguji addFirst()
        System.out.println("\n1. Pengujian addFirst()");

        list.add(20);
        list.add(30);
        list.addFirst(10);

        printList(list);

        // 2. Menguji insert()
        System.out.println("\n2. Pengujian insert()");

        list.insert(2, 25);

        printList(list);

        // 3. Menguji contains()
        System.out.println("\n3. Pengujian contains()");

        System.out.println("Apakah data 25 tersedia? " + list.contains(25));
        System.out.println("Apakah data 99 tersedia? " + list.contains(99));

        // 4. Menguji remove() posisi pertama, tengah, dan terakhir
        System.out.println("\n4. Pengujian remove()");

        System.out.println("Hapus data pertama: " + list.remove(0));
        printList(list);

        System.out.println("Hapus data tengah: " + list.remove(1));
        printList(list);

        System.out.println("Hapus data terakhir: " + list.remove(list.size() - 1));
        printList(list);

        // 5. Menguji validateInvariant()
        System.out.println("\n5. Pengujian validateInvariant()");
        System.out.println("Linked List valid: " + list.validateInvariant());

        // 6. Menguji clear()
        System.out.println("\n6. Pengujian clear()");

        list.clear();

        System.out.println("Jumlah data setelah clear: " + list.size());
        System.out.println("Apakah Linked List kosong? " + list.isEmpty());
        System.out.println("Linked List valid: " + list.validateInvariant());
    }

    // Method untuk menampilkan seluruh isi Linked List
    public static void printList(SinglyLinkedList<Integer> list) {

        System.out.print("Isi Linked List: ");

        for (int i = 0; i < list.size(); i++) {
            System.out.print(list.get(i) + " ");
        }

        System.out.println();
    }
}
