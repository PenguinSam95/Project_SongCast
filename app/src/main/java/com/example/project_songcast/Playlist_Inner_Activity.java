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

import com.example.project_songcast.Mp3Class.Mp3Data;
import com.example.project_songcast.Mp3Class.Mp3Dialog;
import com.example.project_songcast.Mp3Class.Playlist;
import com.example.project_songcast.Mp3Class.PlaylistAdapter;
import com.example.project_songcast.Mp3Class.PlaylistInsert;
import com.google.android.gms.cast.framework.CastButtonFactory;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.media.widget.CastSeekBar;

import java.util.ArrayList;
import java.util.List;

public class Playlist_Inner_Activity extends AppCompatActivity {
    private Toolbar toolbar;
    private ActionBar actionBar;
    private RecyclerView recyclerView;
    private LinearLayoutManager manager;
    private PlaylistAdapter playlistAdapter;
    private Playlist thisPlaylist;
    private ImageView iv;
    private TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_inner);
//      ---------------------------------------------------------------------------------------------------------------- 툴바 세팅
        toolbar = findViewById(R.id.playlist_inner_toolbar);
        toolbar.setTitle(MainActivity.playlist.getName());
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
//      ---------------------------------------------------------------------------------------------------------------- 툴바 세팅 ed
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// cast
//      ---------------------------------------------------------------------------------------------------------------- 캐스트 세팅
        mCastContext = CastContext.getSharedInstance(this);
//      ---------------------------------------------------------------------------------------------------------------- 캐스트 세팅 ed
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// cast
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 툴바 메뉴 생성
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.playlist_inner_menu, menu);
        mediaRouteMenuItem = CastButtonFactory.setUpMediaRouteButton(getApplicationContext(), menu, R.id.cast_playlist_inner);
        return true;
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 툴바 메뉴 생성 ed
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 툴바 메뉴 버튼 클릭
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addItem_playlist_inner :      // 오디오 추가
                PlaylistInsert.startPosition = PlaylistInsert.START_ALL;
                MainActivity.insert.start(null, MainActivity.playlist);
                return true;
            case R.id.edit_playlist_inner :         // 편집
                MainActivity.insert.startPlaylistInsert();
                return true;
            default:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 툴바 메뉴 버튼 클릭 ed
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 리사이클러 뷰 세팅 & 새로고침 ed
    public void update() {
        playlistAdapter = new PlaylistAdapter();
        playlistAdapter.setListNum(PlaylistAdapter.PLAYLIST_INNER);
        if(MainActivity.mp3PlayList.size() > 0) {
            if(iv != null) {
                iv.setImageBitmap(null);
            }
            if(tv != null) {
                tv.setText(null);
            }
            for(Mp3Data item : MainActivity.mp3PlayList) {
                playlistAdapter.addItem(item);
            }
        } else {
            iv = findViewById(R.id.playlist_inner_no_item_iv);
            tv = findViewById(R.id.playlist_inner_no_item_tv);
            iv.setImageResource(R.drawable.songcast_no_image);
            tv.setText(Mp3Dialog.ITEM_IS_EMPTY);
        }
        recyclerView = findViewById(R.id.playlist_inner_view);
        manager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(playlistAdapter);
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 리사이클러 뷰 세팅 & 새로고침 ed
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 플레이리스트 내용물 추가
    public void setPlaylist(Playlist item) {
        List<String> list = item.getPlaylist_data();
        MainActivity.mp3PlayList = new ArrayList<>();
        if(list != null) {
            for(String data : list) {
                for(Mp3Data mp3Data : MainActivity.mp3DataList) {
                    if(data.equals(mp3Data.getId())) {
                        MainActivity.mp3PlayList.add(mp3Data);
                    }
                }
            }
        }
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 플레이리스트 내용물 추가 ed
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- Default 오버라이드
    @Override
    protected void onResume() {
        setPlaylist(MainActivity.playlist);
        update();
        super.onResume();
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- Default 오버라이드 ed
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// cast
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 캐스트
    private MenuItem mediaRouteMenuItem;
    private CastContext mCastContext;
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 캐스트 ed
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// cast
}





















