package com.example.project_songcast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.project_songcast.ListenerInterface.OnPlaylistInsertListener;
import com.example.project_songcast.Mp3Class.PlaylistInsert;

public class Playlist_Insert_List_Activity extends AppCompatActivity implements OnPlaylistInsertListener {
    private PlaylistInsert insert;
    private Toolbar toolbar;
    private ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_insert_list);
//      ---------------------------------------------------------------------------------------------------------------- 툴바 세팅
        toolbar = findViewById(R.id.playlist_insert_list_toolbar);
        toolbar.setTitle(MainActivity.INSERT_PLAYLIST);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
//      ---------------------------------------------------------------------------------------------------------------- 툴바 세팅 ed
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
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 툴바 메뉴 버튼 클릭 ed
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 리스너
    @Override
    public void insertFinish() {
        onBackPressed();
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 리스너 ed
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- Default 오버라이드
    @Override
    public void onResume() {
        MainActivity.insert.setPlaylistDataListener(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.playlist_insert_list_view, new PlaylistFragment().setInsertMode()).commit();        // setInsertMode 적용 프래그먼트
        super.onResume();
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- Default 오버라이드 ed
}