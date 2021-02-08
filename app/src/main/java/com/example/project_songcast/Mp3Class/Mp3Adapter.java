package com.example.project_songcast.Mp3Class;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_songcast.Library_Album_Inner_Activity;
import com.example.project_songcast.Library_Artist_Inner_Activity;
import com.example.project_songcast.Library_Folder_Inner_Activity;
import com.example.project_songcast.MainActivity;
import com.example.project_songcast.R;

import java.util.ArrayList;

public class Mp3Adapter extends RecyclerView.Adapter<Mp3Adapter.ViewHolder> {
    private ArrayList<Mp3Data> itemList = new ArrayList<>();
    private int id;
    private Context context;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch(MainActivity.select_library) {
            case MainActivity.LIBRARY_ALL :                 // all
                id = R.layout.library_all_item;
                break;
            case MainActivity.LIBRARY_ARTIST :              // artist
                id = R.layout.library_artist_item;
                break;
            case MainActivity.LIBRARY_ALBUM :               // album
                id = R.layout.library_album_item;
                break;
            case MainActivity.LIBRARY_FOLDER :              // folder
                id = R.layout.library_folder_item;
                break;
        }
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(id, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setItem(itemList.get(position), MainActivity.select_library);
        holder.setContext(context);
    }
    @Override
    public int getItemCount() {
        return itemList.size();
    }
    public void addItem(Mp3Data item) {
        itemList.add(item);
    }
    static class ViewHolder extends RecyclerView.ViewHolder {
        private Context context;
        private TextView tv1, tv2;
        private ImageView iv1;
        private ImageButton ib1;
        private CardView card;
        private Mp3DataFormat format = new Mp3DataFormat();
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            switch(MainActivity.select_library) {
                case MainActivity.LIBRARY_ALL :                 // all
                    iv1 = itemView.findViewById(R.id.library_all_iv);
                    tv1 = itemView.findViewById(R.id.library_all_tv1);
                    tv2 = itemView.findViewById(R.id.library_all_tv2);
                    ib1 = itemView.findViewById(R.id.library_all_ib);
                    card = itemView.findViewById(R.id.library_all_card);
                    break;
                case MainActivity.LIBRARY_ARTIST :              // artist
                    tv1 = itemView.findViewById(R.id.library_artist_tv1);
                    tv2 = itemView.findViewById(R.id.library_artist_tv2);
                    ib1 = itemView.findViewById(R.id.library_artist_ib);
                    card = itemView.findViewById(R.id.library_artist_card);
                    break;
                case MainActivity.LIBRARY_ALBUM :               // album
                    iv1 = itemView.findViewById(R.id.library_album_iv);
                    tv1 = itemView.findViewById(R.id.library_album_tv1);
                    tv2 = itemView.findViewById(R.id.library_album_tv2);
                    ib1 = itemView.findViewById(R.id.library_album_ib);
                    card = itemView.findViewById(R.id.library_album_card);
                    break;
                case MainActivity.LIBRARY_FOLDER :              // folder
                    iv1 = itemView.findViewById(R.id.library_folder_iv);
                    tv1 = itemView.findViewById(R.id.library_folder_tv1);
                    tv2 = itemView.findViewById(R.id.library_folder_tv2);
                    ib1 = itemView.findViewById(R.id.library_folder_ib);
                    card = itemView.findViewById(R.id.library_folder_card);
                    break;
            }
        }
        public static Intent library_intent;
        public void setItem(Mp3Data item, int library_select) {
            switch(library_select) {
                case MainActivity.LIBRARY_ALL :             // all
                    format.setTextViewsText(tv1, item.getTitle(), false);
                    format.setTextViewsText(tv2, item.getArtist(), false);
                    format.setAlbumImage(iv1, item.getAlbum_art_image());
                    ib1.setOnClickListener(v -> {
                        MainActivity.mp3Dialog.showDialog(itemView.getContext(), MainActivity.LIBRARY_ALL, item);
                    });
                    card.setOnClickListener(v -> {
                        MainActivity.setPlaylist(itemView.getContext(), MainActivity.mp3DataList, item, true);
                    });
                    break;
                case MainActivity.LIBRARY_ARTIST :          // artist
                    format.setTextViewsText(tv1, item.getArtist(), false);
                    format.setTextViewsText(tv2, item.getArtistCount(), true);
                    ib1.setOnClickListener(v -> {
                        MainActivity.mp3Dialog.showDialog(itemView.getContext(), MainActivity.LIBRARY_ARTIST, item);
                    });
                    card.setOnClickListener(v -> {
                        MainActivity.libraryData = LibraryAdapter.setLibraryItems(MainActivity.LIBRARY_ARTIST, item);
                        library_intent = new Intent(itemView.getContext(), Library_Artist_Inner_Activity.class);
                        itemView.getContext().startActivity(library_intent);
                    });
                    break;
                case MainActivity.LIBRARY_ALBUM :           // album
                    format.setTextViewsText(tv1, item.getAlbum(), false);
                    format.setTextViewsText(tv2, item.getArtist(), false);
                    format.setAlbumImage(iv1, item.getAlbum_art_image());
                    ib1.setOnClickListener(v -> {
                        MainActivity.mp3Dialog.showDialog(itemView.getContext(), MainActivity.LIBRARY_ALBUM, item);
                    });
                    card.setOnClickListener(v -> {
                        MainActivity.libraryData = LibraryAdapter.setLibraryItems(MainActivity.LIBRARY_ALBUM, item);
                        library_intent = new Intent(itemView.getContext(), Library_Album_Inner_Activity.class);
                        itemView.getContext().startActivity(library_intent);
                    });
                    break;
                case MainActivity.LIBRARY_FOLDER :          // folder
                    format.setTextViewsText(tv1, item.getFolder_name(), false);
                    format.setTextViewsText(tv2, item.getFolderCount(), true);
                    ib1.setOnClickListener(v -> {
                        MainActivity.mp3Dialog.showDialog(itemView.getContext(), MainActivity.LIBRARY_FOLDER, item);
                    });
                    card.setOnClickListener(v -> {
                        MainActivity.libraryData = LibraryAdapter.setLibraryItems(MainActivity.LIBRARY_FOLDER, item);
                        library_intent = new Intent(itemView.getContext(), Library_Folder_Inner_Activity.class);
                        itemView.getContext().startActivity(library_intent);
                    });
                    break;
            }
        }
        public void setContext(Context context) {
            this.context = context;
        }
    }
}















