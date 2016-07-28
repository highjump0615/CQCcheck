package com.highjump.cqccollect.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

import com.highjump.cqccollect.model.ElectricSpecData;
import com.highjump.cqccollect.model.UserData;
import com.highjump.cqccollect.utils.CommonUtils;
import com.highjump.cqccollect.utils.Config;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.LinkedHashMap;

/**
 * Created by highjump on 15-1-30.
 */
public class MyContentProvider extends ContentProvider {

    private static final String TAG = MyContentProvider.class.getSimpleName();

    /**
     * Database version.
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * 数据库名字
     */
    public static final String DATABASE_NAME = "database.sqlite";

    /**
     * 数据库权威
     */
    public static final String AUTHORITY = MyContentProvider.class.getCanonicalName();  //"com.highjump.cqccollect.provider.MyContentProvider";

    /**
     * 数据库辅助
     */
    private static DatabaseHelper dbHelper;

    /**
     * 来帮助匹配Uri
     */
    private static final UriMatcher sUriMatcher;

    /*
     * 数据库表名字
     */
    protected static final String USER_TABLE_NAME = "user_certificated";
    protected static final String REPORT_TABLE_NAME = "verification_summary";
    protected static final String PROCESS_LOG_TABLE_NAME = "process_log";
    protected static final String PRODUCT_TABLE_NAME = "product";
    protected static final String ATTACHMENT_TABLE_NAME = "application_attachment";
    protected static final String ELECTRIC_SPEC_TABLE_NAME = "electrical_specification";
    protected static final String DATA_MEASURED_TABLE_NAME = "data_measured";
    protected static final String INSTRUMENT_TABLE_NAME = "instruments";
    protected static final String ELECTRIC_COMP_TABLE_NAME = "electrical_components";
    protected static final String EXPORT_CARTON_TABLE_NAME = "export_carton";
    protected static final String UNIT_PACK_TABLE_NAME = "unit_packaging";
    protected static final String SAMPLING_TABLE_NAME = "sampling";
    protected static final String COLLECT_IMG_TABLE_NAME = "collect_images";
    protected static final String OTHER_INFO_TABLE_NAME = "other_information";

    /*
     * 数据库表识别符号
     */
    private static final int ID_USER_TABLE = 1;
    private static final int ID_REPORT_TABLE = 2;
    private static final int ID_PROCESS_LOG_TABLE = 3;
    private static final int ID_PRODUCT_TABLE = 4;
    private static final int ID_ELECTRIC_SPEC_TABLE = 5;
    private static final int ID_DATA_MEASURED_TABLE = 6;
    private static final int ID_INSTRUMENT_TABLE = 7;
    private static final int ID_ELECTRIC_COMP_TABLE = 8;
    private static final int ID_EXPORT_CARTON_TABLE = 9;
    private static final int ID_UNIT_PACK_TABLE = 10;
    private static final int ID_SAMPLING_TABLE = 11;
    private static final int ID_COLLECT_IMG_TABLE = 12;
    private static final int ID_OTHER_INFO_TABLE = 13;
    private static final int ID_ATTACHMENT_TABLE = 14;

    /*
     * 为数据库表投影映射
     */
    private static final LinkedHashMap<String, String> projectionMapUser;
    private static final LinkedHashMap<String, String> projectionMapReport;
    private static final LinkedHashMap<String, String> projectionMapProcessLog;
    private static final LinkedHashMap<String, String> projectionMapProduct;
    private static final LinkedHashMap<String, String> projectionMapElectricSpec;
    private static final LinkedHashMap<String, String> projectionMapDataMeasured;
    private static final LinkedHashMap<String, String> projectionMapInstrument;
    private static final LinkedHashMap<String, String> projectionMapElectricComp;
    private static final LinkedHashMap<String, String> projectionMapExportCarton;
    private static final LinkedHashMap<String, String> projectionMapUnitPack;
    private static final LinkedHashMap<String, String> projectionMapSampling;
    private static final LinkedHashMap<String, String> projectionMapCollectImg;
    private static final LinkedHashMap<String, String> projectionMapOtherInfo;
    private static final LinkedHashMap<String, String> projectionMapAttachment;


    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        sUriMatcher.addURI(AUTHORITY, USER_TABLE_NAME, ID_USER_TABLE);
        sUriMatcher.addURI(AUTHORITY, REPORT_TABLE_NAME, ID_REPORT_TABLE);
        sUriMatcher.addURI(AUTHORITY, PROCESS_LOG_TABLE_NAME, ID_PROCESS_LOG_TABLE);
        sUriMatcher.addURI(AUTHORITY, PRODUCT_TABLE_NAME, ID_PRODUCT_TABLE);
        sUriMatcher.addURI(AUTHORITY, ELECTRIC_SPEC_TABLE_NAME, ID_ELECTRIC_SPEC_TABLE);
        sUriMatcher.addURI(AUTHORITY, DATA_MEASURED_TABLE_NAME, ID_DATA_MEASURED_TABLE);
        sUriMatcher.addURI(AUTHORITY, INSTRUMENT_TABLE_NAME, ID_INSTRUMENT_TABLE);
        sUriMatcher.addURI(AUTHORITY, ELECTRIC_COMP_TABLE_NAME, ID_ELECTRIC_COMP_TABLE);
        sUriMatcher.addURI(AUTHORITY, EXPORT_CARTON_TABLE_NAME, ID_EXPORT_CARTON_TABLE);
        sUriMatcher.addURI(AUTHORITY, UNIT_PACK_TABLE_NAME, ID_UNIT_PACK_TABLE);
        sUriMatcher.addURI(AUTHORITY, SAMPLING_TABLE_NAME, ID_SAMPLING_TABLE);
        sUriMatcher.addURI(AUTHORITY, COLLECT_IMG_TABLE_NAME, ID_COLLECT_IMG_TABLE);
        sUriMatcher.addURI(AUTHORITY, OTHER_INFO_TABLE_NAME, ID_OTHER_INFO_TABLE);
        sUriMatcher.addURI(AUTHORITY, ATTACHMENT_TABLE_NAME, ID_ATTACHMENT_TABLE);

        // user table
        projectionMapUser = new LinkedHashMap<String, String>();
        projectionMapUser.put(UserColumns.USERNAME, UserColumns.USERNAME);      //0
        projectionMapUser.put(UserColumns.MD, UserColumns.MD);
        projectionMapUser.put(UserColumns.NAME, UserColumns.NAME);
        projectionMapUser.put(UserColumns.UNIT, UserColumns.UNIT);
        projectionMapUser.put(UserColumns.ROLE, UserColumns.ROLE);

        // report table
        projectionMapReport = new LinkedHashMap<String, String>();
        projectionMapReport.put(ReportColumns.NO, ReportColumns.NO);
        projectionMapReport.put(ReportColumns.APPLICATION_NO, ReportColumns.APPLICATION_NO);
        projectionMapReport.put(ReportColumns.TASK_NO, ReportColumns.TASK_NO);
        projectionMapReport.put(ReportColumns.APPROVAL_DATE, ReportColumns.APPROVAL_DATE);
        projectionMapReport.put(ReportColumns.APPROVED_BY_C, ReportColumns.APPROVED_BY_C);
        projectionMapReport.put(ReportColumns.APPROVED_BY_E, ReportColumns.APPROVED_BY_E);
        projectionMapReport.put(ReportColumns.CONCLUSION, ReportColumns.CONCLUSION);
        projectionMapReport.put(ReportColumns.VERIFICATION_DATE, ReportColumns.VERIFICATION_DATE);
        projectionMapReport.put(ReportColumns.VERIFICATION_DATE_END, ReportColumns.VERIFICATION_DATE_END);
        projectionMapReport.put(ReportColumns.VERIFICATION_PLACE_C, ReportColumns.VERIFICATION_PLACE_C);
        projectionMapReport.put(ReportColumns.VERIFICATION_PLACE_E, ReportColumns.VERIFICATION_PLACE_E);
        projectionMapReport.put(ReportColumns.CONFIRM_DATE, ReportColumns.CONFIRM_DATE);
        projectionMapReport.put(ReportColumns.CONFIRM_SIGNING, ReportColumns.CONFIRM_SIGNING);
        projectionMapReport.put(ReportColumns.APPLICANT_C, ReportColumns.APPLICANT_C);
        projectionMapReport.put(ReportColumns.APPLICANT_E, ReportColumns.APPLICANT_E);
        projectionMapReport.put(ReportColumns.APPLICANT_ADDRESS_C, ReportColumns.APPLICANT_ADDRESS_C);
        projectionMapReport.put(ReportColumns.APPLICANT_ADDRESS_E, ReportColumns.APPLICANT_ADDRESS_E);
        projectionMapReport.put(ReportColumns.APPLICANT_TEL, ReportColumns.APPLICANT_TEL);
        projectionMapReport.put(ReportColumns.APPLICANT_EMAIL, ReportColumns.APPLICANT_EMAIL);
        projectionMapReport.put(ReportColumns.MANUFACTURER_C, ReportColumns.MANUFACTURER_C);
        projectionMapReport.put(ReportColumns.MANUFACTURER_E, ReportColumns.MANUFACTURER_E);
        projectionMapReport.put(ReportColumns.MANUFACTURER_ADDRESS_C, ReportColumns.MANUFACTURER_ADDRESS_C);
        projectionMapReport.put(ReportColumns.MANUFACTURER_ADDRESS_E, ReportColumns.MANUFACTURER_ADDRESS_E);
        projectionMapReport.put(ReportColumns.IMPORTER_C, ReportColumns.IMPORTER_C);
        projectionMapReport.put(ReportColumns.IMPORTER_E, ReportColumns.IMPORTER_E);
        projectionMapReport.put(ReportColumns.IMPORTER_ADDRESS_C, ReportColumns.IMPORTER_ADDRESS_C);
        projectionMapReport.put(ReportColumns.IMPORTER_ADDRESS_E, ReportColumns.IMPORTER_ADDRESS_E);
        projectionMapReport.put(ReportColumns.COMMERCIAL_INVOICE_NO, ReportColumns.COMMERCIAL_INVOICE_NO);
        projectionMapReport.put(ReportColumns.ACCESSORIES_DESCRIPTION_C, ReportColumns.ACCESSORIES_DESCRIPTION_C);
        projectionMapReport.put(ReportColumns.ACCESSORIES_DESCRIPTION_E, ReportColumns.ACCESSORIES_DESCRIPTION_E);
        projectionMapReport.put(ReportColumns.SAMPLING_CONCLUSION_C, ReportColumns.SAMPLING_CONCLUSION_C);
        projectionMapReport.put(ReportColumns.SAMPLING_CONCLUSION_E, ReportColumns.SAMPLING_CONCLUSION_E);
        projectionMapReport.put(ReportColumns.WITNESS_TEST_C, ReportColumns.WITNESS_TEST_C);
        projectionMapReport.put(ReportColumns.WITNESS_TEST_E, ReportColumns.WITNESS_TEST_E);
        projectionMapReport.put(ReportColumns.ADDITIONAL_INFORMATION_C, ReportColumns.ADDITIONAL_INFORMATION_C);
        projectionMapReport.put(ReportColumns.ADDITIONAL_INFORMATION_E, ReportColumns.ADDITIONAL_INFORMATION_E);
        projectionMapReport.put(ReportColumns.VERIFICATION_LOCATION, ReportColumns.VERIFICATION_LOCATION);
        projectionMapReport.put(ReportColumns.LOCATE_TIME, ReportColumns.LOCATE_TIME);
        projectionMapReport.put(ReportColumns.LOCATION_PHOTO, ReportColumns.LOCATION_PHOTO);
        projectionMapReport.put(ReportColumns.PHOTO_TIME, ReportColumns.PHOTO_TIME);
        projectionMapReport.put(ReportColumns.PHOTO_LOCATION, ReportColumns.PHOTO_LOCATION);
        projectionMapReport.put(ReportColumns.INSPECTOR_C, ReportColumns.INSPECTOR_C);
        projectionMapReport.put(ReportColumns.INSPECTOR_E, ReportColumns.INSPECTOR_E);
        projectionMapReport.put(ReportColumns.INSPECTOR_DATE, ReportColumns.INSPECTOR_DATE);
        projectionMapReport.put(ReportColumns.INSPECTOR_SIGNING, ReportColumns.INSPECTOR_SIGNING);
        projectionMapReport.put(ReportColumns.FACTORY_C, ReportColumns.FACTORY_C);
        projectionMapReport.put(ReportColumns.FACTORY_E, ReportColumns.FACTORY_E);
        projectionMapReport.put(ReportColumns.FACTORY_ADDRESS_C, ReportColumns.FACTORY_ADDRESS_C);
        projectionMapReport.put(ReportColumns.FACTORY_ADDRESS_E, ReportColumns.FACTORY_ADDRESS_E);
        projectionMapReport.put(ReportColumns.EXPORTER_C, ReportColumns.EXPORTER_C);
        projectionMapReport.put(ReportColumns.EXPORTER_E, ReportColumns.EXPORTER_E);
        projectionMapReport.put(ReportColumns.EXPORTER_ADDRESS_C, ReportColumns.EXPORTER_ADDRESS_C);
        projectionMapReport.put(ReportColumns.EXPORTER_ADDRESS_E, ReportColumns.EXPORTER_ADDRESS_E);
        projectionMapReport.put(ReportColumns.USERNAME, ReportColumns.USERNAME);
        projectionMapReport.put(ReportColumns.EDIT_TIME, ReportColumns.EDIT_TIME);
        projectionMapReport.put(ReportColumns.REPORT_STATUS, ReportColumns.REPORT_STATUS);
        projectionMapReport.put(ReportColumns.REPORT_TYPE, ReportColumns.REPORT_TYPE);

        // process log table
        projectionMapProcessLog = new LinkedHashMap<String, String>();
        projectionMapProcessLog.put(ProcessLogColumns.NO, ProcessLogColumns.NO);
        projectionMapProcessLog.put(ProcessLogColumns.USERNAME, ProcessLogColumns.USERNAME);
        projectionMapProcessLog.put(ProcessLogColumns.PROCESS, ProcessLogColumns.PROCESS);
        projectionMapProcessLog.put(ProcessLogColumns.PROCESS_TIME, ProcessLogColumns.PROCESS_TIME);

        // product table
        projectionMapProduct = new LinkedHashMap<String, String>();
        projectionMapProduct.put(ProductColumns.PRODUCT_NO, ProductColumns.PRODUCT_NO);
        projectionMapProduct.put(ProductColumns.REPORT_NO, ProductColumns.REPORT_NO);
        projectionMapProduct.put(ProductColumns.PRODUCT_NAME, ProductColumns.PRODUCT_NAME);
        projectionMapProduct.put(ProductColumns.QUANTITY, ProductColumns.QUANTITY);
        projectionMapProduct.put(ProductColumns.PRODUCT_DESCRIPTION, ProductColumns.PRODUCT_DESCRIPTION);
        projectionMapProduct.put(ProductColumns.PACKAGE_MANNER, ProductColumns.PACKAGE_MANNER);

        // electric spec table
        projectionMapElectricSpec = new LinkedHashMap<String, String>();
        projectionMapElectricSpec.put(ElectricSpecColumns.SPECIFICATION_NO, ElectricSpecColumns.SPECIFICATION_NO);
        projectionMapElectricSpec.put(ElectricSpecColumns.PRODUCT_NO, ElectricSpecColumns.PRODUCT_NO);
        projectionMapElectricSpec.put(ElectricSpecColumns.REPORT_NO, ElectricSpecColumns.REPORT_NO);
        projectionMapElectricSpec.put(ElectricSpecColumns.RATED_VOLTAGE, ElectricSpecColumns.RATED_VOLTAGE);
        projectionMapElectricSpec.put(ElectricSpecColumns.RATED_POWER, ElectricSpecColumns.RATED_POWER);
        projectionMapElectricSpec.put(ElectricSpecColumns.RATED_FREQUENCY, ElectricSpecColumns.RATED_FREQUENCY);
        projectionMapElectricSpec.put(ElectricSpecColumns.ELECTRICAL_CLASS, ElectricSpecColumns.ELECTRICAL_CLASS);
        projectionMapElectricSpec.put(ElectricSpecColumns.IP_GRADE, ElectricSpecColumns.IP_GRADE);
        projectionMapElectricSpec.put(ElectricSpecColumns.CABLE_SPECIFICATION, ElectricSpecColumns.CABLE_SPECIFICATION);
        projectionMapElectricSpec.put(ElectricSpecColumns.CONNECTION_TYPE, ElectricSpecColumns.CONNECTION_TYPE);
        projectionMapElectricSpec.put(ElectricSpecColumns.PLUG, ElectricSpecColumns.PLUG);

        // data measured table
        projectionMapDataMeasured = new LinkedHashMap<String, String>();
        projectionMapDataMeasured.put(DataMeasuredColumns.MEASURED_NO, DataMeasuredColumns.MEASURED_NO);
        projectionMapDataMeasured.put(DataMeasuredColumns.REPORT_NO, DataMeasuredColumns.REPORT_NO);
        projectionMapDataMeasured.put(DataMeasuredColumns.PRODUCT_NO, DataMeasuredColumns.PRODUCT_NO);
        projectionMapDataMeasured.put(DataMeasuredColumns.NUMBER_SAMPLING, DataMeasuredColumns.NUMBER_SAMPLING);
        projectionMapDataMeasured.put(DataMeasuredColumns.ELECTRIC_STRENGTH, DataMeasuredColumns.ELECTRIC_STRENGTH);
        projectionMapDataMeasured.put(DataMeasuredColumns.EARTHING_RESISTANCE, DataMeasuredColumns.EARTHING_RESISTANCE);
        projectionMapDataMeasured.put(DataMeasuredColumns.LEAKAGE_CURRENT, DataMeasuredColumns.LEAKAGE_CURRENT);

        // instrument table
        projectionMapInstrument = new LinkedHashMap<String, String>();
        projectionMapInstrument.put(InstrumentColumns.INSTRUMENT_NO, InstrumentColumns.INSTRUMENT_NO);
        projectionMapInstrument.put(InstrumentColumns.REPORT_NO, InstrumentColumns.REPORT_NO);
        projectionMapInstrument.put(InstrumentColumns.INSTRUMENT_NAME, InstrumentColumns.INSTRUMENT_NAME);
        projectionMapInstrument.put(InstrumentColumns.MODEL, InstrumentColumns.MODEL);
        projectionMapInstrument.put(InstrumentColumns.REF_NO, InstrumentColumns.REF_NO);
        projectionMapInstrument.put(InstrumentColumns.VALIDITY_CALIBRATION, InstrumentColumns.VALIDITY_CALIBRATION);

        // electric comp table
        projectionMapElectricComp = new LinkedHashMap<String, String>();
        projectionMapElectricComp.put(ElectricCompColumns.COMPONENT_NO, ElectricCompColumns.COMPONENT_NO);
        projectionMapElectricComp.put(ElectricCompColumns.REPORT_NO, ElectricCompColumns.REPORT_NO);
        projectionMapElectricComp.put(ElectricCompColumns.COMPONENT_NAME, ElectricCompColumns.COMPONENT_NAME);
        projectionMapElectricComp.put(ElectricCompColumns.TRADE_MARK, ElectricCompColumns.TRADE_MARK);
        projectionMapElectricComp.put(ElectricCompColumns.MODEL, ElectricCompColumns.MODEL);
        projectionMapElectricComp.put(ElectricCompColumns.SPECIFICATION, ElectricCompColumns.SPECIFICATION);
        projectionMapElectricComp.put(ElectricCompColumns.CONFORMITY_MARK, ElectricCompColumns.CONFORMITY_MARK);
        projectionMapElectricComp.put(ElectricCompColumns.SAME_AS, ElectricCompColumns.SAME_AS);

        // export carton table
        projectionMapExportCarton = new LinkedHashMap<String, String>();
        projectionMapExportCarton.put(ExportCartonColumns.EXPORT_NO, ExportCartonColumns.EXPORT_NO);
        projectionMapExportCarton.put(ExportCartonColumns.PRODUCT_NO, ExportCartonColumns.PRODUCT_NO);
        projectionMapExportCarton.put(ExportCartonColumns.REPORT_NO, ExportCartonColumns.REPORT_NO);
        projectionMapExportCarton.put(ExportCartonColumns.PCS_CARTON, ExportCartonColumns.PCS_CARTON);
        projectionMapExportCarton.put(ExportCartonColumns.EXTERNAL_DIMENSION, ExportCartonColumns.EXTERNAL_DIMENSION);
        projectionMapExportCarton.put(ExportCartonColumns.THICKNESS, ExportCartonColumns.THICKNESS);
        projectionMapExportCarton.put(ExportCartonColumns.LAYERS, ExportCartonColumns.LAYERS);
        projectionMapExportCarton.put(ExportCartonColumns.SEALING_TYPE, ExportCartonColumns.SEALING_TYPE);

        // unit pack table
        projectionMapUnitPack = new LinkedHashMap<String, String>();
        projectionMapUnitPack.put(UnitPackColumns.PACKAGING_NO, UnitPackColumns.PACKAGING_NO);
        projectionMapUnitPack.put(UnitPackColumns.PRODUCT_NO, UnitPackColumns.PRODUCT_NO);
        projectionMapUnitPack.put(UnitPackColumns.REPORT_NO, UnitPackColumns.REPORT_NO);
        projectionMapUnitPack.put(UnitPackColumns.PACKAGING_TYPE, UnitPackColumns.PACKAGING_TYPE);
        projectionMapUnitPack.put(UnitPackColumns.DIMENSION, UnitPackColumns.DIMENSION);
        projectionMapUnitPack.put(UnitPackColumns.THICKNESS, UnitPackColumns.THICKNESS);
        projectionMapUnitPack.put(UnitPackColumns.LAYERS, UnitPackColumns.LAYERS);
        projectionMapUnitPack.put(UnitPackColumns.PRINTING_COLOR, UnitPackColumns.PRINTING_COLOR);
        projectionMapUnitPack.put(UnitPackColumns.SEALING_TYPE, UnitPackColumns.SEALING_TYPE);

        // sampling table
        projectionMapSampling = new LinkedHashMap<String, String>();
        projectionMapSampling.put(SamplingColumns.SAMPLING_NO, SamplingColumns.SAMPLING_NO);
        projectionMapSampling.put(SamplingColumns.PRODUCT_NO, SamplingColumns.PRODUCT_NO);
        projectionMapSampling.put(SamplingColumns.REPORT_NO, SamplingColumns.REPORT_NO);
        projectionMapSampling.put(SamplingColumns.NUMBER_SAMPLING, SamplingColumns.NUMBER_SAMPLING);
        projectionMapSampling.put(SamplingColumns.AQL, SamplingColumns.AQL);
        projectionMapSampling.put(SamplingColumns.ACCEPT, SamplingColumns.ACCEPT);
        projectionMapSampling.put(SamplingColumns.REJECT, SamplingColumns.REJECT);

        // collect image table
        projectionMapCollectImg = new LinkedHashMap<String, String>();
        projectionMapCollectImg.put(CollectImageColumns.IMAGE_NO, CollectImageColumns.IMAGE_NO);
        projectionMapCollectImg.put(CollectImageColumns.REPORT_NO, CollectImageColumns.REPORT_NO);
        projectionMapCollectImg.put(CollectImageColumns.IMAGE_ADDRESS, CollectImageColumns.IMAGE_ADDRESS);
        projectionMapCollectImg.put(CollectImageColumns.IMAGE_DESCRIPTION, CollectImageColumns.IMAGE_DESCRIPTION);
        projectionMapCollectImg.put(CollectImageColumns.IMAGE_TYPE, CollectImageColumns.IMAGE_TYPE);
        projectionMapCollectImg.put(CollectImageColumns.UPLOADED, CollectImageColumns.UPLOADED);

        // other info table
        projectionMapOtherInfo = new LinkedHashMap<String, String>();
        projectionMapOtherInfo.put(OtherInfoColumns.OTHER_NO, OtherInfoColumns.OTHER_NO);
        projectionMapOtherInfo.put(OtherInfoColumns.REPORT_NO, OtherInfoColumns.REPORT_NO);
        projectionMapOtherInfo.put(OtherInfoColumns.OTHER_INFO, OtherInfoColumns.OTHER_INFO);

        // attachemnt table
        projectionMapAttachment = new LinkedHashMap<String, String>();
        projectionMapAttachment.put(AttachmentColumns.ATTACHMENT_NO, AttachmentColumns.ATTACHMENT_NO);
        projectionMapAttachment.put(AttachmentColumns.REPORT_NO, AttachmentColumns.REPORT_NO);
        projectionMapAttachment.put(AttachmentColumns.FILE_NAME, AttachmentColumns.FILE_NAME);
    }

    @Override
    public boolean onCreate() {
        dbHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        int matcher = sUriMatcher.match(uri);

        switch (matcher) {
            case ID_USER_TABLE:
                qb.setTables(USER_TABLE_NAME);
                qb.setProjectionMap(projectionMapUser);
                break;

            case ID_REPORT_TABLE:
                qb.setTables(REPORT_TABLE_NAME);
                qb.setProjectionMap(projectionMapReport);
                break;

            case ID_PROCESS_LOG_TABLE:
                qb.setTables(PROCESS_LOG_TABLE_NAME);
                qb.setProjectionMap(projectionMapProcessLog);
                break;

            case ID_PRODUCT_TABLE:
                qb.setTables(PRODUCT_TABLE_NAME);
                qb.setProjectionMap(projectionMapProduct);
                break;

            case ID_ELECTRIC_SPEC_TABLE:
                qb.setTables(ELECTRIC_SPEC_TABLE_NAME);
                qb.setProjectionMap(projectionMapElectricSpec);
                break;

            case ID_DATA_MEASURED_TABLE:
                qb.setTables(DATA_MEASURED_TABLE_NAME);
                qb.setProjectionMap(projectionMapDataMeasured);
                break;

            case ID_INSTRUMENT_TABLE:
                qb.setTables(INSTRUMENT_TABLE_NAME);
                qb.setProjectionMap(projectionMapInstrument);
                break;

            case ID_ELECTRIC_COMP_TABLE:
                qb.setTables(ELECTRIC_COMP_TABLE_NAME);
                qb.setProjectionMap(projectionMapElectricComp);
                break;

            case ID_EXPORT_CARTON_TABLE:
                qb.setTables(EXPORT_CARTON_TABLE_NAME);
                qb.setProjectionMap(projectionMapExportCarton);
                break;

            case ID_UNIT_PACK_TABLE:
                qb.setTables(UNIT_PACK_TABLE_NAME);
                qb.setProjectionMap(projectionMapUnitPack);
                break;

            case ID_SAMPLING_TABLE:
                qb.setTables(SAMPLING_TABLE_NAME);
                qb.setProjectionMap(projectionMapSampling);
                break;

            case ID_COLLECT_IMG_TABLE:
                qb.setTables(COLLECT_IMG_TABLE_NAME);
                qb.setProjectionMap(projectionMapCollectImg);
                break;

            case ID_OTHER_INFO_TABLE:
                qb.setTables(OTHER_INFO_TABLE_NAME);
                qb.setProjectionMap(projectionMapOtherInfo);
                break;

            case ID_ATTACHMENT_TABLE:
                qb.setTables(ATTACHMENT_TABLE_NAME);
                qb.setProjectionMap(projectionMapAttachment);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        if (db != null) {
            Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);

            if (c != null)
                c.setNotificationUri(dbHelper.mContext.getContentResolver(), uri);
            return c;
        }

        return null;
    }

    @Override
    public String getType(Uri uri) {

        int matcher = sUriMatcher.match(uri);

        switch (matcher) {
            case ID_USER_TABLE:
                return UserColumns.CONTENT_TYPE;

            case ID_REPORT_TABLE:
                return ReportColumns.CONTENT_TYPE;

            case ID_PROCESS_LOG_TABLE:
                return ProcessLogColumns.CONTENT_TYPE;

            case ID_PRODUCT_TABLE:
                return ProductColumns.CONTENT_TYPE;

            case ID_ELECTRIC_SPEC_TABLE:
                return ElectricSpecColumns.CONTENT_TYPE;

            case ID_DATA_MEASURED_TABLE:
                return DataMeasuredColumns.CONTENT_TYPE;

            case ID_INSTRUMENT_TABLE:
                return InstrumentColumns.CONTENT_TYPE;

            case ID_ELECTRIC_COMP_TABLE:
                return ElectricCompColumns.CONTENT_TYPE;

            case ID_EXPORT_CARTON_TABLE:
                return ExportCartonColumns.CONTENT_TYPE;

            case ID_UNIT_PACK_TABLE:
                return UnitPackColumns.CONTENT_TYPE;

            case ID_SAMPLING_TABLE:
                return SamplingColumns.CONTENT_TYPE;

            case ID_COLLECT_IMG_TABLE:
                return CollectImageColumns.CONTENT_TYPE;

            case ID_OTHER_INFO_TABLE:
                return OtherInfoColumns.CONTENT_TYPE;

            case ID_ATTACHMENT_TABLE:
                return AttachmentColumns.CONTENT_TYPE;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues initialValues) {

        ContentValues values;
        if (initialValues != null) {
            values = new ContentValues(initialValues);
        } else {
            values = new ContentValues();
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long rowId = -1;

        switch (sUriMatcher.match(uri)) {
            case ID_USER_TABLE:
                if (db != null) {
                    rowId = db.insert(USER_TABLE_NAME, null, values);
                }
                if (rowId > -1) {
                    Uri dataUri = ContentUris.withAppendedId(ElectricSpecColumns.CONTENT_URI, rowId);
                    dbHelper.mContext.getContentResolver().notifyChange(dataUri, null);
                    return dataUri;
                }
                else {
                    throw new SQLException("Failed to insert row into " + uri);
                }

            case ID_ELECTRIC_SPEC_TABLE:
                if (db != null) {
                    rowId = db.insert(ELECTRIC_SPEC_TABLE_NAME, null, values);
                }
                if (rowId > -1) {
                    Uri dataUri = ContentUris.withAppendedId(ElectricSpecColumns.CONTENT_URI, rowId);
                    dbHelper.mContext.getContentResolver().notifyChange(dataUri, null);
                    return dataUri;
                }
                else {
                    throw new SQLException("Failed to insert row into " + uri);
                }

            case ID_DATA_MEASURED_TABLE:
                if (db != null) {
                    rowId = db.insert(DATA_MEASURED_TABLE_NAME, null, values);
                }
                if (rowId > -1) {
                    Uri dataUri = ContentUris.withAppendedId(DataMeasuredColumns.CONTENT_URI, rowId);
                    dbHelper.mContext.getContentResolver().notifyChange(dataUri, null);
                    return dataUri;
                }
                else {
                    throw new SQLException("Failed to insert row into " + uri);
                }

            case ID_INSTRUMENT_TABLE:
                if (db != null) {
                    rowId = db.insert(INSTRUMENT_TABLE_NAME, null, values);
                }
                if (rowId > -1) {
                    Uri dataUri = ContentUris.withAppendedId(InstrumentColumns.CONTENT_URI, rowId);
                    dbHelper.mContext.getContentResolver().notifyChange(dataUri, null);
                    return dataUri;
                }
                else {
                    throw new SQLException("Failed to insert row into " + uri);
                }

            case ID_ELECTRIC_COMP_TABLE:
                if (db != null) {
                    rowId = db.insert(ELECTRIC_COMP_TABLE_NAME, null, values);
                }
                if (rowId > -1) {
                    Uri dataUri = ContentUris.withAppendedId(ElectricCompColumns.CONTENT_URI, rowId);
                    dbHelper.mContext.getContentResolver().notifyChange(dataUri, null);
                    return dataUri;
                }
                else {
                    throw new SQLException("Failed to insert row into " + uri);
                }

            case ID_EXPORT_CARTON_TABLE:
                if (db != null) {
                    rowId = db.insert(EXPORT_CARTON_TABLE_NAME, null, values);
                }
                if (rowId > -1) {
                    Uri dataUri = ContentUris.withAppendedId(ExportCartonColumns.CONTENT_URI, rowId);
                    dbHelper.mContext.getContentResolver().notifyChange(dataUri, null);
                    return dataUri;
                }
                else {
                    throw new SQLException("Failed to insert row into " + uri);
                }

            case ID_UNIT_PACK_TABLE:
                if (db != null) {
                    rowId = db.insert(UNIT_PACK_TABLE_NAME, null, values);
                }
                if (rowId > -1) {
                    Uri dataUri = ContentUris.withAppendedId(UnitPackColumns.CONTENT_URI, rowId);
                    dbHelper.mContext.getContentResolver().notifyChange(dataUri, null);
                    return dataUri;
                }
                else {
                    throw new SQLException("Failed to insert row into " + uri);
                }

            case ID_SAMPLING_TABLE:
                if (db != null) {
                    rowId = db.insert(SAMPLING_TABLE_NAME, null, values);
                }
                if (rowId > -1) {
                    Uri dataUri = ContentUris.withAppendedId(SamplingColumns.CONTENT_URI, rowId);
                    dbHelper.mContext.getContentResolver().notifyChange(dataUri, null);
                    return dataUri;
                }
                else {
                    throw new SQLException("Failed to insert row into " + uri);
                }

            case ID_COLLECT_IMG_TABLE:
                if (db != null) {
                    rowId = db.insert(COLLECT_IMG_TABLE_NAME, null, values);
                }
                if (rowId > -1) {
                    Uri dataUri = ContentUris.withAppendedId(CollectImageColumns.CONTENT_URI, rowId);
                    dbHelper.mContext.getContentResolver().notifyChange(dataUri, null);
                    return dataUri;
                }
                else {
                    throw new SQLException("Failed to insert row into " + uri);
                }

            case ID_REPORT_TABLE:
                if (db != null) {
                    rowId = db.insert(REPORT_TABLE_NAME, null, values);
                }
                if (rowId > -1) {
                    Uri dataUri = ContentUris.withAppendedId(ReportColumns.CONTENT_URI, rowId);
                    dbHelper.mContext.getContentResolver().notifyChange(dataUri, null);
                    return dataUri;
                }
                else {
                    throw new SQLException("Failed to insert row into " + uri);
                }

            case ID_PROCESS_LOG_TABLE:
                if (db != null) {
                    rowId = db.insert(PROCESS_LOG_TABLE_NAME, null, values);
                }
                if (rowId > -1) {
                    Uri dataUri = ContentUris.withAppendedId(ProcessLogColumns.CONTENT_URI, rowId);
                    dbHelper.mContext.getContentResolver().notifyChange(dataUri, null);
                    return dataUri;
                }
                else {
                    throw new SQLException("Failed to insert row into " + uri);
                }

            case ID_OTHER_INFO_TABLE:
                if (db != null) {
                    rowId = db.insert(OTHER_INFO_TABLE_NAME, null, values);
                }
                if (rowId > -1) {
                    Uri dataUri = ContentUris.withAppendedId(OtherInfoColumns.CONTENT_URI, rowId);
                    dbHelper.mContext.getContentResolver().notifyChange(dataUri, null);
                    return dataUri;
                }
                else {
                    throw new SQLException("Failed to insert row into " + uri);
                }

            case ID_PRODUCT_TABLE:
                if (db != null) {
                    rowId = db.insert(PRODUCT_TABLE_NAME, null, values);
                }
                if (rowId > -1) {
                    Uri dataUri = ContentUris.withAppendedId(ProductColumns.CONTENT_URI, rowId);
                    dbHelper.mContext.getContentResolver().notifyChange(dataUri, null);
                    return dataUri;
                }
                else {
                    throw new SQLException("Failed to insert row into " + uri);
                }

            case ID_ATTACHMENT_TABLE:
                if (db != null) {
                    rowId = db.insert(ATTACHMENT_TABLE_NAME, null, values);
                }
                if (rowId > -1) {
                    Uri dataUri = ContentUris.withAppendedId(AttachmentColumns.CONTENT_URI, rowId);
                    dbHelper.mContext.getContentResolver().notifyChange(dataUri, null);
                    return dataUri;
                }
                else {
                    throw new SQLException("Failed to insert row into " + uri);
                }

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db == null) return 0;

        int count;
        switch (sUriMatcher.match(uri)) {
            case ID_USER_TABLE:
                count = db.delete(USER_TABLE_NAME, selection, selectionArgs);
                break;

            case ID_ELECTRIC_SPEC_TABLE:
                count = db.delete(ELECTRIC_SPEC_TABLE_NAME, selection, selectionArgs);
                break;

            case ID_DATA_MEASURED_TABLE:
                count = db.delete(DATA_MEASURED_TABLE_NAME, selection, selectionArgs);
                break;

            case ID_INSTRUMENT_TABLE:
                count = db.delete(INSTRUMENT_TABLE_NAME, selection, selectionArgs);
                break;

            case ID_ELECTRIC_COMP_TABLE:
                count = db.delete(ELECTRIC_COMP_TABLE_NAME, selection, selectionArgs);
                break;

            case ID_EXPORT_CARTON_TABLE:
                count = db.delete(EXPORT_CARTON_TABLE_NAME, selection, selectionArgs);
                break;

            case ID_UNIT_PACK_TABLE:
                count = db.delete(UNIT_PACK_TABLE_NAME, selection, selectionArgs);
                break;

            case ID_SAMPLING_TABLE:
                count = db.delete(SAMPLING_TABLE_NAME, selection, selectionArgs);
                break;

            case ID_COLLECT_IMG_TABLE:
                count = db.delete(COLLECT_IMG_TABLE_NAME, selection, selectionArgs);
                break;

            case ID_REPORT_TABLE:
                count = db.delete(REPORT_TABLE_NAME, selection, selectionArgs);
                break;

            case ID_PROCESS_LOG_TABLE:
                count = db.delete(PROCESS_LOG_TABLE_NAME, selection, selectionArgs);
                break;

            case ID_PRODUCT_TABLE:
                count = db.delete(PRODUCT_TABLE_NAME, selection, selectionArgs);
                break;

            case ID_OTHER_INFO_TABLE:
                count = db.delete(OTHER_INFO_TABLE_NAME, selection, selectionArgs);
                break;

            case ID_ATTACHMENT_TABLE:
                count = db.delete(ATTACHMENT_TABLE_NAME, selection, selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        dbHelper.mContext.getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int matcher = sUriMatcher.match(uri);
        int count;

        if (db == null) return 0;

        switch (matcher) {
            case ID_USER_TABLE:
                count = db.update(USER_TABLE_NAME, values, selection, selectionArgs);
                break;

            case ID_ELECTRIC_SPEC_TABLE:
                count = db.update(ELECTRIC_SPEC_TABLE_NAME, values, selection, selectionArgs);
                break;

            case ID_DATA_MEASURED_TABLE:
                count = db.update(DATA_MEASURED_TABLE_NAME, values, selection, selectionArgs);
                break;

            case ID_INSTRUMENT_TABLE:
                count = db.update(INSTRUMENT_TABLE_NAME, values, selection, selectionArgs);
                break;

            case ID_ELECTRIC_COMP_TABLE:
                count = db.update(ELECTRIC_COMP_TABLE_NAME, values, selection, selectionArgs);
                break;

            case ID_EXPORT_CARTON_TABLE:
                count = db.update(EXPORT_CARTON_TABLE_NAME, values, selection, selectionArgs);
                break;

            case ID_UNIT_PACK_TABLE:
                count = db.update(UNIT_PACK_TABLE_NAME, values, selection, selectionArgs);
                break;

            case ID_SAMPLING_TABLE:
                count = db.update(SAMPLING_TABLE_NAME, values, selection, selectionArgs);
                break;

            case ID_COLLECT_IMG_TABLE:
                count = db.update(COLLECT_IMG_TABLE_NAME, values, selection, selectionArgs);
                break;

            case ID_REPORT_TABLE:
                count = db.update(REPORT_TABLE_NAME, values, selection, selectionArgs);
                break;

            case ID_PROCESS_LOG_TABLE:
                count = db.update(PROCESS_LOG_TABLE_NAME, values, selection, selectionArgs);
                break;

            case ID_PRODUCT_TABLE:
                count = db.update(PRODUCT_TABLE_NAME, values, selection, selectionArgs);
                break;

            case ID_OTHER_INFO_TABLE:
                count = db.update(OTHER_INFO_TABLE_NAME, values, selection, selectionArgs);
                break;

            case ID_ATTACHMENT_TABLE:
                count = db.update(ATTACHMENT_TABLE_NAME, values, selection, selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        dbHelper.mContext.getContentResolver().notifyChange(uri, null);
        return count;
    }


    /**
     * DatabaseHelper for English Learning Method
     */
    private static class DatabaseHelper extends SQLiteOpenHelper {

        //private SQLiteDatabase mDatabase;
        private final Context mContext;

        /**
         * Constructor
         * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
         *
         * @param context Application context for managing database
         */
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            mContext = context;
            try {
                createDatabase();
            } catch (IOException e) {
                if (Config.DEBUG) e.printStackTrace();
            }
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }

        /**
         * Creates a empty database on the system and rewrites it with your own database.
         */
        public void createDatabase() throws IOException {
            boolean dbExist = checkDatabase();

            if (dbExist) {
                //do nothing - database already exist
                Log.d(TAG, "createDatabase(): Database already exists!");
            } else {
                //By calling this method and empty database will be created into the default system path
                //of your application so we are gonna be able to overwrite that database with our database.
                this.getReadableDatabase();

                try {
                    copyDatabase();
                } catch (IOException e) {
                    throw new Error("Error copying database");
                }
            }
        }

        /**
         * Check if the database already exist to avoid re-copying the file each time you open the application.
         *
         * @return true if it exists, false if it doesn't
         */
        private boolean checkDatabase() {
            SQLiteDatabase checkDB = null;

            try {
                String myPath = mContext.getDatabasePath(DATABASE_NAME).getPath();
                checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
            } catch (SQLiteException e) {
                //database doesn't exist yet.
                if (Config.DEBUG) e.printStackTrace();
            }

            if (checkDB != null) {
                checkDB.close();
            }

            return checkDB != null;
        }

        /**
         * Copies your database from your local assets-folder to the just created empty database in the
         * system folder, from where it can be accessed and handled.
         * This is done by transferring byte stream.
         */
        private void copyDatabase() throws IOException {
            //Open your local db as the input stream
            InputStream myInput = mContext.getAssets().open(DATABASE_NAME);

            // Path to the just created empty db
            String outFileName = mContext.getDatabasePath(DATABASE_NAME).getPath();

            //Open the empty db as the output stream
            OutputStream myOutput = new FileOutputStream(outFileName);

            //transfer bytes from the input file to the output file
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }

            //Close the streams
            myOutput.flush();
            myOutput.close();
            myInput.close();
        }

        /*
        public void openDatabase() throws SQLException {
            //Open the database
            String myPath = mContext.getDatabasePath(DATABASE_NAME).getPath();
            mDatabase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        }

        @Override
        public synchronized void close() {
            if (mDatabase != null)
                mDatabase.close();
            super.close();
        }*/
    }

    /**
     * 添加系统日志
     */
    public void addSystemLog(String operation, Context context) {

        UserData currentUser = UserData.currentUser(context);
        Date currentDate = new Date();

        ContentValues values = new ContentValues();
        values.put(ProcessLogColumns.USERNAME, currentUser.mStrUsername);
        values.put(ProcessLogColumns.PROCESS, operation);
        values.put(ProcessLogColumns.PROCESS_TIME, CommonUtils.getFormattedDateString(currentDate, "yyyy-MM-dd HH:mm:ss"));

        try {
            insert(ProcessLogColumns.CONTENT_URI, values);
        } catch (android.database.SQLException ex) {
        }
    }
}
