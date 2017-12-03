package com.mitaghude.android.assetprovider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.net.Uri;

import java.io.FileNotFoundException;
import java.io.IOException;

import static java.net.URLConnection.guessContentTypeFromName;
/**
 * Created by mitaghude on 12/2/17.
 */

public class AssetProvider extends ContentProvider {
    AssetManager mAssets;

    @Override
    public boolean onCreate() {
        Context ctx = getContext();
        if (ctx == null) {
            // Context not available. Give up.
            return false;
        }
        mAssets = ctx.getAssets();
        return true;
    }

    @Override
    public String getType(Uri uri){

        String path = uri.getPath();

        // Check if file exists
        if (!fileExists(path)) {
            return null;
        }

        // Determine MIME-type based on filename
        return guessContentTypeFromName(uri.toString());
    }


    @Override
    public AssetFileDescriptor openAssetFile (Uri uri, String mode)
            throws FileNotFoundException, SecurityException {

        if (!"r".equals(mode)) {
            throw new SecurityException("Only read-only access is supported, mode must be [r]");
        }
        String path = uri.getPath();
        try {
            return mAssets.openFd(path);
        } catch (IOException e) {
            throw new FileNotFoundException();
        }
    }

    private boolean fileExists(String path) {
        try {
            // Check to see if file can be opened. If so, file exists.
            mAssets.openFd(path).close();
            return true;
        } catch (IOException e) {
            // Unable to open file descriptor for specified path; file doesn't exist.
            return false;
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        throw new RuntimeException("Operation not supported");
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        throw new RuntimeException("Operation not supported");
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        throw new RuntimeException("Operation not supported");
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        throw new RuntimeException("Operation not supported");
    }
}
