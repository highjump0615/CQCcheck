package com.highjump.cqccollect.model;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;

import com.highjump.cqccollect.api.API_Manager;
import com.highjump.cqccollect.provider.MyContentProvider;
import com.highjump.cqccollect.provider.UserColumns;
import com.highjump.cqccollect.utils.Config;

/**
 * Created by highjump on 15-1-31.
 */
public class UserData {

    public String mStrUsername;
    public String mStrName;
    public String mStrUnit;
    public String mStrRole;

    private static UserData mInstance = null;

    public UserData(String username) {
        mStrUsername = username;

        MyContentProvider contentProvider = new MyContentProvider();

        Cursor cursor = contentProvider.query(UserColumns.CONTENT_URI, null, UserColumns.USERNAME + " IS '" + username + "'", null, null);
        if (cursor != null) {
            if (cursor.getCount() > 0 && cursor.moveToFirst()) {

                // header information
                mStrName = cursor.getString(cursor.getColumnIndex(UserColumns.NAME));
                mStrUnit = cursor.getString(cursor.getColumnIndex(UserColumns.UNIT));
                mStrRole = cursor.getString(cursor.getColumnIndex(UserColumns.ROLE));
            }
        }

        mInstance = this;
    }

    public static UserData currentUser(Context context) {
        if (mInstance == null) {
            // load preference
            SharedPreferences preferences = context.getSharedPreferences(Config.PREF_NAME, Context.MODE_PRIVATE);
            String strUsername = preferences.getString(Config.PREF_USERNAME, "defaultuser");

            new UserData(strUsername);
        }

        return mInstance;
    }
}
