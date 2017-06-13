package com.example.bianguojian.foe_ticket;

import android.graphics.Bitmap;

/**
 * Created by Administrator on 2017/6/12.
 */

public class Film {
    private Bitmap filmBitmap;
    private String filmName;
    private String filmIntroduce;
    private String filmActor;

    public Film(Bitmap filmBitmap, String filmName, String filmIntroduce, String filmActor) {
        this.filmBitmap = filmBitmap;
        this.filmName = filmName;
        this.filmIntroduce = filmIntroduce;
        this.filmActor = filmActor;
    }

    public Bitmap getFilmBitmap() {
        return filmBitmap;
    }

    public void setFilmBitmap(Bitmap filmBitmap) {
        this.filmBitmap = filmBitmap;
    }

    public String getFilmName() {
        return filmName;
    }

    public String getFilmIntroduce() {
        return filmIntroduce;
    }

    public String getFilmActor() {
        return filmActor;
    }
}
