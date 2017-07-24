package com.simonag.simonag.model;
import org.parceler.Parcel;

import java.math.BigInteger;

/**
 * Created by Zaki on 13/06/2017.
 */
@Parcel
public class AktifitasKomisaris{
	int no;
	private int idKategori;
	private String jenisMedia;
	private String namaAktivitas;
	private String akhirPelaksanaan;
	private String capture;
	private int statusKategori;
	private String awalPelaksanaan;
	private BigInteger nilaiRupiah;
	private String url;
	private int idAktivitas;
	private String keterangan;
	private String nama_kategori;

    public AktifitasKomisaris(){}

	public AktifitasKomisaris(int no,int idKategori,
			String jenisMedia,
			String namaAktivitas,
			String akhirPelaksanaan,
			String capture,
			int statusKategori,
			String awalPelaksanaan, BigInteger nilaiRupiah,
			String url,
			int idAktivitas,
							  String keterangan, String nama_kategori){
		this.no = no;
		this.idKategori=idKategori;
		this.jenisMedia=jenisMedia;
		this.namaAktivitas=namaAktivitas;
		this.akhirPelaksanaan=akhirPelaksanaan;
		this.capture=capture;
		this.statusKategori=statusKategori;
		this.awalPelaksanaan=awalPelaksanaan;
		this.nilaiRupiah=nilaiRupiah;
		this.url=url;
		this.idAktivitas=idAktivitas;
		this.keterangan=keterangan;
		this.nama_kategori = nama_kategori;
	}

    public String getNama_kategori() {
        return nama_kategori;
    }

    public void setNama_kategori(String nama_kategori) {
        this.nama_kategori = nama_kategori;
    }

    public String getKeterangan() {
		return keterangan;
	}

	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public void setIdKategori(int idKategori){
		this.idKategori = idKategori;
	}

	public int getIdKategori(){
		return idKategori;
	}

	public void setJenisMedia(String jenisMedia){
		this.jenisMedia = jenisMedia;
	}

	public String getJenisMedia(){
		return jenisMedia;
	}

	public void setNamaAktivitas(String namaAktivitas){
		this.namaAktivitas = namaAktivitas;
	}

	public String getNamaAktivitas(){
		return namaAktivitas;
	}

	public void setAkhirPelaksanaan(String akhirPelaksanaan){
		this.akhirPelaksanaan = akhirPelaksanaan;
	}

	public String getAkhirPelaksanaan(){
		return akhirPelaksanaan;
	}

	public void setCapture(String capture){
		this.capture = capture;
	}

	public String getCapture(){
		return capture;
	}

	public void setStatusKategori(int statusKategori){
		this.statusKategori = statusKategori;
	}

	public int getStatusKategori(){
		return statusKategori;
	}

	public void setAwalPelaksanaan(String awalPelaksanaan){
		this.awalPelaksanaan = awalPelaksanaan;
	}

	public String getAwalPelaksanaan(){
		return awalPelaksanaan;
	}

	public void setNilaiRupiah(BigInteger nilaiRupiah){
		this.nilaiRupiah = nilaiRupiah;
	}

	public BigInteger getNilaiRupiah(){
		return nilaiRupiah;
	}

	public void setUrl(String url){
		this.url = url;
	}

	public String getUrl(){
		return url;
	}

	public void setIdAktivitas(int idAktivitas){
		this.idAktivitas = idAktivitas;
	}

	public int getIdAktivitas(){
		return idAktivitas;
	}

}
