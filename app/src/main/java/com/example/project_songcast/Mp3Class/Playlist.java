package com.example.project_songcast.Mp3Class;

import java.util.List;

public class Playlist {
    private String name;
    private List<String> playlist_data;
    public List<String> getPlaylist_data() {
        return playlist_data;
    }
    public Playlist setPlaylist_data(List<String> playlist_data) {
        this.playlist_data = playlist_data;
        return this;
    }
    public String getName() {
        return name;
    }
    public Playlist setName(String name) {
        this.name = name;
        return this;
    }
}
