package com.highjump.cqccollect.model;

import android.content.ContentValues;
import android.text.TextUtils;

import com.highjump.cqccollect.provider.DataMeasuredColumns;
import com.highjump.cqccollect.provider.MyContentProvider;
import com.highjump.cqccollect.provider.ReportColumns;

/**
 * Created by highjump on 15-2-1.
 */
public class LocationData {

    public String mStrReportId;

    public String mStrVerifyDate;
    public String mStrVerifyPlaceC;
    public String mStrVerifyPlaceE;

    public String mStrVerifyLocation;
    public String mStrLocateDate;
    public String mStrPhoto;
    public String mStrPhotoDate;

    public void saveToDB() {

        MyContentProvider contentProvider = new MyContentProvider();

        ContentValues contentValues = new ContentValues();

        contentValues.put(ReportColumns.VERIFICATION_DATE, mStrVerifyDate);
        contentValues.put(ReportColumns.VERIFICATION_PLACE_C, mStrVerifyPlaceC);
        contentValues.put(ReportColumns.VERIFICATION_PLACE_E, mStrVerifyPlaceE);
        contentValues.put(ReportColumns.VERIFICATION_LOCATION, mStrVerifyLocation);
        contentValues.put(ReportColumns.LOCATE_TIME, mStrLocateDate);
        contentValues.put(ReportColumns.LOCATION_PHOTO, mStrPhoto);
        contentValues.put(ReportColumns.PHOTO_TIME, mStrPhotoDate);

        contentProvider.update(ReportColumns.CONTENT_URI, contentValues,
                ReportColumns.NO + " = '" + mStrReportId + "'", null);
    }

    public boolean isValid() {
        if (TextUtils.isEmpty(mStrVerifyDate)) {
            return false;
        }

        if (TextUtils.isEmpty(mStrVerifyPlaceC)) {
            return false;
        }

        if (TextUtils.isEmpty(mStrVerifyLocation)) {
            return false;
        }

        if (TextUtils.isEmpty(mStrLocateDate)) {
            return false;
        }

        if (TextUtils.isEmpty(mStrPhoto)) {
            return false;
        }

        if (TextUtils.isEmpty(mStrPhotoDate)) {
            return false;
        }

        return true;
    }
}
