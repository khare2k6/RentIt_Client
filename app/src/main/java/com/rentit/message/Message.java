package com.rentit.message;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;

import com.rentit.com.rentit.network.URLConstants;
import com.rentit.projectr.PreferenceManager;
import com.rentit.projectr.ShowMessageActivity;

/**
 * Created by ankit on 24/10/15.
 */
public class Message {

    public enum MessageType{
        UPDATE_SERVER,
        USER_NOTIFICATION
    }

    private MessageType mMessageType;
    private String mMessage;

    /**
     * Message might be of User notification type when need to show user a dialogue
     * OR an update message to update some constant value in the cient
     * @param context
     */
    public void execute(Context context){
        switch (mMessageType){
            case UPDATE_SERVER:
                PreferenceManager.getmInstance(context).setIp(mMessage);
                break;

            case USER_NOTIFICATION:
                Intent intent = new Intent(context, ShowMessageActivity.class);
                intent.putExtra(ShowMessageActivity.MSG_FOR_USR, mMessage);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                break;
        }

    }
}
