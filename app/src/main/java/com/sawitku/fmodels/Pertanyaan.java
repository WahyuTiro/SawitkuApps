package com.sawitku.fmodels;

/**
 * Created by vamsi on 18-Jul-16.
 */

public class Pertanyaan {

    public long id;
    public String idUser;
    public String nama;
    public String question;
    public String provinsi;
    public String profil_picture;

    public Pertanyaan(){

    }
    public Pertanyaan(long id, String nama, String idUser,   String question, String provinsi, String profil_picture){
        this.id = id;
        this.nama = nama;
        this.question = question;
        this.provinsi = provinsi;
        this.profil_picture = profil_picture;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }


    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }


    public String getProvinsi() {
        return provinsi;
    }

    public void setProvinsi(String provinsi) {
        this.provinsi = provinsi;
    }


    public String getProfil_picture() {
        return profil_picture;
    }

    public void setProfil_picture(String profil_picture) {
        this.profil_picture = profil_picture;
    }



  
}
