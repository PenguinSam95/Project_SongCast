package com.example.project_songcast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project_songcast.Mp3Class.Mp3Data;
import com.example.project_songcast.Mp3Class.Mp3DataFormat;

import static com.example.project_songcast.MainActivity.NO_DATA;

public class InformationActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ActionBar actionBar;
    private TextView title_1, title_2, title_3, title_4, title_5, title_6, title_7;
    private TextView data_1, data_2, data_3, data_4, data_5, data_6, data_7;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infomation);
//      ---------------------------------------------------------------------------------------------------------------- 툴바 세팅
        toolbar = findViewById(R.id.info_toolbar);
        toolbar.setTitle(MainActivity.AUDIO_INFO);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
//      ---------------------------------------------------------------------------------------------------------------- 툴바 세팅 ed
        setData();
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 텍스트 세팅
    public void setData() {
        Mp3DataFormat format = new Mp3DataFormat();
        title_1 = findViewById(R.id.info_title_1);
        title_2 = findViewById(R.id.info_title_2);
        title_3 = findViewById(R.id.info_title_3);
        title_4 = findViewById(R.id.info_title_4);
        title_5 = findViewById(R.id.info_title_5);
        title_6 = findViewById(R.id.info_title_6);
        title_7 = findViewById(R.id.info_title_7);
        //
        data_1 = findViewById(R.id.info_data_1);
        data_2 = findViewById(R.id.info_data_2);
        data_3 = findViewById(R.id.info_data_3);
        data_4 = findViewById(R.id.info_data_4);
        data_5 = findViewById(R.id.info_data_5);
        data_6 = findViewById(R.id.info_data_6);
        data_7 = findViewById(R.id.info_data_7);
        //
        Mp3Data audio = null;
        Intent info = getIntent();
        String id = info.getStringExtra("item");
        String ids = null;
        for(Mp3Data mp3Data : MainActivity.mp3DataList) {
            ids = mp3Data.getName()+"%"+mp3Data.getPath();
            if(ids.equals(id)) {
                audio = mp3Data;
                break;
            }
        }
        title_1.setText("파일명");
        getUnderLine(data_1, audio.getName());
        title_2.setText("타이틀");
        getUnderLine(data_2, audio.getTitle());
        title_3.setText("앨범");
        getUnderLine(data_3, audio.getAlbum());
        title_4.setText("아티스트");
        getUnderLine(data_4, audio.getArtist());
        title_5.setText("재생시간");
        getUnderLine(data_5, format.getAudioTime(audio.getDuration()));
        title_6.setText("위치");
        getUnderLine(data_6, audio.getPath());
        title_7.setText("파일크기");
        getUnderLine(data_7, format.getRealSize(audio.getSize()));
    }
    private void getUnderLine(TextView tv, String data) {
        if(data != null) {
            tv.setText(Html.fromHtml("<u>" + data + "</u>"));
        } else {
            tv.setText(Html.fromHtml("<u>" + NO_DATA + "</u>"));
        }
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 텍스트 세팅 ed
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 툴바 메뉴 생성
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.no_menu, menu);
        return true;
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 툴바 메뉴 생성 ed
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 툴바 메뉴 버튼 클릭
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 툴바 메뉴 버튼 클릭 ed
}























