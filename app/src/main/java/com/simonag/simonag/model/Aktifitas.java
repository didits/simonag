package com.simonag.simonag.model;

import org.parceler.Parcel;

/**
 * Created by Zaki on 13/06/2017.
 */
@Parcel
public class Aktifitas {
    int no;
    int id;
    String nama;
    String kategori;
    int target;
    int target_revenue;
    int realisasi;
    int realisasi_revenue;
    String duedate;
    String satuan;
    public Aktifitas(){}
    public Aktifitas(int no, int id, String nama, String kategori, int target, int revenue, int realisasi, int realisasi_revenue, String duedate, String satuan) {
        this.no = no;
        this.id = id;
        this.nama = nama;
        this.kategori = kategori;
        this.target = target;
        this.target_revenue = revenue;
        this.realisasi = realisasi;
        this.realisasi_revenue = realisasi_revenue;
        this.duedate = duedate;
        this.satuan = satuan;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
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

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    public int getTarget_revenue() {
        return target_revenue;
    }

    public void setTarget_revenue(int target_revenue) {
        this.target_revenue = target_revenue;
    }

    public int getRealisasi() {
        return realisasi;
    }

    public void setRealisasi(int realisasi) {
        this.realisasi = realisasi;
    }

    public int getRealisasi_revenue() {
        return realisasi_revenue;
    }

    public void setRealisasi_revenue(int realisasi_revenue) {
        this.realisasi_revenue = realisasi_revenue;
    }

    public String getDuedate() {
        return duedate;
    }

    public void setDuedate(String duedate) {
        this.duedate = duedate;
    }

    public String getSatuan() {
        return satuan;
    }

    public void setSatuan(String satuan) {
        this.satuan = satuan;
    }
}
