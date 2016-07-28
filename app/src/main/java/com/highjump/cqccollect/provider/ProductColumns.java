package com.highjump.cqccollect.provider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by highjump on 15-1-30.
 */
public class ProductColumns implements BaseColumns {

    public static final Uri CONTENT_URI = Uri.parse("content://" + MyContentProvider.AUTHORITY + "/" + MyContentProvider.PRODUCT_TABLE_NAME);

    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.cqccollect.product";

    public static final String PRODUCT_NO = "product_no";
    public static final String REPORT_NO = "report_no";
    public static final String PRODUCT_NAME = "product_name";
    public static final String QUANTITY = "quantity";
    public static final String PRODUCT_DESCRIPTION = "product_description";
    public static final String PACKAGE_MANNER = "package_manner";

}
