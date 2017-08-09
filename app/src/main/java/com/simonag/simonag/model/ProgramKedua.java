package com.simonag.simonag.model;

/**
 * Created by diditsepiyanto on 8/6/17.
 */

public class ProgramKedua {
    int no;
    int id_program;
    String nama_program;
    double realisasi_target;
    String last_update;
    int total_aktivitas;
    String target;

    public ProgramKedua(int no, int id_program, String nama_program, double realisasi_target, String last_update, int total_aktivitas, String target) {
        this.no = no;
        this.id_program = id_program;
        this.nama_program = nama_program;
        this.realisasi_target = realisasi_target;
        this.last_update = last_update;
        this.total_aktivitas = total_aktivitas;
        this.target = target;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
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

    public double getRealisasi_target() {
        return realisasi_target;
    }

    public void setRealisasi_target(double realisasi_target) {
        this.realisasi_target = realisasi_target;
    }

    public String getLast_update() {
        return last_update;
    }

    public void setLast_update(String last_update) {
        this.last_update = last_update;
    }

    public int getTotal_aktivitas() {
        return total_aktivitas;
    }

    public void setTotal_aktivitas(int total_aktivitas) {
        this.total_aktivitas = total_aktivitas;
    }
}
