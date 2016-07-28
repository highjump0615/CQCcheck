package com.highjump.cqccollect.model;

import android.content.ContentUris;
import android.content.ContentValues;
import android.net.Uri;

import com.highjump.cqccollect.provider.CollectImageColumns;
import com.highjump.cqccollect.provider.MyContentProvider;
import com.highjump.cqccollect.provider.SamplingColumns;

/**
 * Created by highjump on 15-2-1.
 */
public class CollectImgData extends BaseData {

    public static final String PHOTO_TYPE_PRODUCT = "产品";
    public static final String PHOTO_TYPE_PACK = "产品包装";
    public static final String PHOTO_TYPE_STRUCT = "产品结构";
    public static final String PHOTO_TYPE_MARK = "缺陷";
    public static final String PHOTO_TYPE_DEFECT = "铭牌/标识";

    public String mStrImgAddress;
    public String mStrDesc;
    public String mStrType;
    public int mnUploaded;

    public void save(String strReportId) {

        if (mStatus == DS_NONE) {
            return;
        }

        ContentValues contentValues = new ContentValues();

        contentValues.put(CollectImageColumns.REPORT_NO, strReportId);
        contentValues.put(CollectImageColumns.IMAGE_ADDRESS, mStrImgAddress);
        contentValues.put(CollectImageColumns.IMAGE_DESCRIPTION, mStrDesc);
        contentValues.put(CollectImageColumns.IMAGE_TYPE, mStrType);
        contentValues.put(CollectImageColumns.UPLOADED, mnUploaded);

        super.save(CollectImageColumns.CONTENT_URI, CollectImageColumns.IMAGE_NO, contentValues);
    }
}
