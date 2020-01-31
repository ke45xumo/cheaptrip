package com.example.cheaptrip.app;

import android.app.Activity;
import android.app.Application;

/**
 * This class represents the app
 * It takes care of logging the current activity on top of the lifecycle stack
 */
public class CheapTripApp extends Application {
    private Activity mCurrentActivity;

    @Override
    public void onCreate () {
        super .onCreate() ;
    }

    public Activity getCurrentActivity() {
        return mCurrentActivity;
    }

    public void setCurrentActivity(Activity currentActivity) {
        this.mCurrentActivity = currentActivity;
    }
}
