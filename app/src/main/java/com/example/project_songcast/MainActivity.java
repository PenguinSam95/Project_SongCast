package com.example.project_songcast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v4.media.MediaMetadataCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.project_songcast.Caster.CasterServer;
import com.example.project_songcast.ListenerInterface.OnAudioPlayerControlListener;
import com.example.project_songcast.ListenerInterface.OnRefreshListener;
import com.example.project_songcast.Mp3Class.AudioPlayer;
import com.example.project_songcast.Mp3Class.AudioPlayerInterface;
import com.example.project_songcast.Mp3Class.InsertAdapter;
import com.example.project_songcast.Mp3Class.LibraryAdapter;
import com.example.project_songcast.Mp3Class.Mp3Adapter;
import com.example.project_songcast.Mp3Class.Mp3Data;
import com.example.project_songcast.Mp3Class.Mp3Dialog;
import com.example.project_songcast.Mp3Class.Mp3Query;
import com.example.project_songcast.Mp3Class.Playlist;
import com.example.project_songcast.Mp3Class.PlaylistInsert;
import com.google.android.gms.cast.framework.CastButtonFactory;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.material.navigation.NavigationView;
import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnAudioPlayerControlListener {
    public final static String TITLE_THIS_APP = "Song Cast";
    public final static String TITLE_PLAY_LIST = "플레이리스트";
    public final static String TITLE_LIBRARY = "라이브러리";
    public final static String APP_INFO = "2021.01.04 \n\t~ \n2021.01.29 \n개인 프로젝트 제작\n제작 : PenguinSam95";
    public final static String NO_DATA = "알 수 없음";
    public final static String NOW_PLAYING_LIST = "재생중인 목록";
    public final static String PLAYLIST_BAR_TITLE = "새 플레이리스트 추가";
    public final static String INSERT_PLAYLIST = "플레이리스트 곡 담기";
    public final static String DELETE_IN_PLAYLIST = "플레이리스트에서 삭제";
    public final static String DELETE_IN_PLAYLIST_MSG = "선택한 곡(들)을 플레이리스트에서 삭제하시겠습니까?";
    public final static String AUDIO_INFO = "곡 정보";
    public final static String FILE_TYPE_AUDIO = "audio";
    public final static String FILE_TYPE_IMAGE = "image";
    public final static String CASTER_APP_CONNECTED = "오디오 캐스트 연결 중";
    public final static String CASTER_APP_DIS_CONNECTED = "오디오 캐스트 연결 종료";
    public final static Uri ALBUM_ART_URI = Uri.parse("content://media/external/audio/albumart");
    public final static int REQUEST_CODE = 108;
    public final static int PORT = 8080;
    public static boolean select_fragments = false;
    public static int select_library = 0;
    public final static int LIBRARY_ALL = 0;
    public final static int LIBRARY_ARTIST = 1;
    public final static int LIBRARY_ALBUM = 2;
    public final static int LIBRARY_FOLDER = 3;
    public final static int PLAYLIST_INNER = 30;
    private Toolbar toolbar;
    private DrawerLayout mainLayout;
    private NavigationView nav;
    public static ActionBar actionBar;
    private ActionBarDrawerToggle toggle;
    private LibraryFragment libraryFragment;
    private PlaylistFragment playlistFragment;
    private Fragment front;
    public static CasterServer mCasterServer;
    public static Intent now_playlist;
    public static AudioPlayerInterface player;
    public static Mp3Dialog mp3Dialog;
    public static Intent audio_intent;
    public static Mp3Data selectedMp3;
    public static Playlist playlist;
    public static ArrayList<Mp3Data> mp3DataList;
    public static ArrayList<Mp3Data> nowPlayList;
    public static ArrayList<Mp3Data> mp3PlayList;
    public static ArrayList<Mp3Data> libraryData;
    public static ArrayList<Mp3Data> instanceData;
    public static ArrayList<Mp3Data> insertList;
    public static PlaylistInsert insert;
    public static HashMap<String, Playlist> customPlayList;
    public static HashMap<String, Mp3Data> albumList;
    public static HashMap<String, Mp3Data> artistList;
    public static HashMap<String, Mp3Data> folderList;
    public static Mp3Adapter mp3Adapter;
    public static LibraryAdapter libraryAdapter;
    public static InsertAdapter insertAdapter;
    public static Intent background_audio;
    public static final boolean RANDOM_PLAYLIST_ON = true;
    public static final boolean RANDOM_PLAYLIST_OFF=false;
    public static final int RECYCLE_FALSE = 100;
    public static final int RECYCLE_TRUE = 200;
    public static final int RECYCLE_ONE = 300;
    public static boolean select_random = RANDOM_PLAYLIST_OFF;
    public static boolean checkUpdateRemote;
    public static int select_recycle = RECYCLE_FALSE;
    private MenuItem mediaRouteMenuItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//      ---------------------------------------------------------------------------------------------------------------- 툴바 세팅
        toolbar = findViewById(R.id.main_toolbar);
        setToolbarTitle();
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
//      ---------------------------------------------------------------------------------------------------------------- 툴바 세팅 ed
//      ---------------------------------------------------------------------------------------------------------------- 네비게이션 세팅
        mainLayout = findViewById(R.id.main_layout);
        nav = findViewById(R.id.nav_view);
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.menu_library : {              // 라이브러리 Fragment
                        select_fragments = false;
                        break;
                    }
                    case R.id.menu_playList : {             // 플레이리스트 Fragment
                        select_fragments = true;
                        break;
                    }
                    case R.id.menu_app_info : {             // 앱 정보
                        appInformationDialog();
                        break;
                    }
                    case R.id.menu_refresh : {              // 새로고침
                        ConstraintLayout con = findViewById(R.id.main_pb1);
                        con.setVisibility(View.VISIBLE);
                        final Handler handler = new Handler(){
                            @SuppressLint("HandlerLeak")
                            public void handleMessage(Message msg){
                                con.setVisibility(View.GONE);
                                refreshListener.onRefresh();
                            }
                        };
                        class UpdateThread extends Thread {
                            @Override
                            public void run() {
                                MainActivity.mp3DataList = null;
                                MainActivity.artistList = null;
                                MainActivity.albumList = null;
                                MainActivity.folderList = null;
                                Mp3Query query = new Mp3Query(getApplicationContext());
                                MainActivity.mp3DataList = query.getMusicList();
                                MainActivity.artistList = query.getArtistList();
                                MainActivity.albumList = query.getAlbumList();
                                MainActivity.folderList = query.getFolderList();
                                handler.sendMessage(handler.obtainMessage());
                            }
                        }
                        new UpdateThread().start();
                        break;
                    }
                }
                mainLayout.closeDrawer(GravityCompat.START);
                setFrontDisplay();
                return false;
            }
        });
//      ---------------------------------------------------------------------------------------------------------------- 네비게이션 세팅 ed
//      ---------------------------------------------------------------------------------------------------------------- 햄버거 세팅
        toggle = new ActionBarDrawerToggle(this, mainLayout, toolbar, R.string.nav_open, R.string.nav_close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                toolbar.setTitle(TITLE_THIS_APP);
                super.onDrawerSlide(drawerView, slideOffset);
            }
            @Override
            public void onDrawerClosed(View drawerView) {
                setToolbarTitle();
                super.onDrawerClosed(drawerView);
            }

        };
        mainLayout.addDrawerListener(toggle);
        toggle.syncState();
//      ---------------------------------------------------------------------------------------------------------------- 햄버거 세팅 ed
//      ---------------------------------------------------------------------------------------------------------------- Main Display
        libraryFragment = new LibraryFragment();
        playlistFragment = new PlaylistFragment();
        setFrontDisplay();
        setDefault();
//      ---------------------------------------------------------------------------------------------------------------- Main Display ed
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// cast
//      ---------------------------------------------------------------------------------------------------------------- 캐스트 세팅
        mCastContext = CastContext.getSharedInstance(this);
        mCastSession = mCastContext.getSessionManager().getCurrentCastSession();
//      ---------------------------------------------------------------------------------------------------------------- 캐스트 세팅 ed
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// cast
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 미니 플레이어 세팅
    private FrameLayout front_layout, mini_layout;
    private LinearLayout mini_view;
    private ConstraintLayout.LayoutParams params;
    private Mini_Player_Fragment mini_player;
    private void setMiniPlayer() {
        front_layout = findViewById(R.id.fragment_display);
        mini_layout = findViewById(R.id.mini_audio_player);
        mini_view = findViewById(R.id.mini_audio_player_view);
        params = (ConstraintLayout.LayoutParams) front_layout.getLayoutParams();
        if(MainActivity.selectedMp3 != null) {
            mini_view.setVisibility(View.VISIBLE);
            mini_player = new Mini_Player_Fragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.mini_audio_player, mini_player).commit();
            params.bottomMargin = getResources().getDimensionPixelSize(R.dimen.fragment_display_margin);
            MainActivity.player.setOnAudioPlayerControlListener_main(this);
        } else {
            mini_view.setVisibility(View.GONE);
            params.bottomMargin = getResources().getDimensionPixelSize(R.dimen.fragment_display_no_margin);
        }
        front_layout.setLayoutParams(params);
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 미니 플레이어 세팅 ed
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 오디오 세팅
    public void setDefault() {
        if(mp3Dialog == null) {
            mp3Dialog = new Mp3Dialog();
        }
        if(customPlayList == null) {
            customPlayList = new LinkedHashMap();
        }
        instanceData = new ArrayList<>();
        insert = new PlaylistInsert(this);
        if(background_audio == null) {
            background_audio = new Intent(this, AudioPlayer.class);
            startService(background_audio);
            bindService(background_audio, serviceConnection, BIND_AUTO_CREATE);
        }
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 오디오 세팅 ed
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 서비스 커넥션
    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            AudioPlayer.ServiceBinder binder = (AudioPlayer.ServiceBinder) iBinder;
            player = (AudioPlayer) binder.getService();
        }
        @Override
        public void onServiceDisconnected(ComponentName componentName) {
        }
    };
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 서비스 커넥션 ed
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 인텐트 플레이리스트 세팅
    public static void setPlaylist(Context context, ArrayList<Mp3Data> nowPlayList, Mp3Data selectedMp3, boolean createNowIntent) {
        MainActivity.nowPlayList = nowPlayList;
        MainActivity.selectedMp3 = selectedMp3;
        setBackgroundService(context, true, createNowIntent);
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 인텐트 플레이리스트 세팅 ed
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 백그라운드 서비스
    public static void setBackgroundService(Context context, boolean background_reset, boolean createNowIntent) {
        if(background_reset) {
            MainActivity.checkUpdateRemote = true;
            if(MainActivity.background_audio != null) {
                MainActivity.player.stop();
            }
            MainActivity.background_audio = new Intent(context, AudioPlayer.class);
            context.startService(MainActivity.background_audio);
        }
        if(createNowIntent) {
            MainActivity.audio_intent = new Intent(context, AudioActivity.class);
            context.startActivity(MainActivity.audio_intent);
        }
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 백그라운드 서비스 ed
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 프래그멘트 변경
    public void setFrontDisplay() {
        if(select_fragments) {
            front = playlistFragment;
        } else {
            front = libraryFragment;
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_display, front).commit();
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 프래그멘트 변경 ed
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 프래그멘트 타이틀 변경
    public void setToolbarTitle() {
        if(select_fragments) {
            toolbar.setTitle(TITLE_PLAY_LIST);
        } else {
            toolbar.setTitle(TITLE_LIBRARY);
        }
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 프래그멘트 타이틀 변경 ed
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 툴바 메뉴 생성
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.tool_menu, menu);
        mediaRouteMenuItem = CastButtonFactory.setUpMediaRouteButton(getApplicationContext(), menu, R.id.cast);
        return true;
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 툴바 메뉴 생성 ed
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 툴바 메뉴 버튼 클릭
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nowPlayList :
                now_playlist = new Intent(getApplicationContext(), NowPlayListActivity.class);
                startActivity(now_playlist);
                return true;
            case R.id.app_info :
                appInformationDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 툴바 메뉴 버튼 클릭 ed
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 네비게이션
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 네비게이션 ed
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 앱 정보
    private AlertDialog alert;
    private AlertDialog.Builder ad;
    private void appInformationDialog() {
        ad = new AlertDialog.Builder(this);
        ad.setTitle(TITLE_THIS_APP);
        ad.setMessage(APP_INFO);
        alert = ad.create();
        alert.show();
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 앱 정보 ed
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 리스너
    @Override
    public void onPlayItemChanged() { }
    @Override
    public void playlistFinished() {
        mini_view.setVisibility(View.GONE);
        params.bottomMargin = getResources().getDimensionPixelSize(R.dimen.fragment_display_no_margin);
    }
    public static OnRefreshListener refreshListener;
    public static void setOnRefreshListener(OnRefreshListener refreshListener) {
        MainActivity.refreshListener = refreshListener;
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 리스너 ed
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- Default 오버라이드
    @Override
    public void onBackPressed() {
        if (mainLayout.isDrawerOpen(GravityCompat.START)) {
            mainLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        setMiniPlayer();
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- Default 오버라이드 ed
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// cast
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 캐스트
    private CastContext mCastContext;
    private CastSession mCastSession;
    public enum PlaybackLocation {
        LOCAL,
        REMOTE
    }
    public enum PlaybackState {
        PLAYING, PAUSED, BUFFERING, IDLE, END
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 캐스트 ed
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// cast
}















