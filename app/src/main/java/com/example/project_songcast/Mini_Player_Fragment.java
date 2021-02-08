package com.example.project_songcast;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project_songcast.ListenerInterface.OnAudioPlayerControlListener;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.media.RemoteMediaClient;
import com.google.android.gms.cast.framework.media.widget.CastSeekBar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Mini_Player_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Mini_Player_Fragment extends Fragment implements OnAudioPlayerControlListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Mini_Player_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Mini_Player_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Mini_Player_Fragment newInstance(String param1, String param2) {
        Mini_Player_Fragment fragment = new Mini_Player_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    private View rootView;
    private LinearLayout card;
    private ImageView iv1, play_btn;
    private TextView tv1, tv2;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_mini_player, container, false);
        card = rootView.findViewById(R.id.mini_player_card);
        iv1 = rootView.findViewById(R.id.mini_player_iv);
        play_btn = rootView.findViewById(R.id.mini_player_ib);
        tv1 = rootView.findViewById(R.id.mini_player_tv1);
        tv2 = rootView.findViewById(R.id.mini_player_tv2);
        return rootView;
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- Default 오버라이드
    @Override
    public void onResume() {
        if(MainActivity.selectedMp3 != null) {
            MainActivity.player.setOnAudioPlayerControlListener_mini(this);
            setPlayData();
        }
        super.onResume();
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- Default 오버라이드 ed
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 플레이어 세팅
    private CastSession mCastSession;
    private RemoteMediaClient remoteMediaClient;
    private MainActivity.PlaybackState mPlaybackState;
    @SuppressLint("ClickableViewAccessibility")
    protected void setPlayData() {
        card.setOnClickListener(v -> {
            MainActivity.setBackgroundService(getContext(), false, true);
            MainActivity.checkUpdateRemote = false;
        });
        tv1.setText(MainActivity.selectedMp3.getTitle());
        tv2.setText(MainActivity.selectedMp3.getArtist());
        iv1.setImageBitmap(MainActivity.selectedMp3.getAlbum_art_image());
        if(MainActivity.selectedMp3.getAlbum_art_image() == null) {
            iv1.setImageResource(R.drawable.songcast_no_image);
        }
        play_btn.setImageResource(R.drawable.btn_play);
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// cast
        mCastSession = AudioActivity.mCastSession;
        remoteMediaClient = AudioActivity.remoteMediaClient;
        boolean connection = remoteMediaClient != null && (mCastSession != null && mCastSession.isConnected());
        if(connection || MainActivity.player != null) {
            if(connection) {
                if(AudioActivity.remoteMediaClient.isPlaying()) {
                    play_btn.setImageResource(R.drawable.btn_pause);
                } else {
                    play_btn.setImageResource(R.drawable.btn_play);
                }
            } else {
                if(MainActivity.player != null) {
                    if(MainActivity.player.getIsPlaying()) {
                        play_btn.setImageResource(R.drawable.btn_pause);
                    } else {
                        play_btn.setImageResource(R.drawable.btn_play);
                    }
                }
            }
            play_btn.setOnTouchListener((v, event) -> {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if(remoteMediaClient != null && (mCastSession != null && mCastSession.isConnected()) ) {
                        if(MainActivity.player.getIsPlaying()) {
                            MainActivity.player.pause();
                        }
                        if(remoteMediaClient.isPlaying()) {
                            play_btn.setImageResource(R.drawable.btn_pause_pushed);
                        } else {
                            play_btn.setImageResource(R.drawable.btn_play_pushed);
                        }
                    } else {
                        if(MainActivity.player.getIsPlaying()) {
                            play_btn.setImageResource(R.drawable.btn_pause_pushed);
                        } else {
                            play_btn.setImageResource(R.drawable.btn_play_pushed);
                        }
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    if(remoteMediaClient != null && (mCastSession != null && mCastSession.isConnected()) ) {
                        if(remoteMediaClient.isPlaying()) {
                            remoteMediaClient.pause();
                            play_btn.setImageResource(R.drawable.btn_play);
                        } else {
                            remoteMediaClient.play();
                            play_btn.setImageResource(R.drawable.btn_pause);
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
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// cast
        }
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 플레이어 세팅 ed
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 오디오 기능 PLAY
    private void audioPlay() {
        play_btn.setImageResource(R.drawable.btn_pause);
        MainActivity.player.resume();
        mPlaybackState = MainActivity.PlaybackState.PLAYING;
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 오디오 기능 PLAY ed
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 오디오 기능 PAUSE
    public void audioPause() {
        play_btn.setImageResource(R.drawable.btn_play);
        MainActivity.player.pause();
        mPlaybackState = MainActivity.PlaybackState.PAUSED;
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 오디오 기능 PAUSE ed
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 리스너
    @Override
    public void onPlayItemChanged() {
        onResume();
    }
    @Override
    public void playlistFinished() {
        MainActivity.player.setOnAudioPlayerControlListener_mini(null);
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 리스너 ed
}























