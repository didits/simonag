package com.simonag.simonag.Model;

/**
 * Created by diditsepiyanto on 6/13/17.
 */

public class Dashboard {
    int id;
    int id_bumn;
    String nama_bumn;
    float persentase_komersial;
    float persentase_kualitas;
    float persentase_kapasitas;
    String link_gambar;

    public Dashboard(int id, int id_bumn, String nama_bumn, float persentase_komersial, float persentase_kualitas, float persentase_kapasitas, String link_gambar) {
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

    public float getPersentase_komersial() {
        return persentase_komersial;
    }

    public void setPersentase_komersial(float persentase_komersial) {
        this.persentase_komersial = persentase_komersial;
    }

    public float getPersentase_kualitas() {
        return persentase_kualitas;
    }

    public void setPersentase_kualitas(float persentase_kualitas) {
        this.persentase_kualitas = persentase_kualitas;
    }

    public float getPersentase_kapasitas() {
        return persentase_kapasitas;
    }

    public void setPersentase_kapasitas(float persentase_kapasitas) {
        this.persentase_kapasitas = persentase_kapasitas;
    }

    public String getLink_gambar() {
        return link_gambar;
    }

    public void setLink_gambar(String link_gambar) {
        this.link_gambar = link_gambar;
    }
}
