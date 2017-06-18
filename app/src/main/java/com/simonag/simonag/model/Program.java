package com.simonag.simonag.model;

import org.parceler.Parcel;

/**
 * Created by diditsepiyanto on 6/14/17.
 */
@Parcel
public class Program {
    int no;
    int id_program;
    String nama_program;
    double realisasi_persen;
    double kualitas_persen;
    double kuantitas_persen;
    double komersial_persen;

//    String keterangan;


    public void setRealisasi_persen(double realisasi_persen) {
        this.realisasi_persen = realisasi_persen;
    }

    public double getKualitas_persen() {
        return kualitas_persen;
    }

    public void setKualitas_persen(double kualitas_persen) {
        this.kualitas_persen = kualitas_persen;
    }

    public double getKuantitas_persen() {
        return kuantitas_persen;
    }

    public void setKuantitas_persen(double kuantitas_persen) {
        this.kuantitas_persen = kuantitas_persen;
    }

    public double getKomersial_persen() {
        return komersial_persen;
    }

    public void setKomersial_persen(double komersial_persen) {
        this.komersial_persen = komersial_persen;
    }

    public Program(){}
    public Program(int no, int id_program, String nama_program, Double realisasi_persen) {
        this.no = no;
        this.id_program = id_program;
        this.nama_program = nama_program;
        this.realisasi_persen = realisasi_persen;
    }

    public Program(int no, int id_program, String nama_program, double realisasi_persen,
                   double kualitas_persen, double kuantitas_persen, double komersial_persen) {
        this.no = no;
        this.id_program = id_program;
        this.nama_program = nama_program;
        this.realisasi_persen = realisasi_persen;
        this.kualitas_persen = kualitas_persen;
        this.kuantitas_persen = kuantitas_persen;
        this.komersial_persen = komersial_persen;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public int getId_program() {
        return id_program;
    }

    public void setId_program(int id_program) {
        this.id_program = id_program;
    }

    public String getNama_program() {
        return nama_program;
    }

    public void setNama_program(String nama_program) {
        this.nama_program = nama_program;
    }

    public double getRealisasi_persen() {
        return realisasi_persen;
    }

    public void setRealisasi_persen(Double realisasi_persen) {
        this.realisasi_persen = realisasi_persen;
    }
}
