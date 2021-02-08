package com.example.project_songcast.Mp3Class;

import android.media.MediaPlayer;

import com.example.project_songcast.ListenerInterface.OnAudioPlayerControlListener;
import com.google.android.gms.cast.framework.media.RemoteMediaClient;

import java.util.List;

public interface AudioPlayerInterface {
    public boolean prepared();
    public void setBroadcast();
    public void play();
    public void pause();
    public void resume();
    public void stop();
    public void previous();
    public void rePlay();
    public void next();
    public void onSelectedMp3Change();
    public void setCurrentTime(int currentTime);
    public int getCurrentTime();
    public void setRandomOn();
    public void setRandomOFF();
    public Mp3Data getItem();
    public AudioPlayer setItem(Mp3Data item);
    public List<Mp3Data> getItemList();
    public AudioPlayer setItemList(List<Mp3Data> itemList);
    public boolean getIsPlaying();
    public void setOnAudioPlayerControlListener(OnAudioPlayerControlListener listener);
    public void setOnAudioPlayerControlListener_mini(OnAudioPlayerControlListener listener);
    public void setOnAudioPlayerControlListener_main(OnAudioPlayerControlListener listener);
    public int getListNum();
    public void setRemote(RemoteMediaClient remoteMediaClient);
}
