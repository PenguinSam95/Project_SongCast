package com.example.project_songcast.Mp3Class;

import com.example.project_songcast.ListenerInterface.OnPlaylistInsertListener;

import java.util.ArrayList;

public interface PlaylistInsertInterface {
    public void start(ArrayList<Mp3Data> items, Playlist playlist);
    public void selectPlaylistItem();
    public void selectPlaylistData();
    public void startPlaylistInsert();
    public void setPlaylistItem(ArrayList<Mp3Data> items);
    public void setPlaylistData(Playlist playlist);
    public Playlist getPlaylistData();
    public void setPlaylistItemListener(OnPlaylistInsertListener listener);
    public void setPlaylistDataListener(OnPlaylistInsertListener listener);
    public void finish();
}
