package com.example.musicplayer.Secondaries;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Size;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Extractor {
    private Context context;

    public Extractor(){
        super();
    }

    public Extractor(Context context) {
        this.context = context;
    }

    public ArrayList<Bitmap> catchAlbumArt(ArrayList<Music> MusicsList, boolean ShowArtistCount){
        List<String> artistsNames = new ArrayList<>();
        ArrayList<Bitmap> albuns = new ArrayList<>();
        for (Music music : MusicsList){
            if (!artistsNames.contains(music.getArtista())){
                artistsNames.add(music.getArtista());
                albuns.add(music.getBitmap());
            }
        }
        if (ShowArtistCount)
            Toast.makeText(context, "Numero de artistas: < "+artistsNames.size()+" >", Toast.LENGTH_SHORT).show();
        return albuns;
    }

    @SuppressLint("Range")
    public ArrayList<Music> getAudios(boolean ShowInfos) {
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!=0 AND "+ MediaStore.Audio.Media.DURATION +">=?";
        String[] selectionArgs = {"80000"};
        ArrayList<Music> arrayList = new ArrayList<>();
        Bitmap bm;
        @SuppressLint("Recycle") Cursor cursor = context
                .getContentResolver().
                query(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        null,
                        selection,
                        selectionArgs,
                        sortOrder
                );
        int index =0;
        if (cursor!=null && cursor.getCount()>0) {
            while (cursor.moveToNext()){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

                    Uri albumArt = ContentUris.withAppendedId(
                            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                            cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Albums._ID))
                    );
                    if (albumArt!=null){
                        try {
                            bm =context.getContentResolver().
                                    loadThumbnail(albumArt, new Size(500,500),null);
                        } catch (IOException e) {
                            bm = null;
                            Log.e("ERRO NA THUMB", "ERRO em:"+ index);
                        }
                    } else{
                        Toast.makeText(context, "na", Toast.LENGTH_SHORT).show();
                        bm =null;
                    }

                    arrayList.add(new Music(
                            cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)),
                            cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)),
                            cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)),
                            cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)),
                            bm,
                            cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION))
                    ));
                }
                index++;
            }

        }
        if (arrayList.isEmpty()) Toast.makeText(context, "Nenhuma música encontrada", Toast.LENGTH_SHORT).show();
        if (ShowInfos) Toast.makeText(context, "Músicas encontradas: < "+arrayList.size()+" >", Toast.LENGTH_SHORT).show();
        return (!arrayList.isEmpty()) ? arrayList: new ArrayList<>();
    }
}
