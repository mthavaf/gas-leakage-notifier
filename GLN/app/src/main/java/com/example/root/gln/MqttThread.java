package com.example.root.gln;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * Created by thavaf on 4/18/2017.
 */

public class MqttThread implements Runnable {
    private Context context;
    private String url = "tcp://iot.eclipse.org:1883";
    private String clientID = MqttClient.generateClientId();
    private MqttAndroidClient client;
    private String subscribeTopic;
    MqttThread(Context context, String subscribeTopic){
        this.context = context;
        this.subscribeTopic = subscribeTopic;
        client = new MqttAndroidClient(context, url, clientID);
    }
    MqttAndroidClient getClient(){
        return client;
    }
    @Override
    public void run() {
        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                GLNUtils.print("CONNECTION LOST");
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                GLNUtils.print("Message Arrived : "+message.toString());
                if (Integer.parseInt(message.toString()) > 500){
                    try {
                        android.support.v4.app.NotificationCompat.Builder builder = new android.support.v7.app.NotificationCompat.Builder(context).setSmallIcon(R.mipmap.ic_launcher).setContentTitle("IMPORTANT ALERT").setContentText("LEAKAGE DETECTED: Value = "+ message.toString());
                        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                        notificationManager.notify(0, builder.build());
                        vibrator.vibrate(5000);

                    }catch (Exception e){
                        GLNUtils.print(e.getLocalizedMessage());
                    }
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                GLNUtils.print("DELIVERY COMPLETE");
            }
        });
        try {
            IMqttToken token = client.connect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    GLNUtils.print("CONNECTED TO "+url);
                    try {
                        IMqttToken token1 = client.subscribe(subscribeTopic, 2);
                        token1.setActionCallback(new IMqttActionListener() {
                            @Override
                            public void onSuccess(IMqttToken asyncActionToken) {
                                GLNUtils.print("SUCCESSFULLY SUBSCRIBED");
                            }

                            @Override
                            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                                GLNUtils.print("SUBSCRIPTION FAILURE");
                            }
                        });
                    }catch (MqttException e){
                        GLNUtils.print(e.getLocalizedMessage());
                    }
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    GLNUtils.print("CONNECTION FAILED TO "+url);
                }
            });
        }catch (MqttException e){
            GLNUtils.print(e.getLocalizedMessage());
        }
/*
        GLNUtils.print("Working");
*/
    }
}
