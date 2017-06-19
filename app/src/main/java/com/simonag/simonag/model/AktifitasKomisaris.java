package com.simonag.simonag.model;

public class AktifitasKomisaris{
	int no;
	private int idKategori;
	private String jenisMedia;
	private String namaAktivitas;
	private String akhirPelaksanaan;
	private String capture;
	private int statusKategori;
	private String awalPelaksanaan;
	private int nilaiRupiah;
	private String url;
	private int idAktivitas;
	private String keterangan;

	public AktifitasKomisaris(int no,int idKategori,
			String jenisMedia,
			String namaAktivitas,
			String akhirPelaksanaan,
			String capture,
			int statusKategori,
			String awalPelaksanaan,
			int nilaiRupiah,
			String url,
			int idAktivitas,
							  String keterangan){
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

	public void setNilaiRupiah(int nilaiRupiah){
		this.nilaiRupiah = nilaiRupiah;
	}

	public int getNilaiRupiah(){
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
