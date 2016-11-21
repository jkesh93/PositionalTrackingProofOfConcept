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
    private SensorManager sensorManager;
    private SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
                xAccel = event.values[0];
                yAccel = -event.values[1];
                zAccel = event.values[2];
            }
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
            canvas.drawText("xAccel: " + xAccel, 100, 100, infoPaint);
            canvas.drawText("yAccel: " + yAccel, 100, 150, infoPaint);
            canvas.drawText("zAccel: " + zAccel, 100, 200, infoPaint);
            invalidate();
        }
    }
}
