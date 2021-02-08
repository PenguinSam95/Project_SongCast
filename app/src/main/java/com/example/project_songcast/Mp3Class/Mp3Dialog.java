package com.example.project_songcast.Mp3Class;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.project_songcast.InformationActivity;
import com.example.project_songcast.MainActivity;
import com.example.project_songcast.ListenerInterface.OnDialogSelectListener;

import java.util.ArrayList;

public class Mp3Dialog {
    final public static String ITEM_PLAY = "플레이";
    final public static String ITEM_ADD_PLAYLIST = "플레이리스트에 추가";
    final public static String ITEM_INFO = "곡 정보";
    final public static String ITEM_PLAYLIST_PLAY = "플레이";
    final public static String ITEM_PLAYLIST_DELETE = "삭제";
    final public static String ITEM_IS_EMPTY = "플레이리스트가 비어있습니다";
    private final String[] single_menu = {ITEM_PLAY, ITEM_ADD_PLAYLIST, ITEM_INFO};
    private final String[] non_single_menu = {ITEM_PLAY, ITEM_ADD_PLAYLIST};
    private final String[] playlist = {ITEM_PLAYLIST_PLAY, ITEM_PLAYLIST_DELETE};
    private String[] items;
    private AlertDialog.Builder ad;
    private AlertDialog alert;
    private String title;
    private int select_library_inner = 0;
    private boolean single_check = false;
    public void showDialog(Context context, int select_library, Mp3Data item) {
        items = non_single_menu;
        MainActivity.instanceData = LibraryAdapter.setLibraryItems(select_library, item);
        single_check = false;
        switch(select_library) {
            case MainActivity.LIBRARY_ALL :             // all
                switch(select_library_inner) {
                    case MainActivity.LIBRARY_ARTIST :          // artist
                    case MainActivity.LIBRARY_ALBUM :           // album
                    case MainActivity.LIBRARY_FOLDER :          // folder
                        MainActivity.instanceData = LibraryAdapter.setLibraryItems(select_library_inner, item);
                        break;
                    default:
                        MainActivity.instanceData = MainActivity.mp3DataList;
                        break;
                }
                single_check = true;
                title = item.getTitle();
                items = single_menu;
                break;
            case MainActivity.LIBRARY_ARTIST :          // artist
                title = item.getArtist();
                break;
            case MainActivity.LIBRARY_ALBUM :           // album
                title = item.getAlbum();
                break;
            case MainActivity.LIBRARY_FOLDER :          // folder
                title = item.getFolder_name();
                break;
        }
        setSelect_library_inner(0);
        ad = new AlertDialog.Builder(context);
        ad.setTitle(title);
        ad.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(items[which].equals(ITEM_PLAY)) {                                    // 플레이
                    switch(select_library) {
                        case MainActivity.LIBRARY_ALL :             // all
                            MainActivity.setPlaylist(context, MainActivity.instanceData, item, true);
                            break;
                        case MainActivity.PLAYLIST_INNER :          // playlist
                            MainActivity.setPlaylist(context, MainActivity.mp3PlayList, item, true);
                            break;
                        default:
                            MainActivity.setPlaylist(context, MainActivity.instanceData, MainActivity.instanceData.get(0), true);
                            break;
                    }
                } else if(items[which].equals(ITEM_ADD_PLAYLIST)) {                     // 플레이리스트에 추가
                    if(single_check) {
                        MainActivity.instanceData = new ArrayList<>();
                        MainActivity.instanceData.add(item);
                    }
                    MainActivity.insert.start(MainActivity.instanceData, null);
                } else if(items[which].equals(ITEM_INFO)) {                             // 곡 정보
                    Intent info = new Intent(context, InformationActivity.class);
                    info.putExtra("item", item.getName()+"%"+item.getPath());
                    context.startActivity(info);
                }
            }
        });
        alert = ad.create();
        alert.show();
    }
    public Mp3Dialog setSelect_library_inner(int select_library_inner) {
        this.select_library_inner = select_library_inner;
        return this;
    }
    public void showDialog(Context context, Playlist item) {
        items = playlist;
        ad = new AlertDialog.Builder(context);
        ad.setTitle(item.getName());
        ad.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(items[which].equals(ITEM_PLAYLIST_PLAY)) {                           // 플레이
                    if(item.getPlaylist_data().size() > 0) {
                        listener.setPlaylist(item);
                        MainActivity.setPlaylist(context, MainActivity.mp3PlayList, MainActivity.mp3PlayList.get(0), true);
                    } else {
                        Toast.makeText(context, ITEM_IS_EMPTY, Toast.LENGTH_SHORT).show();
                    }
                } else if(items[which].equals(ITEM_PLAYLIST_DELETE)) {                  // 삭세
                    listener.remove(item);
                }
            }
        });
        alert = ad.create();
        alert.show();
    }
    private OnDialogSelectListener listener;
    public void setOnDialogSelectListener(OnDialogSelectListener listener) {
        this.listener = listener;
    }
}















