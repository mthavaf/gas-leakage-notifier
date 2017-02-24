package com.example.root.gln;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nispok.snackbar.Snackbar;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    private Button loginButton;
    private EditText emailInput;
    private EditText passwordInput;
    private String email;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginButton = (Button) findViewById(R.id.login_button);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailInput = (EditText) findViewById(R.id.email_input);
                passwordInput = (EditText) findViewById(R.id.password_input);
                email = emailInput.getText().toString().trim();
                password = passwordInput.getText().toString().trim();

                /*AsyncHttpClient client = new AsyncHttpClient();
                client.post(Gln.HOST+"/login", new RequestParams("hi","hello"), new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                        try {
                            print(new String(responseBody, "UTF-8"));
                        }catch (UnsupportedEncodingException e){
                            print(e.getMessage());
                        }
                        Intent loggedIn = new Intent(MainActivity.this, LoggedIn.class);
                        startActivity(loggedIn);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        print("Failed");
                        Intent loggedIn = new Intent(MainActivity.this, LoggedIn.class);
                        startActivity(loggedIn);
                    }
                });*/
                if (email.equals("thavaf") && password.equals("thavaf")){
                    Intent i = new Intent(MainActivity.this, LoggedIn.class);
                    startActivity(i);
                }else {
                    android.support.design.widget.Snackbar snackbar = android.support.design.widget.Snackbar.make(v, "Wrong credentials", android.support.design.widget.Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
            }
        });



    }

    void print(String s){
        Log.d(Gln.TAG, s);
    }

    @Override
    public void onBackPressed() {

    }
}
