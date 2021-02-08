package com.example.project_songcast.Mp3Class;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import static com.example.project_songcast.MainActivity.ALBUM_ART_URI;
import static com.example.project_songcast.MainActivity.mp3DataList;

public class Mp3Query {
    private Context mContext;
    public Mp3Query(Context context) {
        mContext = context;
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- MP3 저장
    public ArrayList<Mp3Data> getMusicList() {
        String path = null;
        String data = null;
        String folder = null;
        String[] spliter = null;
        String id = null;
        ArrayList<Mp3Data> mp3DataList = new ArrayList<Mp3Data>();
        String mp3_columns[] = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.ALBUM_KEY,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ARTIST_ID,
                MediaStore.Audio.Media.ARTIST_KEY,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.SIZE,
                MediaStore.Audio.Media.DATE_MODIFIED };
        String asc = MediaStore.Audio.Media.TITLE+" ASC";
        Cursor cursor = mContext.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, mp3_columns, null, null, asc);
        while(cursor.moveToNext()) {
            path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
            data = path;
            spliter = path.split("/");
            folder = spliter[spliter.length-2];
            path = "";
            for(int i=1; i<spliter.length-1; i++) {
                path += "/"+spliter[i];
            }
            id = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
            mp3DataList.add(new Mp3Data()
                    .setId(id)
                    .setPath(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)))
                    .setAlbum(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)))
                    .setAlbum_id(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)))
                    .setAlbum_key(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_KEY)))
                    .setArtist(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)))
                    .setArtist_id(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST_ID)))
                    .setArtist_key(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST_KEY)))
                    .setDuration(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)))
                    .setData(data)
                    .setDate(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATE_MODIFIED)))
                    .setTitle(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)))
                    .setSize(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE)))
                    .setAlbum_art_image(getBitmapImage(Long.parseLong(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID))) , 512, 512))
                    .setPath(path)
                    .setName(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME)))
                    .setUri(ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, Long.parseLong(id)))
                    .setFolder_name(folder) );
        }
        cursor.close();
        return mp3DataList;
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- MP3 저장 ed
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 아티스트 저장
    public HashMap<String, Mp3Data> getArtistList() {
        HashMap<String, Mp3Data> artistList = new LinkedHashMap<>();
        String id = null;
        String artist = null;
        String artist_id = null;
        String mp3_columns[] = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ARTIST_ID,
                MediaStore.Audio.Media.ARTIST_KEY};
        Cursor cursor = mContext.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, mp3_columns, null, null, MediaStore.Audio.Media.ARTIST_KEY+" ASC");
        while(cursor.moveToNext()) {
            artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
            artist_id = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST_ID));
            id = artist+"%"+artist_id;
            if(artistList.containsKey(id)) {
                artistList.put(id, artistList.get(id).setArtistCount(artistList.get(id).getArtistCount()+1));
            } else {
                artistList.put(id, new Mp3Data()
                        .setArtist(artist)
                        .setArtist_id(artist_id)
                        .setArtist_key(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST_KEY)))
                        .setArtistCount(1) );
            }
        }
        cursor.close();
        return artistList;
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 아티스트 저장 ed
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 앨범 저장 ed
    public HashMap<String, Mp3Data> getAlbumList() {
        HashMap<String, Mp3Data> albumList = new LinkedHashMap<>();
        String album = null;
        String artist = null;
        String id = null;
        String mp3_columns[] = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ARTIST_ID,
                MediaStore.Audio.Media.ARTIST_KEY,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.ALBUM_KEY};
        Cursor cursor = mContext.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, mp3_columns, null, null, MediaStore.Audio.Media.ALBUM+" ASC");
        while(cursor.moveToNext()) {
            album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
            artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
            id = album+"%"+artist;
            if(!albumList.containsKey(id)) {
                albumList.put(id, new Mp3Data()
                        .setArtist(artist)
                        .setArtist_id(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST_ID)))
                        .setArtist_key(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST_KEY)))
                        .setAlbum(album)
                        .setAlbum_id(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)))
                        .setAlbum_key(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_KEY)))
                        .setAlbum_artist_id(id)
                        .setAlbum_art_image(getBitmapImage(Long.parseLong(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID))) , 512, 512)) );
            }
        }
        cursor.close();
        return albumList;
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 앨범 저장 ed
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 폴더 경로 저장 ed
    public HashMap<String, Mp3Data> getFolderList() {
        HashMap<String, Mp3Data> folderList = new LinkedHashMap<>();
        for(Mp3Data mp3Data : mp3DataList) {
            if(folderList.containsKey(mp3Data.getPath())) {
                folderList.put(mp3Data.getPath(), folderList.get(mp3Data.getPath()).setFolderCount(folderList.get(mp3Data.getPath()).getFolderCount()+1));
            } else {
                folderList.put(mp3Data.getPath(), mp3Data.setFolderCount(1));
            }
        }
        folderList = orderByFolderName(folderList);
        return folderList;
    }
    private HashMap<String, Mp3Data> orderByFolderName(HashMap<String, Mp3Data> folderList) {
        List<String> mapKeys = new ArrayList<>(folderList.keySet());
        List<Mp3Data> mapValues = new ArrayList<>(folderList.values());
        Collections.sort(mapValues, new Comparator<Mp3Data>() {
            @Override
            public int compare(Mp3Data m1, Mp3Data m2) {
                return m1.getFolder_name().compareToIgnoreCase(m2.getFolder_name());
            }
        });
        Collections.sort(mapKeys);
        LinkedHashMap<String, Mp3Data> sortedMap = new LinkedHashMap<>();
        Iterator<Mp3Data> valueIt = mapValues.iterator();
        while (valueIt.hasNext()) {
            Mp3Data val = valueIt.next();
            Iterator<String> keyIt = mapKeys.iterator();
            while (keyIt.hasNext()) {
                String key = keyIt.next();
                Mp3Data comp1 = folderList.get(key);
                Mp3Data comp2 = val;
                if (comp1.equals(comp2)) {
                    keyIt.remove();
                    sortedMap.put(key, val);
                    break;
                }
            }
        }
        return sortedMap;
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 폴더 경로 저장 ed
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 앨범아트 설정
    private static final BitmapFactory.Options sBitmapOptionsCache = new BitmapFactory.Options();
    public Bitmap getBitmapImage(Long id, int w, int h) {
        ContentResolver res = mContext.getContentResolver();
        Uri uri = ContentUris.withAppendedId(ALBUM_ART_URI, id);
        if (uri != null) {
            ParcelFileDescriptor fd = null;
            try {
                fd = res.openFileDescriptor(uri, "r");
                int sampleSize = 1;
                sBitmapOptionsCache.inJustDecodeBounds = true;
                BitmapFactory.decodeFileDescriptor(fd.getFileDescriptor(), null, sBitmapOptionsCache);
                int nextWidth = sBitmapOptionsCache.outWidth >> 1;
                int nextHeight = sBitmapOptionsCache.outHeight >> 1;
                while (nextWidth>w && nextHeight>h) {
                    sampleSize <<= 1;
                    nextWidth >>= 1;
                    nextHeight >>= 1;
                }
                sBitmapOptionsCache.inSampleSize = sampleSize;
                sBitmapOptionsCache.inJustDecodeBounds = false;
                Bitmap b = BitmapFactory.decodeFileDescriptor(
                        fd.getFileDescriptor(), null, sBitmapOptionsCache);
                if (b != null) {
                    if (sBitmapOptionsCache.outWidth != w || sBitmapOptionsCache.outHeight != h) {
                        Bitmap tmp = Bitmap.createScaledBitmap(b, w, h, true);
                        b.recycle();
                        b = tmp;
                    }
                }
                return b;
            } catch (FileNotFoundException e) { return null;
            } catch (Exception e) { return null;
            } finally {
                try {
                    if (fd != null)
                        fd.close();
                } catch (IOException e) {
                }
            }
        }
        return null;
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 앨범아트 설정 ed
}
















