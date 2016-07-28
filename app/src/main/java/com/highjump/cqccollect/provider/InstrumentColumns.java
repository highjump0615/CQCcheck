package com.highjump.cqccollect.provider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by highjump on 15-1-30.
 */
public class InstrumentColumns implements BaseColumns {

    public static final Uri CONTENT_URI = Uri.parse("content://" + MyContentProvider.AUTHORITY + "/" + MyContentProvider.INSTRUMENT_TABLE_NAME);

    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.cqccollect.instrument";

    public static final String INSTRUMENT_NO = "instruments_no";
    public static final String REPORT_NO = "report_no";
    public static final String INSTRUMENT_NAME = "instruments_name";
    public static final String MODEL = "model";
    public static final String REF_NO = "ref_no";
    public static final String VALIDITY_CALIBRATION = "validity_calibration";

}
