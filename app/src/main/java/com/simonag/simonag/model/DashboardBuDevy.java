package com.simonag.simonag.model;

/**
 * Created by diditsepiyanto on 6/17/17.
 */

public class DashboardBuDevy {
    int id_perusahaan;
    String nama_perusahaan;
    String keterangan;
    int total_rupiah;
    int total_aktifitas;
    String image;

    public DashboardBuDevy(int id_perusahaan, String nama_perusahaan, String keterangan, int total_rupiah, int total_aktifitas, String image) {
        this.id_perusahaan = id_perusahaan;
        this.nama_perusahaan = nama_perusahaan;
        this.keterangan = keterangan;
        this.total_rupiah = total_rupiah;
        this.total_aktifitas = total_aktifitas;
        this.image = image;
    }

    public int getId_perusahaan() {
        return id_perusahaan;
    }

    public void setId_perusahaan(int id_perusahaan) {
        this.id_perusahaan = id_perusahaan;
    }

    public String getNama_perusahaan() {
        return nama_perusahaan;
    }

    public void setNama_perusahaan(String nama_perusahaan) {
        this.nama_perusahaan = nama_perusahaan;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public int getTotal_rupiah() {
        return total_rupiah;
    }

    public void setTotal_rupiah(int total_rupiah) {
        this.total_rupiah = total_rupiah;
    }

    public int getTotal_aktifitas() {
        return total_aktifitas;
    }

    public void setTotal_aktifitas(int total_aktifitas) {
        this.total_aktifitas = total_aktifitas;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
