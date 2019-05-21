package ru.ivanludvig.sword;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;


public class MainActivity extends Activity {

    SensorListener sensorListener;
    Context context = this;
    MediaPlayer mp[] = new MediaPlayer[12];

    int pocket = 0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        sensorListener = new SensorListener(this, context);

        prepare();

    }

    public void detect(float prox, float light, float g[], int inc){
        if((prox<1)&&(light<2)&&(g[1]<-0.6)&&( (inc>75)||(inc<100))){
            pocket=1;
        }
        if((prox>=1)&&(light>=2)&&(g[1]>=-0.7)){
            if(pocket==1){
                playSound();
                pocket=0;
            }
        }
    }


    public void playSound(){
        int i =(int) (Math.random()*10);
        try {
            if(mp[i].isPlaying()) {
                mp[i].stop();
            }
            mp[i].start();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void prepare(){
        for(int i = 0; i<10; i++) {
            mp[i] = MediaPlayer.create(context, R.raw.sa);
            String s = "s"+i;
            Log.v("MMM", s);
            Uri uri=Uri.parse("android.resource://"+getPackageName()+"/raw/" + s);
            mp[i] = MediaPlayer.create(context, uri);
        }
    }

}