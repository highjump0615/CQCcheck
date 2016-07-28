package com.highjump.cqccollect.model;

import android.content.ContentUris;
import android.content.ContentValues;
import android.net.Uri;

import com.highjump.cqccollect.provider.MyContentProvider;
import com.highjump.cqccollect.provider.SamplingColumns;

/**
 * Created by highjump on 15-2-1.
 */
public class SamplingData extends BaseData {

    public ProductData mProduct;
    public String mStrNumSampling;
    public String mStrAql;
    public String mStrNumAccept;
    public String mStrNumReject;

    public void save(String strReportId) {

        if (mStatus == DS_NONE) {
            return;
        }

        ContentValues contentValues = new ContentValues();

        contentValues.put(SamplingColumns.PRODUCT_NO, mProduct.mnNo);
        contentValues.put(SamplingColumns.REPORT_NO, strReportId);
        contentValues.put(SamplingColumns.NUMBER_SAMPLING, mStrNumSampling);
        contentValues.put(SamplingColumns.AQL, mStrAql);
        contentValues.put(SamplingColumns.ACCEPT, mStrNumAccept);
        contentValues.put(SamplingColumns.REJECT, mStrNumReject);

        super.save(SamplingColumns.CONTENT_URI, SamplingColumns.SAMPLING_NO, contentValues);
    }
}
