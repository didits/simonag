package com.simonag.simonag.model;

/**
 * Created by diditsepiyanto on 6/14/17.
 */

public class Program {
    int no;
    int id_program;
    String nama_program;
    Double realisasi_persen;
    Double kualitas_persen;
    Double kuantitas_persen;
    Double komersial_persen;

    public Program(int no, int id_program, String nama_program) {
        this.no = no;
        this.id_program = id_program;
        this.nama_program = nama_program;
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
}
