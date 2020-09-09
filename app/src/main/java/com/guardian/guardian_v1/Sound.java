package com.guardian.guardian_v1;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

import java.util.Random;

public class Sound {
    public static int playAlertSound(Context context){
        int x = new Random().nextInt(8);
        MediaPlayer mediaPlayer;
        mediaPlayer = MediaPlayer.create(context, Uri.parse("android.resource://"+ context.getPackageName() +"/raw/alert" + x));
        mediaPlayer.start();
        return x;
    }
}
