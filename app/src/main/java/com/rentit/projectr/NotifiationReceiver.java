package com.rentit.projectr;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.parse.ParsePushBroadcastReceiver;
import com.rentit.message.Message;

/**
 * Created by ankit on 20/10/15.
 */
public class NotifiationReceiver extends ParsePushBroadcastReceiver {
    private static final String TAG = NotifiationReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        String data = intent.getDataString();
        String dataRx = intent.getExtras().getString("com.parse.Data");
        Log.d(TAG, "dataRx:"+dataRx);
        Gson gson = new GsonBuilder().create();
        Message msg = gson.fromJson(dataRx, Message.class);
        msg.execute(context);
    }
}
