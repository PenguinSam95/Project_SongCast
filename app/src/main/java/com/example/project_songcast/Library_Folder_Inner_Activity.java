package com.example.project_songcast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
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
import static com.example.project_songcast.Mp3Class.PlaylistInsert.START_FOLDER_INNER;

public class Library_Folder_Inner_Activity extends AppCompatActivity {
    private Toolbar toolbar;
    private ActionBar actionBar;
    private TextView tv;
    private ImageView iv;
    private RecyclerView recyclerView;
    private LinearLayoutManager manager;
    private FloatingActionButton fab;
    private String title;
    private String subtitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_folder_inner);
        if(MainActivity.libraryData != null && MainActivity.libraryData.size() > 0) {
            title = MainActivity.libraryData.get(0).getFolder_name();
            subtitle = MainActivity.libraryData.get(0).getPath();
        } else {
            title = NO_DATA;
            subtitle = NO_DATA;
        }
        setFab();
//      ---------------------------------------------------------------------------------------------------------------- 툴바 세팅
        toolbar = findViewById(R.id.library_folder_inner_toolbar);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        tv = findViewById(R.id.library_folder_inner_subtitle);
        tv.setText(subtitle);
//      ---------------------------------------------------------------------------------------------------------------- 툴바 세팅 ed
//      ---------------------------------------------------------------------------------------------------------------- 리사이클러 뷰 세팅 ed
        MainActivity.libraryAdapter = new LibraryAdapter();
        for(Mp3Data item : MainActivity.libraryData) {
            MainActivity.libraryAdapter.addItem(item);
        }
        recyclerView = findViewById(R.id.library_folder_inner_view);
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
        fab = findViewById(R.id.library_folder_inner_fab);
        fab.setOnTouchListener((v, event) -> {
            if(event.getAction() == MotionEvent.ACTION_UP) {
                PlaylistInsert.startPosition = START_FOLDER_INNER;
                MainActivity.insert.start(null, null);
            }
            return true;
        });
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- Fab 세팅 ed
}























