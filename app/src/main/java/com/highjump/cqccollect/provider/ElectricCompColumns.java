package com.highjump.cqccollect.provider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by highjump on 15-1-30.
 */
public class ElectricCompColumns implements BaseColumns {

    public static final Uri CONTENT_URI = Uri.parse("content://" + MyContentProvider.AUTHORITY + "/" + MyContentProvider.ELECTRIC_COMP_TABLE_NAME);

    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.cqccollect.electriccomp";

    public static final String COMPONENT_NO = "component_no";
    public static final String REPORT_NO = "report_no";
    public static final String COMPONENT_NAME = "component_name";
    public static final String TRADE_MARK = "trade_mark";
    public static final String MODEL = "model";
    public static final String SPECIFICATION = "specification";
    public static final String CONFORMITY_MARK = "conformity_mark";
    public static final String SAME_AS = "same_as";

}
