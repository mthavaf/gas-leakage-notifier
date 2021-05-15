package com.example.root.gln;

import android.util.Log;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Created by thavaf on 3/31/2017.
 */

public class GLNUtils {

    final static String TAG = "GLN";
    final static String userNamePattern = "[A-Za-z][A-Za-z0-9_]{5,9}";
    final static String passwordPattern = "[A-Za-z0-9]{8,16}";
    final static String phoneNumberPattern = "\\+[910-9]{12}";
    final static String uniqueIDPattern = "[A-Z0-9]{8}";


    static JSONObject bytesToJSON(byte[] bytes){
        String string = new String(bytes).replaceAll("\\\\","");
        string = string.substring(1, string.lastIndexOf("}")+1);
        JSONParser jsonParser = new JSONParser();
        try{
            return (org.json.simple.JSONObject) jsonParser.parse(string);
        } catch (ParseException e){
            return null;
        }
    }

    static void print(String string){
        Log.d(TAG, string);
    }

    static boolean isUserNameValid(String userName){
        return userName.matches(userNamePattern);
    }

    static boolean isPasswordValid(String password){
        return password.matches(passwordPattern);
    }

    static boolean isPhoneNumberValid(String phoneNumber){
        return phoneNumber.matches(phoneNumberPattern);
    }

    static boolean isUniqueIDValid(String uniqueID){
        return uniqueID.matches(uniqueIDPattern);
    }
}
