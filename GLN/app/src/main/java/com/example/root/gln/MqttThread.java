package com.example.root.gln;

import android.content.Context;

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
    private MqttAndroidClient client = new MqttAndroidClient(context, url, clientID);
    private String subscribeTopic;
    MqttThread(Context context, String subscribeTopic){
        this.context = context;
        this.subscribeTopic = subscribeTopic;
    }
    @Override
    public void run() {
        /*client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                GLNUtils.print("CONNECTION LOST");
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                GLNUtils.print("Message Arrived : "+message.toString());
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
        }*/
        GLNUtils.print("Working");
    }
}
