package com.highjump.cqccollect.model;

import android.content.ContentUris;
import android.content.ContentValues;
import android.net.Uri;

import com.highjump.cqccollect.provider.ExportCartonColumns;
import com.highjump.cqccollect.provider.MyContentProvider;

/**
 * Created by highjump on 15-2-1.
 */
public class ExportCartonData extends BaseData {

    public String mStrPcsCarton;
    public String mStrExtDimen;
    public String mStrThickness;
    public String mStrLayers;
    public String mStrSealingType;

    public void save(String strReportId) {

        if (mStatus == DS_NONE) {
            return;
        }

        ContentValues contentValues = new ContentValues();

        contentValues.put(ExportCartonColumns.REPORT_NO, strReportId);
        contentValues.put(ExportCartonColumns.PCS_CARTON, mStrPcsCarton);
        contentValues.put(ExportCartonColumns.EXTERNAL_DIMENSION, mStrExtDimen);
        contentValues.put(ExportCartonColumns.THICKNESS, mStrThickness);
        contentValues.put(ExportCartonColumns.LAYERS, mStrLayers);
        contentValues.put(ExportCartonColumns.SEALING_TYPE, mStrSealingType);

        super.save(ExportCartonColumns.CONTENT_URI, ExportCartonColumns.EXPORT_NO, contentValues);
    }
}
