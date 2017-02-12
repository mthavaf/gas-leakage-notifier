package com.example.root.gln;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class AddContacts extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contacts);
    }
    void onReportClick(View v){
        Intent i = new Intent(this, Report.class);
        startActivity(i);
    }
}
