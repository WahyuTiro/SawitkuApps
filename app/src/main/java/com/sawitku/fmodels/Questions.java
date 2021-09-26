package com.sawitku.fmodels;

/**
 * Created by vamsi on 18-Jul-16.
 */

public class Questions {

    public long id;
    public String idUser;
    public String nama;
    public String question;

    public Questions(){

    }
    public Questions(long id, String nama, String idUser, String question){
        this.id = id;
        this.nama = nama;
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



  
}
