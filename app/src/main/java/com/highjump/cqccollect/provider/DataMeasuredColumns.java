package com.highjump.cqccollect.provider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by highjump on 15-1-30.
 */
public class DataMeasuredColumns implements BaseColumns {

    public static final Uri CONTENT_URI = Uri.parse("content://" + MyContentProvider.AUTHORITY + "/" + MyContentProvider.DATA_MEASURED_TABLE_NAME);

    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.cqccollect.datameasured";

    public static final String MEASURED_NO = "measured_no";
    public static final String REPORT_NO = "report_no";
    public static final String PRODUCT_NO = "product_no";
    public static final String NUMBER_SAMPLING = "number_sampling";
    public static final String ELECTRIC_STRENGTH = "electric_strength";
    public static final String EARTHING_RESISTANCE = "earthing_resistance";
    public static final String LEAKAGE_CURRENT = "leakage_current";

}
