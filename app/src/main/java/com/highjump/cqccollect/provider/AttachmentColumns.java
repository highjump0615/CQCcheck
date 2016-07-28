package com.highjump.cqccollect.provider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by highjump on 15-1-30.
 */
public class AttachmentColumns implements BaseColumns {

    public static final Uri CONTENT_URI = Uri.parse("content://" + MyContentProvider.AUTHORITY + "/" + MyContentProvider.ATTACHMENT_TABLE_NAME);

    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.cqccollect.attachment";

    public static final String ATTACHMENT_NO = "attachment_no";
    public static final String REPORT_NO = "report_no";
    public static final String FILE_NAME = "filename";
}
