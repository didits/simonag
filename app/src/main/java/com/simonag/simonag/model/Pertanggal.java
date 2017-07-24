package com.simonag.simonag.model;

import java.math.BigInteger;

/**
 * Created by diditsepiyanto on 6/18/17.
 */

public class Pertanggal {
    int id;
    String tanggal;
    int total_aktivitas;
    BigInteger total_biaya;

    public Pertanggal(int id, String tanggal, int total_aktivitas, BigInteger total_biaya) {
        this.id = id;
        this.tanggal = tanggal;
        this.total_aktivitas = total_aktivitas;
        this.total_biaya = total_biaya;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public int getTotal_aktivitas() {
        return total_aktivitas;
    }

    public void setTotal_aktivitas(int total_aktivitas) {
        this.total_aktivitas = total_aktivitas;
    }

    public BigInteger getTotal_biaya() {
        return total_biaya;
    }

    public void setTotal_biaya(BigInteger total_biaya) {
        this.total_biaya = total_biaya;
    }
}
