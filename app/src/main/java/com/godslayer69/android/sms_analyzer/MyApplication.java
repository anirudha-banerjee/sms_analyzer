package com.godslayer69.android.sms_analyzer;

import android.app.Application;
import android.util.Log;

/**
 * Created by root on 5/16/17.
 * Project: ELE_SMS_ANIRUDHA
 */

public class MyApplication extends Application {

    private static final String TAG = "ELESMSANIRUDHA";

    private static MyApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;

        FontsOverride.setDefaultFont(MyApplication.this, "DEFAULT", "Roboto-Regular.ttf");
        FontsOverride.setDefaultFont(this, "MONOSPACE", "Roboto-Medium.ttf");
        FontsOverride.setDefaultFont(this, "SERIF", "Roboto-Black.ttf");
        FontsOverride.setDefaultFont(this, "SANS_SERIF", "Roboto-Light.ttf");

        Log.d(TAG, "Fonts Overriden");

    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

}
