package com.example.project_songcast.ListenerInterface;

import com.example.project_songcast.Mp3Class.Playlist;

public interface OnDialogSelectListener {
    public void setPlaylist(Playlist item);
    public void remove(Playlist item);
    public void update();
}
