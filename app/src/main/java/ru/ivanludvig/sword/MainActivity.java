package ru.ivanludvig.sword;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;


public class MainActivity extends Activity {

    SensorListener sensorListener;
    Context context = this;
    MediaPlayer mp;

    int pocket = 0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        sensorListener = new SensorListener(this, context);

        mp = MediaPlayer.create(context, R.raw.sa);

    }

    public void detect(float prox, float light, float g[], int inc){
        if((prox<1)&&(light<2)&&(g[1]<-0.5)&&( (inc>75)||(inc<100))){
            pocket=1;
        }
        if((prox>=1)&&(light>=2)&&(g[1]>=-0.5)){
            if(pocket==1){
                playSound();
                pocket=0;
            }
        }
    }


    public void playSound(){
        int i =(int) (Math.random()*10);
        String s = "s"+i;
        Log.v("MMM", s);
        Uri uri=Uri.parse("android.resource://"+getPackageName()+"/raw/" + s);
        try {
            if(mp.isPlaying()) {
                mp.stop();
                mp.release();
            }
            mp = MediaPlayer.create(context, uri);
            mp.start();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}