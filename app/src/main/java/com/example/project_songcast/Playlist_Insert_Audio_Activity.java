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
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.project_songcast.Mp3Class.InsertAdapter;
import com.example.project_songcast.Mp3Class.LibraryAdapter;
import com.example.project_songcast.Mp3Class.Mp3Data;
import com.example.project_songcast.ListenerInterface.OnCountChangeListener;
import com.example.project_songcast.ListenerInterface.OnPlaylistInsertListener;
import com.example.project_songcast.Mp3Class.PlaylistInsert;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class Playlist_Insert_Audio_Activity extends AppCompatActivity implements OnPlaylistInsertListener, OnCountChangeListener {
    private PlaylistInsert insert;
    private Toolbar toolbar;
    private ActionBar actionBar;
    private RecyclerView recyclerView;
    private LinearLayoutManager manager;
    private CheckBox cb1;
    private TextView tv1, tv2;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_insert_audio);
        cb1 = findViewById(R.id.playlist_insert_item_cb1);
        tv1 = findViewById(R.id.playlist_insert_item_tv1);
        tv2 = findViewById(R.id.playlist_insert_item_tv2);
//      ---------------------------------------------------------------------------------------------------------------- 툴바 세팅
        toolbar = findViewById(R.id.playlist_insert_item_toolbar);
        toolbar.setTitle(MainActivity.INSERT_PLAYLIST);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
//      ---------------------------------------------------------------------------------------------------------------- 툴바 세팅 ed
//      ---------------------------------------------------------------------------------------------------------------- 리사이클러 뷰 세팅
        String ids = null;
        MainActivity.instanceData = new ArrayList<>();
        InsertAdapter.instanceMap = new LinkedHashMap<>();
        onCountChanged(0);
        if(MainActivity.mp3DataList != null) {
            switch(PlaylistInsert.startPosition) {
                case PlaylistInsert.START_ALL :                                         // 전체
                    tv2.setText("곡 선택");
                    MainActivity.instanceData = MainActivity.mp3DataList;
                    for(Mp3Data mp3Data : MainActivity.instanceData) {
                        ids = mp3Data.getName()+"%"+mp3Data.getPath();
                        InsertAdapter.instanceMap.put(ids, false);
                    }
                    break;
                case PlaylistInsert.START_ARTIST :                                      // 아티스트
                    tv2.setText("개 선택");
                    for(Mp3Data mp3Data : MainActivity.artistList.values()) {
                        MainActivity.instanceData.add(mp3Data);
                        ids = mp3Data.getArtist()+"%"+mp3Data.getArtist_id();
                        InsertAdapter.instanceMap.put(ids, false);
                    }
                    break;
                case PlaylistInsert.START_ALBUM :                                       // 앨범
                    tv2.setText("개 선택");
                    for(Mp3Data mp3Data : MainActivity.albumList.values()) {
                        MainActivity.instanceData.add(mp3Data);
                        ids = mp3Data.getAlbum()+"%"+mp3Data.getArtist();
                        InsertAdapter.instanceMap.put(ids, false);
                    }
                    break;
                case PlaylistInsert.START_FOLDER :                                      // 폴더
                    tv2.setText("개 선택");
                    for(Mp3Data mp3Data : MainActivity.folderList.values()) {
                        MainActivity.instanceData.add(mp3Data);
                        ids = mp3Data.getPath()+"%"+mp3Data.getFolder_name();
                        InsertAdapter.instanceMap.put(ids, false);
                    }
                    break;
                case PlaylistInsert.START_ARTIST_INNER :                                // Inner 아티스트
                case PlaylistInsert.START_ALBUM_INNER :                                 // Inner 앨범
                case PlaylistInsert.START_FOLDER_INNER :                                // Inner 폴더
                    MainActivity.instanceData = MainActivity.libraryData;
                    tv2.setText("곡 선택");
                    for(Mp3Data mp3Data : MainActivity.instanceData) {
                        ids = mp3Data.getName()+"%"+mp3Data.getPath();
                        InsertAdapter.instanceMap.put(ids, false);
                    }
                    break;
            }
        }
//      ---------------------------------------------------------------------------------------------------------------- 리사이클러 뷰 세팅 ed
//      ---------------------------------------------------------------------------------------------------------------- 전체선텍 체크박스 세팅
        cb1.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(InsertAdapter.instanceMap != null) {
                for(String id : InsertAdapter.instanceMap.keySet()) {
                    InsertAdapter.instanceMap.put(id, isChecked);
                }
            }
            onCountChanged(0);
            if(isChecked) {
                if(InsertAdapter.instanceMap != null) {
                    onCountChanged(InsertAdapter.instanceMap.size());
                }
            }
            setRecyclerView();
        });
//      ---------------------------------------------------------------------------------------------------------------- 전체선텍 체크박스 세팅 ed
        setRecyclerView();
        MainActivity.insert.setPlaylistItemListener(this);
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 리사이클러 뷰 리셋
    private void setRecyclerView() {
        MainActivity.insertAdapter = new InsertAdapter();
        MainActivity.insertAdapter.setOnCountChangeListener(this);
        for(Mp3Data item : MainActivity.instanceData) {
            MainActivity.insertAdapter.addItem(item);
        }
        recyclerView = findViewById(R.id.playlist_insert_item_view);
        manager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(MainActivity.insertAdapter);
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 리사이클러 뷰 리셋 ed
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 툴바 메뉴 생성
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.check_menu, menu);
        return true;
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 툴바 메뉴 생성 ed
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 툴바 메뉴 버튼 클릭
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String ids = null;
        MainActivity.insertList = new ArrayList<>();
        switch (item.getItemId()) {
            case R.id.insert_check :
                List<String> list = new ArrayList<>();
                for(String checker : InsertAdapter.instanceMap.keySet()) {
                    if(InsertAdapter.instanceMap.get(checker)) {
                        list.add(checker);
                    }
                }
                switch (PlaylistInsert.startPosition) {
                    case PlaylistInsert.START_ARTIST:                      // 아티스트
                        for(Mp3Data mp3Data : MainActivity.instanceData) {
                            ids = mp3Data.getArtist() + "%" + mp3Data.getArtist_id();
                            for(String id : list) {
                                if(ids.equals(id)) {
                                    for(Mp3Data data : LibraryAdapter.setLibraryItems(MainActivity.LIBRARY_ARTIST, mp3Data)) {
                                        MainActivity.insertList.add(data);
                                    }
                                }
                            }
                        }
                        break;
                    case PlaylistInsert.START_ALBUM:                       // 앨범
                        for(Mp3Data mp3Data : MainActivity.instanceData) {
                            ids = mp3Data.getAlbum() + "%" + mp3Data.getArtist();
                            for(String id : list) {
                                if(ids.equals(id)) {
                                    for(Mp3Data data : LibraryAdapter.setLibraryItems(MainActivity.LIBRARY_ALBUM, mp3Data)) {
                                        MainActivity.insertList.add(data);
                                    }
                                }
                            }
                        }
                        break;
                    case PlaylistInsert.START_FOLDER:                      // 폴더
                        for(Mp3Data mp3Data : MainActivity.instanceData) {
                            ids = mp3Data.getPath() + "%" + mp3Data.getFolder_name();
                            for(String id : list) {
                                if(ids.equals(id)) {
                                    for(Mp3Data data : LibraryAdapter.setLibraryItems(MainActivity.LIBRARY_FOLDER, mp3Data)) {
                                        MainActivity.insertList.add(data);
                                    }
                                }
                            }
                        }
                        break;
                    default:                                               // 그 외
                        for(Mp3Data mp3Data : MainActivity.instanceData) {
                            ids = mp3Data.getName() + "%" + mp3Data.getPath();
                            for(String id : list) {
                                if(ids.equals(id)) {
                                    MainActivity.insertList.add(mp3Data);
                                    break;
                                }
                            }
                        }
                        break;
                }
                MainActivity.insert.setPlaylistItem(MainActivity.insertList);
                if(MainActivity.insert.getPlaylistData() != null) {                 // 1 단계 - 오디오 선택
                    MainActivity.insert.finish();
                } else {                                                            // 2 단계
                    MainActivity.insert.selectPlaylistData();
                }
                return true;
            default:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 툴바 메뉴 버튼 클릭 ed
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 리스너
    @Override
    public void insertFinish() {
        onBackPressed();
    }
    @Override
    public void onCountChanged(int Count) {
        tv1.setText(Integer.toString(Count));
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 리스너 ed
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- Default 오버라이드
    @Override
    public void onBackPressed() {
        MainActivity.insertAdapter.setOnCountChangeListener(null);
        super.onBackPressed();
    }
    @Override
    protected void onResume() {
        setRecyclerView();
        super.onResume();
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- Default 오버라이드 ed
}





















