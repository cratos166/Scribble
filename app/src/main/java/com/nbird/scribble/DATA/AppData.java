package com.nbird.scribble.DATA;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class AppData {

    Context context;


    public AppData(Context context) {
        this.context = context;
    }

    public boolean getSharedPreferencesChangeBollean(String stringB){
         SharedPreferences sharedPreferences = context.getSharedPreferences(AppString.SP_MAIN, 0);
         SharedPreferences.Editor editor = sharedPreferences.edit();

        Boolean value = sharedPreferences.getBoolean(stringB, false);

        if(value){
            return true;
        }else{
            editor.putBoolean(stringB, true);
            editor.apply();
            return false;
        }
    }




    public void setSharedPreferencesString(String stringB,String value){
        final SharedPreferences sharedPreferences = context.getSharedPreferences(AppString.SP_MAIN, 0);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(stringB, value);
        editor.commit();
    }

    public String getSharedPreferencesString(String stringB,Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(AppString.SP_MAIN, 0);
        String value = sharedPreferences.getString(stringB,"");
        return value;
    }

    public boolean getSharedPreferencesBoolean(String stringB){
        SharedPreferences sharedPreferences = context.getSharedPreferences(AppString.SP_MAIN, 0);
        Boolean value = sharedPreferences.getBoolean(stringB,true);

        return value;
    }

    public void setSharedPreferencesBoolean(String stringB,boolean value){
        final SharedPreferences sharedPreferences = context.getSharedPreferences(AppString.SP_MAIN, 0);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(stringB, value);
        editor.commit();
    }

}
