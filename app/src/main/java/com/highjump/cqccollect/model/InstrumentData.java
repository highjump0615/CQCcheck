package com.highjump.cqccollect.model;

import android.content.ContentUris;
import android.content.ContentValues;
import android.net.Uri;

import com.highjump.cqccollect.provider.DataMeasuredColumns;
import com.highjump.cqccollect.provider.InstrumentColumns;
import com.highjump.cqccollect.provider.MyContentProvider;

/**
 * Created by highjump on 15-2-1.
 */
public class InstrumentData extends BaseData {

    public String mStrName;
    public String mStrModel;
    public String mStrRefNo;
    public String mStrValidity;

    public void save(String strReportId) {

        if (mStatus == DS_NONE) {
            return;
        }

        ContentValues contentValues = new ContentValues();

        contentValues.put(InstrumentColumns.REPORT_NO, strReportId);
        contentValues.put(InstrumentColumns.INSTRUMENT_NAME, mStrName);
        contentValues.put(InstrumentColumns.MODEL, mStrModel);
        contentValues.put(InstrumentColumns.REF_NO, mStrRefNo);
        contentValues.put(InstrumentColumns.VALIDITY_CALIBRATION, mStrValidity);

        super.save(InstrumentColumns.CONTENT_URI, InstrumentColumns.INSTRUMENT_NO, contentValues);
    }
}
