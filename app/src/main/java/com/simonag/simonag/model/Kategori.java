package com.simonag.simonag.model;

/**
 * Created by diditsepiyanto on 6/17/17.
 */

public class Kategori {
    int id;
    String nama;
    int status;
    int total_rupiah;
    int total_aktifitas;

    public Kategori(int id, String nama, int status, int total_rupiah, int total_aktifitas) {
        this.id = id;
        this.nama = nama;
        this.status = status;
        this.total_rupiah = total_rupiah;
        this.total_aktifitas = total_aktifitas;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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
}
