package com.highjump.cqccollect.provider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by highjump on 15-1-30.
 */
public class ElectricSpecColumns implements BaseColumns {

    public static final Uri CONTENT_URI = Uri.parse("content://" + MyContentProvider.AUTHORITY + "/" + MyContentProvider.ELECTRIC_SPEC_TABLE_NAME);

    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.cqccollect.electricspec";

    public static final String SPECIFICATION_NO = "specification_no";
    public static final String PRODUCT_NO = "product_no";
    public static final String REPORT_NO = "report_no";
    public static final String RATED_VOLTAGE = "rated_voltage";
    public static final String RATED_POWER = "rated_power";
    public static final String RATED_FREQUENCY = "rated_frequency";
    public static final String ELECTRICAL_CLASS = "electrical_class";
    public static final String IP_GRADE = "ip_grade";
    public static final String CABLE_SPECIFICATION = "cable_specification";
    public static final String CONNECTION_TYPE = "connection_type";
    public static final String PLUG = "plug";

}
