package com.rentit.projectr;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

/**
 * Just for showing the msgs received from server
 * Created by ankit on 25/10/15.
 */
public class ShowMessageActivity extends Activity {
    public static String MSG_FOR_USR = "MSG_FOR_USR";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String msg = intent.getStringExtra(MSG_FOR_USR);
        showDialog(msg);
    }

    private void showDialog(String msg) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Hello")
                .setMessage(msg)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
        dialogBuilder.show();
    }
}
