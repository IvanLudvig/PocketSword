package ru.ivanludvig.sword;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.widget.TextView;

import java.math.BigDecimal;

public class SensorListener implements SensorEventListener {

    private SensorManager mySensorManager;
    TextView proximityAvailable, proximityReading, lightAvailable, lightReading, accAvailable, accReading;
    Sensor proximitySensor, lightSensor, accSensor;
    float rp = -1;
    float rl = -1;
    float[] g = {0, 0, 0};
    int inclination = -1;

    MainActivity main;

    public SensorListener(MainActivity main, Context context){
        this.main = main;
        mySensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);

        accAvailable = (TextView)this.main.findViewById(R.id.accAvailable);
        accReading = (TextView)this.main.findViewById(R.id.accReading);
        accSensor = mySensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        proximityAvailable = (TextView)this.main.findViewById(R.id.proximityAvailable);
        proximityReading = (TextView)this.main.findViewById(R.id.proximityReading);
        proximitySensor = mySensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        lightAvailable = (TextView)this.main.findViewById(R.id.lightAvailable);
        lightReading = (TextView)this.main.findViewById(R.id.lightReading);
        lightSensor = mySensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        if (accSensor == null){
            accAvailable.setText("No Accelerometer!");
        }else{
            accAvailable.setText(accSensor.getName());
            mySensorManager.registerListener(this, accSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }

        if (proximitySensor == null){
            proximityAvailable.setText("No Proximity Sensor!");
        }else{
            proximityAvailable.setText(proximitySensor.getName()+" Maximum Range: "
                    + String.valueOf(proximitySensor.getMaximumRange()));
            mySensorManager.registerListener(this, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
        }

        if(lightSensor != null){
            lightAvailable.setText(lightSensor.getName());
            mySensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);

        } else {
            lightAvailable.setText("No Light Sensor!");
        }



    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType()==Sensor.TYPE_ACCELEROMETER) {
            g = new float[3];
            g = event.values.clone();

            double norm_Of_g = Math.sqrt(g[0] * g[0] + g[1] * g[1] + g[2] * g[2]);

            g[0] = (float) (g[0] / norm_Of_g);
            g[1] = (float) (g[1] / norm_Of_g);
            g[2] = (float) (g[2] / norm_Of_g);

            inclination = (int) Math.round(Math.toDegrees(Math.acos(g[2])));

            //Log.v("SSS", g[0]+"  "+g[1]+"  "+g[2]+"  inc "+inclination);
            accReading.setText("XYZ: "+round(g[0])+",  "+round(g[1])+",  "+round(g[2])+"  inc: "+inclination);
        }
        if(event.sensor.getType()==Sensor.TYPE_PROXIMITY){
            proximityReading.setText("Proximity Sensor Reading:" + String.valueOf(event.values[0]));
            rp=event.values[0];
        }
        if(event.sensor.getType() == Sensor.TYPE_LIGHT){
            lightReading.setText("LIGHT: " + event.values[0]);
            rl=event.values[0];
        }
        if((rp!=-1) && (rl!=-1) && (inclination!=-1)){
            main.detect(rp, rl, g, inclination);
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public BigDecimal round(float d) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(3, BigDecimal.ROUND_HALF_UP);
        return bd;
    }
}
