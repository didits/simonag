package com.simonag.simonag.utils;

/**
 * Created by diditsepiyanto on 6/13/17.
 */

public class Config {
    public static final String URL ="http://server-laravel-project-fadholy.apps.playcourt.id/api/v1/";
    //public static final String URL = "http://simonag.owline.org/api/v1/";
    public static final String URL_GAMBAR = "http://server-laravel-project-fadholy.apps.playcourt.id/logo/";
    public static final String URL_CAPTURE = "http://simonag.owline.org/capture/";
    public static final String URL_LOGIN = URL + "login";
    public static final String URL_GET_ALL_PER = URL + "get/allPerusahaan/";
    public static final String URL_GET_ALL_KATEGORI = URL + "get/allKategori/";
    public static final String URL_GET_ALL_SATUAN = URL + "get/allSatuan/";
    public static final String URL_GET_DASHBOARD = URL + "get/Dashboard/";
    public static final String URL_GET_PROGRAM_PER = URL + "get/programPerusahaan/";
    public static final String URL_DELETE_PROGRAM_PER = URL + "delete/programPerusahaan/";
    public static final String URL_GET_TARGET_PROGRAM = URL + "get/targetProgram/";
    public static final String URL_GET_TOKEN = URL + "getToken";
    public static final String URL_POST_PROGRAM_PER = URL + "post/programPerusahaan/";
    public static final String URL_POST_TARGET_PROGRAM = URL + "post/targetProgram/";
    public static final String URL_POST_REALISASI_TARGET = URL + "post/realisasiTarget/";
    public static final String URL_EDIT_TARGET_PROGRAM = URL + "edit/targetProgram/";
    public static final String URL_EDIT_PROGRAM_PER = URL + "edit/programPerusahaan/";
    public static final String URL_DELETE_TARGET_PROGRAM = URL + "delete/targetProgram/";
    public static final String URL_DELETE_REAL_TARGET = URL + "delete/realisasiTarget/";
    public static final String URL_FILTER_1 = URL + "post/filterDashboard/";
    public static final String URL_GET_REALISASI = URL + "get/realisasiTarget/";

    public static final String URL_SEND_EMAIL_FORGOT_PASSWORD = URL + "sendEmailForgotPassword";
    public static final String URL_VERIFY_CODE_EMAIL = URL + "verifyCodeEmail";
    public static final String URL_CHANGE_PASSWORD = URL + "changePassword";

    //----------- USER KOMISARIS --------------//
    public static final String URL_2 ="http://server-laravel-project-fadholy.apps.playcourt.id/api/v2/";
    //public static final String URL_2 = "http://simonag.owline.org/api/v2/";
    public static final String URL_GET_DASHBOARD_2 = URL_2 + "get/Dashboard/";
    public static final String URL_GET_ALL_KATEGORI_2 = URL_2 + "get/allKategori/";
    public static final String URL_GET_TARGET_PROGRAM_2 = URL_2 + "get/aktivitasPerusahaan/";
    public static final String URL_POST_TARGET_PROGRAM_2 = URL_2 + "post/aktivitasPerusahaan/";
    public static final String URL_EDIT_TARGET_PROGRAM_2 = URL_2 + "edit/aktivitasPerusahaan/";
    public static final String URL_DELETE_TARGET_PROGRAM_2 = URL_2 + "delete/aktivitasPerusahaan/";
    public static final String URL_FILTER_2 = URL_2 + "post/filterDashboard/";

    public static final String FILTER_KOMISARIS = "filter";

    //----------- USER BUMN --------------//
    public static final String SHARED_USER = "user";
    public static final String ID_USER = "id_user";
    public static final String ID_TIPE = "id_tipe";
    public static final String FOTO = "foto";
    public static final String ID_BUMN = "id_bumn";
    public static final String ID_ROLE = "id_role";
    public static final String NAMA_BUMN = "nama_bumn";
    public static final String STATUS_BUMN = "status_daftar";
    public static final String TOKEN_BUMN = "token_bumn";
    public static final String EMAIL_BUMN = "email_bumn";
    public static final String PASSWORD_BUMN = "password_bumn";
    public static final String STATUS_LOGIN = "status_login";

}
