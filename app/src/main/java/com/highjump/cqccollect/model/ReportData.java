package com.highjump.cqccollect.model;

/**
 * Created by highjump on 15-2-1.
 */
public class ReportData {

    public String mStrId;
    public String mStrTime;
    public String mStrPlace;
    public String mStrStatus;

    public static final String REPORT_STATUS_PREPARE = "等待核查";
    public static final String REPORT_STATUS_INSPECTING = "正在核查";
    public static final String REPORT_STATUS_WAIT_SUBMIT = "等待上报";
    public static final String REPORT_STATUS_SUBMITTED = "完成上报";
    public static final String REPORT_STATUS_MODIFY = "修改补充";
}
