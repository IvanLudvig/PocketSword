# SwordSoundApp
An app that detects when the device is in a pocket. When the user takes out the phone, it plays a sword unsheathing sound.

I got the idea for this app from Reddit. During development, I didn't find any implementation of detection if the device in a pocket. So, I made it myself.

## How to detect if the device is in a pocket

The main signs that the phone is in a pocket are: little light, vertical upside-down position and lack of space.
These parameters can be obtained by the Light sensor, Accelerometer and Proximity sensor.

### Implementation

The parameters are obtained by the sensors in `SensorListener.java`

Here's how I get the needed values:

`   @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType()==Sensor.TYPE_ACCELEROMETER) {
            g = new float[3];
            g = event.values.clone();

            double norm_Of_g = Math.sqrt(g[0] * g[0] + g[1] * g[1] + g[2] * g[2]);

            g[0] = (float) (g[0] / norm_Of_g);
            g[1] = (float) (g[1] / norm_Of_g);
            g[2] = (float) (g[2] / norm_Of_g);

            inclination = (int) Math.round(Math.toDegrees(Math.acos(g[2])));
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
`

As you can see, the method `detect` is called, which is located in `MainActivity.java`. This is the core of the mechanism.
It compares to current values (obtained by sensors) to such values that indicate that the phone is in a pocket. The border values given by me are pure approximation. So keep in mind, they have to be adjusted in order to achive accurate results.
`
public void detect(float prox, float light, float g[], int inc){
        if((prox<1)&&(light<2)&&(g[1]<-0.5)&&( (inc>75)||(inc<100))){
            pocket=1;
            //IN POCKET
        }
        if((prox>=1)&&(light>=2)&&(g[1]>=-0.5)){
            if(pocket==1){
                playSound();
                pocket=0;
            }
            //OUT OF POCKET
        }
}
`
In my example the border values are 1 for proximity sensor, 2 for light sensor and -0.5 for accelerometer (it also takes account of inclination).

P.S: This mechanism isn't really precise.


