package com.highjump.cqccollect.model;

import android.content.ContentUris;
import android.content.ContentValues;
import android.net.Uri;

import com.highjump.cqccollect.provider.ElectricCompColumns;
import com.highjump.cqccollect.provider.MyContentProvider;

/**
 * Created by highjump on 15-2-1.
 */
public class ElectricCompData extends BaseData {

    public String mStrName;
    public String mStrTradeMark;
    public String mStrModel;
    public String mStrSpec;
    public String mStrConformityMark;
    public int mnSameAs;

    public void save(String strReportId) {

        if (mStatus == DS_NONE) {
            return;
        }

        ContentValues contentValues = new ContentValues();

        contentValues.put(ElectricCompColumns.REPORT_NO, strReportId);
        contentValues.put(ElectricCompColumns.COMPONENT_NAME, mStrName);
        contentValues.put(ElectricCompColumns.TRADE_MARK, mStrTradeMark);
        contentValues.put(ElectricCompColumns.MODEL, mStrModel);
        contentValues.put(ElectricCompColumns.SPECIFICATION, mStrSpec);
        contentValues.put(ElectricCompColumns.CONFORMITY_MARK, mStrConformityMark);
        contentValues.put(ElectricCompColumns.SAME_AS, mnSameAs);

        super.save(ElectricCompColumns.CONTENT_URI, ElectricCompColumns.COMPONENT_NO, contentValues);
    }
}
