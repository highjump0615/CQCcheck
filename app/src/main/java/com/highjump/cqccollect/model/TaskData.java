package com.highjump.cqccollect.model;

import java.util.Date;

/**
 * Created by highjump on 15-2-1.
 */
public class TaskData {

    public String mStrTaskNo;
    public String mStrReportNo;
    public String mStrVerifyPlace;
    public String mStrApplicant;
    public String mStrDateSchedule;

    public Boolean mbDownloaded;

    public TaskData() {
        mbDownloaded = false;
    }
}
