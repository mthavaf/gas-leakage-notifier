package com.example.root.gln;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
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

                final android.support.design.widget.Snackbar snackbar = android.support.design.widget.Snackbar.make(v, null, android.support.design.widget.Snackbar.LENGTH_SHORT);

                RequestParams params = new RequestParams();
                params.put("user_name", email);
                params.put("password", password);
                AsyncHttpClient client = new AsyncHttpClient();
                client.post(Gln.HOST + "/login", params, new AsyncHttpResponseHandler()  {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                org.json.simple.JSONObject jsonResponse = GLNUtils.bytesToJSON(responseBody);
                                if (jsonResponse.get("response").equals("OK")){
                                    Intent intent = new Intent(getApplicationContext(), LoggedIn.class);
                                    intent.putExtra("ID", (String)jsonResponse.get("message"));
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
        });



    }

    void print(String s){
        Log.d(Gln.TAG, s);
    }

    @Override
    public void onBackPressed() {

    }

    public void onClickRegister(View v){
        Intent intent = new Intent(this, ActivityRegister.class);
        startActivity(intent);
    }
}
