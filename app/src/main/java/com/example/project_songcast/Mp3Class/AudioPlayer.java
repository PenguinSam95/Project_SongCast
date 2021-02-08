package com.example.project_songcast.Mp3Class;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.PowerManager;
import android.os.RemoteException;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.project_songcast.AudioActivity;
import com.example.project_songcast.ListenerInterface.OnAudioPlayerControlListener;
import com.example.project_songcast.MainActivity;
import com.example.project_songcast.R;
import com.google.android.gms.cast.MediaSeekOptions;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.media.RemoteMediaClient;

import java.io.FileDescriptor;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AudioPlayer extends Service implements AudioPlayerInterface {
    private static final String ACTION_PLAY = "com.example.action.PLAY";
    private static int pauseTime;
    private OnAudioPlayerControlListener front;
    private OnAudioPlayerControlListener mini;
    private OnAudioPlayerControlListener main;
    private MediaPlayer player;
    private Mp3Data item;
    private List<Mp3Data> itemList;
    private int listNum;
    private Intent broadcast;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new ServiceBinder();
    }
    public class ServiceBinder extends Binder {
        public AudioPlayer getService() {
            return AudioPlayer.this;
        }
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 생성 ed
    @Override
    public void onCreate() {
        super.onCreate();
        mCastContext = CastContext.getSharedInstance(this);
        mCastSession = mCastContext.getSessionManager().getCurrentCastSession();
        newPlayer();
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 생성 ed
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 서비스 생성
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        onSelectedMp3Change();
        setBroadcast();
        play();
        return START_STICKY;
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 서비스 생성 ed
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 브로드캐스트
    @Override
    public void setBroadcast() {
        broadcast = new Intent();
        broadcast.setAction("android.mybroadcast");
        sendBroadcast(broadcast);
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 브로드캐스트 ed
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 오디오 연결
    @Override
    public boolean prepared() {
        if(item == null) {
            return false;
        }
        try {
            player.setDataSource(getApplicationContext(), item.getUri());
            player.prepare();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 오디오 연결 ed
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 재생
    @Override
    public void play() {
        if(player == null) {
            newPlayer();
        }
        if(prepared()) {
            if(remoteMediaClient != null && mCastSession != null && (mCastSession.isConnected()|| mCastSession.isConnecting()) ) {
                player.pause();
            } else {
                player.start();
            }
        }
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 재생 ed
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 오디오 세팅
    public void newPlayer() {
        if(player == null) {
            player = new MediaPlayer();
            player.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
                @Override
                public void onSeekComplete(MediaPlayer mp) {
                    player.start();
                }
            });
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    switch(MainActivity.select_recycle) {
                        case MainActivity.RECYCLE_FALSE :       // 전체 한 번
                            if(listNum+1 < itemList.size()) {
                                next();
                            } else {
                                stop();
                                onDestroy();
                                if(front != null) {
                                    front.playlistFinished();
                                }
                                if(main != null) {
                                    main.playlistFinished();
                                }
                            }
                            break;
                        case MainActivity.RECYCLE_TRUE :        // 전체 반복
                            next();
                            break;
                        case MainActivity.RECYCLE_ONE :         // 한 곡 반복
                            onItemChanged(listNum);
                            break;
                    }
                }
            });
            player.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        }
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 오디오 세팅 ed
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 일시정지
    @Override
    public void pause() {
        if(player != null) {
            pauseTime = player.getCurrentPosition();
            player.pause();
        }
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 일시정지 ed
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 재시작
    @Override
    public void resume() {
        if(player != null && !player.isPlaying()) {
            player.start();
            player.seekTo(pauseTime);
        }
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 재시작 ed
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 정지
    @Override
    public void stop() {
        if(player != null) {
            player.stop();
            player.release();
            player = null;
        }
        stopService(MainActivity.background_audio);
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 정지 ed
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 노래 이동
    private final int returnAudio = 8;
    @Override
    public void next() {
        onItemChanged(++listNum);
    }
    @Override
    public void previous() {
        if(player.isPlaying() && returnAudio < player.getCurrentPosition() / 1000) {
            setCurrentTime(0);
        } else {
            onItemChanged(--listNum);
        }
    }
    @Override
    public void rePlay() {
        onItemChanged(listNum);
    }
    public void onItemChanged(int newListNum) {
        listNum = newListNum;
        if(itemList.size() == 1 || itemList.size() <= listNum) {
            listNum = 0;
        } else if(listNum < 0) {
            listNum = itemList.size()-1;
        }
        setItem(itemList.get(listNum));
        stop();
        play();
        if(front != null) {
            front.onPlayItemChanged();
        }
        if(mini != null) {
            mini.onPlayItemChanged();
        }
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 노래 이동 ed
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 오디오 변경
    @Override
    public void onSelectedMp3Change() {
        setItem(MainActivity.selectedMp3);
        setItemList(MainActivity.nowPlayList);
        if(itemList != null) {
            for(int i=0; i<itemList.size(); i++) {
                if(itemList.get(i) == item) {
                    listNum = i;
                    break;
                }
            }
        }
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 오디오 변경 ed
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 시크바 설정
    @Override
    public void setCurrentTime(int currentTime) {
        if(player.isPlaying()) {
            player.seekTo(currentTime);
        } else {
            pauseTime = currentTime;
        }
    }
    @Override
    public int getCurrentTime() {
        if(player != null) {
            return player.getCurrentPosition();
        }
        return 0;
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 시크바 설정 ed
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 랜덤 설정
    @Override
    public void setRandomOn() {
        MainActivity.select_random = MainActivity.RANDOM_PLAYLIST_ON;
        List<Mp3Data> randomList = new ArrayList<>();
        List<Integer> randomInt = new ArrayList<>();
        randomInt.add(listNum);
        Random random = new Random();
        int rnd = 0;
        for(int i=0; i<MainActivity.nowPlayList.size()-1; i++) {
            rnd = random.nextInt(MainActivity.nowPlayList.size());
            if(randomInt.contains(rnd)) {
                i--;
            } else {
                randomInt.add(rnd);
            }
        }
        for(int i : randomInt) {
            randomList.add(MainActivity.nowPlayList.get(i));
        }
        setItemList(randomList);
        listNum = 0;
    }
    @Override
    public void setRandomOFF() {
        MainActivity.select_random = MainActivity.RANDOM_PLAYLIST_OFF;
        onSelectedMp3Change();
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 랜덤 설정 ed
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- OTHERS
    @Override
    public Mp3Data getItem() {
        return item;
    }
    @Override
    public AudioPlayer setItem(Mp3Data item) {
        this.item = item;
        MainActivity.selectedMp3 = item;
        return this;
    }
    @Override
    public List<Mp3Data> getItemList() {
        return itemList;
    }
    @Override
    public AudioPlayer setItemList(List<Mp3Data> itemList) {
        this.itemList = itemList;
        return this;
    }
    public boolean getIsPlaying() {
        if(player == null) {
            return false;
        }
        return player.isPlaying();
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- OTHERS ed
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 리스너
    @Override
    public void setOnAudioPlayerControlListener(OnAudioPlayerControlListener listener) {
        front = listener;
    }
    @Override
    public void setOnAudioPlayerControlListener_main(OnAudioPlayerControlListener listener) {
        main = listener;
    }
    @Override
    public void setOnAudioPlayerControlListener_mini(OnAudioPlayerControlListener listener) {
        mini = listener;
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 리스너 ed
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// cast
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 캐스트
    private CastContext mCastContext;
    private CastSession mCastSession;
    private RemoteMediaClient remoteMediaClient;
    @Override
    public int getListNum() {
        return listNum;
    }
    @Override
    public void setRemote(RemoteMediaClient remoteMediaClient) {
        this.remoteMediaClient = remoteMediaClient;
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 캐스트 ed
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// cast
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- Default 오버라이드
    @Override
    public void onDestroy() {
        stop();
        super.onDestroy();
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- Default 오버라이드 ed
}


















