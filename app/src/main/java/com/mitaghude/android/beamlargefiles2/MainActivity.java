package com.mitaghude.android.beamlargefiles2;

/**
 * Created by mitaghude on 12/2/17.
 */

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mitaghude.android.logger.Log;
import com.mitaghude.android.logger.LogFragment;
import com.mitaghude.android.logger.LogWrapper;
import com.mitaghude.android.logger.MessageOnlyLogFilter;

import java.io.File;

public class MainActivity extends SampleActivityBase {

    public static final String TAG = "MainActivity";

    public static final String FRAGTAG = "BeamLargeFilesFragment";

    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView sampleOutput = (TextView) findViewById(R.id.sample_output);
        sampleOutput.setText(Html.fromHtml(getString(R.string.intro_message)));

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        BeamLargeFilesFragment fragment = new BeamLargeFilesFragment();
        transaction.add(fragment, FRAGTAG);
        transaction.commit();
    }*/

    private NfcAdapter nfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PackageManager pm = this.getPackageManager();

        if (!pm.hasSystemFeature(PackageManager.FEATURE_NFC)) {
            Toast.makeText(this, "The device does not has NFC hardware.",
                    Toast.LENGTH_SHORT).show();
        }
        else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            Toast.makeText(this, "Android Beam is not supported.",
                    Toast.LENGTH_SHORT).show();
        }
        else {
            // NFC and Android Beam FILE TRANSFER is supported.
            Toast.makeText(this, "Android Beam is supported on YOUR device.",
                    Toast.LENGTH_LONG).show();
        }
    }


    public void sendFile (View view) {
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if(!nfcAdapter.isEnabled()){
            Toast.makeText(this, "Please enable NFC.",
                    Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Settings.ACTION_NFC_SETTINGS));
        }

        else if(!nfcAdapter.isNdefPushEnabled()) {

            Toast.makeText(this, "Please enable Android Beam.",
                    Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Settings.ACTION_NFCSHARING_SETTINGS));
        }
        else {

            String fileName = "wallpaper.png";

            File fileDirectory = Environment
                    .getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES);
            File fileToTransfer = new File(fileDirectory, fileName);
            fileToTransfer.setReadable(true, false);

            nfcAdapter.setBeamPushUris( new Uri[]{ Uri.fromFile(fileToTransfer)}, this);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main, menu);
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    /** Create a chain of targets that will receive log data */
    @Override
    public void initializeLogging() {
        // Wraps Android's native log framework.
        LogWrapper logWrapper = new LogWrapper();
        // Using Log, front-end to the logging chain, emulates android.util.log method signatures.
        Log.setLogNode(logWrapper);

        // Filter strips out everything except the message text.
        MessageOnlyLogFilter msgFilter = new MessageOnlyLogFilter();
        logWrapper.setNext(msgFilter);

        // On screen logging via a fragment with a TextView.
        LogFragment logFragment = (LogFragment) getSupportFragmentManager().findFragmentById(R.id.log_fragment);
        msgFilter.setNext(logFragment.getLogView());
//        ((TextView) logFragment.getLogView()).setTextAppearance(this, R.style.Log);
        ((TextView) logFragment.getLogView()).setTextAppearance(R.style.Log);
        ((TextView) logFragment.getLogView()).setBackgroundColor(Color.WHITE);

        Log.i(TAG, "Ready");
    }
}
