package com.highjump.cqccollect.model;

import android.content.ContentUris;
import android.content.ContentValues;
import android.net.Uri;

import com.highjump.cqccollect.provider.MyContentProvider;
import com.highjump.cqccollect.provider.OtherInfoColumns;

/**
 * Created by highjump on 15-2-1.
 */
public class OtherInfoData extends BaseData {

    public String mStrOtherInfo;

    public void save(String strReportId) {

        if (mStatus == DS_NONE) {
            return;
        }

        ContentValues contentValues = new ContentValues();

        contentValues.put(OtherInfoColumns.REPORT_NO, strReportId);
        contentValues.put(OtherInfoColumns.OTHER_INFO, mStrOtherInfo);

        super.save(OtherInfoColumns.CONTENT_URI, OtherInfoColumns.OTHER_NO, contentValues);
    }
}
