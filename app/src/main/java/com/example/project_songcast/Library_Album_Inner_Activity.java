package com.example.project_songcast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project_songcast.Mp3Class.LibraryAdapter;
import com.example.project_songcast.Mp3Class.Mp3Data;
import com.example.project_songcast.Mp3Class.PlaylistInsert;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static com.example.project_songcast.MainActivity.NO_DATA;
import static com.example.project_songcast.Mp3Class.PlaylistInsert.START_ALBUM_INNER;

public class Library_Album_Inner_Activity extends AppCompatActivity {
    private Toolbar toolbar;
    private ActionBar actionBar;
    private ImageView iv;
    private ImageView img_background;
    private RecyclerView recyclerView;
    private LinearLayoutManager manager;
    private FloatingActionButton fab;
    private String title;
    private String sub;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_album_inner);
        iv = findViewById(R.id.library_album_inner_iv);
        iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        if(MainActivity.libraryData != null && MainActivity.libraryData.size() > 0) {
            if(MainActivity.libraryData.get(0).getAlbum_art_image() != null) {
                iv.setImageBitmap(MainActivity.libraryData.get(0).getAlbum_art_image());
                img_background = findViewById(R.id.img_background);
                img_background.setScaleType(ImageView.ScaleType.CENTER_CROP);
                img_background.setImageBitmap(MainActivity.libraryData.get(0).getAlbum_art_image());
            } else {
                iv.setBackgroundColor(Color.LTGRAY);
                iv.setImageResource(R.drawable.songcast_no_image_mini);
            }
            if(MainActivity.libraryData.get(0).getTitle() != null) {
                title = MainActivity.libraryData.get(0).getTitle();
            } else {
                title = NO_DATA;
            }
            if(MainActivity.libraryData.get(0).getArtist() != null) {
                sub = MainActivity.libraryData.get(0).getArtist();
            } else {
                sub = NO_DATA;
            }
        } else {
            iv.setBackgroundColor(Color.LTGRAY);
            iv.setImageResource(R.drawable.songcast_no_image_mini);
            title = NO_DATA;
            sub = NO_DATA;
        }
        setFab();
//      ---------------------------------------------------------------------------------------------------------------- 툴바 세팅
        toolbar = findViewById(R.id.library_album_inner_toolbar);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setSubtitle(sub);
        actionBar.setDisplayHomeAsUpEnabled(true);
//      ---------------------------------------------------------------------------------------------------------------- 툴바 세팅 ed
//      ---------------------------------------------------------------------------------------------------------------- 리사이클러 뷰 세팅 ed
        MainActivity.libraryAdapter = new LibraryAdapter();
        for(Mp3Data item : MainActivity.libraryData) {
            MainActivity.libraryAdapter.addItem(item);
        }
        recyclerView = findViewById(R.id.library_album_inner_view);
        manager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(MainActivity.libraryAdapter);
//      ---------------------------------------------------------------------------------------------------------------- 리사이클러 뷰 세팅 ed
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 툴바 메뉴 생성
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.no_menu, menu);
        return true;
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 툴바 메뉴 생성 ed
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 툴바 메뉴 버튼 클릭
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onBackPressed();
        return super.onOptionsItemSelected(item);
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 툴바 메뉴 버튼 클릭 ed
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- Fab 세팅
    @SuppressLint("ClickableViewAccessibility")
    public void setFab() {
        fab = findViewById(R.id.library_album_inner_fab);
        fab.setOnTouchListener((v, event) -> {
            if(event.getAction() == MotionEvent.ACTION_UP) {
                PlaylistInsert.startPosition = START_ALBUM_INNER;
                MainActivity.insert.start(null, null);
            }
            return true;
        });
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- Fab 세팅 ed
}


















