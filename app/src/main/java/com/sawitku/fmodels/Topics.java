package com.sawitku.fmodels;

/**
 * Created by vamsi on 18-Jul-16.
 */

public class Topics {

    public long id;
    public String nama;

    public Topics(){

    }
    public Topics(long id, String nama){
        this.id = id;
        this.nama = nama;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }



  
}
