
package com.kelompok.pnbanjarbaru;

import com.kelompok.pnbanjarbaru.model.Putusan;
import com.kelompok.pnbanjarbaru.service.CsvLoader;
import com.kelompok.pnbanjarbaru.service.PutusanService;
import com.kelompok.pnbanjarbaru.structure.Stack;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

    private static final Scanner input = new Scanner(System.in);

    // Stack sebagai struktur sekunder untuk audit aktivitas
    private static final Stack<String> audit = new Stack<>();

    public static void main(String[] args) {

        PutusanService service = new PutusanService();

        System.out.println("==============================================");
        System.out.println("       SISTEM PENGELOLAAN DATA PUTUSAN");
        System.out.println("       PENGADILAN NEGERI BANJARBARU");
        System.out.println("==============================================");

        // Import data CSV
        try {

            int jumlah = CsvLoader.muatData(service);

            System.out.println("\nData berhasil dimuat!");
            System.out.println("Jumlah data CSV     : " + jumlah);
            System.out.println("Jumlah data service : "
                    + service.jumlahPutusan());

            catatAudit("IMPORT DATA CSV: " + jumlah + " record");

        } catch (Exception e) {

            System.out.println("\nGagal membaca data CSV!");
            System.out.println("Penyebab: " + e.getMessage());
            return;
        }

        int pilihan = -1;

        do {

            tampilkanMenu();

            System.out.print("Masukkan pilihan: ");

            if (!input.hasNextInt()) {
                System.out.println("Input harus berupa angka!");
                input.nextLine();
                continue;
            }

            pilihan = input.nextInt();
            input.nextLine();

            switch (pilihan) {

                case 1:
                    tampilkanSemuaPutusan(service);
                    break;

                case 2:
                    tambahDataPutusan(service);
                    break;

                case 3:
                    hapusDataPutusan(service);
                    break;

                case 4:
                    cariPutusan(service);
                    break;

                case 5:
                    filterPutusan(service);
                    break;

                case 6:
                    tampilkanStatistik(service);
                    break;

                case 7:
                    urutkanBerdasarkanVonis(service);
                    break;

                case 8:
                    tampilkanAudit();
                    break;

                case 9:
                    eksporData(service);
                    break;

                case 0:
                    System.out.println("\nTerima kasih!");
                    System.out.println("Program telah ditutup.");
                    break;

                default:
                    System.out.println("Pilihan tidak tersedia!");
            }

        } while (pilihan != 0);

        input.close();
    }


    // ==================================================
    // MENU UTAMA
    // ==================================================

    private static void tampilkanMenu() {

        System.out.println("\n==============================================");
        System.out.println("                  MENU UTAMA");
        System.out.println("==============================================");

        System.out.println("1. Tampilkan seluruh data putusan");
        System.out.println("2. Tambah data putusan");
        System.out.println("3. Hapus data putusan");
        System.out.println("4. Cari putusan");
        System.out.println("5. Filter data putusan");
        System.out.println("6. Statistik dan agregasi data");
        System.out.println("7. Urutkan putusan berdasarkan vonis");
        System.out.println("8. Lihat audit aktivitas");
        System.out.println("9. Ekspor hasil pencarian/laporan");
        System.out.println("0. Keluar");

        System.out.println("==============================================");
    }


    // ==================================================
    // 1. TAMPILKAN SELURUH DATA
    // ==================================================

    private static void tampilkanSemuaPutusan(
            PutusanService service) {

        System.out.println("\n========== SELURUH DATA PUTUSAN ==========");

        if (service.isEmpty()) {
            System.out.println("Data putusan masih kosong.");
            return;
        }

        for (int i = 0; i < service.jumlahPutusan(); i++) {

            Putusan p = service.ambilPutusan(i);

            System.out.println("\nDATA KE-" + (i + 1));
            System.out.println("----------------------------------------------");

            tampilkanDetail(p);
        }

        System.out.println("\nTotal perkara: " + service.jumlahPutusan());

        catatAudit("MENAMPILKAN SELURUH DATA PUTUSAN");
    }


    // ==================================================
    // 2. TAMBAH DATA PUTUSAN
    // ==================================================

    private static void tambahDataPutusan(
            PutusanService service) {

        System.out.println("\n========== TAMBAH DATA PUTUSAN ==========");

        String caseId = bacaTeks("Case ID", true);

        if (service.cariBerdasarkanCaseId(caseId) != null) {
            System.out.println("Case ID sudah digunakan!");
            return;
        }

        String nomorPerkara = bacaTeks("Nomor Perkara", true);
        String pengadilan = bacaTeks("Pengadilan", true);
        LocalDate tanggal = bacaTanggal();

        String terdakwaKode = bacaTeks("Kode Terdakwa", true);
        String jenisNarkotika = bacaTeks("Jenis Narkotika", true);

        double berat = bacaDouble("Berat Barang Bukti (gram)");

        String pasalDakwaan = bacaTeks("Pasal Dakwaan", true);
        String pasalTerbukti = bacaTeks("Pasal Terbukti", true);

        int tuntutan = bacaInt("Tuntutan (bulan)");
        int vonis = bacaInt("Vonis (bulan)");

        long denda = bacaLong("Denda (rupiah)");

        String barangBukti = bacaTeks("Barang Bukti", false);
        String memberatkan = bacaTeks("Keadaan Memberatkan", false);
        String meringankan = bacaTeks("Keadaan Meringankan", false);

        String sourcePages = bacaTeks("Sumber Halaman", true);
        String status = bacaTeks("Status Verifikasi", true);

        // Constructor satu terdakwa
        Putusan baru = new Putusan(
                caseId,
                nomorPerkara,
                pengadilan,
                tanggal,
                terdakwaKode,
                jenisNarkotika,
                berat,
                pasalDakwaan,
                pasalTerbukti,
                tuntutan,
                vonis,
                denda,
                barangBukti,
                memberatkan,
                meringankan,
                sourcePages,
                status
        );

        service.tambahPutusan(baru);

        System.out.println("\nData berhasil ditambahkan!");
        System.out.println("Jumlah data: " + service.jumlahPutusan());
        System.out.println("Invariant: " + service.validateInvariant());

        catatAudit("MENAMBAH PUTUSAN: " + caseId);
    }


    // ==================================================
    // 3. HAPUS DATA PUTUSAN
    // ==================================================

    private static void hapusDataPutusan(
            PutusanService service) {

        System.out.println("\n========== HAPUS DATA PUTUSAN ==========");

        if (service.isEmpty()) {
            System.out.println("Data kosong.");
            return;
        }

        String caseId = bacaTeks("Masukkan Case ID", true);

        int posisi = -1;

        for (int i = 0; i < service.jumlahPutusan(); i++) {

            Putusan p = service.ambilPutusan(i);

            if (p.getCaseId() != null
                    && p.getCaseId().equalsIgnoreCase(caseId)) {

                posisi = i;
                break;
            }
        }

        if (posisi == -1) {
            System.out.println("Data tidak ditemukan!");
            return;
        }

        Putusan terhapus = service.hapusPutusan(posisi);

        System.out.println("\nData berhasil dihapus!");
        System.out.println("Case ID: " + terhapus.getCaseId());
        System.out.println("Sisa data: " + service.jumlahPutusan());
        System.out.println("Invariant: " + service.validateInvariant());

        catatAudit("MENGHAPUS PUTUSAN: " + caseId);
    }


    // ==================================================
    // 4. PENCARIAN CASE ID ATAU NOMOR PERKARA
    // ==================================================

    private static void cariPutusan(
            PutusanService service) {

        System.out.println("\n========== PENCARIAN PUTUSAN ==========");
        System.out.println("1. Berdasarkan Case ID");
        System.out.println("2. Berdasarkan Nomor Perkara");

        int pilihan = bacaInt("Pilih pencarian");

        if (pilihan != 1 && pilihan != 2) {
            System.out.println("Pilihan tidak tersedia.");
            return;
        }

        String kataKunci = bacaTeks("Masukkan kata kunci", true);

        int langkah = 0;
        Putusan hasil = null;

        for (int i = 0; i < service.jumlahPutusan(); i++) {

            langkah++;

            Putusan p = service.ambilPutusan(i);

            if (pilihan == 1
                    && p.getCaseId() != null
                    && p.getCaseId().equalsIgnoreCase(kataKunci)) {

                hasil = p;
                break;
            }

            if (pilihan == 2
                    && p.getNomorPerkara() != null
                    && p.getNomorPerkara().equalsIgnoreCase(kataKunci)) {

                hasil = p;
                break;
            }
        }

        if (hasil == null) {

            System.out.println("\nData tidak ditemukan!");

        } else {

            System.out.println("\nData ditemukan!");
            tampilkanDetail(hasil);
        }

        System.out.println("Jumlah langkah pencarian: " + langkah);

        catatAudit("PENCARIAN: " + kataKunci
                + " | Langkah: " + langkah);
    }


    // ==================================================
    // 5. FILTER DATA PUTUSAN
    // ==================================================

    private static void filterPutusan(
            PutusanService service) {

        System.out.println("\n========== FILTER DATA PUTUSAN ==========");

        if (service.isEmpty()) {
            System.out.println("Data kosong.");
            return;
        }

        System.out.println(
                "Kosongkan input jika tidak ingin menggunakan kriteria."
        );

        System.out.print("Jenis narkotika: ");
        String jenis = input.nextLine().trim();

        System.out.print("Berat minimum (gram): ");
        String beratMinInput = input.nextLine().trim();

        System.out.print("Berat maksimum (gram): ");
        String beratMaxInput = input.nextLine().trim();

        System.out.print("Pasal terbukti: ");
        String pasal = input.nextLine().trim();

        System.out.print("Vonis minimum (bulan): ");
        String vonisMinInput = input.nextLine().trim();

        System.out.print("Vonis maksimum (bulan): ");
        String vonisMaxInput = input.nextLine().trim();

        double beratMin;
        double beratMax;
        int vonisMin;
        int vonisMax;

        try {

            beratMin = beratMinInput.isEmpty()
                    ? 0 : Double.parseDouble(beratMinInput);

            beratMax = beratMaxInput.isEmpty()
                    ? Double.MAX_VALUE : Double.parseDouble(beratMaxInput);

            vonisMin = vonisMinInput.isEmpty()
                    ? 0 : Integer.parseInt(vonisMinInput);

            vonisMax = vonisMaxInput.isEmpty()
                    ? Integer.MAX_VALUE : Integer.parseInt(vonisMaxInput);

            if (!Double.isFinite(beratMin)
                    || !Double.isFinite(beratMax)
                    || beratMin < 0
                    || beratMax < beratMin
                    || vonisMin < 0
                    || vonisMax < vonisMin) {

                System.out.println("Rentang filter tidak valid.");
                return;
            }

        } catch (NumberFormatException e) {

            System.out.println("Format angka tidak valid.");
            return;
        }

        int ditemukan = 0;

        System.out.println("\nHASIL FILTER");
        System.out.println("----------------------------------------------");

        for (int i = 0; i < service.jumlahPutusan(); i++) {

            Putusan p = service.ambilPutusan(i);

            boolean cocok = true;

            // Filter jenis narkotika
            if (!jenis.isEmpty()) {

                cocok = p.getJenisNarkotika() != null
                        && p.getJenisNarkotika()
                        .toLowerCase()
                        .contains(jenis.toLowerCase());
            }

            // Filter berat barang bukti
            if (cocok) {

                double berat = p.getBeratBarangBuktiGram();

                cocok = berat >= beratMin && berat <= beratMax;
            }

            // Filter pasal terbukti
            if (cocok && !pasal.isEmpty()) {

                cocok = p.getPasalTerbukti() != null
                        && p.getPasalTerbukti()
                        .toLowerCase()
                        .contains(pasal.toLowerCase());
            }

            // Filter vonis multi-terdakwa:
            // Perkara cocok jika minimal satu nilai vonis
            // berada dalam rentang yang diminta.
            if (cocok) {
                cocok = memilikiVonisDalamRentang(
                        p,
                        vonisMin,
                        vonisMax
                );
            }

            if (cocok) {

                ditemukan++;

                System.out.println("\nHASIL KE-" + ditemukan);
                System.out.println("Case ID       : " + nilai(p.getCaseId()));
                System.out.println("Nomor Perkara : " + nilai(p.getNomorPerkara()));
                System.out.println("Jenis         : " + nilai(p.getJenisNarkotika()));
                System.out.println("Berat         : "
                        + angka(p.getBeratBarangBuktiGram(), " gram"));
                System.out.println("Pasal         : " + nilai(p.getPasalTerbukti()));

                tampilkanTuntutanDanVonis(p);
            }
        }

        System.out.println("\n----------------------------------------------");
        System.out.println("Jumlah perkara ditemukan: " + ditemukan);

        catatAudit("FILTER DATA: " + ditemukan + " hasil");
    }


    // Memeriksa apakah salah satu nilai vonis berada dalam rentang
    private static boolean memilikiVonisDalamRentang(
            Putusan p,
            int minimum,
            int maksimum) {

        int[] vonis = p.getVonisBulanPerTerdakwa();

        for (int nilai : vonis) {

            if (nilai >= minimum
                    && nilai <= maksimum) {

                return true;
            }
        }

        return false;
    }


    // ==================================================
    // 6. STATISTIK DAN AGREGASI
    // ==================================================

    private static void tampilkanStatistik(
            PutusanService service) {

        System.out.println("\n========== STATISTIK DATA PUTUSAN ==========");

        int jumlah = service.jumlahPutusan();

        if (jumlah == 0) {
            System.out.println("Data kosong.");
            return;
        }

        long totalVonis = 0;
        long totalDenda = 0;

        int jumlahNilaiVonis = 0;

        String[] jenisUnik = new String[jumlah];
        int[] frekuensi = new int[jumlah];

        int jumlahJenis = 0;

        for (int i = 0; i < jumlah; i++) {

            Putusan p = service.ambilPutusan(i);

            // Menjumlahkan seluruh nilai vonis
            // dari setiap terdakwa.
            int[] vonis = p.getVonisBulanPerTerdakwa();

            for (int nilai : vonis) {

                if (nilai > 0) {
                    totalVonis += nilai;
                    jumlahNilaiVonis++;
                }
            }

            // Denda dihitung sekali per perkara.
            totalDenda += p.getDendaRupiah();

            // Distribusi jenis narkotika
            String jenis = p.getJenisNarkotika();

            if (jenis == null || jenis.trim().isEmpty()) {
                continue;
            }

            int posisi = -1;

            for (int j = 0; j < jumlahJenis; j++) {

                if (jenisUnik[j].equalsIgnoreCase(jenis)) {
                    posisi = j;
                    break;
                }
            }

            if (posisi == -1) {

                jenisUnik[jumlahJenis] = jenis;
                frekuensi[jumlahJenis] = 1;
                jumlahJenis++;

            } else {

                frekuensi[posisi]++;
            }
        }

        double rataRata = jumlahNilaiVonis == 0
                ? 0
                : (double) totalVonis / jumlahNilaiVonis;

        System.out.println("Jumlah perkara       : " + jumlah);
        System.out.println("Jumlah nilai vonis   : " + jumlahNilaiVonis);
        System.out.printf("Rata-rata vonis      : %.2f bulan%n", rataRata);
        System.out.println("Total denda          : Rp"
                + String.format("%,d", totalDenda));

        System.out.println("\nDISTRIBUSI JENIS NARKOTIKA");
        System.out.println("----------------------------------------------");

        for (int i = 0; i < jumlahJenis; i++) {

            System.out.println(
                    jenisUnik[i] + " : "
                            + frekuensi[i] + " perkara"
            );
        }

        catatAudit("MELIHAT STATISTIK DATA");
    }


    // ==================================================
    // 7. SORTING BERDASARKAN VONIS
    // ==================================================

    private static void urutkanBerdasarkanVonis(
            PutusanService service) {

        System.out.println(
                "\n========== SORTING BERDASARKAN VONIS =========="
        );

        int jumlah = service.jumlahPutusan();

        if (jumlah == 0) {
            System.out.println("Data kosong.");
            return;
        }

        // Array sementara agar Linked List tidak berubah
        Putusan[] data = new Putusan[jumlah];

        for (int i = 0; i < jumlah; i++) {
            data[i] = service.ambilPutusan(i);
        }

        // Selection Sort descending
        // Kunci pengurutan: vonis terbesar dalam perkara.
        for (int i = 0; i < jumlah - 1; i++) {

            int indeksTerbesar = i;

            for (int j = i + 1; j < jumlah; j++) {

                if (vonisTerbesar(data[j])
                        > vonisTerbesar(data[indeksTerbesar])) {

                    indeksTerbesar = j;
                }
            }

            Putusan sementara = data[i];
            data[i] = data[indeksTerbesar];
            data[indeksTerbesar] = sementara;
        }

        System.out.println(
                "Urutan vonis terbesar ke terkecil"
        );

        System.out.println("----------------------------------------------");

        for (int i = 0; i < jumlah; i++) {

            System.out.println(
                    (i + 1) + ". "
                            + data[i].getCaseId()
                            + " | "
                            + data[i].getNomorPerkara()
                            + " | Vonis terbesar: "
                            + angka(vonisTerbesar(data[i]), " bulan")
            );

            tampilkanTuntutanDanVonis(data[i]);
        }

        System.out.println("----------------------------------------------");
        System.out.println("Total perkara diurutkan: " + jumlah);

        System.out.println(
                "Invariant Linked List: "
                        + service.validateInvariant()
        );

        catatAudit("SORTING BERDASARKAN VONIS");
    }


    // Mengambil vonis terbesar dari sebuah perkara
    private static int vonisTerbesar(Putusan p) {

        int[] vonis = p.getVonisBulanPerTerdakwa();

        int terbesar = 0;

        for (int nilai : vonis) {

            if (nilai > terbesar) {
                terbesar = nilai;
            }
        }

        return terbesar;
    }


    // ==================================================
    // 8. AUDIT AKTIVITAS MENGGUNAKAN STACK
    // ==================================================

    private static void catatAudit(String aktivitas) {

        String waktu = LocalDate.now().toString();

        audit.push(waktu + " | " + aktivitas);
    }


    private static void tampilkanAudit() {

        System.out.println("\n========== AUDIT AKTIVITAS ==========");

        if (audit.isEmpty()) {
            System.out.println("Belum ada aktivitas.");
            return;
        }

        System.out.println("Urutan: Aktivitas terbaru ke terlama");
        System.out.println("----------------------------------------------");

        Stack<String> temporary = new Stack<>();

        int jumlah = 0;

        // Memindahkan data ke Stack sementara
        // sekaligus menampilkan aktivitas terbaru.
        while (!audit.isEmpty()) {

            String aktivitas = audit.pop();

            temporary.push(aktivitas);

            System.out.println(
                    (++jumlah) + ". " + aktivitas
            );
        }

        // Mengembalikan seluruh data ke Stack utama
        audit.restore(temporary);

        System.out.println("----------------------------------------------");
        System.out.println("Total aktivitas: " + audit.size());

        System.out.println(
                "Invariant Stack: " + audit.validateInvariant()
        );
    }


    // ==================================================
    // 9. EKSPOR DATA TXT / CSV
    // ==================================================

    private static void eksporData(
            PutusanService service) {

        System.out.println("\n========== EKSPOR DATA ==========");
        System.out.println("1. Ekspor TXT");
        System.out.println("2. Ekspor CSV");

        int pilihan = bacaInt("Pilih format ekspor");

        if (pilihan != 1 && pilihan != 2) {
            System.out.println("Pilihan tidak tersedia.");
            return;
        }

        String namaFile;

        if (pilihan == 1) {
            namaFile = "laporan-putusan.txt";
        } else {
            namaFile = "laporan-putusan.csv";
        }

        try (BufferedWriter writer =
                     new BufferedWriter(new FileWriter(namaFile))) {

            if (pilihan == 2) {

                writer.write(
                        "caseId,nomorPerkara,pengadilan,"
                                + "tanggalPutusan,terdakwaKode,jenisNarkotika,"
                                + "beratBarangBuktiGram,pasalDakwaan,pasalTerbukti,"
                                + "tuntutanBulan,vonisBulan,dendaRupiah,"
                                + "barangBukti,keadaanMemberatkan,"
                                + "keadaanMeringankan,sourcePages,statusVerifikasi"
                );

                writer.newLine();

            } else {

                writer.write(
                        "LAPORAN DATA PUTUSAN PENGADILAN NEGERI BANJARBARU"
                );

                writer.newLine();
                writer.write("Jumlah perkara: " + service.jumlahPutusan());
                writer.newLine();
                writer.newLine();
            }

            for (int i = 0; i < service.jumlahPutusan(); i++) {

                Putusan p = service.ambilPutusan(i);

                String[] kolom = {
                        nilai(p.getCaseId()),
                        nilai(p.getNomorPerkara()),
                        nilai(p.getPengadilan()),
                        nilai(p.getTanggalPutusan()),
                        nilai(p.getTerdakwaKode()),
                        nilai(p.getJenisNarkotika()),
                        String.valueOf(p.getBeratBarangBuktiGram()),
                        nilai(p.getPasalDakwaan()),
                        nilai(p.getPasalTerbukti()),

                        // Simpan semua nilai tuntutan dan vonis
                        arrayKeTeks(p.getTuntutanBulanPerTerdakwa()),
                        arrayKeTeks(p.getVonisBulanPerTerdakwa()),

                        String.valueOf(p.getDendaRupiah()),
                        nilai(p.getBarangBukti()),
                        nilai(p.getKeadaanMemberatkan()),
                        nilai(p.getKeadaanMeringankan()),
                        nilai(p.getSourcePages()),
                        nilai(p.getStatusVerifikasi())
                };

                if (pilihan == 2) {

                    for (int j = 0; j < kolom.length; j++) {

                        if (j > 0) {
                            writer.write(",");
                        }

                        writer.write(escapeCSV(kolom[j]));
                    }

                } else {

                    writer.write("Case ID: " + p.getCaseId());
                    writer.newLine();

                    writer.write(
                            "Nomor Perkara: " + p.getNomorPerkara()
                    );

                    writer.newLine();

                    writer.write(
                            "Jenis Narkotika: " + p.getJenisNarkotika()
                    );

                    writer.newLine();

                    writer.write(
                            "Berat Barang Bukti: "
                                    + p.getBeratBarangBuktiGram()
                                    + " gram"
                    );

                    writer.newLine();

                    writer.write(
                            "Tuntutan: "
                                    + arrayKeTeks(
                                    p.getTuntutanBulanPerTerdakwa()
                            )
                    );

                    writer.newLine();

                    writer.write(
                            "Vonis: "
                                    + arrayKeTeks(
                                    p.getVonisBulanPerTerdakwa()
                            )
                    );

                    writer.newLine();

                    writer.write(
                            "Denda: Rp" + p.getDendaRupiah()
                    );

                    writer.newLine();

                    writer.write(
                            "----------------------------------------------"
                    );
                }

                writer.newLine();
            }

            System.out.println("\nEkspor berhasil!");
            System.out.println("Nama file: " + namaFile);

            catatAudit("EKSPOR DATA: " + namaFile);

        } catch (IOException e) {

            System.out.println(
                    "Ekspor gagal: " + e.getMessage()
            );
        }
    }


    // Mengubah array angka menjadi teks
    private static String arrayKeTeks(int[] data) {

        if (data == null || data.length == 0) {
            return "Belum tersedia";
        }

        return Arrays.toString(data);
    }


    // Mengamankan nilai CSV
    private static String escapeCSV(String teks) {

        if (teks == null) {
            teks = "";
        }

        return "\"" + teks.replace("\"", "\"\"") + "\"";
    }


    // ==================================================
    // METHOD BANTU INPUT
    // ==================================================

    private static String bacaTeks(
            String label,
            boolean wajib) {

        while (true) {

            System.out.print(label + ": ");

            String hasil = input.nextLine().trim();

            if (!wajib && hasil.isEmpty()) {
                return null;
            }

            if (wajib && hasil.isEmpty()) {
                System.out.println("Kolom ini wajib diisi!");
                continue;
            }

            return hasil;
        }
    }


    private static LocalDate bacaTanggal() {

        while (true) {

            System.out.print("Tanggal Putusan (YYYY-MM-DD): ");

            try {

                return LocalDate.parse(input.nextLine().trim());

            } catch (DateTimeParseException e) {

                System.out.println("Format tanggal salah!");
            }
        }
    }


    private static double bacaDouble(String label) {

        while (true) {

            System.out.print(label + ": ");

            try {

                double nilai = Double.parseDouble(
                        input.nextLine().trim().replace(',', '.')
                );

                if (!Double.isFinite(nilai) || nilai < 0) {
                    System.out.println("Angka tidak valid!");
                    continue;
                }

                return nilai;

            } catch (NumberFormatException e) {

                System.out.println("Masukkan angka yang valid!");
            }
        }
    }


    private static int bacaInt(String label) {

        while (true) {

            System.out.print(label + ": ");

            try {

                int nilai = Integer.parseInt(
                        input.nextLine().trim()
                );

                if (nilai < 0) {
                    System.out.println("Angka tidak boleh negatif!");
                    continue;
                }

                return nilai;

            } catch (NumberFormatException e) {

                System.out.println("Masukkan bilangan bulat!");
            }
        }
    }


    private static long bacaLong(String label) {

        while (true) {

            System.out.print(label + ": ");

            try {

                long nilai = Long.parseLong(
                        input.nextLine().trim()
                );

                if (nilai < 0) {
                    System.out.println("Angka tidak boleh negatif!");
                    continue;
                }

                return nilai;

            } catch (NumberFormatException e) {

                System.out.println("Masukkan bilangan bulat!");
            }
        }
    }


    // ==================================================
    // MENAMPILKAN DETAIL PUTUSAN
    // ==================================================

    private static void tampilkanDetail(Putusan p) {

        System.out.println(
                "Case ID                 : " + nilai(p.getCaseId())
        );

        System.out.println(
                "Nomor Perkara           : " + nilai(p.getNomorPerkara())
        );

        System.out.println(
                "Pengadilan              : " + nilai(p.getPengadilan())
        );

        System.out.println(
                "Tanggal Putusan         : " + nilai(p.getTanggalPutusan())
        );

        System.out.println(
                "Kode Terdakwa           : " + nilai(p.getTerdakwaKode())
        );

        System.out.println(
                "Jenis Narkotika         : " + nilai(p.getJenisNarkotika())
        );

        System.out.println(
                "Berat Barang Bukti      : "
                        + angka(p.getBeratBarangBuktiGram(), " gram")
        );

        System.out.println(
                "Pasal Dakwaan           : " + nilai(p.getPasalDakwaan())
        );

        System.out.println(
                "Pasal Terbukti          : " + nilai(p.getPasalTerbukti())
        );

        // Menampilkan seluruh tuntutan dan vonis
        tampilkanTuntutanDanVonis(p);

        System.out.println(
                "Denda                   : "
                        + angka(p.getDendaRupiah(), " rupiah")
        );

        System.out.println(
                "Barang Bukti            : " + nilai(p.getBarangBukti())
        );

        System.out.println(
                "Keadaan Memberatkan     : "
                        + nilai(p.getKeadaanMemberatkan())
        );

        System.out.println(
                "Keadaan Meringankan     : "
                        + nilai(p.getKeadaanMeringankan())
        );

        System.out.println(
                "Sumber Halaman          : " + nilai(p.getSourcePages())
        );

        System.out.println(
                "Status Verifikasi       : " + nilai(p.getStatusVerifikasi())
        );
    }


    // Menampilkan tuntutan dan vonis masing-masing terdakwa
    private static void tampilkanTuntutanDanVonis(Putusan p) {

        int[] tuntutan = p.getTuntutanBulanPerTerdakwa();
        int[] vonis = p.getVonisBulanPerTerdakwa();

        System.out.println("\nRINCIAN TUNTUTAN DAN VONIS");

        int jumlah = Math.max(tuntutan.length, vonis.length);

        if (jumlah == 0) {
            System.out.println("Data tuntutan dan vonis belum tersedia.");
            return;
        }

        for (int i = 0; i < jumlah; i++) {

            System.out.println("Terdakwa ke-" + (i + 1));

            if (i < tuntutan.length) {
                System.out.println(
                        "  Tuntutan : "
                                + angka(tuntutan[i], " bulan")
                );
            } else {
                System.out.println("  Tuntutan : Belum tersedia");
            }

            if (i < vonis.length) {
                System.out.println(
                        "  Vonis    : "
                                + angka(vonis[i], " bulan")
                );
            } else {
                System.out.println("  Vonis    : Belum tersedia");
            }
        }
    }


    // ==================================================
    // METHOD BANTU NILAI DAN ANGKA
    // ==================================================

    private static String nilai(Object data) {

        return data == null
                ? "Belum tersedia"
                : data.toString();
    }


    private static String angka(double data, String satuan) {

        return data <= 0
                ? "Belum tersedia"
                : data + satuan;
    }


    private static String angka(long data, String satuan) {

        return data <= 0
                ? "Belum tersedia"
                : String.format("%,d", data) + satuan;
    }
}
