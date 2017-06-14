package com.simonag.simonag.model;

/**
 * Created by diditsepiyanto on 6/14/17.
 */

public class Program {
    int id_program;
    String nama_program;

    public Program(int id_program, String nama_program) {
        this.id_program = id_program;
        this.nama_program = nama_program;
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
