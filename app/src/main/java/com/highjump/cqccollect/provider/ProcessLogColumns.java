package com.highjump.cqccollect.provider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by highjump on 15-1-30.
 */
public class ProcessLogColumns implements BaseColumns {

    public static final Uri CONTENT_URI = Uri.parse("content://" + MyContentProvider.AUTHORITY + "/" + MyContentProvider.PROCESS_LOG_TABLE_NAME);

    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.cqccollect.processlog";

    public static final String NO = "process_no";
    public static final String USERNAME = "username";
    public static final String PROCESS = "process";
    public static final String PROCESS_TIME = "process_time";

    /**
     * Sort order
     */
    public static final String SORT_ORDER_DEFAULT = PROCESS_TIME + " DESC";

}
