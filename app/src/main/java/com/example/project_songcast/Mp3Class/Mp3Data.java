package com.example.project_songcast.Mp3Class;

import android.graphics.Bitmap;
import android.net.Uri;

import androidx.annotation.NonNull;

public class Mp3Data {
    private String id;
    private String path;
    private String name;
    private String folder_name;
    private String album;
    private String album_id;
    private String album_key;
    private String artist;
    private String artist_id;
    private String artist_key;
    private String data;
    private String date;
    private String duration;
    private String title;
    private String size;
    private String album_artist_id;
    private Bitmap album_art_image;
    private Uri uri;
    private int artistCount;
    private int folderCount;
    public Mp3Data() { };
    @NonNull
    @Override
    public String toString() {
        return "Mp3Data{id='" +id+ '\''+", album id='" +album_id+ '\'' +", title='" +title+ '\'' +", artist='" +artist+ '\''+'}';
    }
    public String getId() {
        return id;
    }
    public Mp3Data setId(String id) {
        this.id = id;
        return this;
    }
    public String getPath() {
        return path;
    }
    public Mp3Data setPath(String path) {
        this.path = path;
        return this;
    }
    public String getName() {
        return name;
    }
    public Mp3Data setName(String name) {
        this.name = name;
        return this;
    }
    public String getFolder_name() {
        return folder_name;
    }
    public Mp3Data setFolder_name(String folder_name) {
        this.folder_name = folder_name;
        return this;
    }
    public String getAlbum() {
        return album;
    }
    public Mp3Data setAlbum(String album) {
        this.album = album;
        return this;
    }
    public String getAlbum_id() {
        return album_id;
    }
    public Mp3Data setAlbum_id(String album_id) {
        this.album_id = album_id;
        return this;
    }
    public String getAlbum_key() {
        return album_key;
    }
    public Mp3Data setAlbum_key(String album_key) {
        this.album_key = album_key;
        return this;
    }
    public String getArtist() {
        return artist;
    }
    public Mp3Data setArtist(String artist) {
        this.artist = artist;
        return this;
    }
    public String getArtist_id() {
        return artist_id;
    }
    public Mp3Data setArtist_id(String artist_id) {
        this.artist_id = artist_id;
        return this;
    }
    public String getArtist_key() {
        return artist_key;
    }
    public Mp3Data setArtist_key(String artist_key) {
        this.artist_key = artist_key;
        return this;
    }
    public String getData() {
        return data;
    }
    public Mp3Data setData(String data) {
        this.data = data;
        return this;
    }
    public String getDate() {
        return date;
    }
    public Mp3Data setDate(String date) {
        this.date = date;
        return this;
    }
    public String getDuration() {
        return duration;
    }
    public Mp3Data setDuration(String duration) {
        this.duration = duration;
        return this;
    }
    public String getTitle() {
        return title;
    }
    public Mp3Data setTitle(String title) {
        this.title = title;
        return this;
    }
    public String getSize() {
        return size;
    }
    public Mp3Data setSize(String size) {
        this.size = size;
        return this;
    }
    public int getArtistCount() {
        return artistCount;
    }
    public Mp3Data setArtistCount(int artistCount) {
        this.artistCount = artistCount;
        return this;
    }
    public String getAlbum_artist_id() {
        return album_artist_id;
    }
    public Mp3Data setAlbum_artist_id(String album_artist_id) {
        this.album_artist_id = album_artist_id;
        return this;
    }
    public Bitmap getAlbum_art_image() {
        return album_art_image;
    }
    public Mp3Data setAlbum_art_image(Bitmap album_art_image) {
        this.album_art_image = album_art_image;
        return this;
    }
    public int getFolderCount() {
        return folderCount;
    }
    public Mp3Data setFolderCount(int folderCount) {
        this.folderCount = folderCount;
        return this;
    }
    public Uri getUri() {
        return uri;
    }
    public Mp3Data setUri(Uri uri) {
        this.uri = uri;
        return this;
    }
}















