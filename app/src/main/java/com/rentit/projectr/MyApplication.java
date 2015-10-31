package com.rentit.projectr;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseInstallation;

/**
 * Created by ankit on 14/10/15.
 */
public class MyApplication extends Application {

    public static String ApplicationId = "jfETvWEL75WiPG86pJY7zK3HHhFWblAxCOnCnm5c";
    public static String ClientId = "BvCrBgGX6zVe7muw1TA2MeNHCqbpBWeGeYB43o2l";

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, ApplicationId, ClientId);
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }
}
