package com.example.root.gln;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

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
        print("hi");

        loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                print("Onclick");
                AsyncHttpClient client = new AsyncHttpClient();
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
                });
            }
        });

        emailInput = (EditText) findViewById(R.id.email_input);
        passwordInput = (EditText) findViewById(R.id.password_input);
        email = emailInput.getText().toString().trim();
        password = passwordInput.getText().toString().trim();

        print(email);
        print(password);


    }

    void print(String s){
        Log.d(Gln.TAG, s);
    }

    @Override
    public void onBackPressed() {

    }
}
