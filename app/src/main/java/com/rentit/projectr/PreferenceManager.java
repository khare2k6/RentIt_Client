package com.rentit.projectr;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.util.Log;

/**
 * Created by AKhare on 10-Aug-15.
 */
public class PreferenceManager {
    private static final String TAG = PreferenceManager.class.getSimpleName();
    private SharedPreferences mPreferences;
    private static PreferenceManager mInstance;
    private static final String PREF_NAME = "preferencefile";
    public static final String LOGGED_IN_USER = "username";
    public static final String SERVER_IP = "ip";
    private static final int PREF_MODE = Context.MODE_PRIVATE;


    private PreferenceManager(Context context){
        mPreferences = context.getSharedPreferences(PREF_NAME,PREF_MODE);
    }

    public static PreferenceManager getmInstance(Context context){
        if(mInstance == null){
            mInstance = new PreferenceManager(context);
        }
        return mInstance;
    }

    public void setLoggedInUser(String value){
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(LOGGED_IN_USER,value);
        editor.commit();
    }
    public void setIp(String value){
        SharedPreferences.Editor editor = mPreferences.edit();
        Log.d(TAG, "setting server IP as :" + value);
        editor.putString(SERVER_IP, value);
        editor.commit();

        Log.d(TAG, "server ip value from prefernece:" + mPreferences.getString(SERVER_IP, ""));
    }

    public String getServerIp(){
        return mPreferences.getString(SERVER_IP,null);
    }
    public String getUserName(){
        return mPreferences.getString(LOGGED_IN_USER,null);
    }


}
