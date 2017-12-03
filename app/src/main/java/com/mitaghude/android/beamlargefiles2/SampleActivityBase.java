package com.mitaghude.android.beamlargefiles2;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.mitaghude.android.logger.Log;
import com.mitaghude.android.logger.LogWrapper;

/**
 * Created by mitaghude on 12/2/17.
 */

class SampleActivityBase extends FragmentActivity{
    public static final String TAG = "SampleActivityBase";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected  void onStart() {
        super.onStart();
        initializeLogging();
    }


    public void initializeLogging() {

        LogWrapper logWrapper = new LogWrapper();
        Log.setLogNode(logWrapper);

        Log.i(TAG, "Ready");
    }
}
