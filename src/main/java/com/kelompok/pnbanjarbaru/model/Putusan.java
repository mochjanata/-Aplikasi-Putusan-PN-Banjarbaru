
package com.kelompok.pnbanjarbaru.model;

import java.time.LocalDate;
import java.util.Arrays;

public class Putusan {

    // 1. Identitas perkara
    private String caseId;
    private String nomorPerkara;
    private String pengadilan;
    private LocalDate tanggalPutusan;

    // 2. Identitas terdakwa
    private String terdakwaKode;

    // 3. Informasi narkotika
    private String jenisNarkotika;
    private double beratBarangBuktiGram;

    // 4. Informasi pasal
    private String pasalDakwaan;
    private String pasalTerbukti;

    // 5. Tuntutan dan vonis
    // Dapat menyimpan lebih dari satu terdakwa
    private int[] tuntutanBulanPerTerdakwa;
    private int[] vonisBulanPerTerdakwa;

    // Nilai tunggal untuk kompatibilitas perkara satu terdakwa.
    // Bernilai null jika terdapat lebih dari satu nilai.
    private Integer tuntutanBulan;
    private Integer vonisBulan;

    // 6. Informasi denda dan barang bukti
    private long dendaRupiah;
    private String barangBukti;

    // 7. Keadaan perkara
    private String keadaanMemberatkan;
    private String keadaanMeringankan;

    // 8. Sumber dan verifikasi
    private String sourcePages;
    private String statusVerifikasi;


    // ==========================================
    // CONSTRUCTOR 1: PERKARA SATU TERDAKWA
    // ==========================================

    public Putusan(
            String caseId,
            String nomorPerkara,
            String pengadilan,
            LocalDate tanggalPutusan,
            String terdakwaKode,
            String jenisNarkotika,
            double beratBarangBuktiGram,
            String pasalDakwaan,
            String pasalTerbukti,
            int tuntutanBulan,
            int vonisBulan,
            long dendaRupiah,
            String barangBukti,
            String keadaanMemberatkan,
            String keadaanMeringankan,
            String sourcePages,
            String statusVerifikasi) {

        this(
                caseId,
                nomorPerkara,
                pengadilan,
                tanggalPutusan,
                terdakwaKode,
                jenisNarkotika,
                beratBarangBuktiGram,
                pasalDakwaan,
                pasalTerbukti,
                new int[]{tuntutanBulan},
                new int[]{vonisBulan},
                dendaRupiah,
                barangBukti,
                keadaanMemberatkan,
                keadaanMeringankan,
                sourcePages,
                statusVerifikasi
        );
    }


    // ==========================================
    // CONSTRUCTOR 2: PERKARA MULTI-TERDAKWA
    // ==========================================

    public Putusan(
            String caseId,
            String nomorPerkara,
            String pengadilan,
            LocalDate tanggalPutusan,
            String terdakwaKode,
            String jenisNarkotika,
            double beratBarangBuktiGram,
            String pasalDakwaan,
            String pasalTerbukti,
            int[] tuntutanBulanPerTerdakwa,
            int[] vonisBulanPerTerdakwa,
            long dendaRupiah,
            String barangBukti,
            String keadaanMemberatkan,
            String keadaanMeringankan,
            String sourcePages,
            String statusVerifikasi) {

        this.caseId = caseId;
        this.nomorPerkara = nomorPerkara;
        this.pengadilan = pengadilan;
        this.tanggalPutusan = tanggalPutusan;
        this.terdakwaKode = terdakwaKode;
        this.jenisNarkotika = jenisNarkotika;
        this.beratBarangBuktiGram = beratBarangBuktiGram;
        this.pasalDakwaan = pasalDakwaan;
        this.pasalTerbukti = pasalTerbukti;

        setTuntutanBulanPerTerdakwa(tuntutanBulanPerTerdakwa);
        setVonisBulanPerTerdakwa(vonisBulanPerTerdakwa);

        this.dendaRupiah = dendaRupiah;
        this.barangBukti = barangBukti;
        this.keadaanMemberatkan = keadaanMemberatkan;
        this.keadaanMeringankan = keadaanMeringankan;
        this.sourcePages = sourcePages;
        this.statusVerifikasi = statusVerifikasi;
    }


    // ==========================================
    // GETTER DAN SETTER IDENTITAS PERKARA
    // ==========================================

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public String getNomorPerkara() {
        return nomorPerkara;
    }

    public void setNomorPerkara(String nomorPerkara) {
        this.nomorPerkara = nomorPerkara;
    }

    public String getPengadilan() {
        return pengadilan;
    }

    public void setPengadilan(String pengadilan) {
        this.pengadilan = pengadilan;
    }

    public LocalDate getTanggalPutusan() {
        return tanggalPutusan;
    }

    public void setTanggalPutusan(LocalDate tanggalPutusan) {
        this.tanggalPutusan = tanggalPutusan;
    }


    // ==========================================
    // GETTER DAN SETTER TERDAKWA
    // ==========================================

    public String getTerdakwaKode() {
        return terdakwaKode;
    }

    public void setTerdakwaKode(String terdakwaKode) {
        this.terdakwaKode = terdakwaKode;
    }


    // ==========================================
    // GETTER DAN SETTER INFORMASI NARKOTIKA
    // ==========================================

    public String getJenisNarkotika() {
        return jenisNarkotika;
    }

    public void setJenisNarkotika(String jenisNarkotika) {
        this.jenisNarkotika = jenisNarkotika;
    }

    public double getBeratBarangBuktiGram() {
        return beratBarangBuktiGram;
    }

    public void setBeratBarangBuktiGram(
            double beratBarangBuktiGram) {

        this.beratBarangBuktiGram = beratBarangBuktiGram;
    }


    // ==========================================
    // GETTER DAN SETTER PASAL
    // ==========================================

    public String getPasalDakwaan() {
        return pasalDakwaan;
    }

    public void setPasalDakwaan(String pasalDakwaan) {
        this.pasalDakwaan = pasalDakwaan;
    }

    public String getPasalTerbukti() {
        return pasalTerbukti;
    }

    public void setPasalTerbukti(String pasalTerbukti) {
        this.pasalTerbukti = pasalTerbukti;
    }


    // ==========================================
    // TUNTUTAN MULTI-TERDAKWA
    // ==========================================

    public int[] getTuntutanBulanPerTerdakwa() {
        return tuntutanBulanPerTerdakwa.clone();
    }

    public void setTuntutanBulanPerTerdakwa(
            int[] tuntutanBulanPerTerdakwa) {

        if (tuntutanBulanPerTerdakwa == null) {
            this.tuntutanBulanPerTerdakwa = new int[0];
        } else {
            this.tuntutanBulanPerTerdakwa =
                    tuntutanBulanPerTerdakwa.clone();
        }

        if (this.tuntutanBulanPerTerdakwa.length == 1) {
            this.tuntutanBulan =
                    this.tuntutanBulanPerTerdakwa[0];
        } else {
            this.tuntutanBulan = null;
        }
    }

    public Integer getTuntutanBulan() {
        return tuntutanBulan;
    }

    public void setTuntutanBulan(int tuntutanBulan) {
        setTuntutanBulanPerTerdakwa(
                new int[]{tuntutanBulan}
        );
    }

    public int getJumlahNilaiTuntutan() {
        return tuntutanBulanPerTerdakwa.length;
    }


    // ==========================================
    // VONIS MULTI-TERDAKWA
    // ==========================================

    public int[] getVonisBulanPerTerdakwa() {
        return vonisBulanPerTerdakwa.clone();
    }

    public void setVonisBulanPerTerdakwa(
            int[] vonisBulanPerTerdakwa) {

        if (vonisBulanPerTerdakwa == null) {
            this.vonisBulanPerTerdakwa = new int[0];
        } else {
            this.vonisBulanPerTerdakwa =
                    vonisBulanPerTerdakwa.clone();
        }

        if (this.vonisBulanPerTerdakwa.length == 1) {
            this.vonisBulan =
                    this.vonisBulanPerTerdakwa[0];
        } else {
            this.vonisBulan = null;
        }
    }

    public Integer getVonisBulan() {
        return vonisBulan;
    }

    public void setVonisBulan(int vonisBulan) {
        setVonisBulanPerTerdakwa(
                new int[]{vonisBulan}
        );
    }

    public int getJumlahNilaiVonis() {
        return vonisBulanPerTerdakwa.length;
    }


    // ==========================================
    // GETTER DAN SETTER DENDA
    // ==========================================

    public long getDendaRupiah() {
        return dendaRupiah;
    }

    public void setDendaRupiah(long dendaRupiah) {
        this.dendaRupiah = dendaRupiah;
    }


    // ==========================================
    // GETTER DAN SETTER BARANG BUKTI
    // ==========================================

    public String getBarangBukti() {
        return barangBukti;
    }

    public void setBarangBukti(String barangBukti) {
        this.barangBukti = barangBukti;
    }


    // ==========================================
    // GETTER DAN SETTER KEADAAN PERKARA
    // ==========================================

    public String getKeadaanMemberatkan() {
        return keadaanMemberatkan;
    }

    public void setKeadaanMemberatkan(
            String keadaanMemberatkan) {

        this.keadaanMemberatkan = keadaanMemberatkan;
    }

    public String getKeadaanMeringankan() {
        return keadaanMeringankan;
    }

    public void setKeadaanMeringankan(
            String keadaanMeringankan) {

        this.keadaanMeringankan = keadaanMeringankan;
    }


    // ==========================================
    // GETTER DAN SETTER SUMBER DATA
    // ==========================================

    public String getSourcePages() {
        return sourcePages;
    }

    public void setSourcePages(String sourcePages) {
        this.sourcePages = sourcePages;
    }

    public String getStatusVerifikasi() {
        return statusVerifikasi;
    }

    public void setStatusVerifikasi(String statusVerifikasi) {
        this.statusVerifikasi = statusVerifikasi;
    }


    // ==========================================
    // TAMPILAN DATA PUTUSAN
    // ==========================================

    @Override
    public String toString() {

        return "Putusan{" +
                "caseId='" + caseId + '\'' +
                ", nomorPerkara='" + nomorPerkara + '\'' +
                ", pengadilan='" + pengadilan + '\'' +
                ", tanggalPutusan=" + tanggalPutusan +
                ", terdakwaKode='" + terdakwaKode + '\'' +
                ", jenisNarkotika='" + jenisNarkotika + '\'' +
                ", beratBarangBuktiGram=" + beratBarangBuktiGram +
                ", pasalDakwaan='" + pasalDakwaan + '\'' +
                ", pasalTerbukti='" + pasalTerbukti + '\'' +
                ", tuntutan=" +
                Arrays.toString(tuntutanBulanPerTerdakwa) +
                ", vonis=" +
                Arrays.toString(vonisBulanPerTerdakwa) +
                ", dendaRupiah=" + dendaRupiah +
                ", barangBukti='" + barangBukti + '\'' +
                ", keadaanMemberatkan='" + keadaanMemberatkan + '\'' +
                ", keadaanMeringankan='" + keadaanMeringankan + '\'' +
                ", sourcePages='" + sourcePages + '\'' +
                ", statusVerifikasi='" + statusVerifikasi + '\'' +
                '}';
    }
}
