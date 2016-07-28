package com.highjump.cqccollect.model;

import android.content.ContentUris;
import android.content.ContentValues;
import android.net.Uri;

import com.highjump.cqccollect.provider.MyContentProvider;
import com.highjump.cqccollect.provider.UnitPackColumns;

/**
 * Created by highjump on 15-2-1.
 */
public class UnitPackData extends BaseData {

    public String mStrPackType;
    public String mStrDimen;
    public String mStrThickness;
    public String mStrLayers;
    public String mStrPrintingColor;
    public String mStrSealingType;

    public void save(String strReportId) {

        if (mStatus == DS_NONE) {
            return;
        }

        ContentValues contentValues = new ContentValues();

        contentValues.put(UnitPackColumns.REPORT_NO, strReportId);
        contentValues.put(UnitPackColumns.PACKAGING_TYPE, mStrPackType);
        contentValues.put(UnitPackColumns.DIMENSION, mStrDimen);
        contentValues.put(UnitPackColumns.THICKNESS, mStrThickness);
        contentValues.put(UnitPackColumns.LAYERS, mStrLayers);
        contentValues.put(UnitPackColumns.PRINTING_COLOR, mStrPrintingColor);
        contentValues.put(UnitPackColumns.SEALING_TYPE, mStrSealingType);

        super.save(UnitPackColumns.CONTENT_URI, UnitPackColumns.PACKAGING_NO, contentValues);
    }
}
