package com.example.musicplayer.Secondaries;

import android.graphics.Bitmap;

public class Music {
    private String Title, Artista, Album, Data;
    private Bitmap bitmap;

    public Music() {
        super();
    }

    public Music(String title, String artista, String album, String data, Bitmap bitmap) {
        Title = title;
        Artista = artista;
        Album = album;
        Data = data;
        this.bitmap = bitmap;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getArtista() {
        return Artista;
    }

    public void setArtista(String artista) {
        Artista = artista;
    }

    public String getAlbum() {
        return Album;
    }

    public void setAlbum(String album) {
        Album = album;
    }

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
