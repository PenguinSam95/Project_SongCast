package com.example.project_songcast.Caster;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.support.v4.media.MediaMetadataCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.project_songcast.MainActivity;
import com.example.project_songcast.Mp3Class.Mp3Data;
import com.example.project_songcast.R;
import com.google.android.gms.common.images.WebImage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD;

import static com.example.project_songcast.MainActivity.ALBUM_ART_URI;
import static com.example.project_songcast.MainActivity.FILE_TYPE_AUDIO;
import static com.example.project_songcast.MainActivity.FILE_TYPE_IMAGE;

public class CasterServer extends NanoHTTPD {
    private Context mContext;
    public CasterServer() {
        super(MainActivity.PORT);
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 아이피 획득
    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if(!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
//    ----------------------------------------------------------------------------------------------------------------------------------------------------- 아이피 획득 ed
    @Override
    public Response serve(String uri, Method method, Map<String, String> headers, Map<String, String> parms, Map<String, String> files) {
        InputStream fis = null;
        String mType = "audio/*";
        try {
            if(uri.contains(FILE_TYPE_IMAGE)) {
                mType = "image/*";
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                Bitmap bigPictureBitmap = null;
                if(MainActivity.selectedMp3.getAlbum_art_image() == null) {
                    bigPictureBitmap  = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.songcast_no_image);
                } else {
                    bigPictureBitmap = MainActivity.selectedMp3.getAlbum_art_image();
                }
                if(bigPictureBitmap.compress(Bitmap.CompressFormat.JPEG, 30, stream)) {
                    fis = new ByteArrayInputStream(stream.toByteArray());
                }
            } else if(uri.contains(FILE_TYPE_AUDIO)) {
                fis = new FileInputStream(MainActivity.selectedMp3.getPath()+"/"+MainActivity.selectedMp3.getName());
            } else {
                return null;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return new NanoHTTPD.Response(Response.Status.OK, mType, fis);
    }
}
















