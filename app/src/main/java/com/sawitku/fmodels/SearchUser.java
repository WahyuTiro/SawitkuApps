package com.sawitku.fmodels;

/**
 * Created by vamsi on 18-Jul-16.
 */

public class SearchUser {

    public String id;
    public String nama;
    public String kabupaten;
    public String provinsi;
    public String foto;

    public SearchUser(){

    }
    public SearchUser(String id, String nama, String kabupaten, String provinsi, String foto){
        this.id = id;
        this.nama = nama;
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

    public String getKabupaten() {
        return kabupaten;
    }

    public void setKabupaten(String kabupaten) {
        this.kabupaten = kabupaten;
    }


    public String getProvinsi() {
        return provinsi;
    }

    public void setProvinsi(String provinsi) {
        this.provinsi = provinsi;
    }


    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }



  
}
