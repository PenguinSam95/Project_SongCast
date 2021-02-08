package com.example.project_songcast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.project_songcast.Mp3Class.Mp3Data;
import com.example.project_songcast.Mp3Class.Mp3Query;
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

import static com.example.project_songcast.MainActivity.ALBUM_ART_URI;
import static com.example.project_songcast.MainActivity.REQUEST_CODE;
import static com.example.project_songcast.MainActivity.mp3DataList;

public class IntroActivity extends AppCompatActivity implements AutoPermissionsListener {
    private Handler handler = new Handler();
    private ProgressBar pb1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
//      ---------------------------------------------------------------------------------------------------------------- 프로그래스 핸들러
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                pb1 = findViewById(R.id.progressBar2);
                pb1.setVisibility(View.VISIBLE);
                setAutoPermissions();
            }
        }, 1000);
//      ---------------------------------------------------------------------------------------------------------------- 프로그래스 핸들러 ed
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- Auto Permission
    public void setAutoPermissions() {
        AutoPermissions.Companion.loadAllPermissions(this, REQUEST_CODE);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        AutoPermissions.Companion.parsePermissions(this, requestCode, permissions, this);
    }
    public void onDenied(int i, String[] strings) {
        class DefaultThread extends Thread {
            @Override
            public void run() {
                Mp3Query query = new Mp3Query(getApplicationContext());
                if(MainActivity.mp3DataList == null) {
                    MainActivity.mp3DataList = query.getMusicList();
                }
                if(MainActivity.artistList == null) {
                    MainActivity.artistList = query.getArtistList();
                }
                if(MainActivity.albumList == null) {
                    MainActivity.albumList = query.getAlbumList();
                }
                if(MainActivity.folderList == null) {
                    MainActivity.folderList = query.getFolderList();
                }
                Intent main = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(main);
                finish();
            }
        }
        new DefaultThread().start();
    }
    public void onGranted(int i, String[] strings) {
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- Auto Permission ed
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- Default 오버라이드
    @Override
    protected void onPause() {
        super.onPause();
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- Default 오버라이드 ed
}












