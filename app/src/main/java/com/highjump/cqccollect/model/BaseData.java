package com.highjump.cqccollect.model;

import android.content.ContentUris;
import android.content.ContentValues;
import android.net.Uri;

import com.highjump.cqccollect.provider.MyContentProvider;

public class BaseData {

    public static final int DS_NONE = 0x0;
    public static final int DS_ADDED = 0x1;
    public static final int DS_UPDATED = 0x10;
    public static final int DS_DELETED = 0x100;

    public int mnNo;
    public int mStatus;

    public BaseData() {
        mStatus = DS_NONE;
    }

    public void save(Uri uri, String strNo, ContentValues contentValues) {

        MyContentProvider contentProvider = new MyContentProvider();

        if ((mStatus & DS_ADDED) != 0) {
            if ((mStatus & DS_DELETED) == 0) {
                // add
                Uri contentUri = contentProvider.insert(uri, contentValues);
                mnNo = (int) ContentUris.parseId(contentUri);
            }

            mStatus = DS_NONE;
        }
        else if ((mStatus & DS_DELETED) != 0) {
            contentProvider.delete(uri, strNo + " = " + mnNo, null);
        }
        else if ((mStatus & DS_UPDATED) != 0) {
            contentProvider.update(uri, contentValues, strNo + " = " + mnNo, null);

            mStatus = DS_NONE;
        }
    }
}
