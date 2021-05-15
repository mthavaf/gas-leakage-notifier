package com.example.root.gln;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import cz.msebera.android.httpclient.Header;

public class LoggedIn extends AppCompatActivity {
    private String[] drawerOptionNames;
    private DrawerLayout glnDrawerLayout;
    private ListView glnDrawerListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_logged_in);

        drawerOptionNames = getResources().getStringArray(R.array.drawer_items);
        glnDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        glnDrawerListView = (ListView) findViewById(R.id.left_drawer);
        glnDrawerListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, drawerOptionNames));
        //glnDrawerListView.setBackgroundColor(Color.DKGRAY);
        glnDrawerListView.setOnItemClickListener(new DrawerItemClickListener());
        changeFragment(0);
        MqttTask task = new MqttTask();
        task.execute();

    }

    public class DrawerItemClickListener implements ListView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            changeFragment(position);
        }
    }
    public class MqttTask extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... params) {

            String clientId = MqttClient.generateClientId();
            final MqttAndroidClient client = new MqttAndroidClient(getApplicationContext(), "tcp://iot.eclipse.org:1883", clientId);
            client.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    Log.d(Gln.TAG, "Connection Lost");
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    TextView reportSection = (TextView) findViewById(R.id.report_text);
                    reportSection.setTextColor(Color.GREEN);
                    if (Integer.parseInt(message.toString())>500){
                        reportSection.setTextColor(Color.RED);
                    }
                    reportSection.setText(message.toString());
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    Log.d(Gln.TAG, "delivery complete");
                }
            });
            try {
                IMqttToken token = client.connect();

                token.setActionCallback(new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        try {
                            Log.d("GLN", getIntent().getStringExtra("ID"));
                            IMqttToken subToken = client.subscribe(getIntent().getStringExtra("ID"), 1);
                            subToken.setActionCallback(new IMqttActionListener() {
                                @Override
                                public void onSuccess(IMqttToken asyncActionToken) {
                                    Log.d(Gln.TAG, "Subscription success");
                                }

                                @Override
                                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                                    Log.d(Gln.TAG, "Subscription failed");
                                }
                            });
                        }catch (MqttException e){
                            Log.d(Gln.TAG, e.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                        Log.d(Gln.TAG, "connection failed");
                    }
                });
            }catch (MqttException e){
                Log.d(Gln.TAG, e.getMessage());
            }

            return null;
        }
    }
    private void changeFragment(int pos){
        if (pos == 2){
            stopService(new Intent(LoggedIn.this, MyService.class));
            GLNSharedPreferences.clearPreferences(LoggedIn.this);
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            return;
        }
        Fragment fragment = new GLNFragment();
        Bundle args = new Bundle();
        int resId = getResourceId(pos);
        args.putInt(GLNFragment.RESOURCE, resId);
        fragment.setArguments(args);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
        glnDrawerListView.setItemChecked(pos, true);
        setTitle(drawerOptionNames[pos]);
        glnDrawerLayout.closeDrawer(glnDrawerListView);

    }
    private int getResourceId(int pos){

        int res = R.layout.fragment_report;
        if (pos == 1){
            res = R.layout.fragment_emergency_contacts;
        }
        return res;
    }

    @Override
    public void onBackPressed() {

    }
    public void onSubmit(View v){
        EditText eTPhoneNumber1 = (EditText) findViewById(R.id.number_1);
        EditText eTPhoneNumber2 = (EditText) findViewById(R.id.number_2);
        EditText eTPhoneNumber3 = (EditText) findViewById(R.id.number_3);

        String phoneNumber1 = eTPhoneNumber1.getText().toString().trim();
        String phoneNumber2 = eTPhoneNumber2.getText().toString().trim();
        String phoneNumber3 = eTPhoneNumber3.getText().toString().trim();

        final android.support.design.widget.Snackbar snackbar = android.support.design.widget.Snackbar.make(v, "", Snackbar.LENGTH_LONG);

        if (!GLNUtils.isPhoneNumberValid(phoneNumber1)){
            snackbar.setText("Invalid Phone Number - 1 format!!!");
            snackbar.show();
            return;
        }

        if (!GLNUtils.isPhoneNumberValid(phoneNumber2)){
            snackbar.setText("Invalid Phone Number - 2 format!!!");
            snackbar.show();
            return;
        }

        if (!GLNUtils.isPhoneNumberValid(phoneNumber3)){
            snackbar.setText("Invalid Phone Number - 3 format!!!");
            snackbar.show();
            return;
        }

        RequestParams params = new RequestParams();
        params.put("unique_id", getIntent().getStringExtra("ID"));
        params.put("ph_no_1",phoneNumber1);
        params.put("ph_no_2",phoneNumber2);
        params.put("ph_no_3",phoneNumber3);

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(Gln.HOST + "/update-numbers", params, new AsyncHttpResponseHandler()  {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        org.json.simple.JSONObject jsonResponse = GLNUtils.bytesToJSON(responseBody);
                        Log.d("GLN", jsonResponse.toString());
                        if (jsonResponse.get("response").equals("OK")){
                            snackbar.setText((String)jsonResponse.get("message"));
                            snackbar.show();
                        }else {
                            snackbar.setText((String)jsonResponse.get("message"));
                            snackbar.show();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        snackbar.setText("No network connection!!!");
                        snackbar.show();
                    }
                }
        );
    }
}
