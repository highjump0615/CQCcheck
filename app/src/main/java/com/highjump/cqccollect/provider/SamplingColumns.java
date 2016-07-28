package com.highjump.cqccollect.provider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by highjump on 15-1-30.
 */
public class SamplingColumns implements BaseColumns {

    public static final Uri CONTENT_URI = Uri.parse("content://" + MyContentProvider.AUTHORITY + "/" + MyContentProvider.SAMPLING_TABLE_NAME);

    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.cqccollect.sampling";

    public static final String SAMPLING_NO = "sampling_no";
    public static final String PRODUCT_NO = "product_no";
    public static final String REPORT_NO = "report_no";
    public static final String NUMBER_SAMPLING = "number_sampling";
    public static final String AQL = "aql";
    public static final String ACCEPT = "accept";
    public static final String REJECT = "reject";

}
