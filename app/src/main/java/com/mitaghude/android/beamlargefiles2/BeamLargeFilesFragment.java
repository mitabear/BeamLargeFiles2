package com.mitaghude.android.beamlargefiles2;

import android.app.Activity;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;


public class BeamLargeFilesFragment extends Fragment implements NfcAdapter.CreateBeamUrisCallback {

    private static final String TAG = "BeamLargeFilesFragment";
    private static final String FILENAME = "test_file.jpg";
    private static final String CONTENT_BASE_URI =
            "content://com.example.android.beamlargefiles.files/";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Activity a = getActivity();
        NfcAdapter nfc = NfcAdapter.getDefaultAdapter(a);
        if (nfc != null) {
            Log.w(TAG, "NFC available. Setting Beam Push URI callback");
            nfc.setBeamPushUrisCallback(this, a);
        } else {
            Log.w(TAG, "NFC is not available");
        }

    }


    @Override
    public Uri[] createBeamUris(NfcEvent nfcEvent) {
        Log.i(TAG, "Beam event in progress; createBeamUris() called.");
        // Images are served using a content:// URI. See AssetProvider for implementation.
        Uri photoUri = Uri.parse(CONTENT_BASE_URI + FILENAME);
        Log.i(TAG, "Sending URI: " + photoUri);
        return new Uri[] {photoUri};
    }

}
