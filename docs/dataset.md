# Dokumentasi Dataset Putusan

## 1. Deskripsi Dataset
Dataset ini berisi 50 data putusan Pengadilan Negeri Banjarbaru yang digunakan sebagai sumber data utama aplikasi.

## 2. Lokasi File
File dataset utama berada di:

`src/main/resources/data/putusan-50.csv`

## 3. Format Dataset
Dataset menggunakan format CSV (Comma-Separated Values).

## 4. Struktur Kolom

| No. | Nama Kolom | Keterangan |
|---|---|---|
| 1 | `caseId` | Identitas unik data putusan |
| 2 | `nomorPerkara` | Nomor perkara |
| 3 | `pengadilan` | Nama pengadilan |
| 4 | `tanggalPutusan` | Tanggal putusan |
| 5 | `terdakwaKode` | Kode identitas terdakwa |

## 5. Jumlah Data
Dataset berisi 50 record data, tidak termasuk baris header.

## 6. Penggunaan Dataset
Dataset digunakan oleh aplikasi untuk membaca dan mengolah informasi putusan.