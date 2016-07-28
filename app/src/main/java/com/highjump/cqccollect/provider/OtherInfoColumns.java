package com.highjump.cqccollect.provider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by highjump on 15-1-30.
 */
public class OtherInfoColumns implements BaseColumns {

    public static final Uri CONTENT_URI = Uri.parse("content://" + MyContentProvider.AUTHORITY + "/" + MyContentProvider.OTHER_INFO_TABLE_NAME);

    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.cqccollect.otherinfo";

    public static final String OTHER_NO = "other_no";
    public static final String REPORT_NO = "report_no";
    public static final String OTHER_INFO = "other_information";
}
