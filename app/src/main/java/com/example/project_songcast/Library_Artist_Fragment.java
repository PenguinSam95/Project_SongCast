package com.example.project_songcast;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.project_songcast.ListenerInterface.OnRefreshListener;
import com.example.project_songcast.Mp3Class.Mp3Adapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Library_Artist_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Library_Artist_Fragment extends Fragment implements OnRefreshListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Library_Artist_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Library_Artist_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Library_Artist_Fragment newInstance(String param1, String param2) {
        Library_Artist_Fragment fragment = new Library_Artist_Fragment();
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
    private RecyclerView recyclerView;
    private LinearLayoutManager manager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_library_artist, container, false);
        MainActivity.mp3Adapter = new Mp3Adapter();
        if(MainActivity.artistList != null) {
            for(String id : MainActivity.artistList.keySet()) {
                MainActivity.mp3Adapter.addItem(MainActivity.artistList.get(id));
            }
        }
        recyclerView = rootView.findViewById(R.id.library_artist_view);
        manager = new LinearLayoutManager(rootView.getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(MainActivity.mp3Adapter);
        return rootView;
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 리스너
    @Override
    public void onRefresh() {
        onResume();
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 리스너 ed
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- Default 오버라이드
    @Override
    public void onResume() {
        MainActivity.setOnRefreshListener(this);
        super.onResume();
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- Default 오버라이드 ed
}
















