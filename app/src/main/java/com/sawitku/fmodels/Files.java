package com.sawitku.fmodels;

/**
 * Created by vamsi on 18-Jul-16.
 */

public class Files {

    public long id;
    public String nama;
    public String file_url;
    public String caption;

    public Files(){

    }
    public Files(long id, String nama, String file_url, String caption){
        this.id = id;
        this.nama = nama;
        this.file_url = file_url;
        this.caption = caption;

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


    public String getFile_url() {
        return file_url;
    }

    public void setFile_url(String file_url) {
        this.file_url = file_url;
    }


    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }



}
