package com.example.project_songcast.Mp3Class;

import android.content.Context;
import android.content.Intent;

import com.example.project_songcast.ListenerInterface.OnPlaylistInsertListener;
import com.example.project_songcast.MainActivity;
import com.example.project_songcast.Playlist_Edit_Activity;
import com.example.project_songcast.Playlist_Insert_Audio_Activity;
import com.example.project_songcast.Playlist_Insert_List_Activity;

import java.util.ArrayList;
import java.util.List;

public class PlaylistInsert implements PlaylistInsertInterface {
    public static final int START_ALL = 10;
    public static final int START_ARTIST = 11;
    public static final int START_ALBUM = 12;
    public static final int START_FOLDER = 13;
    public static final int START_ARTIST_INNER = 21;
    public static final int START_ALBUM_INNER = 22;
    public static final int START_FOLDER_INNER = 23;
    public static int startPosition;
    private MainActivity mainActivity;
    private Context context;
    private ArrayList<Mp3Data> selected_item;
    private Playlist selected_playlist;
    private Intent item_intent;
    private Intent list_intent;
    private OnPlaylistInsertListener itemListener;
    private OnPlaylistInsertListener listListener;
    public PlaylistInsert(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        context = mainActivity.getApplicationContext();
    }
    @Override
    public void start(ArrayList<Mp3Data> items, Playlist playlist) {
        setPlaylistItem(items);
        setPlaylistData(playlist);
        setPlaylistItemListener(null);
        setPlaylistDataListener(null);
        if(selected_item != null && selected_playlist != null) {                    // 2 단계
            return;
        } else {
            if (selected_item == null) {                                            // 1 단계 - 오디오 선택
                selectPlaylistItem();
            } else if(selected_playlist == null) {                                  // 1 단계 - 플레이리스트 선택
                selectPlaylistData();
            }
        }
    }
    @Override
    public void selectPlaylistItem() {
        selected_item = new ArrayList<>();
        item_intent = new Intent(context, Playlist_Insert_Audio_Activity.class);
        item_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(item_intent);
    }
    @Override
    public void selectPlaylistData() {
        selected_playlist = new Playlist();
        list_intent = new Intent(context, Playlist_Insert_List_Activity.class);
        list_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(list_intent);
    }
    @Override
    public void startPlaylistInsert() {
        setPlaylistItem(null);
        setPlaylistData(null);
        setPlaylistItemListener(null);
        setPlaylistDataListener(null);
        selected_item = new ArrayList<>();
        item_intent = new Intent(context, Playlist_Edit_Activity.class);
        item_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(item_intent);
    }
    @Override
    public void setPlaylistItem(ArrayList<Mp3Data> items) {
        this.selected_item = items;
    }
    @Override
    public void setPlaylistData(Playlist playlist) {
        this.selected_playlist = playlist;
    }
    @Override
    public Playlist getPlaylistData() {
        return selected_playlist;
    }
    @Override
    public void setPlaylistItemListener(OnPlaylistInsertListener listener) {
        this.itemListener = listener;
    }
    @Override
    public void setPlaylistDataListener(OnPlaylistInsertListener listener) {
        this.listListener = listener;
    }
    @Override
    public void finish() {
        if(selected_item != null && selected_playlist != null) {
            List<String> listData = selected_playlist.getPlaylist_data();
            for(Mp3Data mp3Data : selected_item) {
                if(!listData.contains(mp3Data.getId())) {
                    listData.add(mp3Data.getId());
                }
            }
            selected_playlist.setPlaylist_data(listData);
        }
        if (itemListener != null) {
            itemListener.insertFinish();
        }
        if (listListener != null) {
            listListener.insertFinish();
        }
        startPosition = 0;
    }
}














