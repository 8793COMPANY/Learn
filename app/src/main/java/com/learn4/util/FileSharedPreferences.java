package com.learn4.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class
FileSharedPreferences {
    public static final String PREFERENCES_NAME = "file_list";

    private static SharedPreferences getPreferences(Context context) {

        return context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);

    }

    public static void setStringArrayList(Context context, String key, ArrayList<String> values) {

        SharedPreferences prefs = getPreferences(context);

        SharedPreferences.Editor editor = prefs.edit();

        JsonArray a = new JsonArray();

        for (int i=0; i < values.size(); i++){
            a.add(values.get(i));
        }
         if (!values.isEmpty()){
             editor.putString(key, a.toString());
         } else {
             editor.putString(key, null);
         }

        editor.commit();
    }

    public static ArrayList<String> getStringArrayList(Context context, String key){
        SharedPreferences prefs = getPreferences(context);
        String json = prefs.getString(key, null);
        ArrayList<String> values = new ArrayList<String>();
        if (json != null){
            try {
                JSONArray a = new JSONArray(json);
                for (int i=0; i< a.length(); i++){
                    String value = a.optString(i);
                    values.add(value);
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return values;
    }




}
