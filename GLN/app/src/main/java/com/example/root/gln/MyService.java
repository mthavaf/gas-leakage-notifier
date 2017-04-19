package com.example.root.gln;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.IntDef;

public class MyService extends Service {
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
        GLNUtils.print("SERVICE STATRTED" + intent.getStringExtra("ID"));

        try {
            Thread mqttThread = new Thread(new MqttThread(MyService.this, intent.getStringExtra("ID")));
            mqttThread.start();
        }catch (Exception e){
            GLNUtils.print(e.getLocalizedMessage());
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        GLNUtils.print("DESTROYED");
    }
}
