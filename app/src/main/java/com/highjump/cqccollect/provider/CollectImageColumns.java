package com.highjump.cqccollect.provider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by highjump on 15-1-30.
 */
public class CollectImageColumns implements BaseColumns {

    public static final Uri CONTENT_URI = Uri.parse("content://" + MyContentProvider.AUTHORITY + "/" + MyContentProvider.COLLECT_IMG_TABLE_NAME);

    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.cqccollect.collectimg";

    public static final String IMAGE_NO = "image_no";
    public static final String REPORT_NO = "report_no";
    public static final String IMAGE_ADDRESS = "image_address";
    public static final String IMAGE_DESCRIPTION = "image_description";
    public static final String IMAGE_TYPE = "image_type";
    public static final String UPLOADED = "uploaded";

}
