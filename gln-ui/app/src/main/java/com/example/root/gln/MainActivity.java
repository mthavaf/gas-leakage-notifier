package com.example.root.gln;

import android.content.Intent;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
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
        if (GLNSharedPreferences.getUserName(MainActivity.this) != null){
            changeActivity(GLNSharedPreferences.getUniqueID(MainActivity.this));
        }
        setContentView(R.layout.activity_main);

        loginButton = (Button) findViewById(R.id.login_button);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailInput = (EditText) findViewById(R.id.email_input);
                passwordInput = (EditText) findViewById(R.id.password_input);
                email = emailInput.getText().toString().trim();
                password = passwordInput.getText().toString().trim();

                final Snackbar snackbar = Snackbar.make(v, "", Snackbar.LENGTH_LONG);

                if(!GLNUtils.isUserNameValid(email)){
                    snackbar.setText("Invalid username format!!!");
                    snackbar.show();
                    return;
                }

                if(!GLNUtils.isPasswordValid(password)){
                    snackbar.setText("Invalid password format!!!");
                    snackbar.show();
                    return;
                }

                RequestParams params = new RequestParams();
                params.put("user_name", email);
                params.put("password", password);
                AsyncHttpClient client = new AsyncHttpClient();
                client.post(Gln.HOST + "/login", params, new AsyncHttpResponseHandler()  {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                org.json.simple.JSONObject jsonResponse = GLNUtils.bytesToJSON(responseBody);
                                if (jsonResponse.get("response").equals("OK")){
                                    String id = (String)jsonResponse.get("message");
                                    GLNSharedPreferences.setUserName(MainActivity.this, email);
                                    GLNSharedPreferences.setUniqueId(MainActivity.this, id);
                                    Intent intentService = new Intent(MainActivity.this, MyService.class);
                                    intentService.putExtra("ID", id);
                                    GLNUtils.print(intentService.getStringExtra("ID"));
                                    startService(intentService);
                                    changeActivity(id);
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

    void changeActivity(String id){
        Intent intent = new Intent(getApplicationContext(), LoggedIn.class);
        intent.putExtra("ID", id);
        startActivity(intent);
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
