package com.example.jay.positionaltrackingproofofconcept;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private float xAccel, yAccel, zAccel = 0.0f;
    // low pass filter variables
    private float lowPassFilter = .1f;
    private float lpX, lpY,lpZ = 0.0f;
    private float fX, fY, fZ = 0.0f;
    private boolean firstpass = true;
    private SensorManager sensorManager;
    private SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){


                // set x-Accel to the sensor value;
                xAccel = event.values[0];
                // if it's not the first pass -- compare the last value to the current value and if it's greater than the
                // lowpassFilter thresh hold, then pass that value to fX (filtered value of X);
                if(!firstpass){
                    float diff = Math.abs(xAccel - lpX);
                    if(diff > lowPassFilter){
                        Log.v("DiffX", "" + diff);
                        fX = xAccel;
                    }
                }

                // value to compare the next sensor reading to;
                lpX = xAccel;

                // set y-Accel to the sensor value;
                yAccel = -event.values[1];
                // if it's not the first pass -- compare the last value to the current value and if it's greater than the
                // lowpassFilter thresh hold, then pass that value to fY (filtered value of Y);
                if(!firstpass){
                    float diff = Math.abs(yAccel - lpY);
                    if(diff > lowPassFilter){
                        Log.v("DiffY", "" + diff);
                        fY = yAccel;
                    }
                }

                // value to compare the next sensor reading to;
                lpY = yAccel;

                // set z-Accel to the sensor value
                zAccel = event.values[2];
                // if it's not the first pass -- compare the last value to the current value and if it's greater than the
                // lowpassFilter thresh hold, then pass that value to fZ (filtered value of Z);
                if(!firstpass){
                    float diff = Math.abs(zAccel - lpZ);
                    if(diff > lowPassFilter){
                        Log.v("DiffZ", "" + diff);
                        fZ = zAccel;
                    }
                }

                // value to compare the next sensor reading to;
                lpZ = zAccel;
            }

            // after the firstpass this is set to false to enable sensor-reading comparisons to take place
            firstpass = false;
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };
    private Paint infoPaint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomView customView = new CustomView(this);
        setContentView(customView);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

    }

    @Override
    protected void onStart(){
        super.onStart();
        sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onStop(){
        sensorManager.unregisterListener(sensorEventListener);
        super.onStop();
    }


    private class CustomView extends View {

        public CustomView(Context context){
            super(context);
            infoPaint = new Paint();
            infoPaint.setTextAlign(Paint.Align.LEFT);
            infoPaint.setColor(Color.BLACK);
            infoPaint.setTextSize(48);
        }

        @Override
        protected void onDraw(Canvas canvas){
           // canvas.drawText("xAccel: " + xAccel, 100, 100, infoPaint);
            canvas.drawText("xAccel: " + fX, 100, 100, infoPaint);
            //canvas.drawText("yAccel: " + yAccel, 100, 150, infoPaint);
            canvas.drawText("yAccel: " + fY, 100, 150, infoPaint);
            canvas.drawText("zAccel: " + zAccel, 100, 200, infoPaint);
            invalidate();
        }
    }
}
