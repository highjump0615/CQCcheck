package com.highjump.cqccollect.model;

import android.content.ContentUris;
import android.content.ContentValues;
import android.net.Uri;

import com.highjump.cqccollect.provider.ElectricSpecColumns;
import com.highjump.cqccollect.provider.MyContentProvider;

/**
 * Created by highjump on 15-2-1.
 */
public class ElectricSpecData extends BaseData {

    public ProductData mProduct;

    public String mStrRatedVoltage;
    public String mStrRatedPower;
    public String mStrRatedFreq;
    public String mStrClass;
    public String mStrIpGrade;
    public String mStrCableSpec;
    public String mStrSupply;
    public String mStrPlug;

    public void save(String strReportId) {

        if (mStatus == DS_NONE) {
            return;
        }

        ContentValues contentValues = new ContentValues();

        contentValues.put(ElectricSpecColumns.PRODUCT_NO, mProduct.mnNo);
        contentValues.put(ElectricSpecColumns.REPORT_NO, strReportId);
        contentValues.put(ElectricSpecColumns.RATED_VOLTAGE, mStrRatedVoltage);
        contentValues.put(ElectricSpecColumns.RATED_POWER, mStrRatedPower);
        contentValues.put(ElectricSpecColumns.RATED_FREQUENCY, mStrRatedFreq);
        contentValues.put(ElectricSpecColumns.ELECTRICAL_CLASS, mStrClass);
        contentValues.put(ElectricSpecColumns.IP_GRADE, mStrIpGrade);
        contentValues.put(ElectricSpecColumns.CABLE_SPECIFICATION, mStrCableSpec);
        contentValues.put(ElectricSpecColumns.CONNECTION_TYPE, mStrSupply);
        contentValues.put(ElectricSpecColumns.PLUG, mStrPlug);

        super.save(ElectricSpecColumns.CONTENT_URI, ElectricSpecColumns.SPECIFICATION_NO, contentValues);
    }
}
