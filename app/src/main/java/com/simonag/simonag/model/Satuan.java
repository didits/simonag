package com.simonag.simonag.model;

/**
 * Created by Zaki on 17/06/2017.
 */

public class Satuan {
    int id_satuan;
    String nama_satuan;

    public int getId_satuan() {
        return id_satuan;
    }

    public String getNama_satuan() {
        return nama_satuan;
    }

    public void setId_satuan(int id_satuan) {
        this.id_satuan = id_satuan;
    }

    public void setNama_satuan(String nama_satuan) {
        this.nama_satuan = nama_satuan;
    }

    public Satuan(int id_satuan, String nama_satuan){
        this.id_satuan=id_satuan;
        this.nama_satuan=nama_satuan;
    }
}
