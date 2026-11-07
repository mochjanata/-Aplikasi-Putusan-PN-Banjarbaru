
package com.kelompok.pnbanjarbaru.service;

import java.io.IOException;

public class PutusanServiceTest {

    public static void main(String[] args) {

        System.out.println("======================================");
        System.out.println("       PENGUJIAN PUTUSAN SERVICE");
        System.out.println("======================================");

        // 1. Membuat objek PutusanService
        PutusanService service = new PutusanService();

        // 2. Membaca data dari file CSV
        System.out.println("\n1. Pengujian CsvLoader");

        try {

            int jumlahData = CsvLoader.muatData(service);

            System.out.println(
                    "Jumlah data berhasil dimuat: " + jumlahData
            );

            // 3. Memeriksa jumlah data
            System.out.println("\n2. Pemeriksaan jumlah data");

            if (jumlahData == 50) {
                System.out.println("BERHASIL: 50 data berhasil dimuat.");
            } else {
                System.out.println(
                        "PERINGATAN: Data yang dimuat hanya "
                                + jumlahData
                );
            }

        } catch (IOException e) {

            System.out.println(
                    "Gagal membaca CSV: " + e.getMessage()
            );

            return;
        }

        // 4. Menampilkan hasil pengujian
        System.out.println("\n3. Pengujian selesai");

        System.out.println("======================================");
        System.out.println("       PENGUJIAN SELESAI");
        System.out.println("======================================");
    }
}
