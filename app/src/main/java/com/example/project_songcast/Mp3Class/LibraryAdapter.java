package com.example.project_songcast.Mp3Class;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_songcast.MainActivity;
import com.example.project_songcast.R;

import java.util.ArrayList;

public class LibraryAdapter extends RecyclerView.Adapter<LibraryAdapter.ViewHolder> {
    private ArrayList<Mp3Data> itemList = new ArrayList<>();
    private int id;
    @NonNull
    @Override
    public LibraryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch(MainActivity.select_library) {
            case MainActivity.LIBRARY_ARTIST :      // 아티스트
                id = R.layout.library_artist_inner_item;
                break;
            case MainActivity.LIBRARY_ALBUM :       // 앨범
                id = R.layout.library_album_inner_item;
                break;
            case MainActivity.LIBRARY_FOLDER :      // 폴더
                id = R.layout.library_all_item;
                break;
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(id, parent, false);
        return new LibraryAdapter.ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull LibraryAdapter.ViewHolder holder, int position) {
        holder.setItem(itemList.get(position));
    }
    @Override
    public int getItemCount() {
        return itemList.size();
    }
    public void addItem(Mp3Data item) {
        itemList.add(item);
    }
    public static ArrayList<Mp3Data> setLibraryItems(int library_select, Mp3Data item) {
        ArrayList<Mp3Data> libraryItem = new ArrayList<>();
        for(Mp3Data mp3Data : MainActivity.mp3DataList) {
            switch(library_select) {
                case MainActivity.LIBRARY_ARTIST :      // 아티스트
                    if(item.getArtist().equals(mp3Data.getArtist()) && item.getArtist_id().equals(mp3Data.getArtist_id())) {
                        libraryItem.add(mp3Data);
                    }
                    break;
                case MainActivity.LIBRARY_ALBUM :       // 앨범
                    if(item.getAlbum().equals(mp3Data.getAlbum()) && item.getArtist().equals(mp3Data.getArtist())) {
                        libraryItem.add(mp3Data);
                    }
                    break;
                case MainActivity.LIBRARY_FOLDER :      // 폴더
                    if(item.getPath().equals(mp3Data.getPath()) && item.getFolder_name().equals(mp3Data.getFolder_name())) {
                        libraryItem.add(mp3Data);
                    }
                    break;
            }
        }
        return libraryItem;
    }
    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv1, tv2;
        private ImageView iv1;
        private ImageButton ib1;
        private CardView card;
        private Mp3DataFormat format = new Mp3DataFormat();
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            switch(MainActivity.select_library) {
                case MainActivity.LIBRARY_ARTIST :      // 아티스트
                    tv1 = itemView.findViewById(R.id.library_artist_inner_tv1);
                    tv2 = itemView.findViewById(R.id.library_artist_inner_tv2);
                    iv1 = itemView.findViewById(R.id.library_artist_inner_iv);
                    ib1 = itemView.findViewById(R.id.library_artist_inner_ib);
                    card = itemView.findViewById(R.id.library_artist_inner_card);
                    break;
                case MainActivity.LIBRARY_ALBUM :       // 앨범
                    tv1 = itemView.findViewById(R.id.library_album_inner_tv1);
                    tv2 = itemView.findViewById(R.id.library_album_inner_tv2);
                    ib1 = itemView.findViewById(R.id.library_album_inner_ib);
                    card = itemView.findViewById(R.id.library_album_inner_card);
                    break;
                case MainActivity.LIBRARY_FOLDER :      // 폴더
                    tv1 = itemView.findViewById(R.id.library_all_tv1);
                    tv2 = itemView.findViewById(R.id.library_all_tv2);
                    iv1 = itemView.findViewById(R.id.library_all_iv);
                    ib1 = itemView.findViewById(R.id.library_all_ib);
                    card = itemView.findViewById(R.id.library_all_card);
                    break;
            }
        }
        public void setItem(Mp3Data item) {
            switch(MainActivity.select_library) {
                case MainActivity.LIBRARY_ARTIST :      // 아티스트
                    format.setTextViewsText(tv1, item.getTitle(), false);
                    format.setTextViewsText(tv2, format.getAudioTime(item.getDuration()), false);
                    format.setAlbumImage(iv1, item.getAlbum_art_image());
                    break;
                case MainActivity.LIBRARY_ALBUM :       // 앨범
                    format.setTextViewsText(tv1, item.getTitle(), false);
                    format.setTextViewsText(tv2, format.getAudioTime(item.getDuration()), false);
                    break;
                case MainActivity.LIBRARY_FOLDER :      // 폴더
                    format.setTextViewsText(tv1, item.getName(), false);
                    format.setTextViewsText(tv2, item.getArtist(), false);
                    format.setAlbumImage(iv1, item.getAlbum_art_image());
                    break;
            }
            MainActivity.mp3Dialog = new Mp3Dialog().setSelect_library_inner(MainActivity.select_library);
            ib1.setOnClickListener(v -> {
                MainActivity.mp3Dialog.showDialog(itemView.getContext(), MainActivity.LIBRARY_ALL, item);
            });
            card.setOnClickListener(v -> {
                MainActivity.setPlaylist(itemView.getContext(), MainActivity.libraryData, item, true);
            });
        }
    }
}



















