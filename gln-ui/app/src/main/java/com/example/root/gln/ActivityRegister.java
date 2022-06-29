package com.example.root.gln;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;

public class ActivityRegister extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle("Register");
    }
    public void onClickSignIn(View v){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {

    }
    public void onClickRegister(View v){

        EditText eTuniqueID = (EditText) findViewById(R.id.edit_text_unique_id);
        EditText eTusername = (EditText) findViewById(R.id.edit_text_username);
        EditText eTpassword = (EditText) findViewById(R.id.edit_text_password);
        EditText eTprimaryNumber = (EditText) findViewById(R.id.edit_text_ph_no_p);
        EditText eTemergencyNumber1 = (EditText) findViewById(R.id.edit_text_emergency_number_1);
        EditText eTemergencyNumber2 = (EditText) findViewById(R.id.edit_text_emergency_number_2);
        EditText etemergencyNumber3 = (EditText) findViewById(R.id.edit_text_emergency_number_3);

        final String uniqueID = eTuniqueID.getText().toString().trim();
        String username = eTusername.getText().toString().trim();
        String password = eTpassword.getText().toString().trim();
        String primaryNumber = eTprimaryNumber.getText().toString().trim();
        String emergencyNumber1 = eTemergencyNumber1.getText().toString().trim();
        String emergencyNumber2 = eTemergencyNumber2.getText().toString().trim();
        String emergencyNumber3 = etemergencyNumber3.getText().toString().trim();

        final Snackbar snackbar = Snackbar.make(v, null, Snackbar.LENGTH_LONG);

        if(!GLNUtils.isUniqueIDValid(uniqueID)){
            snackbar.setText("Invalid Unique ID format!!!");
            snackbar.show();
            return;
        }

        if(!GLNUtils.isUserNameValid(username)){
            snackbar.setText("Invalid username format!!!");
            snackbar.show();
            return;
        }

        if(!GLNUtils.isPasswordValid(password)){
            snackbar.setText("Invalid password format!!!");
            snackbar.show();
            return;
        }

        if(!GLNUtils.isPhoneNumberValid(primaryNumber)){
            snackbar.setText("Invalid primary number format!!!");
            snackbar.show();
            return;
        }

        if(!GLNUtils.isPhoneNumberValid(emergencyNumber1)){
            snackbar.setText("Invalid Emergency number - 1 format!!!");
            snackbar.show();
            return;
        }

        if(!GLNUtils.isPhoneNumberValid(emergencyNumber2)){
            snackbar.setText("Invalid Emergency number - 2 format!!!");
            return;
        }

        if(!GLNUtils.isPhoneNumberValid(emergencyNumber3)){
            snackbar.setText("Invalid Emergency number - 3 format!!!");
            return;
        }

        RequestParams params = new RequestParams();
        params.put("unique_id", uniqueID);
        params.put("username", username);
        params.put("password", password);
        params.put("ph_no_p", primaryNumber);
        params.put("ph_no_1", emergencyNumber1);
        params.put("ph_no_2", emergencyNumber2);
        params.put("ph_no_3", emergencyNumber3);

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(Gln.HOST + "/register", params, new AsyncHttpResponseHandler()  {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        org.json.simple.JSONObject jsonResponse = GLNUtils.bytesToJSON(responseBody);
                        Log.d("GLN", jsonResponse.toString());
                        if (jsonResponse.get("response").equals("OK")){
                            snackbar.setText((String)jsonResponse.get("message"));
                            snackbar.show();
                            Intent intent = new Intent(getApplicationContext(), LoggedIn.class);
                            intent.putExtra("ID", uniqueID);
                            startActivity(intent);
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
