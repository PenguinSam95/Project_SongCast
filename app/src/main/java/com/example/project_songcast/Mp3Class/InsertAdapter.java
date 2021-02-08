package com.example.project_songcast.Mp3Class;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_songcast.ListenerInterface.OnCountChangeListener;
import com.example.project_songcast.MainActivity;
import com.example.project_songcast.R;

import java.util.ArrayList;
import java.util.HashMap;

public class InsertAdapter extends RecyclerView.Adapter<InsertAdapter.ViewHolder> {
    private ArrayList<Mp3Data> itemList = new ArrayList<>();
    public static HashMap<String, Boolean> instanceMap;
    private int id;
    public static final int selected_color = Color.parseColor("#d6c0fc");
    private OnCountChangeListener listener;
    @NonNull
    @Override
    public InsertAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch(PlaylistInsert.startPosition) {
            case PlaylistInsert.START_ARTIST :                      // 아티스트
                id = R.layout.playlist_insert_artist_item;
                break;
            case PlaylistInsert.START_ALBUM :                       // 앨범
                id = R.layout.playlist_insert_album_item;
                break;
            case PlaylistInsert.START_FOLDER :                      // 폴더
                id = R.layout.playlist_insert_folder_item;
                break;
            default :                                               // 그 외
                id = R.layout.playlist_insert_all_item;
                break;
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(id, parent, false);
        return new InsertAdapter.ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull InsertAdapter.ViewHolder holder, int position) {
        holder.setItem(itemList.get(position));
        holder.setListener(listener);
    }
    @Override
    public int getItemCount() {
        return itemList.size();
    }
    public void addItem(Mp3Data item) {
        itemList.add(item);
    }
    public void setOnCountChangeListener(OnCountChangeListener listener) {
        this.listener = listener;
    }
    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv1, tv2;
        private ImageView iv1;
        private CardView card;
        private OnCountChangeListener listener;
        private Mp3DataFormat format = new Mp3DataFormat();
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            switch(PlaylistInsert.startPosition) {
                case PlaylistInsert.START_ARTIST :                      // 아티스트
                    tv1 = itemView.findViewById(R.id.playlist_insert_artist_tv1);
                    tv2 = itemView.findViewById(R.id.playlist_insert_artist_tv2);
                    card = itemView.findViewById(R.id.playlist_insert_artist_card);
                    break;
                case PlaylistInsert.START_ALBUM :                       // 앨범
                    tv1 = itemView.findViewById(R.id.playlist_insert_album_tv1);
                    tv2 = itemView.findViewById(R.id.playlist_insert_album_tv2);
                    iv1 = itemView.findViewById(R.id.playlist_insert_album_iv);
                    card = itemView.findViewById(R.id.playlist_insert_album_card);
                    break;
                case PlaylistInsert.START_FOLDER :                      // 폴더
                    tv1 = itemView.findViewById(R.id.playlist_insert_folder_tv1);
                    tv2 = itemView.findViewById(R.id.playlist_insert_folder_tv2);
                    card = itemView.findViewById(R.id.playlist_insert_folder_card);
                    break;
                default :                                               // 그 외
                    tv1 = itemView.findViewById(R.id.playlist_insert_all_tv1);
                    tv2 = itemView.findViewById(R.id.playlist_insert_all_tv2);
                    iv1 = itemView.findViewById(R.id.playlist_insert_all_iv);
                    card = itemView.findViewById(R.id.playlist_insert_all_card);
                    break;
            }
        }
        public int count;
        public String ids = null;
        public void setItem(Mp3Data item) {
            ids = null;
            switch(PlaylistInsert.startPosition) {
                case PlaylistInsert.START_ARTIST :                      // 아티스트
                    ids = item.getArtist()+"%"+item.getArtist_id();
                    format.setTextViewsText(tv1, item.getArtist(), false);
                    format.setTextViewsText(tv2, item.getArtistCount(), true);
                    break;
                case PlaylistInsert.START_ALBUM :                       // 앨범
                    ids = item.getAlbum()+"%"+item.getArtist();
                    format.setTextViewsText(tv1, item.getAlbum(), false);
                    format.setTextViewsText(tv2, item.getArtist(), false);
                    format.setAlbumImage(iv1, item.getAlbum_art_image());
                    break;
                case PlaylistInsert.START_FOLDER :                      // 폴더
                    ids = item.getPath()+"%"+item.getFolder_name();
                    format.setTextViewsText(tv1, item.getFolder_name(), false);
                    format.setTextViewsText(tv2, item.getFolderCount(), true);
                    break;
                default :                                               // 그 외
                    ids = item.getName()+"%"+item.getPath();
                    format.setTextViewsText(tv1, item.getTitle(), false);
                    format.setTextViewsText(tv2, item.getArtist(), false);
                    format.setAlbumImage(iv1, item.getAlbum_art_image());
                    break;
            }
            // ------------------------------------------------------------------------------------------- 공통 세팅
            if(instanceMap.get(ids)) {
                card.setBackgroundColor(selected_color);
            } else {
                card.setBackgroundColor(Color.WHITE);
            }
            card.setOnClickListener(v -> {
                count = getCount(instanceMap);
                if(!instanceMap.get(ids)) {
                    count++;
                    card.setBackgroundColor(selected_color);
                    instanceMap.put(ids, true);
                } else {
                    count--;
                    card.setBackgroundColor(Color.WHITE);
                    instanceMap.put(ids, false);
                }
                listener.onCountChanged(count);
            });
            // ------------------------------------------------------------------------------------------- 공통 세팅 ed
        }
        private int getCount(HashMap<String, Boolean> map) {
            int cnt = 0;
            for(boolean counter : map.values()) {
                if(counter) {
                    cnt++;
                }
            }
            return cnt;
        }
        public void setListener(OnCountChangeListener listener) {
            this.listener = listener;
        }
    }
}



















