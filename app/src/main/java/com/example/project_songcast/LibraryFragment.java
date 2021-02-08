package com.example.project_songcast;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.project_songcast.Mp3Class.PlaylistInsert;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.project_songcast.MainActivity.select_library;
import static com.example.project_songcast.Mp3Class.PlaylistInsert.START_ALBUM;
import static com.example.project_songcast.Mp3Class.PlaylistInsert.START_ALL;
import static com.example.project_songcast.Mp3Class.PlaylistInsert.START_ARTIST;
import static com.example.project_songcast.Mp3Class.PlaylistInsert.START_FOLDER;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LibraryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LibraryFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LibraryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LibraryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LibraryFragment newInstance(String param1, String param2) {
        LibraryFragment fragment = new LibraryFragment();
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
    private FloatingActionButton fab;
    private TabLayout tabLayout;
    private Fragment library_display;
    private Library_All_Fragment library_all;
    private Library_Artist_Fragment library_artist;
    private Library_Album_Fragment library_album;
    private Library_Folder_Fragment library_folder;
    public static final String LIBRARY_ALL = "전체";
    public static final String LIBRARY_ARTIST = "아티스트";
    public static final String LIBRARY_ALBUM = "앨범";
    public static final String LIBRARY_FOLDER = "폴더";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_library, container, false);
//      ---------------------------------------------------------------------------------------------------------------- Fragment 세팅
        library_all     = new Library_All_Fragment();
        library_artist  = new Library_Artist_Fragment();
        library_album   = new Library_Album_Fragment();
        library_folder  = new Library_Folder_Fragment();
//      ---------------------------------------------------------------------------------------------------------------- Fragment 세팅 ed
//      ---------------------------------------------------------------------------------------------------------------- TabLayout 세팅
        tabLayout = rootView.findViewById(R.id.library_tab);
        tabLayout.addTab(tabLayout.newTab().setText(LIBRARY_ALL));
        tabLayout.addTab(tabLayout.newTab().setText(LIBRARY_ARTIST));
        tabLayout.addTab(tabLayout.newTab().setText(LIBRARY_ALBUM));
        tabLayout.addTab(tabLayout.newTab().setText(LIBRARY_FOLDER));
        setLibraryDisplay(select_library);
        tabLayout.getTabAt(select_library).select();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setLibraryDisplay(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }
            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });
//      ---------------------------------------------------------------------------------------------------------------- TabLayout 세팅 ed
        setFab(rootView);
        return rootView;
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- Fab 세팅
    @SuppressLint("ClickableViewAccessibility")
    public void setFab(View rootView) {
        fab = rootView.findViewById(R.id.library_fab);
        fab.setOnTouchListener((v, event) -> {
            if(event.getAction() == MotionEvent.ACTION_UP) {
                switch(select_library) {
                    case MainActivity.LIBRARY_ALL :
                        PlaylistInsert.startPosition = START_ALL;
                        break;
                    case MainActivity.LIBRARY_ARTIST :
                        PlaylistInsert.startPosition = START_ARTIST;
                        break;
                    case MainActivity.LIBRARY_ALBUM :
                        PlaylistInsert.startPosition = START_ALBUM;
                        break;
                    case MainActivity.LIBRARY_FOLDER :
                        PlaylistInsert.startPosition = START_FOLDER;
                        break;
                }
                MainActivity.insert.start(null, null);
            }
            return true;
        });
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- Fab 세팅 ed
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 프래그먼트 세팅
    public void setLibraryDisplay(int tabPosition) {
        switch(tabPosition) {
            case 0 :
                library_display = library_all;
                break;
            case 1 :
                library_display = library_artist;
                break;
            case 2 :
                library_display = library_album;
                break;
            case 3 :
                library_display = library_folder;
                break;
            default:
                library_display = library_all;
                tabPosition = 0;
                break;
        }
        select_library = tabPosition;
        getFragmentManager().beginTransaction().replace(R.id.library_display, library_display).commit();
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 프래그먼트 세팅 ed
}











