package com.simonag.simonag.model;

/**
 * Created by diditsepiyanto on 6/13/17.
 */

public class Dashboard {
    int id;
    int id_bumn;
    String nama_bumn;
    double persentase_komersial;
    double persentase_kualitas;
    double persentase_kapasitas;
    String link_gambar;

    public Dashboard(int id, int id_bumn, String nama_bumn, double persentase_komersial, double persentase_kualitas, double persentase_kapasitas, String link_gambar) {
        this.id = id;
        this.id_bumn = id_bumn;
        this.nama_bumn = nama_bumn;
        this.persentase_komersial = persentase_komersial;
        this.persentase_kualitas = persentase_kualitas;
        this.persentase_kapasitas = persentase_kapasitas;
        this.link_gambar = link_gambar;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_bumn() {
        return id_bumn;
    }

    public void setId_bumn(int id_bumn) {
        this.id_bumn = id_bumn;
    }

    public String getNama_bumn() {
        return nama_bumn;
    }

    public void setNama_bumn(String nama_bumn) {
        this.nama_bumn = nama_bumn;
    }

    public double getPersentase_komersial() {
        return persentase_komersial;
    }

    public void setPersentase_komersial(double persentase_komersial) {
        this.persentase_komersial = persentase_komersial;
    }

    public double getPersentase_kualitas() {
        return persentase_kualitas;
    }

    public void setPersentase_kualitas(double persentase_kualitas) {
        this.persentase_kualitas = persentase_kualitas;
    }

    public double getPersentase_kapasitas() {
        return persentase_kapasitas;
    }

    public void setPersentase_kapasitas(double persentase_kapasitas) {
        this.persentase_kapasitas = persentase_kapasitas;
    }

    public String getLink_gambar() {
        return link_gambar;
    }

    public void setLink_gambar(String link_gambar) {
        this.link_gambar = link_gambar;
    }
}
