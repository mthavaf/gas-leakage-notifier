package com.example.root.gln;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import androidx.annotation.IntDef;

import org.eclipse.paho.client.mqttv3.MqttException;

public class MyService extends Service {
    Thread mqttThread;
    MqttThread mqttRunnable;
    public MyService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        GLNUtils.print("SERVICE CREATED");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //GLNUtils.print("SERVICE STATRTED" + intent.getStringExtra("ID"));
        GLNUtils.print("SERVICE STARTED");
        try {
            mqttRunnable = new MqttThread(MyService.this.getApplicationContext(), GLNSharedPreferences.getUniqueID(MyService.this));
            mqttThread = new Thread(mqttRunnable);
            mqttThread.start();
        }catch (Exception e){
            GLNUtils.print(e.getLocalizedMessage());
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        GLNUtils.print("DESTROYED");
        try {
            mqttRunnable.getClient().disconnect();
        }catch (MqttException e){
            GLNUtils.print(e.getLocalizedMessage());
        }
    }
}
