package com.example.project_songcast.ListenerInterface;

import com.example.project_songcast.Mp3Class.AudioPlayerInterface;

public interface OnAudioPlayerControlListener {
    public void onPlayItemChanged();
    public void playlistFinished();
}
