package com.example.project_songcast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.project_songcast.ListenerInterface.OnCountChangeListener;
import com.example.project_songcast.ListenerInterface.OnPlaylistInsertListener;
import com.example.project_songcast.Mp3Class.InsertAdapter;
import com.example.project_songcast.Mp3Class.Mp3Data;
import com.example.project_songcast.Mp3Class.Mp3Dialog;
import com.example.project_songcast.Mp3Class.Playlist;
import com.example.project_songcast.Mp3Class.PlaylistAdapter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static com.example.project_songcast.PlaylistFragment.NO_SIGN;
import static com.example.project_songcast.PlaylistFragment.YES_SIGN;

public class Playlist_Edit_Activity extends AppCompatActivity implements OnCountChangeListener, OnPlaylistInsertListener {
    private Toolbar toolbar;
    private ActionBar actionBar;
    private RecyclerView recyclerView;
    private LinearLayoutManager manager;
    ConstraintLayout lo1, lo2;
    private ImageView iv, iv1, iv2;
    private TextView tv, tv1, tv2, bar_tv1, bar_tv2;
    private CheckBox cb1;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_edit);
//      ---------------------------------------------------------------------------------------------------------------- 툴바 세팅
        toolbar = findViewById(R.id.playlist_edit_toolbar);
        toolbar.setTitle(MainActivity.playlist.getName());
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
//      ---------------------------------------------------------------------------------------------------------------- 툴바 세팅 ed
//      ---------------------------------------------------------------------------------------------------------------- 버튼 세팅 ed
        lo1 = findViewById(R.id.playlist_edit_layout1);
        lo2 = findViewById(R.id.playlist_edit_layout2);
        iv1 = findViewById(R.id.playlist_edit_iv1);
        iv2 = findViewById(R.id.playlist_edit_iv2);
        tv1 = findViewById(R.id.playlist_edit_tv1);
        tv2 = findViewById(R.id.playlist_edit_tv2);
        lo1.setOnTouchListener((v, event) -> {                              // 추가
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                iv1.setImageResource(R.drawable.ic_add_playlist_pushed);
                tv1.setTextColor(Color.parseColor("#6A1FF1"));
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                iv1.setImageResource(R.drawable.ic_add_playlist);
                tv1.setTextColor(Color.parseColor("#FF000000"));
                String ids = null;
                MainActivity.insertList = new ArrayList<>();
                for(String checker : InsertAdapter.instanceMap.keySet()) {
                    if(InsertAdapter.instanceMap.get(checker)) {
                        for(Mp3Data mp3Data : MainActivity.mp3PlayList) {
                            ids = mp3Data.getName()+"%"+mp3Data.getPath();
                            if(ids.equals(checker)) {
                                MainActivity.insertList.add(mp3Data);
                                break;
                            }
                        }
                    }
                }
                MainActivity.insert.start(MainActivity.insertList, null);
                MainActivity.insert.setPlaylistItemListener(this);
            }
            return true;
        });
        lo2.setOnTouchListener((v, event) -> {                              // 삭제
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                iv2.setImageResource(R.drawable.ic_delete_pushed);
                tv2.setTextColor(Color.parseColor("#6A1FF1"));
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                iv2.setImageResource(R.drawable.ic_delete);
                tv2.setTextColor(Color.parseColor("#FF000000"));
                deleteItemInPlaylist();
            }
            return true;
        });
//      ---------------------------------------------------------------------------------------------------------------- 버튼 세팅 ed
//      ---------------------------------------------------------------------------------------------------------------- 전체선텍 체크박스 세팅
        cb1 = findViewById(R.id.playlist_edit_bar_cb1);
        cb1.setOnCheckedChangeListener((buttonView, isChecked) -> {
            for(String id : InsertAdapter.instanceMap.keySet()) {
                InsertAdapter.instanceMap.put(id, isChecked);
            }
            onCountChanged(0);
            if(isChecked) {
                onCountChanged(InsertAdapter.instanceMap.size());
            }
            update();
        });
        bar_tv1 = findViewById(R.id.playlist_edit_bar_tv1);
        bar_tv2 = findViewById(R.id.playlist_edit_bar_tv2);
        onCountChanged(0);
        bar_tv2.setText("곡 선택");
//      ---------------------------------------------------------------------------------------------------------------- 전체선텍 체크박스 세팅 ed
//        update();
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
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 리사이클러 뷰 세팅 & 새로고침 ed
    public void update() {
        InsertAdapter.instanceMap = new LinkedHashMap<>();
        MainActivity.instanceData = MainActivity.mp3PlayList;
        for(Mp3Data mp3Data : MainActivity.instanceData) {
            InsertAdapter.instanceMap.put(mp3Data.getName()+"%"+mp3Data.getPath(), false);
        }
        MainActivity.insertAdapter = new InsertAdapter();
        if(MainActivity.instanceData.size() > 0) {
            if(iv != null) {
                iv.setImageBitmap(null);
            }
            if(tv != null) {
                tv.setText(null);
            }
            for(Mp3Data item : MainActivity.instanceData) {
                MainActivity.insertAdapter.addItem(item);
            }
        } else {
            iv = findViewById(R.id.playlist_edit_no_item_iv);
            tv = findViewById(R.id.playlist_edit_no_item_tv);
            iv.setImageResource(R.drawable.songcast_no_image);
            tv.setText(Mp3Dialog.ITEM_IS_EMPTY);
        }
        MainActivity.insertAdapter.setOnCountChangeListener(this);
        recyclerView = findViewById(R.id.playlist_edit_view);
        manager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(MainActivity.insertAdapter);
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 리사이클러 뷰 세팅 & 새로고침 ed
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 다이얼로그
    private AlertDialog.Builder ad;
    private AlertDialog alert;
    public void deleteItemInPlaylist() {
        ad = new AlertDialog.Builder(this);
        ad.setTitle(MainActivity.DELETE_IN_PLAYLIST);
        ad.setMessage(MainActivity.DELETE_IN_PLAYLIST_MSG);
        ad.setPositiveButton(YES_SIGN, (dialog, which) -> {
            if(InsertAdapter.instanceMap.size() > 0) {
                for(String checker : InsertAdapter.instanceMap.keySet()) {
                    if(InsertAdapter.instanceMap.get(checker)) {
                        MainActivity.mp3PlayList.remove(InsertAdapter.instanceMap.get(checker));
                    }
                }
                String ids = null;
                for(String checker : InsertAdapter.instanceMap.keySet()) {
                    if(InsertAdapter.instanceMap.get(checker)) {
                        for(Mp3Data mp3Data : MainActivity.mp3PlayList) {
                            ids = mp3Data.getName()+"%"+mp3Data.getPath();
                            if(ids.equals(checker)) {
                                MainActivity.mp3PlayList.remove(mp3Data);
                                break;
                            }
                        }
                    }
                }
                List<String> list = new ArrayList<>();
                for(Mp3Data mp3Data : MainActivity.mp3PlayList) {
                    list.add(mp3Data.getId());
                };
                MainActivity.playlist.setPlaylist_data(list);
                onResume();
            }
        });
        ad.setNegativeButton(NO_SIGN, (dia, which) -> { });
        alert = ad.create();
        alert.show();
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 다이얼로그 ed
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
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 리스너
    @Override
    public void onCountChanged(int Count) {
        bar_tv1.setText(Integer.toString(Count));
    }
    @Override
    public void insertFinish() {
        onBackPressed();
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 리스너 ed
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- Default 오버라이드
    @Override
    protected void onResume() {
        update();
        onCountChanged(0);
        super.onResume();
    }
    @Override
    public void onBackPressed() {
        MainActivity.insertAdapter.setOnCountChangeListener(null);
        super.onBackPressed();
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- Default 오버라이드 ed
}




















