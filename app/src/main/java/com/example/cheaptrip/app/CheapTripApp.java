package com.example.cheaptrip.app;

import android.app.Activity;
import android.app.Application;

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
