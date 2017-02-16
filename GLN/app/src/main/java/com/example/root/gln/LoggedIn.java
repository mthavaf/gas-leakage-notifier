package com.example.root.gln;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
        glnDrawerListView.setOnItemClickListener(new DrawerItemClickListener());
    }

    public class DrawerItemClickListener implements ListView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            changeFragment(position);
        }
    }
    private void changeFragment(int pos){
        if (pos == 2){
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
}
