package com.highjump.cqccollect.provider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by highjump on 15-1-30.
 */
public class ReportColumns implements BaseColumns {

    public static final Uri CONTENT_URI = Uri.parse("content://" + MyContentProvider.AUTHORITY + "/" + MyContentProvider.REPORT_TABLE_NAME);

    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.cqccollect.report";

    public static final String NO = "report_no";
    public static final String APPLICATION_NO = "application_no";
    public static final String TASK_NO = "task_no";
    public static final String APPROVAL_DATE = "approval_date";
    public static final String APPROVED_BY_C = "approved_by_c";
    public static final String APPROVED_BY_E = "approved_by_e";
    public static final String CONCLUSION = "conclusion";
    public static final String VERIFICATION_DATE = "verification_date";
    public static final String VERIFICATION_DATE_END = "verification_date_end";
    public static final String VERIFICATION_PLACE_C = "verification_place_c";
    public static final String VERIFICATION_PLACE_E = "verification_place_e";
    public static final String CONFIRM_DATE = "confirm_date";
    public static final String CONFIRM_SIGNING = "confirm_signing";
    public static final String APPLICANT_C = "applicant_c";
    public static final String APPLICANT_E = "applicant_e";
    public static final String APPLICANT_ADDRESS_C = "applicant_address_c";
    public static final String APPLICANT_ADDRESS_E = "applicant_address_e";
    public static final String APPLICANT_TEL = "applicant_tel";
    public static final String APPLICANT_EMAIL = "applicant_email";
    public static final String MANUFACTURER_C = "manufacturer_c";
    public static final String MANUFACTURER_E = "manufacturer_e";
    public static final String MANUFACTURER_ADDRESS_C = "manufacturer_address_c";
    public static final String MANUFACTURER_ADDRESS_E = "manufacturer_address_e";
    public static final String IMPORTER_C = "importer_c";
    public static final String IMPORTER_E = "importer_e";
    public static final String IMPORTER_ADDRESS_C = "importer_address_c";
    public static final String IMPORTER_ADDRESS_E = "importer_address_e";
    public static final String COMMERCIAL_INVOICE_NO = "commercial_invoice_no";
    public static final String ACCESSORIES_DESCRIPTION_C = "accessories_description_c";
    public static final String ACCESSORIES_DESCRIPTION_E = "accessories_description_e";
    public static final String SAMPLING_CONCLUSION_C = "sampling_conclusions_c";
    public static final String SAMPLING_CONCLUSION_E = "sampling_conclusions_e";
    public static final String WITNESS_TEST_C = "witness_test_c";
    public static final String WITNESS_TEST_E = "witness_test_e";
    public static final String ADDITIONAL_INFORMATION_C = "additional_information_c";
    public static final String ADDITIONAL_INFORMATION_E = "additional_information_e";
    public static final String VERIFICATION_LOCATION = "verification_location";
    public static final String LOCATE_TIME = "locate_time";
    public static final String LOCATION_PHOTO = "location_photo";
    public static final String PHOTO_LOCATION = "photo_location";
    public static final String PHOTO_TIME = "photo_time";
    public static final String INSPECTOR_C = "inspector_c";
    public static final String INSPECTOR_E = "inspector_e";
    public static final String INSPECTOR_DATE = "inspector_date";
    public static final String INSPECTOR_SIGNING = "inspector_signing";
    public static final String FACTORY_C = "factory_c";
    public static final String FACTORY_E = "factory_e";
    public static final String FACTORY_ADDRESS_C = "factory_address_c";
    public static final String FACTORY_ADDRESS_E = "factory_address_e";
    public static final String EXPORTER_C = "exporter_c";
    public static final String EXPORTER_E = "exporter_e";
    public static final String EXPORTER_ADDRESS_C = "exporter_address_c";
    public static final String EXPORTER_ADDRESS_E = "exporter_address_e";
    public static final String USERNAME = "username";
    public static final String EDIT_TIME = "edit_time";
    public static final String REPORT_STATUS = "report_status";
    public static final String REPORT_TYPE = "report_type";

}
