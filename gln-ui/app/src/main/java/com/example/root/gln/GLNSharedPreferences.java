package com.example.root.gln;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by thavaf on 4/17/2017.
 */

public class GLNSharedPreferences {
    static final String USER_NAME = "username";
    static final String UNIQUE_ID = "id";

    static SharedPreferences getSharedPreferences(Context context){
        return context.getSharedPreferences("GLN_Shared_Preferences", Context.MODE_PRIVATE);
    }

    static void setUserName(Context context, String userName){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(USER_NAME, userName);
        editor.apply();
    }

    static void setUniqueId(Context context, String uniqueID){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(UNIQUE_ID, uniqueID);
        editor.apply();
    }

    static String getUserName(Context context){
        return getSharedPreferences(context).getString(USER_NAME, null);
    }

    static String getUniqueID(Context context){
        return  getSharedPreferences(context).getString(UNIQUE_ID, null);
    }

    static void clearPreferences(Context context){
        getSharedPreferences(context).edit().clear().apply();
    }
}
