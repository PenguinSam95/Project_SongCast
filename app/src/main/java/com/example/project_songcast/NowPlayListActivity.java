package com.example.project_songcast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.project_songcast.ListenerInterface.OnSelectedMp3ChangeListener;
import com.example.project_songcast.Mp3Class.Mp3Data;
import com.example.project_songcast.Mp3Class.Mp3Dialog;
import com.example.project_songcast.Mp3Class.PlaylistAdapter;

public class NowPlayListActivity extends AppCompatActivity implements OnSelectedMp3ChangeListener {
    private Toolbar toolbar;
    private ActionBar actionBar;
    private RecyclerView recyclerView;
    private PlaylistAdapter playlistAdapter;
    private LinearLayoutManager manager;
    private ImageView iv;
    private TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now_play_list);
//      ---------------------------------------------------------------------------------------------------------------- 툴바 세팅
        toolbar = findViewById(R.id.main_toolbar);
        toolbar.setTitle(MainActivity.NOW_PLAYING_LIST);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
//      ---------------------------------------------------------------------------------------------------------------- 툴바 세팅 ed
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 리사이클러 뷰 세팅
    public void setRecyclerView() {
        playlistAdapter = new PlaylistAdapter();
        playlistAdapter.setListNum(PlaylistAdapter.PLAYLIST_NOW);
        if(MainActivity.nowPlayList != null && MainActivity.nowPlayList.size() > 0) {
            if(iv != null) {
                iv.setImageBitmap(null);
            }
            if(tv != null) {
                tv.setText(null);
            }
            for(Mp3Data item : MainActivity.nowPlayList) {
                playlistAdapter.addItem(item);
            }
        } else {
            iv = findViewById(R.id.playlist_now_no_item_iv);
            tv = findViewById(R.id.playlist_now_no_item_tv);
            iv.setImageResource(R.drawable.songcast_no_image);
            tv.setText(Mp3Dialog.ITEM_IS_EMPTY);
        }
        recyclerView = findViewById(R.id.playlist_now_view);
        manager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(playlistAdapter);
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 리사이클러 뷰 세팅 ed
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
    public void onSelectedMp3Change() {
        onBackPressed();
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 리스너 ed
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- Default 오버라이드
    @Override
    protected void onResume() {
        setRecyclerView();
        playlistAdapter.setOnSelectedMp3ChangeListener(this);
        super.onResume();
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- Default 오버라이드 ed
}


















