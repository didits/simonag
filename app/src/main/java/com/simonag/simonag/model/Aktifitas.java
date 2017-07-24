package com.simonag.simonag.model;

import org.parceler.Parcel;

import java.math.BigInteger;

/**
 * Created by Zaki on 13/06/2017.
 */
@Parcel
public class Aktifitas {
    int no;
    int id;
    String nama;
    String kategori;
    BigInteger target;
    BigInteger target_revenue;
    BigInteger realisasi;
    double realisasi_persen;
    BigInteger realisasi_revenue;
    String duedate;
    String satuan;

    int statusRevenue;
    int idKategori;
    int idSatuan;

//    String keterangan;
//    String koordinat

    public Aktifitas(){}
    public Aktifitas(int no, int id, String nama, String kategori, BigInteger target, BigInteger revenue, BigInteger realisasi, BigInteger realisasi_revenue, String duedate, String satuan ,double realisasi_persen, int statusRevenue, int idKategori,int idSatuan) {
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
        this.realisasi_persen = realisasi_persen;
        this.statusRevenue=statusRevenue;
        this.idKategori=idKategori;
        this.idSatuan=idSatuan;
    }

    public int getIdKategori() {
        return idKategori;
    }

    public int getIdSatuan() {
        return idSatuan;
    }

    public int getStatusRevenue() {
        return statusRevenue;
    }

    public void setIdKategori(int idKategori) {
        this.idKategori = idKategori;
    }

    public void setIdSatuan(int idSatuan) {
        this.idSatuan = idSatuan;
    }

    public void setStatusRevenue(int statusRevenue) {
        this.statusRevenue = statusRevenue;
    }

    public double getRealisasi_persen() {
        return realisasi_persen;
    }

    public void setRealisasi_persen(double realisasi_persen) {
        this.realisasi_persen = realisasi_persen;
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

    public BigInteger getTarget() {
        return target;
    }

    public void setTarget(BigInteger target) {
        this.target = target;
    }

    public BigInteger getTarget_revenue() {
        return target_revenue;
    }

    public void setTarget_revenue(BigInteger target_revenue) {
        this.target_revenue = target_revenue;
    }

    public BigInteger getRealisasi() {
        return realisasi;
    }

    public void setRealisasi(BigInteger realisasi) {
        this.realisasi = realisasi;
    }

    public BigInteger getRealisasi_revenue() {
        return realisasi_revenue;
    }

    public void setRealisasi_revenue(BigInteger realisasi_revenue) {
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
