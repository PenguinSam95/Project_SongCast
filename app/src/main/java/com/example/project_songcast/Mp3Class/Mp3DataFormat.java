package com.example.project_songcast.Mp3Class;

import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.project_songcast.MainActivity;
import com.example.project_songcast.R;

public class Mp3DataFormat {
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 시간 세팅
    public String getAudioTime(String duration) {
        return getAudioTimeTask(duration, false);
    }
    private String getAudioTimeTask(String duration, boolean maxTime) {
        String time = "";
        String minStr = null;
        String secStr = null;
        int hor = 0;
        int current = Integer.parseInt(duration) / 1000;
        int min = current / 60;
        int sec = current % 60;
        if(min >= 60) {
            hor = min / 60;
            min %= 60;
            time = hor+":";
        }
        if(min < 10) {
            minStr = "0"+min;
        } else {
            minStr = Integer.toString(min);
        }
        if(maxTime) {
            if(current * 1000 < Integer.parseInt(duration)) {
                sec++;
            }
        }
        if(sec < 10) {
            secStr = "0"+sec;
        } else {
            secStr = Integer.toString(sec);
        }
        time += minStr +":"+ secStr;
        return time;
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 시간 세팅 ed
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 용량 세팅
    public String getRealSize(String size) {
        double size_int = Double.parseDouble(size);
        double gb = 0;
        double mb = 0;
        String size_str = null;
        mb = size_int / (1024 * 1024);  // 메가
        if(mb >= 800) {      // 기가 세팅
            gb = mb /= 1024;
            size_str = String.format("%.1f", gb)+" GB";
            return size_str;
        }
        size_str = String.format("%.1f", mb)+" MB";
        return size_str;
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 용량 세팅 ed
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 앨범 이미지 세팅
    public void setAlbumImage(ImageView iv, Bitmap albumImage) {
        if(albumImage != null) {
            iv.setImageBitmap(albumImage);
        } else {
            iv.setImageResource(R.drawable.songcast_no_image_mini);
        }
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 앨범 이미지 세팅 ed
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 텍스트 세팅
    public void setTextViewsText(TextView tv, Object text, boolean audio_count) {
        final String head = "총 ";
        final String tail = "곡";
        if(text != null) {
            if(audio_count) {
                tv.setText(head + text.toString() + tail);
            } else {
                tv.setText(text.toString());
            }
        } else {
            tv.setText(MainActivity.NO_DATA);
        }
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 텍스트 세팅 ed
}


















