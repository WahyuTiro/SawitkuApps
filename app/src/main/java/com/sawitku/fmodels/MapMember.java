package com.sawitku.fmodels;

/**
 * Created by vamsi on 18-Jul-16.
 */

public class MapMember {

    public String id;
    public String nama;
    public String tipe;
    public String gambar;
    public String provinsi;
    public String kecamatan;


    public MapMember(){

    }
    public MapMember(String id, String nama, String tipe, String gambar, String provinsi, String kecamatan){
        this.id = id;
        this.nama = nama;
        this.tipe = tipe;
        this.gambar = gambar;
        this.provinsi = provinsi;
        this.kecamatan = kecamatan;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }


    public String getTipe() {
        return tipe;
    }

    public void setTipe(String tipe) {
        this.tipe = tipe;
    }


    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public String getProvinsi() {
        return provinsi;
    }

    public void setProvinsi(String provinsi) {
        this.provinsi = provinsi;
    }


    public String getKecamatan() {
        return kecamatan;
    }

    public void setKecamatan(String kecamatan) {
        this.kecamatan = kecamatan;
    }







  
}
