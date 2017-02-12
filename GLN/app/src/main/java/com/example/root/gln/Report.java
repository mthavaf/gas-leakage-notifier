package com.example.root.gln;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Report extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
    }
    void onEcClick(View v){
        Intent i = new Intent(this, AddContacts.class);
        startActivity(i);
    }
}
