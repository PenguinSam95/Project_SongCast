package com.example.project_songcast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.media.MediaMetadataCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project_songcast.Caster.CasterServer;
import com.example.project_songcast.ListenerInterface.OnAudioPlayerControlListener;
import com.example.project_songcast.Mp3Class.AudioPlayer;
import com.example.project_songcast.Mp3Class.AudioPlayerInterface;
import com.example.project_songcast.Mp3Class.Mp3Data;
import com.example.project_songcast.Mp3Class.Mp3DataFormat;
import com.google.android.gms.cast.MediaInfo;
import com.google.android.gms.cast.MediaLoadRequestData;
import com.google.android.gms.cast.MediaMetadata;
import com.google.android.gms.cast.MediaSeekOptions;
import com.google.android.gms.cast.MediaStatus;
import com.google.android.gms.cast.framework.CastButtonFactory;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.SessionManagerListener;
import com.google.android.gms.cast.framework.media.RemoteMediaClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.images.WebImage;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fi.iki.elonen.NanoHTTPD;

import static com.example.project_songcast.MainActivity.CASTER_APP_CONNECTED;
import static com.example.project_songcast.MainActivity.CASTER_APP_DIS_CONNECTED;
import static com.example.project_songcast.MainActivity.FILE_TYPE_AUDIO;
import static com.example.project_songcast.MainActivity.FILE_TYPE_IMAGE;
import static com.example.project_songcast.MainActivity.PlaybackLocation.LOCAL;
import static com.example.project_songcast.MainActivity.PlaybackLocation.REMOTE;
import static com.example.project_songcast.MainActivity.PlaybackState.BUFFERING;
import static com.example.project_songcast.MainActivity.PlaybackState.IDLE;
import static com.example.project_songcast.MainActivity.PlaybackState.PAUSED;
import static com.example.project_songcast.MainActivity.PlaybackState.PLAYING;
import static com.example.project_songcast.MainActivity.mCasterServer;

public class AudioActivity extends AppCompatActivity implements OnAudioPlayerControlListener {
    private Toolbar toolbar;
    private ActionBar actionBar;
    private ImageView img_background;
    private ImageView img_front;
    private TextView tv1, tv2;
    private TextView time1, time2;
    private ImageView play, next, prev, rnd, cycle;
    private SeekBar seekBar;
    private Mp3DataFormat format = new Mp3DataFormat();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);
        seekBar = findViewById(R.id.audio_timeLine);
//      ---------------------------------------------------------------------------------------------------------------- 툴바 세팅
        toolbar = findViewById(R.id.audio_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
//      ---------------------------------------------------------------------------------------------------------------- 툴바 세팅 ed
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// cast
//      ---------------------------------------------------------------------------------------------------------------- 캐스트 세팅
        setupCastListener();
        mCastContext = CastContext.getSharedInstance(this);
        mCastSession = mCastContext.getSessionManager().getCurrentCastSession();
//      ---------------------------------------------------------------------------------------------------------------- 캐스트 세팅 ed
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// cast
        MainActivity.background_audio = new Intent(getApplicationContext(), AudioPlayer.class);
        startService(MainActivity.background_audio);
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 툴바 메뉴 생성
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.audio_menu, menu);
        mediaRouteMenuItem = CastButtonFactory.setUpMediaRouteButton(getApplicationContext(), menu, R.id.cast_audio);
        return true;
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 툴바 메뉴 생성 ed
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 툴바 메뉴 버튼 클릭
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.playlist_audio :
                MainActivity.now_playlist = new Intent(getApplicationContext(), NowPlayListActivity.class);
                startActivity(MainActivity.now_playlist);
                break;
            case R.id.add_playList_audio :
                MainActivity.instanceData = new ArrayList<>();
                MainActivity.instanceData.add(MainActivity.selectedMp3);
                MainActivity.insert.start(MainActivity.instanceData, null);
                break;
            case R.id.info_audio :
                Intent info = new Intent(this, InformationActivity.class);
                info.putExtra("item", MainActivity.selectedMp3.getName()+"%"+MainActivity.selectedMp3.getPath());
                startActivity(info);
                break;
            default :
                super.onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 툴바 메뉴 버튼 클릭 ed
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- onResume
    @Override
    protected void onResume() {
        super.onResume();
        if(MainActivity.selectedMp3 != null) {
            setSeekBar();
            setImage();
            setTextView();
            setButton();
            startTimeThread();
        }
        if (mCastSession != null && mCastSession.isConnected()) {
            updatePlaybackLocation(REMOTE);
            if(remoteMediaClient != null && remoteMediaClient.isPlaying()) {
                updatePlayButton(PLAYING);
            } else {
                updatePlayButton(IDLE);
            }
        } else {
            updatePlaybackLocation(LOCAL);
            updatePlayButton(IDLE);
        }
        MainActivity.player.setOnAudioPlayerControlListener(this);
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// cast
        mCastContext.getSessionManager().addSessionManagerListener( mSessionManagerListener, CastSession.class );
        if(mCastSession != null && mCastSession.isConnected()) {
            updatePlaybackLocation(REMOTE);
            if(mPlaybackState == PLAYING) {
                startCasterThread();
            }
            onApplicationConnected(mCastSession);
        } else {
            updatePlaybackLocation(LOCAL);
        }
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// cast
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- onResume ed
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- OTHER
    @Override
    protected void onPause() {
        super.onPause();
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// cast
        mCastContext.getSessionManager().addSessionManagerListener( mSessionManagerListener, CastSession.class );
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// cast
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- OTHER ed
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 타임 스레드
    public boolean isTouching;
    public boolean breaker;
    private ThreadTimeLine thread;
    final Handler handler = new Handler(){
        @SuppressLint("HandlerLeak")
        public void handleMessage(Message msg){
            time1.setText(format.getAudioTime(Integer.toString(MainActivity.player.getCurrentTime())));
        }
    };
    private void startTimeThread() {
        breaker = false;
        thread = new ThreadTimeLine();
        thread.start();
    }
    class ThreadTimeLine extends Thread {
        int max = 0;
        Message msg;
        @Override
        public void run() {
            max = Integer.parseInt(MainActivity.selectedMp3.getDuration());
            try {
                while(MainActivity.player != null && MainActivity.player.getIsPlaying()) {
                    if(!isTouching) {
                        seekBar.setProgress(MainActivity.player.getCurrentTime());
                    }
                    msg = handler.obtainMessage();
                    handler.sendMessage(msg);
                    if(MainActivity.player.getCurrentTime() >= max || breaker) {
                        breaker = false;
                        break;
                    }
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    private CasterThread casterThread;
    final Handler casterHandler = new Handler(){
        @SuppressLint("HandlerLeak")
        public void handleMessage(Message msg){
            time1.setText(format.getAudioTime(Long.toString(remoteMediaClient.getApproximateStreamPosition())));
        }
    };
    final Handler casterSupportHandler = new Handler(){
        @SuppressLint("HandlerLeak")
        public void handleMessage(Message msg){
            duration = (int) AudioActivity.remoteMediaClient.getApproximateStreamPosition();
            whileCheck = remoteMediaClient != null && (remoteMediaClient.isPlaying() || remoteMediaClient.isBuffering() || remoteMediaClient.isPaused());
            ifCheck = (remoteMediaClient.isBuffering() || remoteMediaClient.isPaused() );
        }
    };
    boolean whileCheck;
    boolean ifCheck;
    int duration;
    private void startCasterThread() {
        breaker = false;
        casterThread = new CasterThread();
        casterThread.start();
    }
    class CasterThread extends Thread {
        int max = 0;
        Message msg;
        @Override
        public void run() {
            max = Integer.parseInt(MainActivity.selectedMp3.getDuration());
            try {
                do {
                    msg = casterSupportHandler.obtainMessage();
                    casterSupportHandler.sendMessage(msg);
                    if(!ifCheck) {
                        if(!isTouching) {
                            seekBar.setProgress( duration );
                        }
                        msg = casterHandler.obtainMessage();
                        casterHandler.sendMessage(msg);
                    }
                    if( duration >= max || breaker ) {
                        breaker = false;
                        break;
                    }
                    Thread.sleep(1000);
                } while(MainActivity.player != null && whileCheck);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 타임 스레드 ed
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 시크바 세팅
    public void setSeekBar() {
        seekBar.setMax(Integer.parseInt(MainActivity.selectedMp3.getDuration()));
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) { }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {             // 잡고 있을 때
                isTouching = true;
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {              // 놓았을 때
                isTouching = false;
                MainActivity.player.setCurrentTime(seekBar.getProgress());
                time1.setText(format.getAudioTime(Integer.toString(seekBar.getProgress())));
                if(checkConnected()) {
                    MediaSeekOptions options = new MediaSeekOptions.Builder()
                            .setIsSeekToInfinite(false)
                            .setPosition(seekBar.getProgress())
                            .setResumeState(MediaSeekOptions.RESUME_STATE_UNCHANGED)
                            .build();
                    remoteMediaClient.seek(options);
                }
            }
        });
        seekBar.setProgress(MainActivity.player.getCurrentTime());
        if(checkConnected()) {
            seekBar.setProgress((int) remoteMediaClient.getApproximateStreamPosition());
        }
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 시크바 세팅 ed
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 백그라운드 이미지 세팅
    public void setImage() {
        img_background = findViewById(R.id.img_background);
        img_background.setScaleType(ImageView.ScaleType.CENTER_CROP);
        img_background.setImageBitmap(MainActivity.selectedMp3.getAlbum_art_image());
        img_front = findViewById(R.id.img_front);
        img_front.setImageBitmap(MainActivity.selectedMp3.getAlbum_art_image());
        if(MainActivity.selectedMp3.getAlbum_art_image() == null) {
            img_background.setImageResource(R.drawable.songcast_no_image_background);
            img_front.setImageResource(R.drawable.songcast_no_image);
        }
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 백그라운드 이미지 세팅 ed
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 텍스트 세팅
    public static final String START_TIME = "00:00";
    public void setTextView() {
        tv1 = findViewById(R.id.audio_tv1);
        tv2 = findViewById(R.id.audio_tv2);
        tv1.setText(MainActivity.selectedMp3.getTitle());
        tv2.setText(MainActivity.selectedMp3.getArtist());
        time1 = findViewById(R.id.audio_time1);
        time2 = findViewById(R.id.audio_time2);
        time1.setText(START_TIME);
        if(MainActivity.player != null) {
            time1.setText(format.getAudioTime(Integer.toString(MainActivity.player.getCurrentTime())));
        }
        time2.setText(format.getAudioTime(MainActivity.selectedMp3.getDuration()));
        if(checkConnected()) {
            time1.setText(format.getAudioTime(Long.toString(remoteMediaClient.getApproximateStreamPosition())));
        }
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 텍스트 세팅 ed
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 버튼 세팅
    @SuppressLint("ClickableViewAccessibility")
    public void setButton() {
        cycle = findViewById(R.id.btn_recycle);
        prev = findViewById(R.id.btn_prev);
        play = findViewById(R.id.btn_play);
        next = findViewById(R.id.btn_next);
        rnd = findViewById(R.id.btn_random);
        switch(MainActivity.select_recycle) {
            case MainActivity.RECYCLE_FALSE :                        // 한 바퀴
                cycle.setImageResource(R.drawable.btn_recycle_no);
                break;
            case MainActivity.RECYCLE_TRUE :                         // 무한
                cycle.setImageResource(R.drawable.btn_recycle_all);
                break;
            case MainActivity.RECYCLE_ONE :                          // 한곡 반복
                cycle.setImageResource(R.drawable.btn_recycle_one);
                break;
        }
        if(MainActivity.select_random) {
            rnd.setImageResource(R.drawable.btn_random_on);
        } else {
            rnd.setImageResource(R.drawable.btn_random);
        }
        if(checkConnected()) {
            if(remoteMediaClient.isPlaying()) {
                play.setImageResource(R.drawable.btn_pause);
            } else {
                play.setImageResource(R.drawable.btn_play);
            }
        } else {
            if(MainActivity.player.getIsPlaying()) {
                play.setImageResource(R.drawable.btn_pause);
            } else {
                play.setImageResource(R.drawable.btn_play);
            }
        }
        cycle.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                switch(MainActivity.select_recycle) {
                    case MainActivity.RECYCLE_FALSE :                        // 한 바퀴     =====>  무한
                        MainActivity.select_recycle = MainActivity.RECYCLE_ONE;
                        cycle.setImageResource(R.drawable.btn_recycle_one);
                        break;
                    case MainActivity.RECYCLE_TRUE :                         // 무한       =====>  한곡 반복
                        MainActivity.select_recycle = MainActivity.RECYCLE_FALSE;
                        cycle.setImageResource(R.drawable.btn_recycle_no);
                        break;
                    case MainActivity.RECYCLE_ONE :                          // 한곡 반복  =====>  한 바퀴
                        MainActivity.select_recycle = MainActivity.RECYCLE_TRUE;
                        cycle.setImageResource(R.drawable.btn_recycle_all);
                        break;
                }
            }
            return true;
        });
        prev.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                prev.setImageResource(R.drawable.btn_prev_pushed);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                audioPrevious();
            }
            return true;
        });
        play.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if(checkConnected()) {
                    if(MainActivity.player.getIsPlaying()) {
                        MainActivity.player.pause();
                    }
                    if(remoteMediaClient.isPlaying()) {
                        play.setImageResource(R.drawable.btn_pause_pushed);
                    } else {
                        play.setImageResource(R.drawable.btn_play_pushed);
                    }
                } else {
                    if(MainActivity.player.getIsPlaying()) {
                        play.setImageResource(R.drawable.btn_pause_pushed);
                    } else {
                        play.setImageResource(R.drawable.btn_play_pushed);
                    }
                }
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                if(checkConnected()) {
                    if(remoteMediaClient.isPlaying()) {
                        updatePlayButton(PAUSED);
                    } else {
                        updatePlayButton(PLAYING);
                    }
                } else {
                    if(MainActivity.player.getIsPlaying()) {
                        audioPause();
                    } else {
                        audioPlay();
                    }
                }
            }
            return true;
        });
        next.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                next.setImageResource(R.drawable.btn_next_pushed);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                audioNext();
            }
            return true;
        });
        rnd.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if(MainActivity.select_random) {
                    rnd.setImageResource(R.drawable.btn_random);
                    MainActivity.player.setRandomOFF();
                } else {
                    rnd.setImageResource(R.drawable.btn_random_on);
                    MainActivity.player.setRandomOn();
                }
            }
            return true;
        });
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 버튼 세팅 ed
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 리스너
    @Override
    public void onPlayItemChanged() {
        MainActivity.checkUpdateRemote = true;
        onResume();
    }
    @Override
    public void playlistFinished() {
        MainActivity.nowPlayList = null;
        MainActivity.selectedMp3 = null;
        onBackPressed();
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 리스너 ed
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 오디오 기능 PLAY
    private void audioPlay() {
        play.setImageResource(R.drawable.btn_pause);
        MainActivity.player.resume();
        startTimeThread();
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 오디오 기능 PLAY ed
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 오디오 기능 PAUSE
    public void audioPause() {
        play.setImageResource(R.drawable.btn_play);
        MainActivity.player.pause();
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 오디오 기능 PAUSE ed
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 오디오 기능 PREVIOUS
    public void audioPrevious() {
        prev.setImageResource(R.drawable.btn_prev);
        MainActivity.player.previous();
        time1.setText(START_TIME);
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 오디오 기능 PREVIOUS ed
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 오디오 기능 NEXT
    public void audioNext() {
        next.setImageResource(R.drawable.btn_next);
        MainActivity.player.next();
        MainActivity.checkUpdateRemote = false;
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 오디오 기능 NEXT ed
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- Default 오버라이드
    @Override
    public void onBackPressed() {
        MainActivity.player.setOnAudioPlayerControlListener(null);
        breaker = true;
        super.onBackPressed();
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- Default 오버라이드 ed
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// cast
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 캐스트
    private static final String TAG = "Audio_Activity";
    private MenuItem mediaRouteMenuItem;
    private CastContext mCastContext;
    private MainActivity.PlaybackState mPlaybackState;
    private MainActivity.PlaybackLocation mLocation;
    private SessionManagerListener<CastSession> mSessionManagerListener;
    public static CastSession mCastSession;
    public static RemoteMediaClient remoteMediaClient;
    private boolean checkConnected() {
        return remoteMediaClient != null && (mCastSession != null && (mCastSession.isConnected()|| mCastSession.isConnecting() ) );
    }
    private void setupCastListener() {
        mSessionManagerListener = new SessionManagerListener<CastSession>() {
            @Override
            public void onSessionStarting(CastSession castSession) {
            }
            @Override
            public void onSessionStarted(CastSession castSession, String s) {
                createCasterServer();
                MainActivity.checkUpdateRemote = true;
                onApplicationConnected(castSession);
            }
            @Override
            public void onSessionStartFailed(CastSession castSession, int i) {
                onApplicationDisconnected();
            }
            @Override
            public void onSessionEnding(CastSession castSession) {
            }
            @Override
            public void onSessionEnded(CastSession castSession, int i) {
                onApplicationDisconnected();
            }
            @Override
            public void onSessionResuming(CastSession castSession, String s) {
            }
            @Override
            public void onSessionResumed(CastSession castSession, boolean b) {
                onApplicationConnected(castSession);
            }
            @Override
            public void onSessionResumeFailed(CastSession castSession, int i) {
                onApplicationDisconnected();
            }
            @Override
            public void onSessionSuspended(CastSession castSession, int i) {
            }
        };
    }
    private void onApplicationDisconnected() {
        updatePlaybackLocation(LOCAL);
        updatePlayButton(IDLE);
        invalidateOptionsMenu();
        destroyCasterServer();
        MainActivity.player.setRemote(null);
    }
    private void onApplicationConnected(CastSession castSession) {
        updatePlaybackLocation(REMOTE);
        mCastSession = castSession;
        audioPause();
        play.setImageResource(R.drawable.btn_pause);
        if (null != MainActivity.selectedMp3) {
            if (MainActivity.player != null && MainActivity.checkUpdateRemote) {
                loadRemoteMedia(seekBar.getProgress(), true);
                MainActivity.checkUpdateRemote = false;
                updatePlayButton(PLAYING);
                return;
            } else {
                updatePlayButton(BUFFERING);
                updatePlaybackLocation(REMOTE);
            }
        }
        invalidateOptionsMenu();
    }
    private void updatePlaybackLocation(MainActivity.PlaybackLocation location) {
        mLocation = location;
        if(mLocation == LOCAL) {
            if(MainActivity.player.getIsPlaying()) {
                audioPlay();
            } else {
                audioPause();
            }
        } else if(mLocation == REMOTE) {
            MainActivity.player.pause();
        }
    }
    private void updatePlayButton(MainActivity.PlaybackState state) {
        mPlaybackState = state;
        switch (state) {
            case PLAYING:
                remoteMediaClient.play();
                play.setImageResource(R.drawable.btn_pause);
                startCasterThread();
                break;
            case PAUSED:
                remoteMediaClient.pause();
            case IDLE:
                if(mLocation == REMOTE) {
                    breaker = true;
                    play.setImageResource(R.drawable.btn_play);
                }
                break;
            case BUFFERING:
                play.setImageResource(R.drawable.btn_pause);
                breaker = true;
                break;
            default:
                break;
        }
    }
    private void loadRemoteMedia(int position, boolean autoPlay) {
        if (mCastSession == null) {
            return;
        }
        remoteMediaClient = mCastSession.getRemoteMediaClient();
        if (remoteMediaClient == null) {
            return;
        }
        Toast.makeText(getApplicationContext(), CASTER_APP_CONNECTED, Toast.LENGTH_LONG).show();
        remoteMediaClient.load(new MediaLoadRequestData.Builder()
                .setMediaInfo(buildMediaInfo())
                .setAutoplay(autoPlay)
                .setCurrentTime(position)
                .build());
        remoteMediaClient.addListener(new RemoteMediaClient.Listener() {
            @Override
            public void onStatusUpdated() {
                if(remoteMediaClient.getMediaStatus() != null) {
                    if (remoteMediaClient.getMediaStatus().getPlayerState() ==  MediaStatus.PLAYER_STATE_IDLE
                            && remoteMediaClient.getMediaStatus().getIdleReason() ==  MediaStatus.IDLE_REASON_FINISHED) {
                        int listNum = MainActivity.player.getListNum();
                        List<Mp3Data> itemList = MainActivity.player.getItemList();
                        switch(MainActivity.select_recycle) {
                            case MainActivity.RECYCLE_FALSE :       // 전체 한 번
                                if(listNum+1 < itemList.size()) {
                                    audioNext();
                                } else {
                                    remoteMediaClient.stop();
                                }
                                break;
                            case MainActivity.RECYCLE_TRUE :        // 전체 반복
                                audioNext();
                                break;
                            case MainActivity.RECYCLE_ONE :         // 한 곡 반복
                                MainActivity.player.rePlay();
                                break;
                        }
                    }
                }
            }
            @Override
            public void onMetadataUpdated() {
            }
            @Override
            public void onQueueStatusUpdated() {
            }
            @Override
            public void onPreloadStatusUpdated() {
            }
            @Override
            public void onSendingRemoteMediaRequest() {
            }
            @Override
            public void onAdBreakStatusUpdated() {
            }
        });
        mPlaybackState = PLAYING;
        MainActivity.player.setRemote(remoteMediaClient);
    }
    private String ip;
    private int port = 8080;
    private MediaInfo buildMediaInfo() {
        ip = CasterServer.getLocalIpAddress();
        //
        MediaMetadata audioMetadata = new MediaMetadata(MediaMetadata.MEDIA_TYPE_MUSIC_TRACK);
        audioMetadata.putString(MediaMetadata.KEY_TITLE, MainActivity.selectedMp3.getTitle());
        audioMetadata.putString(MediaMetadata.KEY_ARTIST, MainActivity.selectedMp3.getArtist());
        audioMetadata.putString(MediaMetadata.KEY_ALBUM_TITLE, MainActivity.selectedMp3.getAlbum());
        audioMetadata.addImage(new WebImage(Uri.parse("http://"+ip+":"+port+"/"+FILE_TYPE_IMAGE)));
        MediaInfo info = new MediaInfo.Builder("http://"+ip+":"+port+"/"+FILE_TYPE_AUDIO)
                .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
                .setContentType("audio/*")
                .setMetadata(audioMetadata)
                .setStreamDuration(Integer.parseInt(MainActivity.selectedMp3.getDuration()))
                .build();
        return info;
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 캐스트 ed
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 캐스터 서버
    private void createCasterServer() {
        mCasterServer = new CasterServer();
        try {
            mCasterServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mCasterServer.setTempFileManagerFactory(new NanoHTTPD.TempFileManagerFactory() {
            @Override
            public NanoHTTPD.TempFileManager create() {
                return null;
            }
        });
    }
    private void destroyCasterServer() {
        if(mCasterServer != null) {
            mCasterServer.stop();
        }
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 캐스터 서버 ed
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// cast
}



















