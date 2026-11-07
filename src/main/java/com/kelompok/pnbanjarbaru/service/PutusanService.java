package com.kelompok.pnbanjarbaru.service;

import com.kelompok.pnbanjarbaru.model.Putusan;
import com.kelompok.pnbanjarbaru.structure.SinglyLinkedList;

import java.util.Objects;

public class PutusanService {

    // 1. Menyimpan data putusan menggunakan Singly Linked List
    private final SinglyLinkedList<Putusan> daftarPutusan;

    // 2. Constructor
    public PutusanService() {
        daftarPutusan = new SinglyLinkedList<>();
    }

    // 3. Menambahkan putusan
    public void tambahPutusan(Putusan putusan) {
        Objects.requireNonNull(putusan, "Data putusan tidak boleh null!");

        daftarPutusan.add(putusan);
    }

    // 4. Mengambil putusan berdasarkan posisi
    public Putusan ambilPutusan(int posisi) {
        return daftarPutusan.get(posisi);
    }

    // 5. Menghapus putusan berdasarkan posisi
    public Putusan hapusPutusan(int posisi) {
        return daftarPutusan.remove(posisi);
    }

    // 6. Mencari putusan berdasarkan caseId
    public Putusan cariBerdasarkanCaseId(String caseId) {

        for (int i = 0; i < daftarPutusan.size(); i++) {
            Putusan putusan = daftarPutusan.get(i);

            if (Objects.equals(putusan.getCaseId(), caseId)) {
                return putusan;
            }
        }

        return null;
    }

    // 7. Menghitung jumlah putusan
    public int jumlahPutusan() {
        return daftarPutusan.size();
    }

    // 8. Memeriksa apakah daftar putusan kosong
    public boolean isEmpty() {
        return daftarPutusan.isEmpty();
    }

    // 9. Menghapus seluruh putusan
    public void hapusSemuaPutusan() {
        daftarPutusan.clear();
    }

    // 10. Memeriksa konsistensi struktur data
    public boolean validateInvariant() {
        return daftarPutusan.validateInvariant();
    }
}
