package com.highjump.cqccollect.provider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by highjump on 15-1-30.
 */
public class ExportCartonColumns implements BaseColumns {

    public static final Uri CONTENT_URI = Uri.parse("content://" + MyContentProvider.AUTHORITY + "/" + MyContentProvider.EXPORT_CARTON_TABLE_NAME);

    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.cqccollect.exportcarton";

    public static final String EXPORT_NO = "export_no";
    public static final String PRODUCT_NO = "product_no";
    public static final String REPORT_NO = "report_no";
    public static final String PCS_CARTON = "pcs_carton";
    public static final String EXTERNAL_DIMENSION = "external_dimension";
    public static final String THICKNESS = "thickness";
    public static final String LAYERS = "layers";
    public static final String SEALING_TYPE = "sealing_type";

}
