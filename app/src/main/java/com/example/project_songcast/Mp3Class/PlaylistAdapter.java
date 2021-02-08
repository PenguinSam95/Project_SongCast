package com.example.project_songcast.Mp3Class;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_songcast.AudioActivity;
import com.example.project_songcast.ListenerInterface.OnDialogSelectListener;
import com.example.project_songcast.ListenerInterface.OnSelectedMp3ChangeListener;
import com.example.project_songcast.MainActivity;
import com.example.project_songcast.Playlist_Inner_Activity;
import com.example.project_songcast.R;

import java.util.ArrayList;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.ViewHolder> {
    private ArrayList<Playlist> customPlaylist = new ArrayList<>();
    private ArrayList<Mp3Data> itemList = new ArrayList<>();
    public final static int PLAYLIST_NOW = 100;
    public final static int PLAYLIST_INNER = 110;
    public final static int PLAYLIST_CUSTOM = 120;
    public final static int PLAYLIST_EDIT = 130;
    public static int listNum;
    private int id;
    private boolean insertMode;
    private OnDialogSelectListener listener;
    private OnSelectedMp3ChangeListener mp3Listener;
    @NonNull
    @Override
    public PlaylistAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch(listNum) {
            case PlaylistAdapter.PLAYLIST_NOW :         // 현재 플레이리스트
                id = R.layout.playlist_now_item;
                break;
            case PlaylistAdapter.PLAYLIST_INNER :       // 커스텀 플레이리스트 내부
                id = R.layout.library_all_item;
                break;
            case PlaylistAdapter.PLAYLIST_CUSTOM :      // 플레이리스트 리스트
                id = R.layout.playlist_custom_item;
                break;
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(id, parent, false);
        return new PlaylistAdapter.ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull PlaylistAdapter.ViewHolder holder, int position) {
        if (listNum == PLAYLIST_CUSTOM) {
            holder.setItem(customPlaylist.get(position));
            holder.setInsertMode(insertMode);
        } else {
            holder.setItem(itemList.get(position));
        }
        holder.setOnDialogSelectListener(listener);
        holder.setOnSelectedMp3ChangeListener(mp3Listener);
    }
    @Override
    public int getItemCount() {
        if(listNum == PlaylistAdapter.PLAYLIST_CUSTOM) {
            return customPlaylist.size();
        }
        return itemList.size();
    }
    public void addItem(Mp3Data item) {
        itemList.add(item);
    }
    public void addItem(Playlist item) {
        customPlaylist.add(item);
    }
    public void removeItem(Playlist item) {
        customPlaylist.remove(item);
    }
    public void setListNum(int listNum) {
        this.listNum = listNum;
    }
    public ArrayList<Mp3Data> getPlaylist() {
        return itemList;
    }
    public void setOnDialogSelectListener(OnDialogSelectListener listener) {
        this.listener = listener;
    }
    public void setOnSelectedMp3ChangeListener(OnSelectedMp3ChangeListener mp3Listener) {
        this.mp3Listener = mp3Listener;
    }
    public void setInsertMode(boolean insertMode) {
        this.insertMode = insertMode;
    }
    static class ViewHolder extends RecyclerView.ViewHolder {
        private OnDialogSelectListener listener;
        private OnSelectedMp3ChangeListener mp3Listener;
        private TextView tv1, tv2;
        private ImageView iv1;
        private ImageButton ib1;
        private CardView card;
        private boolean insertMode;
        private Mp3DataFormat format = new Mp3DataFormat();
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            switch(listNum) {
                case PlaylistAdapter.PLAYLIST_NOW :         // 현재 플레이리스트
                    tv1 = itemView.findViewById(R.id.playlist_now_tv1);
                    tv2 = itemView.findViewById(R.id.playlist_now_tv2);
                    iv1 = itemView.findViewById(R.id.playlist_now_iv);
                    card = itemView.findViewById(R.id.playlist_now_card);
                    break;
                case PlaylistAdapter.PLAYLIST_INNER :      // 커스텀 플레이리스트 내부
                    iv1 = itemView.findViewById(R.id.library_all_iv);
                    tv1 = itemView.findViewById(R.id.library_all_tv1);
                    tv2 = itemView.findViewById(R.id.library_all_tv2);
                    ib1 = itemView.findViewById(R.id.library_all_ib);
                    card = itemView.findViewById(R.id.library_all_card);
                    break;
                case PlaylistAdapter.PLAYLIST_CUSTOM :      // 플레이리스트 리스트
                    tv1 = itemView.findViewById(R.id.playlist_custom_tv1);
                    tv2 = itemView.findViewById(R.id.playlist_custom_tv2);
                    ib1 = itemView.findViewById(R.id.playlist_custom_ib);
                    card = itemView.findViewById(R.id.playlist_custom_card);
                    break;
            }
        }
        public void setItem(Mp3Data item) {
            switch(listNum) {
                case PlaylistAdapter.PLAYLIST_NOW :         // 현재 플레이리스트
                    format.setTextViewsText(tv1, item.getTitle(), false);
                    format.setTextViewsText(tv2, item.getArtist(), false);
                    format.setAlbumImage(iv1, item.getAlbum_art_image());
                    card.setOnClickListener(v -> {
                        MainActivity.setPlaylist(itemView.getContext(), MainActivity.nowPlayList, item, false);
                        mp3Listener.onSelectedMp3Change();
                    });
                    break;
                case PlaylistAdapter.PLAYLIST_INNER :      // 커스텀 플레이리스트 내부
                    format.setTextViewsText(tv1, item.getTitle(), false);
                    format.setTextViewsText(tv2, item.getArtist(), false);
                    format.setAlbumImage(iv1, item.getAlbum_art_image());
                    ib1.setOnClickListener(v -> {
                        MainActivity.mp3Dialog.showDialog(itemView.getContext(), MainActivity.LIBRARY_ALL, item);
                    });
                    card.setOnClickListener(v -> {
                        MainActivity.setPlaylist(itemView.getContext(), MainActivity.mp3PlayList, item, true);
                    });
                    break;
            }
        }
        public void setItem(Playlist item) {                // 플레이리스트 리스트
            format.setTextViewsText(tv1, item.getName(), false);
            format.setTextViewsText(tv2, item.getPlaylist_data().size(), true);
            card.setOnClickListener(v -> {
                if(insertMode) {
                    MainActivity.insert.setPlaylistData(item);
                    MainActivity.insert.finish();
                } else {
                    listener.setPlaylist(item);                     // in PlaylistFragment method
                    MainActivity.playlist = item;
                    Intent intent = new Intent(itemView.getContext(), Playlist_Inner_Activity.class);
                    itemView.getContext().startActivity(intent);
                }
            });
            ib1.setOnClickListener(v -> {
                MainActivity.mp3Dialog.showDialog(itemView.getContext(), item);
            });
        }
        public void setInsertMode(boolean insertMode) {
            this.insertMode = insertMode;
        }
        public void setOnDialogSelectListener(OnDialogSelectListener listener) {
            this.listener = listener;
        }
        public void setOnSelectedMp3ChangeListener(OnSelectedMp3ChangeListener mp3Listener) {
            this.mp3Listener = mp3Listener;
        }
    }
}



















