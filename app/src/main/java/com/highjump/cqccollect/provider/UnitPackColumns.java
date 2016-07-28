package com.highjump.cqccollect.provider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by highjump on 15-1-30.
 */
public class UnitPackColumns implements BaseColumns {

    public static final Uri CONTENT_URI = Uri.parse("content://" + MyContentProvider.AUTHORITY + "/" + MyContentProvider.UNIT_PACK_TABLE_NAME);

    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.cqccollect.unitpack";

    public static final String PACKAGING_NO = "packaging_no";
    public static final String PRODUCT_NO = "product_no";
    public static final String REPORT_NO = "report_no";
    public static final String PACKAGING_TYPE = "packaging_type";
    public static final String DIMENSION = "dimension";
    public static final String THICKNESS = "thickness";
    public static final String LAYERS = "layers";
    public static final String PRINTING_COLOR = "printing_color";
    public static final String SEALING_TYPE = "sealing_type";

}
