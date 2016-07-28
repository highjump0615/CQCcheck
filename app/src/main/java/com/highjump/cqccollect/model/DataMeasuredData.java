package com.highjump.cqccollect.model;

import android.content.ContentUris;
import android.content.ContentValues;
import android.net.Uri;

import com.highjump.cqccollect.provider.DataMeasuredColumns;
import com.highjump.cqccollect.provider.MyContentProvider;

/**
 * Created by highjump on 15-2-1.
 */
public class DataMeasuredData extends BaseData {

    public ProductData mProduct;

    public String mStrNumberSampling;
    public String mStrElectricStrength;
    public String mStrEarthResist;
    public String mStrLeakCur;

    public void save(String strReportId) {

        if (mStatus == DS_NONE) {
            return;
        }

        ContentValues contentValues = new ContentValues();

        contentValues.put(DataMeasuredColumns.PRODUCT_NO, mProduct.mnNo);
        contentValues.put(DataMeasuredColumns.REPORT_NO, strReportId);
        contentValues.put(DataMeasuredColumns.NUMBER_SAMPLING, mStrNumberSampling);
        contentValues.put(DataMeasuredColumns.ELECTRIC_STRENGTH, mStrElectricStrength);
        contentValues.put(DataMeasuredColumns.EARTHING_RESISTANCE, mStrEarthResist);
        contentValues.put(DataMeasuredColumns.LEAKAGE_CURRENT, mStrLeakCur);

        super.save(DataMeasuredColumns.CONTENT_URI, DataMeasuredColumns.MEASURED_NO, contentValues);
    }

}
