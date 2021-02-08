package com.example.project_songcast;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.project_songcast.ListenerInterface.OnRefreshListener;
import com.example.project_songcast.Mp3Class.Mp3Data;
import com.example.project_songcast.ListenerInterface.OnDialogSelectListener;
import com.example.project_songcast.Mp3Class.Playlist;
import com.example.project_songcast.Mp3Class.PlaylistAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.example.project_songcast.Mp3Class.PlaylistAdapter.PLAYLIST_CUSTOM;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlaylistFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlaylistFragment extends Fragment implements OnDialogSelectListener, OnRefreshListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PlaylistFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PlaylistFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PlaylistFragment newInstance(String param1, String param2) {
        PlaylistFragment fragment = new PlaylistFragment();
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
    public final static String YES_SIGN = "확인";
    public final static String NO_SIGN = "취소";
    public final static String ERROR_ALREADY = "이미 있는 플레이리스트 입니다";
    public boolean insertMode;
    private ViewGroup rootView;
    private Toolbar toolbar;
    private ImageView iv;
    private RecyclerView recyclerView;
    private PlaylistAdapter playlistAdapter;
    private LinearLayoutManager manager;
    private View dialog;
    private AlertDialog.Builder ad;
    private EditText et;
    private TextView tv;
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 생성
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_playlist, container, false);
//      ---------------------------------------------------------------------------------------------------------------- 툴바 세팅
        iv = rootView.findViewById(R.id.playlist_custom_toolbar_iv);
        toolbar = rootView.findViewById(R.id.playlist_custom_toolbar);
        toolbar.setTitle(MainActivity.PLAYLIST_BAR_TITLE);
        toolbar.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                toolbar.setTitleTextColor(Color.parseColor("#6A1FF1"));
                iv.setImageResource(R.drawable.ic_add_new_pushed);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                toolbar.setTitleTextColor(Color.parseColor("#FF000000"));
                iv.setImageResource(R.drawable.ic_add_new);
                createNewPlaylist();
            }
            return true;
        });
//      ---------------------------------------------------------------------------------------------------------------- 툴바 세팅 ed
        update();
        MainActivity.mp3Dialog.setOnDialogSelectListener(this);
        return rootView;
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 생성 ed
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 다이얼로그
    public void createNewPlaylist() {
        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dialog = inflater.inflate(R.layout.dialog_create_playlist, null);
        ad = new AlertDialog.Builder(this.getContext());
        ad.setTitle(MainActivity.PLAYLIST_BAR_TITLE);
        et = dialog.findViewById(R.id.create_playlist_dialog_et);
        ad.setView(dialog);
        ad.setPositiveButton(YES_SIGN, null);
        ad.setNegativeButton(NO_SIGN, (dia, which) -> {
            dia.dismiss();
        });
        AlertDialog alert = ad.create();
        alert.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dlg) {
                Button btn = ((AlertDialog)dlg).getButton(AlertDialog.BUTTON_POSITIVE);
                btn.setOnClickListener(v -> {
                    if(MainActivity.customPlayList.containsKey(et.getText().toString())) {
                        tv = dialog.findViewById(R.id.create_playlist_dialog_error);
                        tv.setText(ERROR_ALREADY);
                    } else {
                        MainActivity.customPlayList.put(et.getText().toString(), new Playlist().setName(et.getText().toString()).setPlaylist_data(new ArrayList<>()));
                        playlistAdapter.addItem(MainActivity.customPlayList.get(et.getText().toString()));
                        playlistAdapter.notifyItemInserted(MainActivity.customPlayList.size()-1);
                        recyclerView.setAdapter(playlistAdapter);
                        dlg.dismiss();
                    }
                });
            }
        });
        alert.show();
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 다이얼로그 ed
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 리사이클러 뷰 세팅 & 새로고침 ed
    @Override
    public void update() {
        playlistAdapter = new PlaylistAdapter();
        playlistAdapter.setListNum(PLAYLIST_CUSTOM);
        playlistAdapter.setInsertMode(insertMode);
        playlistAdapter.setOnDialogSelectListener(this);
        for(Playlist item : MainActivity.customPlayList.values()) {
            playlistAdapter.addItem(item);
        }
        recyclerView = rootView.findViewById(R.id.playlist_custom_view);
        manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(playlistAdapter);
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 리사이클러 뷰 세팅 & 새로고침 ed
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 리사이클러 아이템 제거 ed
    @Override
    public void remove(Playlist item) {
        MainActivity.customPlayList.remove(item.getName());
        playlistAdapter.removeItem(item);
        update();
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 리사이클러 아이템 제거 ed
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 플레이리스트 내용물 추가
    @Override
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
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 플레이리스트 아이템 추가 모드
    public PlaylistFragment setInsertMode() {
        insertMode = true;
        return this;
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 플레이리스트 아이템 추가 모드 ed
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 리스너
    @Override
    public void onRefresh() {
        onResume();
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 리스너 ed
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- Default 오버라이드
    @Override
    public void onResume() {
        update();
        MainActivity.setOnRefreshListener(this);
        super.onResume();
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- Default 오버라이드 ed
}















