
package com.kelompok.pnbanjarbaru.service;

import com.kelompok.pnbanjarbaru.model.Putusan;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CsvLoader {

    private static final int JUMLAH_KOLOM = 17;
    private static final int TARGET_DATA = 50;

    // ==========================================
    // 1. MEMUAT DATA CSV
    // ==========================================

    public static int muatData(PutusanService service)
            throws IOException {

        if (service == null) {
            throw new IllegalArgumentException(
                    "PutusanService tidak boleh null!"
            );
        }

        InputStream input = CsvLoader.class
                .getClassLoader()
                .getResourceAsStream("data/putusan-50.csv");

        if (input == null) {
            throw new IOException(
                    "File putusan-50.csv tidak ditemukan!"
            );
        }

        int jumlahData = 0;
        int jumlahGagal = 0;
        int nomorRecord = 0;

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        input,
                        StandardCharsets.UTF_8))) {

            String record;
            boolean header = true;

            while ((record = bacaRecord(reader)) != null) {

                // Melewati header CSV
                if (header) {
                    header = false;
                    continue;
                }

                if (record.isBlank()) {
                    continue;
                }

                nomorRecord++;

                // Membatasi jumlah data sebanyak 50
                if (nomorRecord > TARGET_DATA) {

                    jumlahGagal++;

                    System.out.println(
                            "Record ke-" + nomorRecord
                                    + " ditolak: melebihi batas 50 data."
                    );

                    continue;
                }

                try {

                    // Memisahkan 17 kolom CSV
                    String[] kolom = pisahkanKolom(record);

                    if (kolom.length != JUMLAH_KOLOM) {
                        throw new IllegalArgumentException(
                                "Jumlah kolom harus 17, ditemukan "
                                        + kolom.length
                        );
                    }

                    // Membuat objek Putusan
                    Putusan putusan = buatPutusan(kolom);

                    // Memeriksa Case ID
                    if (putusan.getCaseId() == null) {
                        throw new IllegalArgumentException(
                                "Case ID tidak boleh kosong."
                        );
                    }

                    // Memeriksa nomor perkara
                    if (putusan.getNomorPerkara() == null) {
                        throw new IllegalArgumentException(
                                "Nomor perkara tidak boleh kosong."
                        );
                    }

                    // Memeriksa Case ID duplikat
                    if (service.cariBerdasarkanCaseId(
                            putusan.getCaseId()) != null) {

                        throw new IllegalArgumentException(
                                "Case ID duplikat: "
                                        + putusan.getCaseId()
                        );
                    }

                    // Memasukkan data ke Singly Linked List
                    service.tambahPutusan(putusan);

                    jumlahData++;

                } catch (RuntimeException e) {

                    jumlahGagal++;

                    System.out.println(
                            "Record ke-" + nomorRecord
                                    + " ditolak: "
                                    + e.getMessage()
                    );
                }
            }
        }

        // Menampilkan hasil import
        System.out.println("\n===== HASIL IMPORT CSV =====");
        System.out.println("Target data       : " + TARGET_DATA);
        System.out.println("Berhasil dimuat   : " + jumlahData);
        System.out.println("Gagal / ditolak   : " + jumlahGagal);

        if (jumlahData == TARGET_DATA) {
            System.out.println(
                    "Import berhasil memenuhi target 50 data."
            );
        } else {
            System.out.println(
                    "PERINGATAN: Jumlah data valid belum mencapai 50."
            );
        }

        System.out.println(
                "Invariant struktur: "
                        + service.validateInvariant()
        );

        return jumlahData;
    }


    // ==========================================
    // 2. MEMBUAT OBJEK PUTUSAN
    // ==========================================

    private static Putusan buatPutusan(String[] kolom) {

        double berat = angkaDouble(kolom[6]);

        // Mendukung satu atau beberapa terdakwa
        int[] tuntutan = angkaPerTerdakwa(kolom[9]);
        int[] vonis = angkaPerTerdakwa(kolom[10]);

        long denda = angkaLong(kolom[11]);

        if (berat < 0 || denda < 0) {
            throw new IllegalArgumentException(
                    "Berat barang bukti dan denda tidak boleh negatif."
            );
        }

        for (int nilai : tuntutan) {
            if (nilai < 0) {
                throw new IllegalArgumentException(
                        "Nilai tuntutan tidak boleh negatif."
                );
            }
        }

        for (int nilai : vonis) {
            if (nilai < 0) {
                throw new IllegalArgumentException(
                        "Nilai vonis tidak boleh negatif."
                );
            }
        }

        // Memeriksa jumlah nilai tuntutan dan vonis
        if (tuntutan.length != vonis.length) {
            throw new IllegalArgumentException(
                    "Jumlah nilai tuntutan dan vonis tidak sama."
            );
        }

        return new Putusan(
                teks(kolom[0]),       // caseId
                teks(kolom[1]),       // nomorPerkara
                teks(kolom[2]),       // pengadilan
                tanggal(kolom[3]),    // tanggalPutusan
                teks(kolom[4]),       // terdakwaKode
                teks(kolom[5]),       // jenisNarkotika
                berat,                // beratBarangBuktiGram
                teks(kolom[7]),       // pasalDakwaan
                teks(kolom[8]),       // pasalTerbukti
                tuntutan,             // tuntutanBulanPerTerdakwa
                vonis,                // vonisBulanPerTerdakwa
                denda,                // dendaRupiah
                teks(kolom[12]),      // barangBukti
                teks(kolom[13]),      // keadaanMemberatkan
                teks(kolom[14]),      // keadaanMeringankan
                teks(kolom[15]),      // sourcePages
                teks(kolom[16])       // statusVerifikasi
        );
    }


    // ==========================================
    // 3. MEMBACA ANGKA MULTI-TERDAKWA
    // ==========================================

    private static int[] angkaPerTerdakwa(String nilai) {

        String hasil = teks(nilai);

        if (hasil == null
                || hasil.equalsIgnoreCase("Tidak disebutkan")) {

            return new int[]{0};
        }

        // Format yang didukung:
        // 72
        // 72 (Terdakwa I); 66 (Terdakwa II)

        String[] bagian = hasil.split(";", -1);

        int[] angka = new int[bagian.length];

        Pattern pola = Pattern.compile(
                "^\\s*(\\d+)\\s*(?:\\(\\s*Terdakwa\\s+[^)]*\\))?\\s*$",
                Pattern.CASE_INSENSITIVE
        );

        for (int i = 0; i < bagian.length; i++) {

            Matcher matcher = pola.matcher(bagian[i]);

            if (!matcher.matches()) {
                throw new IllegalArgumentException(
                        "Format angka multi-terdakwa tidak valid: "
                                + bagian[i]
                );
            }

            angka[i] = Integer.parseInt(matcher.group(1));
        }

        return angka;
    }


    // ==========================================
    // 4. MEMBACA SATU RECORD CSV
    // ==========================================

    private static String bacaRecord(BufferedReader reader)
            throws IOException {

        StringBuilder record = new StringBuilder();

        String baris;

        boolean dalamKutipan = false;
        boolean sudahMembaca = false;

        while ((baris = reader.readLine()) != null) {

            if (sudahMembaca) {
                record.append("\n");
            }

            record.append(baris);
            sudahMembaca = true;

            for (int i = 0; i < baris.length(); i++) {

                char karakter = baris.charAt(i);

                if (karakter == '"') {

                    if (dalamKutipan
                            && i + 1 < baris.length()
                            && baris.charAt(i + 1) == '"') {

                        // Dua tanda kutip berarti satu kutip literal
                        i++;

                    } else {
                        dalamKutipan = !dalamKutipan;
                    }
                }
            }

            if (!dalamKutipan) {
                return record.toString();
            }
        }

        if (dalamKutipan) {
            throw new IOException(
                    "Tanda kutip CSV tidak berpasangan."
            );
        }

        return sudahMembaca ? record.toString() : null;
    }


    // ==========================================
    // 5. MEMISAHKAN KOLOM CSV
    // ==========================================

    private static String[] pisahkanKolom(String baris) {

        String[] hasil = new String[JUMLAH_KOLOM];

        int jumlahKolom = 0;

        StringBuilder nilai = new StringBuilder();

        boolean dalamKutipan = false;
        boolean kutipanDitutup = false;

        for (int i = 0; i < baris.length(); i++) {

            char karakter = baris.charAt(i);

            if (dalamKutipan) {

                if (karakter == '"') {

                    if (i + 1 < baris.length()
                            && baris.charAt(i + 1) == '"') {

                        nilai.append('"');
                        i++;

                    } else {
                        dalamKutipan = false;
                        kutipanDitutup = true;
                    }

                } else {
                    nilai.append(karakter);
                }

            } else {

                if (karakter == '"') {

                    if (nilai.length() != 0 || kutipanDitutup) {
                        throw new IllegalArgumentException(
                                "Format tanda kutip CSV tidak valid."
                        );
                    }

                    dalamKutipan = true;

                } else if (karakter == ',') {

                    if (jumlahKolom >= JUMLAH_KOLOM) {
                        throw new IllegalArgumentException(
                                "Jumlah kolom melebihi 17."
                        );
                    }

                    hasil[jumlahKolom++] = nilai.toString();

                    nilai.setLength(0);
                    kutipanDitutup = false;

                } else {

                    if (kutipanDitutup
                            && !Character.isWhitespace(karakter)) {

                        throw new IllegalArgumentException(
                                "Karakter tidak valid setelah tanda kutip."
                        );
                    }

                    if (!kutipanDitutup) {
                        nilai.append(karakter);
                    }
                }
            }
        }

        if (dalamKutipan) {
            throw new IllegalArgumentException(
                    "Tanda kutip CSV tidak berpasangan."
            );
        }

        if (jumlahKolom >= JUMLAH_KOLOM) {
            throw new IllegalArgumentException(
                    "Jumlah kolom melebihi 17."
            );
        }

        hasil[jumlahKolom++] = nilai.toString();

        // Menghasilkan array sesuai jumlah kolom sebenarnya
        String[] kolomAkhir = new String[jumlahKolom];

        for (int i = 0; i < jumlahKolom; i++) {
            kolomAkhir[i] = hasil[i];
        }

        return kolomAkhir;
    }


    // ==========================================
    // 6. MENGUBAH NILAI TEKS
    // ==========================================

    private static String teks(String nilai) {

        if (nilai == null) {
            return null;
        }

        nilai = nilai.trim();

        if (nilai.isEmpty()
                || nilai.equalsIgnoreCase("PERLU_VERIFIKASI")) {

            return null;
        }

        return nilai;
    }


    // ==========================================
    // 7. MENGUBAH TEKS MENJADI LOCALDATE
    // ==========================================

    private static LocalDate tanggal(String nilai) {

        String hasil = teks(nilai);

        if (hasil == null) {
            return null;
        }

        return LocalDate.parse(hasil);
    }


    // ==========================================
    // 8. MENGUBAH TEKS MENJADI DOUBLE
    // ==========================================

    private static double angkaDouble(String nilai) {

        String hasil = teks(nilai);

        if (hasil == null
                || hasil.equalsIgnoreCase("Tidak disebutkan")) {

            return 0;
        }

        double angka = Double.parseDouble(hasil);

        if (!Double.isFinite(angka)) {
            throw new IllegalArgumentException(
                    "Nilai berat barang bukti tidak valid."
            );
        }

        return angka;
    }


    // ==========================================
    // 9. MENGUBAH TEKS MENJADI LONG
    // ==========================================

    private static long angkaLong(String nilai) {

        String hasil = teks(nilai);

        if (hasil == null
                || hasil.equalsIgnoreCase("Tidak disebutkan")) {

            return 0;
        }

        return Long.parseLong(hasil);
    }
}
