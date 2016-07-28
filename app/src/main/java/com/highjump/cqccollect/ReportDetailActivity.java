package com.highjump.cqccollect;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.view.ViewGroup.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.highjump.cqccollect.api.API_Manager;
import com.highjump.cqccollect.model.AttachmentData;
import com.highjump.cqccollect.model.BaseData;
import com.highjump.cqccollect.model.CollectImgData;
import com.highjump.cqccollect.model.DataMeasuredData;
import com.highjump.cqccollect.model.ElectricCompData;
import com.highjump.cqccollect.model.ElectricSpecData;
import com.highjump.cqccollect.model.ExportCartonData;
import com.highjump.cqccollect.model.InstrumentData;
import com.highjump.cqccollect.model.LocationData;
import com.highjump.cqccollect.model.OtherInfoData;
import com.highjump.cqccollect.model.ProductData;
import com.highjump.cqccollect.model.ReportData;
import com.highjump.cqccollect.model.SamplingData;
import com.highjump.cqccollect.model.TaskData;
import com.highjump.cqccollect.model.UnitPackData;
import com.highjump.cqccollect.model.UserData;
import com.highjump.cqccollect.provider.AttachmentColumns;
import com.highjump.cqccollect.provider.CollectImageColumns;
import com.highjump.cqccollect.provider.DataMeasuredColumns;
import com.highjump.cqccollect.provider.ElectricCompColumns;
import com.highjump.cqccollect.provider.ElectricSpecColumns;
import com.highjump.cqccollect.provider.ExportCartonColumns;
import com.highjump.cqccollect.provider.InstrumentColumns;
import com.highjump.cqccollect.provider.MyContentProvider;
import com.highjump.cqccollect.provider.OtherInfoColumns;
import com.highjump.cqccollect.provider.ProductColumns;
import com.highjump.cqccollect.provider.ReportColumns;
import com.highjump.cqccollect.provider.SamplingColumns;
import com.highjump.cqccollect.provider.UnitPackColumns;
import com.highjump.cqccollect.utils.CommonUtils;
import com.highjump.cqccollect.utils.Config;
import com.highjump.cqccollect.view.CollectImgItemView;
import com.highjump.cqccollect.view.DataMeasuredItemView;
import com.highjump.cqccollect.view.DetailAttachmentItemView;
import com.highjump.cqccollect.view.DetailProductItemView;
import com.highjump.cqccollect.view.ElectricCompItemView;
import com.highjump.cqccollect.view.ElectricSpecItemView;
import com.highjump.cqccollect.view.ExportCartonItemView;
import com.highjump.cqccollect.view.InstrumentItemView;
import com.highjump.cqccollect.view.OtherInfoItemView;
import com.highjump.cqccollect.view.SamplingItemView;
import com.highjump.cqccollect.view.UnitPackItemView;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.apache.http.client.HttpResponseException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;


public class ReportDetailActivity extends MenuActivity implements CompoundButton.OnCheckedChangeListener, TextWatcher, View.OnTouchListener {

    private static final String TAG = ReportDetailActivity.class.getSimpleName();
    public static final String SELECTED_REPORT_ID = "selected_report_id";

    public String mStrReportID = "";

    private TextView mTxtReportStatus;
    private String mStrReportStatus;

    private boolean bLoaded = false;

    private ProgressDialog mProgressDialog;

    //
    // upload status const
    //
    public static final int UPLOAD_STATUS_REPORT = 1;
    public static final int UPLOAD_STATUS_LOCATION_PHOTO = 2;
    public static final int UPLOAD_STATUS_COLLECTION_PHOTO = 3;
    public static final int UPLOAD_STATUS_CLIENT_SIGN = 4;
    public static final int UPLOAD_STATUS_INSPECTOR_SIGN = 5;
    private int mnUploadStatus = 0;

    public static final int UPLOAD_MAX_RETRY = 3;
    private int mnUploadRetry = 0;

    //
    // tag string const
    //
    public static final String PRODUCT_TAG = "product";
    public static final String ATTACHMENT_TAG = "attachment";
    public static final String ELECTRIC_SPEC_TAG = "electric_spec";
    public static final String DATA_MEASURED_TAG = "data_measured";
    public static final String INSTRUMENT_TAG = "instrument";
    public static final String ELECTRIC_COMP_TAG = "electric_comp";
    public static final String EXPORT_CARTON_TAG = "export_carton";
    public static final String UNIT_PACK_TAG = "unit_pack";
    public static final String OTHER_INFO_TAG = "other_info";
    public static final String SAMPLING_TAG = "sampling";
    public static final String PACK_PHOTO_TAG = "pack_photo";
    public static final String PROD_PHOTO_TAG = "prod_photo";
    public static final String STRUCT_PHOTO_TAG = "struct_photo";
    public static final String MARK_PHOTO_TAG = "mark_photo";
    public static final String DEFECT_PHOTO_TAG = "defect_photo";

    public boolean mbEnableEdit;

    // upload collection photo related
    private int mnTotalCollectionCount = 0;
    private int mnCurCollectionIndex = 0;

    // scroll view
    private ScrollView mScrollReport;

    //
    // header info
    //
    private String mStrApprovalDate;
    private EditText mEdtApprovalDate;
    private String mStrApprovalBy;
    private EditText mEdtApprovalBy;

    public int mnConclusion = -1;
    private CheckBox mChkResultPass;
    private CheckBox mChkResultFail;

    public String mStrConfirmClientDate;
//    public String mStrClientName;
//    private EditText mEdtConfirmClient;
    public String mStrClientImgPath = "";
    private TextView mTxtConfirmClient;
    private ImageView mImgViewClient;

    private TextView mTxtReportDatePlace;

    private TextView mTxtInspectorInfo;
    private ImageView mImgViewInspector;
    public String mStrInspectorC;
    public String mStrInspectorE;
    public String mStrInspectorDate;
    public String mStrInspectorImgPath = "";
    public String mStrVerifyEndDate = "";

    public LocationData mLocationData;


    //
    // application info
    //
    private TextView mTxtApplicant;
    private String mStrApplicantC;
    private String mStrApplicantE;

    private TextView mTxtApplicantAddress;
    private String mStrApplicantAddressC;
    private String mStrApplicantAddressE;

    private TextView mTxtApplicantTel;
    private String mStrApplicantTel;
    private String mStrApplicantEmail;

    private TextView mTxtManufacturer;
    private String mStrManufacturerC;
    private String mStrManufacturerE;

    private TextView mTxtManufacturerAddress;
    private String mStrManufacturerAddressC;
    private String mStrManufacturerAddressE;

    private TextView mTxtFactory;
    private String mStrFactoryC;
    private String mStrFactoryE;

    private TextView mTxtFactoryAddress;
    private String mStrFactoryAddressC;
    private String mStrFactoryAddressE;

    private TextView mTxtExporter;
    private String mStrExporterC;
    private String mStrExporterE;

    private TextView mTxtExporterAddress;
    private String mStrExporterAddressC;
    private String mStrExporterAddressE;

    private TextView mTxtImporter;
    private String mStrImporterC;
    private String mStrImporterE;

    private TextView mTxtImporterAddress;
    private String mStrImporterAddressC;
    private String mStrImporterAddressE;

    private TextView mTxtCommercialInvoice;
    private String mStrCommercialInvoice;

    //
    // product info
    //
    public ArrayList<ProductData> mArrayProduct = new ArrayList<ProductData>();
    private LinearLayout mLayoutProduct;

    //
    // attachment info
    //
    public ArrayList<AttachmentData> mArrayAttach = new ArrayList<AttachmentData>();
    private LinearLayout mLayoutAttach;

    //
    // electrical spec
    //
    private Button mButAddElectricSpec;
    public ArrayList<ElectricSpecData> mArrayElectricSpec = new ArrayList<ElectricSpecData>();
    public int mnCurElectricSpec = -1;
    private LinearLayout mLayoutElectricSpec;

    //
    // data measured
    //
    private Button mButAddDataMeasured;
    public ArrayList<DataMeasuredData> mArrayDataMeasured = new ArrayList<DataMeasuredData>();
    public int mnCurDataMeasured = -1;
    private LinearLayout mLayoutDataMeasured;

    //
    // instrument
    //
    private Button mButAddInstrument;
    public ArrayList<InstrumentData> mArrayInstrument = new ArrayList<InstrumentData>();
    public int mnCurInstrument = -1;
    private LinearLayout mLayoutInstrument;

    //
    // electric component
    //
    private Button mButAddElectricComp;
    public ArrayList<ElectricCompData> mArrayElectricComp = new ArrayList<ElectricCompData>();
    public int mnCurElectricComp = -1;
    private LinearLayout mLayoutElectricComp;

    //
    // export carton
    //
    private Button mButAddExportCarton;
    public ArrayList<ExportCartonData> mArrayExportCarton = new ArrayList<ExportCartonData>();
    public int mnCurExportCarton = -1;
    private LinearLayout mLayoutExportCarton;

    //
    // unit pack
    //
    private Button mButAddUnitPack;
    public ArrayList<UnitPackData> mArrayUnitPack = new ArrayList<UnitPackData>();
    public int mnCurUnitPack = -1;
    private LinearLayout mLayoutUnitPack;

    // accessories
    private String mStrAccessories;
    private EditText mEdtAccessories;

    //
    // other info
    //
    private Button mButAddOtherInfo;
    public ArrayList<OtherInfoData> mArrayOtherInfo = new ArrayList<OtherInfoData>();
    public int mnCurOtherInfo = -1;
    private LinearLayout mLayoutOtherInfo;

    //
    // sampling
    //
    private Button mButAddSampling;
    public ArrayList<SamplingData> mArraySampling = new ArrayList<SamplingData>();
    public int mnCurSampling = -1;
    private LinearLayout mLayoutSampling;

    // sampling result
    private String mStrSamplingResult = "";
    private EditText mEdtSamplingResult;

    // photo view
    public int mnCurCollectImg = -1;
    public String mstrCurPhotoType;

    // package photo
    private Button mButAddPhotoPack;
    public ArrayList<CollectImgData> mArrayPhotoPack = new ArrayList<CollectImgData>();
    private LinearLayout mLayoutPhotoPack;

    // product photo
    private Button mButAddPhotoProduct;
    public ArrayList<CollectImgData> mArrayPhotoProduct = new ArrayList<CollectImgData>();
    private LinearLayout mLayoutPhotoProduct;

    // structure photo
    private Button mButAddPhotoStructure;
    public ArrayList<CollectImgData> mArrayPhotoStructure = new ArrayList<CollectImgData>();
    private LinearLayout mLayoutPhotoStructure;

    // mark photo
    private Button mButAddPhotoMark;
    public ArrayList<CollectImgData> mArrayPhotoMark = new ArrayList<CollectImgData>();
    private LinearLayout mLayoutPhotoMark;

    // defect photo
    private Button mButAddPhotoDefect;
    public ArrayList<CollectImgData> mArrayPhotoDefect = new ArrayList<CollectImgData>();
    private LinearLayout mLayoutPhotoDefect;

    // witness & additional
    private String mStrWitness = "";
    private EditText mEdtWitness;
    private String mStrAdditional = "";
    private EditText mEdtAdditional;

    // button
    private Button mButEdit;
    private Button mButLocation;
    private Button mButSave;
    private Button mButConfirm;
    private Button mButSubmit;
    private Button mButCancel;

    private static ReportDetailActivity mInstance = null;
    private boolean mbNeedSave = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_detail);

        Intent intent = getIntent();
        if (intent.hasExtra(SELECTED_REPORT_ID)) {
            mStrReportID = intent.getStringExtra(SELECTED_REPORT_ID);
        }

        super.initView(getString(R.string.menu_detail));

        // init data
        mLocationData = new LocationData();
        mLocationData.mStrReportId = mStrReportID;
        CommonUtils.mLocationSelected = mLocationData;

        //
        // init controls
        //
        mScrollReport = (ScrollView)findViewById(R.id.scroll_report);
        mScrollReport.setOnTouchListener(this);
        mTxtReportStatus = (TextView)findViewById(R.id.text_report_status);

        // header info
        mEdtApprovalDate = (EditText)findViewById(R.id.edit_approval_date);
        mEdtApprovalDate.setOnTouchListener(CommonUtils.getOnTouchListenerForDatePicker(this, mEdtApprovalDate));
        mEdtApprovalDate.addTextChangedListener(this);

        mEdtApprovalBy = (EditText)findViewById(R.id.edit_approval_by);
        mEdtApprovalBy.addTextChangedListener(this);

        mChkResultPass = (CheckBox)findViewById(R.id.chk_result_pass);
        mChkResultPass.setOnCheckedChangeListener(this);
        mChkResultFail = (CheckBox)findViewById(R.id.chk_result_fail);
        mChkResultFail.setOnCheckedChangeListener(this);

//        mEdtConfirmClient = (EditText)findViewById(R.id.edit_confirm_client);
//        mEdtConfirmClient.setOnTouchListener(CommonUtils.getOnTouchListenerForDatePicker(this, mEdtConfirmClient));
//        mEdtConfirmClient.addTextChangedListener(this);
        mTxtConfirmClient = (TextView)findViewById(R.id.text_client);
        mImgViewClient = (ImageView)findViewById(R.id.imgview_client);

        mTxtReportDatePlace = (TextView)findViewById(R.id.text_report_date_place);
        mTxtInspectorInfo = (TextView)findViewById(R.id.text_inspector_info);
        mImgViewInspector = (ImageView)findViewById(R.id.imgview_inspector);

        // applicant info
        mTxtApplicant = (TextView)findViewById(R.id.text_applicant);
//        ColorStateList colors = mTxtApplicant.getTextColors();
        mTxtApplicantAddress = (TextView)findViewById(R.id.text_applicant_address);
        mTxtApplicantTel = (TextView)findViewById(R.id.text_applicant_tel);
        mTxtManufacturer = (TextView)findViewById(R.id.text_manufacturer);
        mTxtManufacturerAddress = (TextView)findViewById(R.id.text_manufacturer_address);
        mTxtFactory = (TextView)findViewById(R.id.text_factory);
        mTxtFactoryAddress = (TextView)findViewById(R.id.text_factory_address);
        mTxtExporter = (TextView)findViewById(R.id.text_exporter);
        mTxtExporterAddress = (TextView)findViewById(R.id.text_exporter_address);
        mTxtImporter = (TextView)findViewById(R.id.text_importer);
        mTxtImporterAddress = (TextView)findViewById(R.id.text_importer_address);
        mTxtCommercialInvoice = (TextView)findViewById(R.id.text_commercial_invoice);

        // product info
        mLayoutProduct = (LinearLayout)findViewById(R.id.layout_prod_info);

        // attachment info
        mLayoutAttach = (LinearLayout)findViewById(R.id.layout_attach_info);

        // electric spec
        mButAddElectricSpec = (Button)findViewById(R.id.but_add_electric_spec);
        mButAddElectricSpec.setOnClickListener(this);
        mLayoutElectricSpec = (LinearLayout)findViewById(R.id.layout_electric_spec);

        // data measured
        mButAddDataMeasured = (Button)findViewById(R.id.but_add_tech_data);
        mButAddDataMeasured.setOnClickListener(this);
        mLayoutDataMeasured = (LinearLayout)findViewById(R.id.layout_data_measured);

        // instrument
        mButAddInstrument = (Button)findViewById(R.id.but_add_instrument);
        mButAddInstrument.setOnClickListener(this);
        mLayoutInstrument = (LinearLayout)findViewById(R.id.layout_instrument);

        // electric component
        mButAddElectricComp = (Button)findViewById(R.id.but_add_electric_comp);
        mButAddElectricComp.setOnClickListener(this);
        mLayoutElectricComp = (LinearLayout)findViewById(R.id.layout_electric_comp);

        // export carton
        mButAddExportCarton = (Button)findViewById(R.id.but_add_export_carton);
        mButAddExportCarton.setOnClickListener(this);
        mLayoutExportCarton = (LinearLayout)findViewById(R.id.layout_export_carton);

        // unit pack
        mButAddUnitPack = (Button)findViewById(R.id.but_add_unit_pack);
        mButAddUnitPack.setOnClickListener(this);
        mLayoutUnitPack = (LinearLayout)findViewById(R.id.layout_unit_pack);

        // other info
        mButAddOtherInfo = (Button)findViewById(R.id.but_add_other_info);
        mButAddOtherInfo.setOnClickListener(this);
        mLayoutOtherInfo = (LinearLayout)findViewById(R.id.layout_other_info);

        // sampling
        mButAddSampling = (Button)findViewById(R.id.but_add_sampling);
        mButAddSampling.setOnClickListener(this);
        mLayoutSampling = (LinearLayout)findViewById(R.id.layout_sampling);

        // pack photo
        mButAddPhotoPack = (Button)findViewById(R.id.but_add_photo_pack);
        mButAddPhotoPack.setOnClickListener(this);
        mLayoutPhotoPack = (LinearLayout)findViewById(R.id.layout_pic_pack);

        // product photo
        mButAddPhotoProduct = (Button)findViewById(R.id.but_add_photo_product);
        mButAddPhotoProduct.setOnClickListener(this);
        mLayoutPhotoProduct = (LinearLayout)findViewById(R.id.layout_pic_prod);

        // structure photo
        mButAddPhotoStructure = (Button)findViewById(R.id.but_add_photo_structure);
        mButAddPhotoStructure.setOnClickListener(this);
        mLayoutPhotoStructure = (LinearLayout)findViewById(R.id.layout_pic_struct);

        // mark photo
        mButAddPhotoMark = (Button)findViewById(R.id.but_add_photo_mark);
        mButAddPhotoMark.setOnClickListener(this);
        mLayoutPhotoMark = (LinearLayout)findViewById(R.id.layout_pic_mark);

        // defect photo
        mButAddPhotoDefect = (Button)findViewById(R.id.but_add_photo_defect);
        mButAddPhotoDefect.setOnClickListener(this);
        mLayoutPhotoDefect = (LinearLayout)findViewById(R.id.layout_pic_defect);

        // button
        mButEdit = (Button)findViewById(R.id.but_edit);
        mButEdit.setOnClickListener(this);
        mButLocation = (Button)findViewById(R.id.but_location);
        mButLocation.setOnClickListener(this);
        mButSave = (Button)findViewById(R.id.but_save);
        mButSave.setOnClickListener(this);
        mButConfirm = (Button)findViewById(R.id.but_confirm);
        mButConfirm.setOnClickListener(this);
        mButSubmit = (Button)findViewById(R.id.but_submit);
        mButSubmit.setOnClickListener(this);
        mButCancel = (Button)findViewById(R.id.but_cancel);
        mButCancel.setOnClickListener(this);

        // other
        mEdtAccessories = (EditText)findViewById(R.id.edit_accessories);
        mEdtAccessories.addTextChangedListener(this);
        mEdtSamplingResult = (EditText)findViewById(R.id.edit_sampling_result);
        mEdtSamplingResult.addTextChangedListener(this);
        mEdtWitness = (EditText)findViewById(R.id.edit_witness);
        mEdtWitness.addTextChangedListener(this);
        mEdtAdditional = (EditText)findViewById(R.id.edit_additional);
        mEdtAdditional.addTextChangedListener(this);

        enableControls(false);

        mInstance = this;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        loadData();
        showData();
        showEditData();
        showArrayData();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        showData();
        showArrayData();
        updateButtons();
    }

    @Override
    public void onBackPressed() {

        // check data status
        if (needToSave()) {
            Dialog alertBuilder = new AlertDialog.Builder(this)
                    .setTitle("")
                    .setMessage("是否保存当前录入内容？")
                    .setPositiveButton("保存", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            saveToDatabase();
                            ReportDetailActivity.this.finish();
                        }
                    })
                    .setNegativeButton("放弃", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            ReportDetailActivity.this.finish();
                        }
                    })
                    .create();

            alertBuilder.show();
        }
        else {
            super.onBackPressed();
        }
    }

    private void saveToDatabase() {

        setEditData();

        // list data
        int i;
        for (i = 0; i < mArrayElectricSpec.size(); i++) {
            ElectricSpecData esData = mArrayElectricSpec.get(i);
            esData.save(mStrReportID);

            if ((esData.mStatus & BaseData.DS_DELETED) != 0) {
                mArrayElectricSpec.remove(i);
                i--;
            }
        }

        for (i = 0; i < mArrayDataMeasured.size(); i++) {
            DataMeasuredData dmData = mArrayDataMeasured.get(i);
            dmData.save(mStrReportID);

            if ((dmData.mStatus & BaseData.DS_DELETED) != 0) {
                mArrayDataMeasured.remove(i);
                i--;
            }
        }

        for (i = 0; i < mArrayInstrument.size(); i++) {
            InstrumentData iData = mArrayInstrument.get(i);
            iData.save(mStrReportID);

            if ((iData.mStatus & BaseData.DS_DELETED) != 0) {
                mArrayInstrument.remove(i);
                i--;
            }
        }

        for (i = 0; i < mArrayElectricComp.size(); i++) {
            ElectricCompData ecData = mArrayElectricComp.get(i);
            ecData.save(mStrReportID);

            if ((ecData.mStatus & BaseData.DS_DELETED) != 0) {
                mArrayElectricComp.remove(i);
                i--;
            }
        }

        for (i = 0; i < mArrayExportCarton.size(); i++) {
            ExportCartonData ecData = mArrayExportCarton.get(i);
            ecData.save(mStrReportID);

            if ((ecData.mStatus & BaseData.DS_DELETED) != 0) {
                mArrayExportCarton.remove(i);
                i--;
            }
        }

        for (i = 0; i < mArrayUnitPack.size(); i++) {
            UnitPackData upData = mArrayUnitPack.get(i);
            upData.save(mStrReportID);

            if ((upData.mStatus & BaseData.DS_DELETED) != 0) {
                mArrayUnitPack.remove(i);
                i--;
            }
        }

        for (i = 0; i < mArrayOtherInfo.size(); i++) {
            OtherInfoData oiData = mArrayOtherInfo.get(i);
            oiData.save(mStrReportID);

            if ((oiData.mStatus & BaseData.DS_DELETED) != 0) {
                mArrayOtherInfo.remove(i);
                i--;
            }
        }

        for (i = 0; i < mArraySampling.size(); i++) {
            SamplingData sData = mArraySampling.get(i);
            sData.save(mStrReportID);

            if ((sData.mStatus & BaseData.DS_DELETED) != 0) {
                mArraySampling.remove(i);
                i--;
            }
        }

        for (i = 0; i < mArrayPhotoPack.size(); i++) {
            CollectImgData ciData = mArrayPhotoPack.get(i);
            ciData.save(mStrReportID);

            if ((ciData.mStatus & BaseData.DS_DELETED) != 0) {
                mArrayPhotoPack.remove(i);
                i--;
            }
        }

        for (i = 0; i < mArrayPhotoProduct.size(); i++) {
            CollectImgData ciData = mArrayPhotoProduct.get(i);
            ciData.save(mStrReportID);

            if ((ciData.mStatus & BaseData.DS_DELETED) != 0) {
                mArrayPhotoProduct.remove(i);
                i--;
            }
        }

        for (i = 0; i < mArrayPhotoStructure.size(); i++) {
            CollectImgData ciData = mArrayPhotoStructure.get(i);
            ciData.save(mStrReportID);

            if ((ciData.mStatus & BaseData.DS_DELETED) != 0) {
                mArrayPhotoStructure.remove(i);
                i--;
            }
        }

        for (i = 0; i < mArrayPhotoMark.size(); i++) {
            CollectImgData ciData = mArrayPhotoMark.get(i);
            ciData.save(mStrReportID);

            if ((ciData.mStatus & BaseData.DS_DELETED) != 0) {
                mArrayPhotoMark.remove(i);
                i--;
            }
        }

        for (i = 0; i < mArrayPhotoDefect.size(); i++) {
            CollectImgData ciData = mArrayPhotoDefect.get(i);
            ciData.save(mStrReportID);

            if ((ciData.mStatus & BaseData.DS_DELETED) != 0) {
                mArrayPhotoDefect.remove(i);
                i--;
            }
        }

        if (mStrReportStatus.contentEquals(ReportData.REPORT_STATUS_PREPARE)) {
            mStrReportStatus = ReportData.REPORT_STATUS_INSPECTING;
        }

        // save report data
        MyContentProvider contentProvider = new MyContentProvider();
        ContentValues contentValues = new ContentValues();

        contentValues.put(ReportColumns.APPROVAL_DATE, mEdtApprovalDate.getText().toString());
        contentValues.put(ReportColumns.APPROVED_BY_C, mEdtApprovalBy.getText().toString());

//        if (mChkResultPass.isChecked()) {
//            mnConclusion = 1;
//        }
//        else if (mChkResultFail.isChecked()) {
//            mnConclusion = 0;
//        }
        contentValues.put(ReportColumns.CONCLUSION, mnConclusion);
        contentValues.put(ReportColumns.CONFIRM_DATE, mStrConfirmClientDate);
        contentValues.put(ReportColumns.CONFIRM_SIGNING, mStrClientImgPath);
        contentValues.put(ReportColumns.INSPECTOR_C, mStrInspectorC);
        contentValues.put(ReportColumns.INSPECTOR_E, mStrInspectorE);
        contentValues.put(ReportColumns.INSPECTOR_DATE, mStrInspectorDate);
        contentValues.put(ReportColumns.INSPECTOR_SIGNING, mStrInspectorImgPath);
        contentValues.put(ReportColumns.VERIFICATION_DATE_END, mStrVerifyEndDate);

        contentValues.put(ReportColumns.ACCESSORIES_DESCRIPTION_C, mEdtAccessories.getText().toString());
        contentValues.put(ReportColumns.SAMPLING_CONCLUSION_C, mEdtSamplingResult.getText().toString());
        contentValues.put(ReportColumns.WITNESS_TEST_C, mEdtWitness.getText().toString());
        contentValues.put(ReportColumns.ADDITIONAL_INFORMATION_C, mEdtAdditional.getText().toString());

        contentValues.put(ReportColumns.REPORT_STATUS, mStrReportStatus);

        contentProvider.update(ReportColumns.CONTENT_URI, contentValues,
                ReportColumns.NO + " = '" + mStrReportID + "'", null);

        new MyContentProvider().addSystemLog("更新核查报告", this);

        mbNeedSave = false;

        showReportStatus();
    }

    private void saveReportStatusToDB() {
        MyContentProvider contentProvider = new MyContentProvider();
        ContentValues contentValues = new ContentValues();

        contentValues.put(ReportColumns.REPORT_STATUS, mStrReportStatus);
        contentProvider.update(ReportColumns.CONTENT_URI, contentValues,
                ReportColumns.NO + " = '" + mStrReportID + "'", null);

        showReportStatus();
    }

    private boolean needToSave() {
        boolean bRes = mbNeedSave;

        for (ElectricSpecData esData : mArrayElectricSpec) {
            if (esData.mStatus != BaseData.DS_NONE) {
                bRes = true;
            }
        }

        for (DataMeasuredData dmData : mArrayDataMeasured) {
            if (dmData.mStatus != BaseData.DS_NONE) {
                bRes = true;
            }
        }

        for (InstrumentData iData : mArrayInstrument) {
            if (iData.mStatus != BaseData.DS_NONE) {
                bRes = true;
            }
        }

        for (ElectricCompData ecData : mArrayElectricComp) {
            if (ecData.mStatus != BaseData.DS_NONE) {
                bRes = true;
            }
        }

        for (ExportCartonData ecData : mArrayExportCarton) {
            if (ecData.mStatus != BaseData.DS_NONE) {
                bRes = true;
            }
        }

        for (UnitPackData upData : mArrayUnitPack) {
            if (upData.mStatus != BaseData.DS_NONE) {
                bRes = true;
            }
        }

        for (OtherInfoData oiData : mArrayOtherInfo) {
            if (oiData.mStatus != BaseData.DS_NONE) {
                bRes = true;
            }
        }

        for (SamplingData sData : mArraySampling) {
            if (sData.mStatus != BaseData.DS_NONE) {
                bRes = true;
            }
        }

        for (CollectImgData ciData : mArrayPhotoPack) {
            if (ciData.mStatus != BaseData.DS_NONE) {
                bRes = true;
            }
        }

        for (CollectImgData ciData : mArrayPhotoProduct) {
            if (ciData.mStatus != BaseData.DS_NONE) {
                bRes = true;
            }
        }

        for (CollectImgData ciData : mArrayPhotoStructure) {
            if (ciData.mStatus != BaseData.DS_NONE) {
                bRes = true;
            }
        }

        for (CollectImgData ciData : mArrayPhotoMark) {
            if (ciData.mStatus != BaseData.DS_NONE) {
                bRes = true;
            }
        }

        for (CollectImgData ciData : mArrayPhotoDefect) {
            if (ciData.mStatus != BaseData.DS_NONE) {
                bRes = true;
            }
        }

        return bRes;
    }

    public static ReportDetailActivity getInstance() {
        if (mInstance == null) {
            mInstance = new ReportDetailActivity();
        }

        return mInstance;
    }

    public ArrayList<String> getProductNames() {
        ArrayList<String> arrayList = new ArrayList<String>();

        for (ProductData pData : mArrayProduct) {
            arrayList.add(pData.mStrName);
        }

        return arrayList;
    }

    public int getProductIndex(ProductData productData) {
        int nIndex = 0;

        for (ProductData pData : mArrayProduct) {
            if (pData.mnNo == productData.mnNo) {
                break;
            }
            nIndex++;
        }

        return nIndex;
    }

    private void loadData() {

        if (bLoaded) {
            return;
        }

        MyContentProvider contentProvider = new MyContentProvider();

        Cursor cursor = contentProvider.query(ReportColumns.CONTENT_URI, null, ReportColumns.NO + " IS '" + mStrReportID + "'", null, null);

        if (cursor != null) {
            if (cursor.getCount() > 0 && cursor.moveToFirst()) {

                // header information
                mStrApprovalDate = cursor.getString(cursor.getColumnIndex(ReportColumns.APPROVAL_DATE));
                mStrApprovalBy = cursor.getString(cursor.getColumnIndex(ReportColumns.APPROVED_BY_C));
                mStrReportStatus = cursor.getString(cursor.getColumnIndex(ReportColumns.REPORT_STATUS));
                mnConclusion = cursor.getInt(cursor.getColumnIndex(ReportColumns.CONCLUSION));

                mLocationData.mStrVerifyDate = cursor.getString(cursor.getColumnIndex(ReportColumns.VERIFICATION_DATE));
                if (mLocationData.mStrVerifyDate == null) {
                    mLocationData.mStrVerifyDate = "";
                }

                mStrVerifyEndDate = cursor.getString(cursor.getColumnIndex(ReportColumns.VERIFICATION_DATE_END));
                if (mStrVerifyEndDate == null) {
                    mStrVerifyEndDate = "";
                }

                mLocationData.mStrVerifyPlaceC = cursor.getString(cursor.getColumnIndex(ReportColumns.VERIFICATION_PLACE_C));
                if (mLocationData.mStrVerifyPlaceC == null) {
                    mLocationData.mStrVerifyPlaceC = "";
                }

                mLocationData.mStrVerifyPlaceE = cursor.getString(cursor.getColumnIndex(ReportColumns.VERIFICATION_PLACE_E));
                if (mLocationData.mStrVerifyPlaceE == null) {
                    mLocationData.mStrVerifyPlaceE = "";
                }

                mStrConfirmClientDate = cursor.getString(cursor.getColumnIndex(ReportColumns.CONFIRM_DATE));
                if (mStrConfirmClientDate == null) {
                    mStrConfirmClientDate = "";
                }

                mStrClientImgPath = cursor.getString(cursor.getColumnIndex(ReportColumns.CONFIRM_SIGNING));
                if (mStrClientImgPath == null) {
                    mStrClientImgPath = "";
                }

                mStrInspectorC = cursor.getString(cursor.getColumnIndex(ReportColumns.INSPECTOR_C));
                if (mStrInspectorC == null) {
                    mStrInspectorC = "";
                }

                mStrInspectorE = cursor.getString(cursor.getColumnIndex(ReportColumns.INSPECTOR_E));
                if (mStrInspectorE == null) {
                    mStrInspectorE = "";
                }

                mStrInspectorDate = cursor.getString(cursor.getColumnIndex(ReportColumns.INSPECTOR_DATE));
                if (mStrInspectorDate == null) {
                    mStrInspectorDate = "";
                }

                mStrInspectorImgPath = cursor.getString(cursor.getColumnIndex(ReportColumns.INSPECTOR_SIGNING));
                if (mStrInspectorImgPath == null) {
                    mStrInspectorImgPath = "";
                }

                mLocationData.mStrVerifyLocation = cursor.getString(cursor.getColumnIndex(ReportColumns.VERIFICATION_LOCATION));
                mLocationData.mStrLocateDate = cursor.getString(cursor.getColumnIndex(ReportColumns.LOCATE_TIME));
                mLocationData.mStrPhoto = cursor.getString(cursor.getColumnIndex(ReportColumns.LOCATION_PHOTO));
                mLocationData.mStrPhotoDate = cursor.getString(cursor.getColumnIndex(ReportColumns.PHOTO_TIME));

                // application information
                mStrApplicantC = cursor.getString(cursor.getColumnIndex(ReportColumns.APPLICANT_C));
                if (mStrApplicantC == null) {
                    mStrApplicantC = "";
                }

                mStrApplicantE = cursor.getString(cursor.getColumnIndex(ReportColumns.APPLICANT_E));
                if (mStrApplicantE == null) {
                    mStrApplicantE = "";
                }

                mStrApplicantAddressC = cursor.getString(cursor.getColumnIndex(ReportColumns.APPLICANT_ADDRESS_C));
                if (mStrApplicantAddressC == null) {
                    mStrApplicantAddressC = "";
                }

                mStrApplicantAddressE = cursor.getString(cursor.getColumnIndex(ReportColumns.APPLICANT_ADDRESS_E));
                if (mStrApplicantAddressE == null) {
                    mStrApplicantAddressE = "";
                }

                mStrApplicantTel = cursor.getString(cursor.getColumnIndex(ReportColumns.APPLICANT_TEL));
                if (mStrApplicantTel == null) {
                    mStrApplicantTel = "";
                }

                mStrApplicantEmail = cursor.getString(cursor.getColumnIndex(ReportColumns.APPLICANT_EMAIL));
                if (mStrApplicantEmail == null) {
                    mStrApplicantEmail = "";
                }

                mStrManufacturerC = cursor.getString(cursor.getColumnIndex(ReportColumns.MANUFACTURER_C));
                if (mStrManufacturerC == null) {
                    mStrManufacturerC = "";
                }

                mStrManufacturerE = cursor.getString(cursor.getColumnIndex(ReportColumns.MANUFACTURER_E));
                if (mStrManufacturerE == null) {
                    mStrManufacturerE = "";
                }

                mStrManufacturerAddressC = cursor.getString(cursor.getColumnIndex(ReportColumns.MANUFACTURER_ADDRESS_C));
                if (mStrManufacturerAddressC == null) {
                    mStrManufacturerAddressC = "";
                }

                mStrManufacturerAddressE = cursor.getString(cursor.getColumnIndex(ReportColumns.MANUFACTURER_ADDRESS_E));
                if (mStrManufacturerAddressE == null) {
                    mStrManufacturerAddressE = "";
                }

                mStrFactoryC = cursor.getString(cursor.getColumnIndex(ReportColumns.FACTORY_C));
                if (mStrFactoryC == null) {
                    mStrFactoryC = "";
                }

                mStrFactoryE = cursor.getString(cursor.getColumnIndex(ReportColumns.FACTORY_E));
                if (mStrFactoryE == null) {
                    mStrFactoryE = "";
                }

                mStrFactoryAddressC = cursor.getString(cursor.getColumnIndex(ReportColumns.FACTORY_ADDRESS_C));
                if (mStrFactoryAddressC == null) {
                    mStrFactoryAddressC = "";
                }

                mStrFactoryAddressE = cursor.getString(cursor.getColumnIndex(ReportColumns.FACTORY_ADDRESS_E));
                if (mStrFactoryAddressE == null) {
                    mStrFactoryAddressE = "";
                }

                mStrExporterC = cursor.getString(cursor.getColumnIndex(ReportColumns.EXPORTER_C));
                if (mStrExporterC == null) {
                    mStrExporterC = "";
                }

                mStrExporterE = cursor.getString(cursor.getColumnIndex(ReportColumns.EXPORTER_E));
                if (mStrExporterE == null) {
                    mStrExporterE = "";
                }

                mStrExporterAddressC = cursor.getString(cursor.getColumnIndex(ReportColumns.EXPORTER_ADDRESS_C));
                if (mStrExporterAddressC == null) {
                    mStrExporterAddressC = "";
                }

                mStrExporterAddressE = cursor.getString(cursor.getColumnIndex(ReportColumns.EXPORTER_ADDRESS_E));
                if (mStrExporterAddressE == null) {
                    mStrExporterAddressE = "";
                }

                mStrImporterC = cursor.getString(cursor.getColumnIndex(ReportColumns.IMPORTER_C));
                if (mStrImporterC == null) {
                    mStrImporterC = "";
                }

                mStrImporterE = cursor.getString(cursor.getColumnIndex(ReportColumns.IMPORTER_E));
                if (mStrImporterE == null) {
                    mStrImporterE = "";
                }

                mStrImporterAddressC = cursor.getString(cursor.getColumnIndex(ReportColumns.IMPORTER_ADDRESS_C));
                if (mStrImporterAddressC == null) {
                    mStrImporterAddressC = "";
                }

                mStrImporterAddressE = cursor.getString(cursor.getColumnIndex(ReportColumns.IMPORTER_ADDRESS_E));
                if (mStrImporterAddressE == null) {
                    mStrImporterAddressE = "";
                }

                mStrCommercialInvoice = cursor.getString(cursor.getColumnIndex(ReportColumns.COMMERCIAL_INVOICE_NO));
                if (mStrCommercialInvoice == null) {
                    mStrCommercialInvoice = "";
                }

                // product information
                loadProductData();

                // attachment information
                loadAttachmentData();

                // electric spec
                loadElectricSpecData();

                // data measured
                loadDataMeasuredData();

                // instrument
                loadInstrumentData();

                // electric component
                loadElectricCompData();

                // export carton
                loadExportCartonData();

                // unit pack
                loadUnitPackData();

                // other info
                loadOtherInfoData();

                // sampling
                loadSamplingData();

                loadPhoto();

                // witness & additional
                mStrAccessories = cursor.getString(cursor.getColumnIndex(ReportColumns.ACCESSORIES_DESCRIPTION_C));
                mStrSamplingResult = cursor.getString(cursor.getColumnIndex(ReportColumns.SAMPLING_CONCLUSION_C));
                mStrWitness = cursor.getString(cursor.getColumnIndex(ReportColumns.WITNESS_TEST_C));
                mStrAdditional = cursor.getString(cursor.getColumnIndex(ReportColumns.ADDITIONAL_INFORMATION_C));
            }

            cursor.close();

            bLoaded = true;
        }
    }

    private void loadProductData() {
        mArrayProduct.clear();

        MyContentProvider contentProvider = new MyContentProvider();

        Cursor cursor = contentProvider.query(ProductColumns.CONTENT_URI, null, ProductColumns.REPORT_NO + " IS '" + mStrReportID + "'", null, null);

        int nIndex = 0;

        if (cursor != null) {
            if (cursor.getCount() > 0 && cursor.moveToFirst()) {
                do {
                    nIndex++;

                    ProductData data = new ProductData();
                    data.mnNo = cursor.getInt(cursor.getColumnIndex(ProductColumns.PRODUCT_NO));
                    data.mnNoInternal = nIndex;
                    data.mStrName = cursor.getString(cursor.getColumnIndex(ProductColumns.PRODUCT_NAME));
                    data.mStrQuantity = cursor.getString(cursor.getColumnIndex(ProductColumns.QUANTITY));
                    data.mStrDescription = cursor.getString(cursor.getColumnIndex(ProductColumns.PRODUCT_DESCRIPTION));

                    mArrayProduct.add(data);
                } while (cursor.moveToNext());
            }
        }
    }

    private void loadAttachmentData() {
        mArrayAttach.clear();

        MyContentProvider contentProvider = new MyContentProvider();

        Cursor cursor = contentProvider.query(AttachmentColumns.CONTENT_URI, null, AttachmentColumns.REPORT_NO + " IS '" + mStrReportID + "'", null, null);

        if (cursor != null) {
            if (cursor.getCount() > 0 && cursor.moveToFirst()) {
                do {
                    AttachmentData data = new AttachmentData();
                    data.mnNo = cursor.getInt(cursor.getColumnIndex(AttachmentColumns.ATTACHMENT_NO));
                    data.mStrFileName = cursor.getString(cursor.getColumnIndex(AttachmentColumns.FILE_NAME));

                    mArrayAttach.add(data);
                } while (cursor.moveToNext());
            }
        }
    }

    private void loadElectricSpecData() {
        mArrayElectricSpec.clear();

        MyContentProvider contentProvider = new MyContentProvider();

        Cursor cursor = contentProvider.query(ElectricSpecColumns.CONTENT_URI, null, ElectricSpecColumns.REPORT_NO + " IS '" + mStrReportID + "'", null, null);

        if (cursor != null) {
            if (cursor.getCount() > 0 && cursor.moveToFirst()) {
                do {
                    int nProductId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ElectricSpecColumns.PRODUCT_NO)));

                    ElectricSpecData data = new ElectricSpecData();
                    data.mnNo = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ElectricSpecColumns.SPECIFICATION_NO)));
                    data.mProduct = getProductWithId(nProductId);
                    data.mStrRatedVoltage = cursor.getString(cursor.getColumnIndex(ElectricSpecColumns.RATED_VOLTAGE));
                    data.mStrRatedPower = cursor.getString(cursor.getColumnIndex(ElectricSpecColumns.RATED_POWER));
                    data.mStrRatedFreq = cursor.getString(cursor.getColumnIndex(ElectricSpecColumns.RATED_FREQUENCY));
                    data.mStrClass = cursor.getString(cursor.getColumnIndex(ElectricSpecColumns.ELECTRICAL_CLASS));
                    data.mStrIpGrade = cursor.getString(cursor.getColumnIndex(ElectricSpecColumns.IP_GRADE));
                    data.mStrCableSpec = cursor.getString(cursor.getColumnIndex(ElectricSpecColumns.CABLE_SPECIFICATION));
                    data.mStrSupply = cursor.getString(cursor.getColumnIndex(ElectricSpecColumns.CONNECTION_TYPE));
                    data.mStrPlug = cursor.getString(cursor.getColumnIndex(ElectricSpecColumns.PLUG));

                    mArrayElectricSpec.add(data);

                } while (cursor.moveToNext());
            }
        }
    }

    private void loadDataMeasuredData() {
        mArrayDataMeasured.clear();

        MyContentProvider contentProvider = new MyContentProvider();

        Cursor cursor = contentProvider.query(DataMeasuredColumns.CONTENT_URI, null, DataMeasuredColumns.REPORT_NO + " IS '" + mStrReportID + "'", null, null);

        if (cursor != null) {
            if (cursor.getCount() > 0 && cursor.moveToFirst()) {
                do {
                    int nProductId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DataMeasuredColumns.PRODUCT_NO)));

                    DataMeasuredData data = new DataMeasuredData();
                    data.mnNo = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DataMeasuredColumns.MEASURED_NO)));
                    data.mProduct = getProductWithId(nProductId);
                    data.mStrNumberSampling = cursor.getString(cursor.getColumnIndex(DataMeasuredColumns.NUMBER_SAMPLING));
                    data.mStrElectricStrength = cursor.getString(cursor.getColumnIndex(DataMeasuredColumns.ELECTRIC_STRENGTH));
                    data.mStrEarthResist = cursor.getString(cursor.getColumnIndex(DataMeasuredColumns.EARTHING_RESISTANCE));
                    data.mStrLeakCur = cursor.getString(cursor.getColumnIndex(DataMeasuredColumns.LEAKAGE_CURRENT));

                    mArrayDataMeasured.add(data);

                } while (cursor.moveToNext());
            }
        }
    }

    private void loadInstrumentData() {
        mArrayInstrument.clear();

        MyContentProvider contentProvider = new MyContentProvider();

        Cursor cursor = contentProvider.query(InstrumentColumns.CONTENT_URI, null, InstrumentColumns.REPORT_NO + " IS '" + mStrReportID + "'", null, null);
        if (cursor != null) {
            if (cursor.getCount() > 0 && cursor.moveToFirst()) {
                do {
                    InstrumentData data = new InstrumentData();
                    data.mnNo = Integer.parseInt(cursor.getString(cursor.getColumnIndex(InstrumentColumns.INSTRUMENT_NO)));
                    data.mStrName = cursor.getString(cursor.getColumnIndex(InstrumentColumns.INSTRUMENT_NAME));
                    data.mStrModel = cursor.getString(cursor.getColumnIndex(InstrumentColumns.MODEL));
                    data.mStrRefNo = cursor.getString(cursor.getColumnIndex(InstrumentColumns.REF_NO));
                    data.mStrValidity = cursor.getString(cursor.getColumnIndex(InstrumentColumns.VALIDITY_CALIBRATION));

                    mArrayInstrument.add(data);

                } while (cursor.moveToNext());
            }
        }
    }

    private void loadElectricCompData() {
        mArrayElectricComp.clear();

        MyContentProvider contentProvider = new MyContentProvider();

        Cursor cursor = contentProvider.query(ElectricCompColumns.CONTENT_URI, null, ElectricCompColumns.REPORT_NO + " IS '" + mStrReportID + "'", null, null);
        if (cursor != null) {
            if (cursor.getCount() > 0 && cursor.moveToFirst()) {
                do {
                    ElectricCompData data = new ElectricCompData();
                    data.mnNo = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ElectricCompColumns.COMPONENT_NO)));
                    data.mStrName = cursor.getString(cursor.getColumnIndex(ElectricCompColumns.COMPONENT_NAME));
                    data.mStrTradeMark = cursor.getString(cursor.getColumnIndex(ElectricCompColumns.TRADE_MARK));
                    data.mStrModel = cursor.getString(cursor.getColumnIndex(ElectricCompColumns.MODEL));
                    data.mStrSpec = cursor.getString(cursor.getColumnIndex(ElectricCompColumns.SPECIFICATION));
                    data.mStrConformityMark = cursor.getString(cursor.getColumnIndex(ElectricCompColumns.CONFORMITY_MARK));
                    data.mnSameAs = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ElectricCompColumns.SAME_AS)));

                    mArrayElectricComp.add(data);

                } while (cursor.moveToNext());
            }
        }
    }

    private void loadExportCartonData() {
        mArrayExportCarton.clear();

        MyContentProvider contentProvider = new MyContentProvider();

        Cursor cursor = contentProvider.query(ExportCartonColumns.CONTENT_URI, null, ExportCartonColumns.REPORT_NO + " IS '" + mStrReportID + "'", null, null);
        if (cursor != null) {
            if (cursor.getCount() > 0 && cursor.moveToFirst()) {
                do {
                    ExportCartonData data = new ExportCartonData();
                    data.mnNo = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ExportCartonColumns.EXPORT_NO)));
                    data.mStrPcsCarton = cursor.getString(cursor.getColumnIndex(ExportCartonColumns.PCS_CARTON));
                    data.mStrExtDimen = cursor.getString(cursor.getColumnIndex(ExportCartonColumns.EXTERNAL_DIMENSION));
                    data.mStrThickness = cursor.getString(cursor.getColumnIndex(ExportCartonColumns.THICKNESS));
                    data.mStrLayers = cursor.getString(cursor.getColumnIndex(ExportCartonColumns.LAYERS));
                    data.mStrSealingType = cursor.getString(cursor.getColumnIndex(ExportCartonColumns.SEALING_TYPE));

                    mArrayExportCarton.add(data);

                } while (cursor.moveToNext());
            }
        }
    }

    private void loadUnitPackData() {
        mArrayUnitPack.clear();

        MyContentProvider contentProvider = new MyContentProvider();

        Cursor cursor = contentProvider.query(UnitPackColumns.CONTENT_URI, null, UnitPackColumns.REPORT_NO + " IS '" + mStrReportID + "'", null, null);
        if (cursor != null) {
            if (cursor.getCount() > 0 && cursor.moveToFirst()) {
                do {
                    UnitPackData data = new UnitPackData();
                    data.mnNo = Integer.parseInt(cursor.getString(cursor.getColumnIndex(UnitPackColumns.PACKAGING_NO)));
                    data.mStrPackType = cursor.getString(cursor.getColumnIndex(UnitPackColumns.PACKAGING_TYPE));
                    data.mStrDimen = cursor.getString(cursor.getColumnIndex(UnitPackColumns.DIMENSION));
                    data.mStrThickness = cursor.getString(cursor.getColumnIndex(UnitPackColumns.THICKNESS));
                    data.mStrLayers = cursor.getString(cursor.getColumnIndex(UnitPackColumns.LAYERS));
                    data.mStrPrintingColor = cursor.getString(cursor.getColumnIndex(UnitPackColumns.PRINTING_COLOR));
                    data.mStrSealingType = cursor.getString(cursor.getColumnIndex(UnitPackColumns.SEALING_TYPE));

                    mArrayUnitPack.add(data);

                } while (cursor.moveToNext());
            }
        }
    }

    private void loadOtherInfoData() {
        mArrayOtherInfo.clear();

        MyContentProvider contentProvider = new MyContentProvider();

        Cursor cursor = contentProvider.query(OtherInfoColumns.CONTENT_URI, null, OtherInfoColumns.REPORT_NO + " IS '" + mStrReportID + "'", null, null);
        if (cursor != null) {
            if (cursor.getCount() > 0 && cursor.moveToFirst()) {
                do {
                    OtherInfoData data = new OtherInfoData();
                    data.mnNo = Integer.parseInt(cursor.getString(cursor.getColumnIndex(OtherInfoColumns.OTHER_NO)));
                    data.mStrOtherInfo = cursor.getString(cursor.getColumnIndex(OtherInfoColumns.OTHER_INFO));

                    mArrayOtherInfo.add(data);

                } while (cursor.moveToNext());
            }
        }
    }

    private void loadSamplingData() {
        mArraySampling.clear();

        MyContentProvider contentProvider = new MyContentProvider();

        Cursor cursor = contentProvider.query(SamplingColumns.CONTENT_URI, null, SamplingColumns.REPORT_NO + " IS '" + mStrReportID + "'", null, null);
        if (cursor != null) {
            if (cursor.getCount() > 0 && cursor.moveToFirst()) {
                do {
                    int nProductId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SamplingColumns.PRODUCT_NO)));

                    SamplingData data = new SamplingData();
                    data.mnNo = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SamplingColumns.SAMPLING_NO)));
                    data.mProduct = getProductWithId(nProductId);
                    data.mStrNumSampling = cursor.getString(cursor.getColumnIndex(SamplingColumns.NUMBER_SAMPLING));
                    data.mStrAql = cursor.getString(cursor.getColumnIndex(SamplingColumns.AQL));
                    data.mStrNumAccept = cursor.getString(cursor.getColumnIndex(SamplingColumns.ACCEPT));
                    data.mStrNumReject = cursor.getString(cursor.getColumnIndex(SamplingColumns.REJECT));

                    mArraySampling.add(data);

                } while (cursor.moveToNext());
            }
        }
    }

    private void loadPhoto() {
        mArrayPhotoPack.clear();
        mArrayPhotoProduct.clear();
        mArrayPhotoStructure.clear();
        mArrayPhotoMark.clear();
        mArrayPhotoDefect.clear();

        MyContentProvider contentProvider = new MyContentProvider();

        Cursor cursor = contentProvider.query(CollectImageColumns.CONTENT_URI, null, CollectImageColumns.REPORT_NO + " IS '" + mStrReportID + "'", null, null);
        if (cursor != null) {
            if (cursor.getCount() > 0 && cursor.moveToFirst()) {
                do {
                    CollectImgData data = new CollectImgData();
                    data.mnNo = Integer.parseInt(cursor.getString(cursor.getColumnIndex(CollectImageColumns.IMAGE_NO)));
                    data.mStrImgAddress = cursor.getString(cursor.getColumnIndex(CollectImageColumns.IMAGE_ADDRESS));
                    data.mStrDesc = cursor.getString(cursor.getColumnIndex(CollectImageColumns.IMAGE_DESCRIPTION));
                    data.mStrType = cursor.getString(cursor.getColumnIndex(CollectImageColumns.IMAGE_TYPE));
                    data.mnUploaded = cursor.getInt(cursor.getColumnIndex(CollectImageColumns.UPLOADED));

                    if (data.mStrType.contentEquals(CollectImgData.PHOTO_TYPE_PACK)) {
                        mArrayPhotoPack.add(data);
                    }
                    else if (data.mStrType.contentEquals(CollectImgData.PHOTO_TYPE_PRODUCT)) {
                        mArrayPhotoProduct.add(data);
                    }
                    else if (data.mStrType.contentEquals(CollectImgData.PHOTO_TYPE_STRUCT)) {
                        mArrayPhotoStructure.add(data);
                    }
                    else if (data.mStrType.contentEquals(CollectImgData.PHOTO_TYPE_MARK)) {
                        mArrayPhotoMark.add(data);
                    }
                    else if (data.mStrType.contentEquals(CollectImgData.PHOTO_TYPE_DEFECT)) {
                        mArrayPhotoDefect.add(data);
                    }

                } while (cursor.moveToNext());
            }
        }
    }

    private String getProductNameWithId(int nProductId) {

        String strName = "";

        for (ProductData pData : mArrayProduct) {
            if (pData.mnNo == nProductId) {
                strName = pData.mStrName;
                break;
            }
        }

        return strName;
    }

    private ProductData getProductWithId(int nProductId) {

        ProductData dataRes = null;

        for (ProductData pData : mArrayProduct) {
            if (pData.mnNo == nProductId) {
                dataRes = pData;
                break;
            }
        }

        return dataRes;
    }

    private void showReportStatus() {
        // header information
        mTxtReportStatus.setText(mStrReportStatus);
    }

    private void enableAddButtons(boolean enable) {
        mButAddOtherInfo.setEnabled(enable);
        mButAddDataMeasured.setEnabled(enable);
        mButAddPhotoDefect.setEnabled(enable);
        mButAddPhotoStructure.setEnabled(enable);
        mButAddElectricComp.setEnabled(enable);
        mButAddElectricSpec.setEnabled(enable);
        mButAddExportCarton.setEnabled(enable);
        mButAddInstrument.setEnabled(enable);
        mButAddPhotoPack.setEnabled(enable);
        mButAddPhotoProduct.setEnabled(enable);
        mButAddSampling.setEnabled(enable);
        mButAddUnitPack.setEnabled(enable);
        mButAddPhotoMark.setEnabled(enable);
    }

    private void updateButtons() {
        closeMenu();

        mButConfirm.setVisibility(View.GONE);
        mButEdit.setVisibility(View.GONE);
        mButLocation.setVisibility(View.GONE);
        mButSave.setVisibility(View.GONE);
        mButSubmit.setVisibility(View.GONE);
        mButCancel.setVisibility(View.GONE);

        if (mbEnableEdit) {
            mButSave.setVisibility(View.VISIBLE);
            enableMenuItem(false);

            enableAddButtons(true);
        }
        else {
            enableMenuItem(true);

            if (mStrReportStatus.contentEquals(ReportData.REPORT_STATUS_PREPARE)) {
                mButEdit.setVisibility(View.VISIBLE);
                mButLocation.setVisibility(View.VISIBLE);

                if (mLocationData.isValid()) {
                    mButEdit.setEnabled(true);
                }
                else {
                    mButEdit.setEnabled(false);
                }
            }
            if (mStrReportStatus.contentEquals(ReportData.REPORT_STATUS_INSPECTING)) {
                mButConfirm.setVisibility(View.VISIBLE);
                mButEdit.setVisibility(View.VISIBLE);
            }
            else if (mStrReportStatus.contentEquals(ReportData.REPORT_STATUS_WAIT_SUBMIT)) {
                mButSubmit.setVisibility(View.VISIBLE);
            }
            else if (mStrReportStatus.contentEquals(ReportData.REPORT_STATUS_SUBMITTED)) {
                mButCancel.setVisibility(View.VISIBLE);
            }
            else if (mStrReportStatus.contentEquals(ReportData.REPORT_STATUS_MODIFY)) {
                mButConfirm.setVisibility(View.VISIBLE);
                mButEdit.setVisibility(View.VISIBLE);
            }
        }
    }

    private void showData() {

        showReportStatus();

        mEdtApprovalDate.setText(mStrApprovalDate);
        mEdtApprovalBy.setText(mStrApprovalBy);

        String strText = "";
        if (!TextUtils.isEmpty(mLocationData.mStrVerifyDate)) {
            strText += mLocationData.mStrVerifyDate + "/";
        }
        if (!TextUtils.isEmpty(mLocationData.mStrVerifyPlaceC)) {
            strText += mLocationData.mStrVerifyPlaceC + "\n";
        }
        if (!TextUtils.isEmpty(mLocationData.mStrVerifyPlaceE)) {
            strText += mLocationData.mStrVerifyPlaceE;
        }
        mTxtReportDatePlace.setText(strText);


        strText = "";
//        if (!TextUtils.isEmpty(mStrInspectorC)) {
//            strText += mStrInspectorC + "/";
//        }
        if (!TextUtils.isEmpty(mStrInspectorDate)) {
            strText += mStrInspectorDate;
        }
        mTxtInspectorInfo.setText(strText);
//        mImg.setImageBitmap(CommonUtils.getBitmapFromUri(Uri.parse(strFilePath), 1, 200));
        mImgViewInspector.setImageBitmap(CommonUtils.getBitmapFromUri(Uri.parse(mStrInspectorImgPath), 1, 200));

        strText = "";
//        if (!TextUtils.isEmpty(mStrClientName)) {
//            strText += mStrClientName;
//            if (!TextUtils.isEmpty(mStrConfirmClientDate)) {
//                strText += "/";
//            }
//        }
        strText += mStrConfirmClientDate;
//        mEdtConfirmClient.setText(strText);
        mTxtConfirmClient.setText(strText);
        mImgViewClient.setImageBitmap(CommonUtils.getBitmapFromUri(Uri.parse(mStrClientImgPath), 1, 200));

        if (mnConclusion == 1) {
            mChkResultPass.setChecked(true);
        }
        else if (mnConclusion == 0) {
            mChkResultFail.setChecked(true);
        }

        // applicant information
        strText = mStrApplicantC + "\n" + mStrApplicantE;
        mTxtApplicant.setText(strText);

        strText = mStrApplicantAddressC + "\n" + mStrApplicantAddressE;
        mTxtApplicantAddress.setText(strText);

        strText = "TEL:" + mStrApplicantTel + " / E-mail:" + mStrApplicantEmail;
        mTxtApplicantTel.setText(strText);

        strText = mStrManufacturerC + "\n" + mStrManufacturerE;
        mTxtManufacturer.setText(strText);

        strText = mStrManufacturerAddressC + "\n" + mStrManufacturerAddressE;
        mTxtManufacturerAddress.setText(strText);

        strText = mStrFactoryC + "\n" + mStrFactoryE;
        mTxtFactory.setText(strText);

        strText = mStrFactoryAddressC + "\n" + mStrFactoryAddressE;
        mTxtFactoryAddress.setText(strText);

        strText = mStrExporterC + "\n" + mStrExporterE;
        mTxtExporter.setText(strText);

        strText = mStrExporterAddressC + "\n" + mStrExporterAddressE;
        mTxtExporterAddress.setText(strText);

        strText = mStrImporterC + "\n" + mStrImporterE;
        mTxtImporter.setText(strText);

        strText = mStrImporterAddressC + "\n" + mStrImporterAddressE;
        mTxtImporterAddress.setText(strText);

        mTxtCommercialInvoice.setText(mStrCommercialInvoice);

    }

    private void setEditData() {
        // witness & additional
        mStrAccessories = mEdtAccessories.getText().toString();
        mStrSamplingResult = mEdtSamplingResult.getText().toString();
        mStrWitness = mEdtWitness.getText().toString();
        mStrAdditional = mEdtAdditional.getText().toString();
    }

    private void showEditData() {
        // witness & additional
        mEdtAccessories.setText(mStrAccessories);
        mEdtSamplingResult.setText(mStrSamplingResult);
        mEdtWitness.setText(mStrWitness);
        mEdtAdditional.setText(mStrAdditional);
    }

    private void showArrayData() {
        // product info
        mLayoutProduct.removeAllViews();

        int nLen = mArrayProduct.size();
        for (int i = 0; i < nLen; i++) {
            ProductData pData = mArrayProduct.get(i);
            DetailProductItemView viewProductItem = new DetailProductItemView(this);
            viewProductItem.initData(pData);
            viewProductItem.setTag(PRODUCT_TAG + i);

            mLayoutProduct.addView(viewProductItem);
        }

        // product info
        mLayoutAttach.removeAllViews();

        nLen = mArrayAttach.size();
        for (int i = 0; i < nLen; i++) {
            AttachmentData aData = mArrayAttach.get(i);

            DetailAttachmentItemView viewAttachItem = new DetailAttachmentItemView(this);
            viewAttachItem.initData(aData, i + 1);
            viewAttachItem.setTag(ATTACHMENT_TAG + i);
            viewAttachItem.setOnClickListener(this);

            mLayoutAttach.addView(viewAttachItem);
        }

        // electric spec
        mLayoutElectricSpec.removeAllViews();

        nLen = -1;
        for (ElectricSpecData esData : mArrayElectricSpec) {
            nLen++;

            if ((esData.mStatus & BaseData.DS_DELETED) != 0) {
                continue;
            }

            ElectricSpecItemView viewElectricSpecItem = new ElectricSpecItemView(this);
            viewElectricSpecItem.initData(esData);
            viewElectricSpecItem.setTag(ELECTRIC_SPEC_TAG + nLen);
            viewElectricSpecItem.setOnClickListener(this);

            mLayoutElectricSpec.addView(viewElectricSpecItem);
        }

        // data measured
        mLayoutDataMeasured.removeAllViews();

        nLen = -1;
        for (DataMeasuredData dmData : mArrayDataMeasured) {
            nLen++;

            if ((dmData.mStatus & BaseData.DS_DELETED) != 0) {
                continue;
            }

            DataMeasuredItemView viewDataMeasuredItem = new DataMeasuredItemView(this);
            viewDataMeasuredItem.initData(dmData);
            viewDataMeasuredItem.setTag(DATA_MEASURED_TAG + nLen);
            viewDataMeasuredItem.setOnClickListener(this);

            mLayoutDataMeasured.addView(viewDataMeasuredItem);
        }

        // instrument
        mLayoutInstrument.removeAllViews();

        nLen = -1;
        for (InstrumentData iData : mArrayInstrument) {
            nLen++;

            if ((iData.mStatus & BaseData.DS_DELETED) != 0) {
                continue;
            }

            InstrumentItemView viewInstrumentItem = new InstrumentItemView(this);
            viewInstrumentItem.initData(iData);
            viewInstrumentItem.setTag(INSTRUMENT_TAG + nLen);
            viewInstrumentItem.setOnClickListener(this);

            mLayoutInstrument.addView(viewInstrumentItem);
        }

        // electric component
        mLayoutElectricComp.removeAllViews();

        nLen = -1;
        for (ElectricCompData ecData : mArrayElectricComp) {
            nLen++;

            if ((ecData.mStatus & BaseData.DS_DELETED) != 0) {
                continue;
            }

            ElectricCompItemView viewElectricComp = new ElectricCompItemView(this);
            viewElectricComp.initData(ecData);
            viewElectricComp.setTag(ELECTRIC_COMP_TAG + nLen);
            viewElectricComp.setOnClickListener(this);

            mLayoutElectricComp.addView(viewElectricComp);
        }

        // export carton
        mLayoutExportCarton.removeAllViews();

        nLen = -1;
        for (ExportCartonData ecData : mArrayExportCarton) {
            nLen++;

            if ((ecData.mStatus & BaseData.DS_DELETED) != 0) {
                continue;
            }

            ExportCartonItemView viewExportCartonItem = new ExportCartonItemView(this);
            viewExportCartonItem.initData(ecData);
            viewExportCartonItem.setTag(EXPORT_CARTON_TAG + nLen);
            viewExportCartonItem.setOnClickListener(this);

            mLayoutExportCarton.addView(viewExportCartonItem);
        }

        // unit pack
        mLayoutUnitPack.removeAllViews();

        nLen = -1;
        for (UnitPackData upData : mArrayUnitPack) {
            nLen++;

            if ((upData.mStatus & BaseData.DS_DELETED) != 0) {
                continue;
            }

            UnitPackItemView viewUnitPackItem = new UnitPackItemView(this);
            viewUnitPackItem.initData(upData);
            viewUnitPackItem.setTag(UNIT_PACK_TAG + nLen);
            viewUnitPackItem.setOnClickListener(this);

            mLayoutUnitPack.addView(viewUnitPackItem);
        }

        // other info
        mLayoutOtherInfo.removeAllViews();

        nLen = -1;
        int nIndex = 0;
        for (OtherInfoData oiData : mArrayOtherInfo) {
            nLen++;

            if ((oiData.mStatus & BaseData.DS_DELETED) != 0) {
                continue;
            }

            nIndex++;

            OtherInfoItemView viewOtherInfoItem = new OtherInfoItemView(this);
            viewOtherInfoItem.initData(oiData, nIndex);
            viewOtherInfoItem.setTag(OTHER_INFO_TAG + nLen);
            viewOtherInfoItem.setOnClickListener(this);

            mLayoutOtherInfo.addView(viewOtherInfoItem);
        }

        // sampling
        mLayoutSampling.removeAllViews();

        nLen = -1;
        for (SamplingData sData : mArraySampling) {
            nLen++;

            if ((sData.mStatus & BaseData.DS_DELETED) != 0) {
                continue;
            }

            SamplingItemView viewSamplingItem = new SamplingItemView(this);
            viewSamplingItem.initData(sData);
            viewSamplingItem.setTag(SAMPLING_TAG + nLen);
            viewSamplingItem.setOnClickListener(this);

            mLayoutSampling.addView(viewSamplingItem);
        }

        // pack photo
        mLayoutPhotoPack.removeAllViews();

        nLen = -1;
        nIndex = 0;
        LinearLayout linLayout = null;

        for (CollectImgData ciData : mArrayPhotoPack) {
            nLen++;

            if ((ciData.mStatus & BaseData.DS_DELETED) != 0) {
                continue;
            }

            if (nIndex % 2 == 0) {
                linLayout = newLinearLayout();
                mLayoutPhotoPack.addView(linLayout);
            }

            if (linLayout == null) {
                break;
            }

            CollectImgItemView viewCollectionImgItem = new CollectImgItemView(this);
            viewCollectionImgItem.initData(ciData);
            viewCollectionImgItem.setTag(PACK_PHOTO_TAG + nLen);
            viewCollectionImgItem.setOnClickListener(this);
            viewCollectionImgItem.mLayoutParent = linLayout;

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1);
            linLayout.addView(viewCollectionImgItem, params);

            nIndex++;
        }

        if (nIndex % 2 == 1 && linLayout != null) {
            linLayout.addView(newEmptyView());
        }

        // product photo
        mLayoutPhotoProduct.removeAllViews();

        nLen = -1;
        nIndex = 0;

        for (CollectImgData ciData : mArrayPhotoProduct) {
            nLen++;

            if ((ciData.mStatus & BaseData.DS_DELETED) != 0) {
                continue;
            }

            if (nIndex % 2 == 0) {
                linLayout = newLinearLayout();
                mLayoutPhotoProduct.addView(linLayout);
            }

            if (linLayout == null) {
                break;
            }

            CollectImgItemView viewCollectionImgItem = new CollectImgItemView(this);
            viewCollectionImgItem.initData(ciData);
            viewCollectionImgItem.setTag(PROD_PHOTO_TAG + nLen);
            viewCollectionImgItem.setOnClickListener(this);
            viewCollectionImgItem.mLayoutParent = linLayout;

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1);
            linLayout.addView(viewCollectionImgItem, params);

            nIndex++;
        }

        if (nIndex % 2 == 1 && linLayout != null) {
            linLayout.addView(newEmptyView());
        }

        // struct photo
        mLayoutPhotoStructure.removeAllViews();

        nLen = -1;
        nIndex = 0;

        for (CollectImgData ciData : mArrayPhotoStructure) {
            nLen++;

            if ((ciData.mStatus & BaseData.DS_DELETED) != 0) {
                continue;
            }

            if (nIndex % 2 == 0) {
                linLayout = newLinearLayout();
                mLayoutPhotoStructure.addView(linLayout);
            }

            if (linLayout == null) {
                break;
            }

            CollectImgItemView viewCollectionImgItem = new CollectImgItemView(this);
            viewCollectionImgItem.initData(ciData);
            viewCollectionImgItem.setTag(STRUCT_PHOTO_TAG + nLen);
            viewCollectionImgItem.setOnClickListener(this);
            viewCollectionImgItem.mLayoutParent = linLayout;

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1);
            linLayout.addView(viewCollectionImgItem, params);

            nIndex++;
        }

        if (nIndex % 2 == 1 && linLayout != null) {
            linLayout.addView(newEmptyView());
        }

        // mark photo
        mLayoutPhotoMark.removeAllViews();

        nLen = -1;
        nIndex = 0;

        for (CollectImgData ciData : mArrayPhotoMark) {
            nLen++;

            if ((ciData.mStatus & BaseData.DS_DELETED) != 0) {
                continue;
            }

            if (nIndex % 2 == 0) {
                linLayout = newLinearLayout();
                mLayoutPhotoMark.addView(linLayout);
            }

            if (linLayout == null) {
                break;
            }

            CollectImgItemView viewCollectionImgItem = new CollectImgItemView(this);
            viewCollectionImgItem.initData(ciData);
            viewCollectionImgItem.setTag(MARK_PHOTO_TAG + nLen);
            viewCollectionImgItem.setOnClickListener(this);
            viewCollectionImgItem.mLayoutParent = linLayout;

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1);
            linLayout.addView(viewCollectionImgItem, params);

            nIndex++;
        }

        if (nIndex % 2 == 1 && linLayout != null) {
            linLayout.addView(newEmptyView());
        }

        // defect photo
        mLayoutPhotoDefect.removeAllViews();

        nLen = -1;
        nIndex = 0;

        for (CollectImgData ciData : mArrayPhotoDefect) {
            nLen++;

            if ((ciData.mStatus & BaseData.DS_DELETED) != 0) {
                continue;
            }

            if (nIndex % 2 == 0) {
                linLayout = newLinearLayout();
                mLayoutPhotoDefect.addView(linLayout);
            }

            if (linLayout == null) {
                break;
            }

            CollectImgItemView viewCollectionImgItem = new CollectImgItemView(this);
            viewCollectionImgItem.initData(ciData);
            viewCollectionImgItem.setTag(DEFECT_PHOTO_TAG + nLen);
            viewCollectionImgItem.setOnClickListener(this);
            viewCollectionImgItem.mLayoutParent = linLayout;

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1);
            linLayout.addView(viewCollectionImgItem, params);

            nIndex++;
        }

        if (nIndex % 2 == 1 && linLayout != null) {
            linLayout.addView(newEmptyView());
        }
    }

    private LinearLayout newLinearLayout() {
        LinearLayout linLayout = new LinearLayout(this);
        linLayout.setOrientation(LinearLayout.HORIZONTAL);
        LayoutParams linLayoutParam = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        linLayout.setLayoutParams(linLayoutParam);

        return linLayout;
    }

    private View newEmptyView() {
        View viewEmpty = new View(this);
        LinearLayout.LayoutParams viewLayoutParam = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1f);
        viewEmpty.setLayoutParams(viewLayoutParam);

        return viewEmpty;
    }

    private void enableControls(boolean b) {

        mbEnableEdit = b;

        // header info
        mEdtApprovalDate.setEnabled(false);
        mEdtApprovalBy.setEnabled(false);
        mChkResultPass.setEnabled(false);
        mChkResultFail.setEnabled(false);
//        mEdtConfirmClient.setEnabled(false);

        mEdtAccessories.setEnabled(b);
        mEdtSamplingResult.setEnabled(b);
        mEdtWitness.setEnabled(b);
        mEdtAdditional.setEnabled(b);

        if (b) {
            // data
            mButAddElectricSpec.setVisibility(View.VISIBLE);
            mButAddDataMeasured.setVisibility(View.VISIBLE);
            mButAddInstrument.setVisibility(View.VISIBLE);
            mButAddElectricComp.setVisibility(View.VISIBLE);
            mButAddExportCarton.setVisibility(View.VISIBLE);
            mButAddUnitPack.setVisibility(View.VISIBLE);
            mButAddOtherInfo.setVisibility(View.VISIBLE);
            mButAddSampling.setVisibility(View.VISIBLE);

            // photo
            mButAddPhotoPack.setVisibility(View.VISIBLE);
            mButAddPhotoProduct.setVisibility(View.VISIBLE);
            mButAddPhotoStructure.setVisibility(View.VISIBLE);
            mButAddPhotoMark.setVisibility(View.VISIBLE);
            mButAddPhotoDefect.setVisibility(View.VISIBLE);

            mButEdit.setVisibility(View.GONE);
            mButSave.setVisibility(View.VISIBLE);
        }
        else {
            // data
            mButAddElectricSpec.setVisibility(View.INVISIBLE);
            mButAddDataMeasured.setVisibility(View.INVISIBLE);
            mButAddInstrument.setVisibility(View.GONE);
            mButAddElectricComp.setVisibility(View.INVISIBLE);
            mButAddExportCarton.setVisibility(View.INVISIBLE);
            mButAddUnitPack.setVisibility(View.INVISIBLE);
            mButAddOtherInfo.setVisibility(View.INVISIBLE);
            mButAddSampling.setVisibility(View.INVISIBLE);

            // photo
            mButAddPhotoPack.setVisibility(View.INVISIBLE);
            mButAddPhotoProduct.setVisibility(View.INVISIBLE);
            mButAddPhotoStructure.setVisibility(View.INVISIBLE);
            mButAddPhotoMark.setVisibility(View.INVISIBLE);
            mButAddPhotoDefect.setVisibility(View.INVISIBLE);

            mButEdit.setVisibility(View.VISIBLE);
            mButSave.setVisibility(View.GONE);
        }

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int id = buttonView.getId();

        switch (id) {
            case R.id.chk_result_pass:
                if (isChecked) {
                    if (mChkResultFail.isChecked()) {
                        mChkResultFail.setChecked(false);
                    }
                }
                break;

            case R.id.chk_result_fail:
                if (isChecked) {
                    if (mChkResultPass.isChecked()) {
                        mChkResultPass.setChecked(false);
                    }
                }
                break;
        }

        if (mbEnableEdit) {
            mbNeedSave = true;
        }
    }

    public void doConfirmReport() {
        mStrReportStatus = ReportData.REPORT_STATUS_WAIT_SUBMIT;
        saveToDatabase();
        updateButtons();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        String strTag = (String) v.getTag();

        switch (id) {
            case R.id.but_edit:
                closeMenu();
                enableControls(true);
                showReportStatus();
                updateButtons();
                break;

            case R.id.but_location:
                closeMenu();

//                String strUrl = "http://192.168.1.134:80080/cqc/download_attachment.jsp";
//
//                try {
//                    downloadFile(strUrl, "asdfasdf.jpg");
//                }
//                catch (MalformedURLException e) {
//                    e.printStackTrace();
//                }

                CommonUtils.moveNextActivity(this, LocationActivity.class, false);

                break;

            case R.id.but_save:
                closeMenu();
                saveToDatabase();
                enableControls(false);
                updateButtons();
                break;

            case R.id.but_confirm:
                // update status
                closeMenu();

                CommonUtils.moveNextActivity(this, DialogConfirmReportActivity.class, false);
                break;

            case R.id.but_submit:
                closeMenu();
                // submit to backend
                uploadReportData();

                break;

            case R.id.but_cancel:
                closeMenu();

                getbackReport();
                break;

            case R.id.but_add_electric_spec:
                enableAddButtons(false);
                closeMenu();
                mnCurElectricSpec = -1;
                removeEditFocus();

                CommonUtils.moveNextActivity(this, DialogElectricSpecActivity.class, false);
                break;

            case R.id.but_add_tech_data:
                enableAddButtons(false);
                closeMenu();
                mnCurDataMeasured = -1;
                removeEditFocus();

                CommonUtils.moveNextActivity(this, DialogDataMeasuredActivity.class, false);
                break;

            case R.id.but_add_instrument:
                enableAddButtons(false);
                closeMenu();
                mnCurInstrument = -1;
                removeEditFocus();

                CommonUtils.moveNextActivity(this, DialogInstrumentActivity.class, false);
                break;

            case R.id.but_add_electric_comp:
                enableAddButtons(false);
                closeMenu();
                mnCurElectricComp = -1;
                removeEditFocus();

                CommonUtils.moveNextActivity(this, DialogElectricCompActivity.class, false);
                break;

            case R.id.but_add_export_carton:
                enableAddButtons(false);
                closeMenu();
                mnCurExportCarton = -1;
                removeEditFocus();

                CommonUtils.moveNextActivity(this, DialogExportCartonActivity.class, false);
                break;

            case R.id.but_add_unit_pack:
                enableAddButtons(false);
                closeMenu();
                mnCurUnitPack = -1;
                removeEditFocus();

                CommonUtils.moveNextActivity(this, DialogUnitPackActivity.class, false);
                break;

            case R.id.but_add_other_info:
                enableAddButtons(false);
                closeMenu();
                mnCurOtherInfo = -1;
                removeEditFocus();

                CommonUtils.moveNextActivity(this, DialogOtherInfoActivity.class, false);
                break;

            case R.id.but_add_sampling:
                enableAddButtons(false);
                closeMenu();
                mnCurSampling = -1;
                removeEditFocus();

                CommonUtils.moveNextActivity(this, DialogSamplingActivity.class, false);
                break;

            case R.id.but_add_photo_pack:
                enableAddButtons(false);
                closeMenu();
                mnCurCollectImg = -1;
                removeEditFocus();

                mstrCurPhotoType = CollectImgData.PHOTO_TYPE_PACK;
                CommonUtils.moveNextActivity(this, DialogPhotoActivity.class, false);
                break;

            case R.id.but_add_photo_product:
                enableAddButtons(false);
                closeMenu();
                mnCurCollectImg = -1;
                removeEditFocus();

                mstrCurPhotoType = CollectImgData.PHOTO_TYPE_PRODUCT;
                CommonUtils.moveNextActivity(this, DialogPhotoActivity.class, false);
                break;

            case R.id.but_add_photo_structure:
                enableAddButtons(false);
                closeMenu();
                mnCurCollectImg = -1;
                removeEditFocus();

                mstrCurPhotoType = CollectImgData.PHOTO_TYPE_STRUCT;
                CommonUtils.moveNextActivity(this, DialogPhotoActivity.class, false);
                break;

            case R.id.but_add_photo_mark:
                enableAddButtons(false);
                closeMenu();
                mnCurCollectImg = -1;
                removeEditFocus();

                mstrCurPhotoType = CollectImgData.PHOTO_TYPE_MARK;
                CommonUtils.moveNextActivity(this, DialogPhotoActivity.class, false);
                break;

            case R.id.but_add_photo_defect:
                enableAddButtons(false);
                closeMenu();
                mnCurCollectImg = -1;
                removeEditFocus();

                mstrCurPhotoType = CollectImgData.PHOTO_TYPE_DEFECT;
                CommonUtils.moveNextActivity(this, DialogPhotoActivity.class, false);
                break;

            case R.id.but_menu_task:
            case R.id.but_menu_location:
                break;

            default:
                break;
        }

        super.onClick(v);

        if (strTag == null && !(strTag instanceof String)) {
            return;
        }

        if (strTag.contains(ATTACHMENT_TAG)) {
            // download attachment
            int nAttachIndex = Integer.parseInt(strTag.substring(ATTACHMENT_TAG.length()));
            AttachmentData aData = mArrayAttach.get(nAttachIndex);

            UserData currentUser = UserData.currentUser(this);
            String strUrl = API_Manager.API_DOWNLOAD_PATH + "?username=" + currentUser.mStrUsername + "&attachment_no=" + aData.mnNo;

            try {
                downloadFile(strUrl, aData.mStrFileName);
            }
            catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        if (!mbEnableEdit) {
            return;
        }

        if (strTag.contains(ELECTRIC_SPEC_TAG)) {
            mnCurElectricSpec = Integer.parseInt(strTag.substring(ELECTRIC_SPEC_TAG.length()));
            CommonUtils.moveNextActivity(this, DialogElectricSpecActivity.class, false);
        }
        else if (strTag.contains(DATA_MEASURED_TAG)) {
            mnCurDataMeasured = Integer.parseInt(strTag.substring(DATA_MEASURED_TAG.length()));
            CommonUtils.moveNextActivity(this, DialogDataMeasuredActivity.class, false);
        }
        else if (strTag.contains(INSTRUMENT_TAG)) {
            mnCurInstrument = Integer.parseInt(strTag.substring(INSTRUMENT_TAG.length()));
            CommonUtils.moveNextActivity(this, DialogInstrumentActivity.class, false);
        }
        else if (strTag.contains(ELECTRIC_COMP_TAG)) {
            mnCurElectricComp = Integer.parseInt(strTag.substring(ELECTRIC_COMP_TAG.length()));
            CommonUtils.moveNextActivity(this, DialogElectricCompActivity.class, false);
        }
        else if (strTag.contains(EXPORT_CARTON_TAG)) {
            mnCurExportCarton = Integer.parseInt(strTag.substring(EXPORT_CARTON_TAG.length()));
            CommonUtils.moveNextActivity(this, DialogExportCartonActivity.class, false);
        }
        else if (strTag.contains(UNIT_PACK_TAG)) {
            mnCurUnitPack = Integer.parseInt(strTag.substring(UNIT_PACK_TAG.length()));
            CommonUtils.moveNextActivity(this, DialogUnitPackActivity.class, false);
        }
        else if (strTag.contains(OTHER_INFO_TAG)) {
            mnCurOtherInfo = Integer.parseInt(strTag.substring(OTHER_INFO_TAG.length()));
            CommonUtils.moveNextActivity(this, DialogOtherInfoActivity.class, false);
        }
        else if (strTag.contains(SAMPLING_TAG)) {
            mnCurSampling = Integer.parseInt(strTag.substring(SAMPLING_TAG.length()));
            CommonUtils.moveNextActivity(this, DialogSamplingActivity.class, false);
        }
        else if (strTag.contains(PACK_PHOTO_TAG)) {
            mnCurCollectImg = Integer.parseInt(strTag.substring(PACK_PHOTO_TAG.length()));
            mstrCurPhotoType = CollectImgData.PHOTO_TYPE_PACK;
            CommonUtils.moveNextActivity(this, DialogPhotoActivity.class, false);
        }
        else if (strTag.contains(PROD_PHOTO_TAG)) {
            mnCurCollectImg = Integer.parseInt(strTag.substring(PROD_PHOTO_TAG.length()));
            mstrCurPhotoType = CollectImgData.PHOTO_TYPE_PRODUCT;
            CommonUtils.moveNextActivity(this, DialogPhotoActivity.class, false);
        }
        else if (strTag.contains(STRUCT_PHOTO_TAG)) {
            mnCurCollectImg = Integer.parseInt(strTag.substring(STRUCT_PHOTO_TAG.length()));
            mstrCurPhotoType = CollectImgData.PHOTO_TYPE_STRUCT;
            CommonUtils.moveNextActivity(this, DialogPhotoActivity.class, false);
        }
        else if (strTag.contains(MARK_PHOTO_TAG)) {
            mnCurCollectImg = Integer.parseInt(strTag.substring(MARK_PHOTO_TAG.length()));
            mstrCurPhotoType = CollectImgData.PHOTO_TYPE_MARK;
            CommonUtils.moveNextActivity(this, DialogPhotoActivity.class, false);
        }
        else if (strTag.contains(DEFECT_PHOTO_TAG)) {
            mnCurCollectImg = Integer.parseInt(strTag.substring(DEFECT_PHOTO_TAG.length()));
            mstrCurPhotoType = CollectImgData.PHOTO_TYPE_DEFECT;
            CommonUtils.moveNextActivity(this, DialogPhotoActivity.class, false);
        }
    }

    private void downloadFile(String strUrl, String strFilename) throws MalformedURLException {
        DownloadManager downloadManager = (DownloadManager)getSystemService(DOWNLOAD_SERVICE);

        Uri resource = Uri.parse(strUrl);
        DownloadManager.Request request = new DownloadManager.Request(resource);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setVisibleInDownloadsUi(true);

        File mediaStorageDir = CommonUtils.getMyApplicationDirectory(this, Config.DOWNLOAD_DIRECTORY_NAME);
        File fileDest = new File(mediaStorageDir.getPath() + File.separator + strFilename);
        Uri destUri = Uri.parse(fileDest.toURI().toURL().toString());

        request.setDestinationUri(destUri);

        request.setTitle("下载 " + strFilename);
        request.setDescription(strUrl);

        downloadManager.enqueue(request);

        int nPx = (int) getResources().getDimension(R.dimen.detail_toast_bottom_margin);

        Toast toast = Toast.makeText(this, "正在下载... " + strFilename, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, nPx);
        toast.show();

//
//        // notification area
//        NotificationCompat.Builder mBuilder =
//                new NotificationCompat.Builder(this)
//                        .setSmallIcon(R.drawable.logo)
//                        .setContentTitle("My notification")
//                        .setContentText("Hello World!")
//                        .setTicker("adsasdf asdfasdf");
//
//        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        // mId allows you to update the notification later on.
//        mNotificationManager.notify(0, mBuilder.build());
    }

    private boolean uploadReportData() {

        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            return false;
        }

        //
        // upload report detail
        //
        mnUploadStatus = UPLOAD_STATUS_REPORT;
        mnUploadRetry = 0;

        JSONObject dataObject = new JSONObject();

        try {
            mProgressDialog = ProgressDialog.show(this, "", "正在上报...");

            dataObject.put(ReportColumns.NO, mStrReportID);
            dataObject.put(ReportColumns.VERIFICATION_DATE, mLocationData.mStrVerifyDate);
            dataObject.put(ReportColumns.VERIFICATION_DATE_END, mStrVerifyEndDate);
            dataObject.put(ReportColumns.VERIFICATION_PLACE_C, mLocationData.mStrVerifyPlaceC);
            dataObject.put(ReportColumns.VERIFICATION_PLACE_E, mLocationData.mStrVerifyPlaceE);
            dataObject.put(ReportColumns.CONFIRM_DATE, mStrConfirmClientDate);
            dataObject.put(ReportColumns.CONFIRM_SIGNING, mStrConfirmClientDate);
            dataObject.put(ReportColumns.ACCESSORIES_DESCRIPTION_C, mStrAccessories);
//            dataObject.put(ReportColumns.ACCESSORIES_DESCRIPTION_E, mStrAccessories);
            dataObject.put(ReportColumns.SAMPLING_CONCLUSION_C, mStrSamplingResult);
//            dataObject.put(ReportColumns.SAMPLING_CONCLUSION_E, );
            dataObject.put(ReportColumns.WITNESS_TEST_C, mStrWitness);
//            dataObject.put(ReportColumns.WITNESS_TEST_E, mStrWitness);
            dataObject.put(ReportColumns.ADDITIONAL_INFORMATION_C, mStrAdditional);
//            dataObject.put(ReportColumns.ADDITIONAL_INFORMATION_E, mStrAdditional);
            dataObject.put(ReportColumns.VERIFICATION_LOCATION, mLocationData.mStrVerifyLocation);
            dataObject.put(ReportColumns.LOCATE_TIME, mLocationData.mStrLocateDate);
            dataObject.put(ReportColumns.PHOTO_LOCATION, mLocationData.mStrVerifyLocation);
            dataObject.put(ReportColumns.PHOTO_TIME, mLocationData.mStrPhotoDate);

            dataObject.put(ReportColumns.INSPECTOR_C, mStrInspectorC);
            dataObject.put(ReportColumns.INSPECTOR_E, mStrInspectorE);
            dataObject.put(ReportColumns.INSPECTOR_DATE, mStrInspectorDate);
            dataObject.put(ReportColumns.CONCLUSION, mnConclusion);

            // electric spec
            JSONArray arrayElectricSpec = new JSONArray();
            for (ElectricSpecData esData : mArrayElectricSpec) {
                JSONObject objElectricSpec = new JSONObject();

                objElectricSpec.put(ElectricSpecColumns.PRODUCT_NO, esData.mProduct.mnNo);
                objElectricSpec.put(ElectricSpecColumns.RATED_VOLTAGE, esData.mStrRatedVoltage);
                objElectricSpec.put(ElectricSpecColumns.RATED_POWER, esData.mStrRatedPower);
                objElectricSpec.put(ElectricSpecColumns.RATED_FREQUENCY, esData.mStrRatedFreq);
                objElectricSpec.put(ElectricSpecColumns.ELECTRICAL_CLASS, esData.mStrClass);
                objElectricSpec.put(ElectricSpecColumns.IP_GRADE, esData.mStrIpGrade);
                objElectricSpec.put(ElectricSpecColumns.CABLE_SPECIFICATION, esData.mStrCableSpec);
                objElectricSpec.put(ElectricSpecColumns.CONNECTION_TYPE, esData.mStrSupply);
                objElectricSpec.put(ElectricSpecColumns.PLUG, esData.mStrPlug);

                arrayElectricSpec.put(objElectricSpec);
            }
            dataObject.put("electrical_specification", arrayElectricSpec);

            // data measured
            JSONArray arrayDataMeasured = new JSONArray();
            for (DataMeasuredData dmData : mArrayDataMeasured) {
                JSONObject objDataMeasured = new JSONObject();

                objDataMeasured.put(DataMeasuredColumns.MEASURED_NO, dmData.mnNo);
                objDataMeasured.put(DataMeasuredColumns.PRODUCT_NO, dmData.mProduct.mnNo);
                objDataMeasured.put(DataMeasuredColumns.NUMBER_SAMPLING, dmData.mStrNumberSampling);
                objDataMeasured.put(DataMeasuredColumns.ELECTRIC_STRENGTH, dmData.mStrElectricStrength);
                objDataMeasured.put(DataMeasuredColumns.EARTHING_RESISTANCE, dmData.mStrEarthResist);
                objDataMeasured.put(DataMeasuredColumns.LEAKAGE_CURRENT, dmData.mStrLeakCur);

                arrayDataMeasured.put(objDataMeasured);
            }
            dataObject.put("data_measured", arrayDataMeasured);

            // instruments
            JSONArray arrayInstrument = new JSONArray();
            for (InstrumentData iData : mArrayInstrument) {
                JSONObject objInstrument = new JSONObject();

                objInstrument.put(InstrumentColumns.INSTRUMENT_NO, iData.mnNo);
                objInstrument.put(InstrumentColumns.INSTRUMENT_NAME, iData.mStrName);
                objInstrument.put(InstrumentColumns.MODEL, iData.mStrModel);
                objInstrument.put(InstrumentColumns.REF_NO, iData.mStrRefNo);
                objInstrument.put(InstrumentColumns.VALIDITY_CALIBRATION, iData.mStrValidity);

                arrayInstrument.put(objInstrument);
            }
            dataObject.put("instruments", arrayInstrument);

            // electrical components
            JSONArray arrayElectricComp = new JSONArray();
            for (ElectricCompData ecData : mArrayElectricComp) {
                JSONObject objElectricComp = new JSONObject();

                objElectricComp.put(ElectricCompColumns.COMPONENT_NO, ecData.mnNo);
                objElectricComp.put(ElectricCompColumns.COMPONENT_NAME, ecData.mStrName);
                objElectricComp.put(ElectricCompColumns.TRADE_MARK, ecData.mStrTradeMark);
                objElectricComp.put(ElectricCompColumns.MODEL, ecData.mStrModel);
                objElectricComp.put(ElectricCompColumns.SPECIFICATION, ecData.mStrSpec);
                objElectricComp.put(ElectricCompColumns.CONFORMITY_MARK, ecData.mStrConformityMark);
                objElectricComp.put(ElectricCompColumns.SAME_AS, ecData.mnSameAs);

                arrayElectricComp.put(objElectricComp);
            }
            dataObject.put("electrical_components", arrayElectricComp);

            // export carton
            JSONArray arrayExportCart = new JSONArray();
            for (ExportCartonData ecData : mArrayExportCarton) {
                JSONObject objExportCart = new JSONObject();

                objExportCart.put(ExportCartonColumns.EXPORT_NO, ecData.mnNo);
//                objExportCart.put(ExportCskeypartonColumns.PRODUCT_NO, ecData.p);
                objExportCart.put(ExportCartonColumns.PCS_CARTON, ecData.mStrPcsCarton);
                objExportCart.put(ExportCartonColumns.EXTERNAL_DIMENSION, ecData.mStrExtDimen);
                objExportCart.put(ExportCartonColumns.THICKNESS, ecData.mStrThickness);
                objExportCart.put(ExportCartonColumns.LAYERS, ecData.mStrLayers);
                objExportCart.put(ExportCartonColumns.SEALING_TYPE, ecData.mStrSealingType);

                arrayExportCart.put(objExportCart);
            }
            dataObject.put("export_carton", arrayExportCart);

            // unit pack
            JSONArray arrayUnitPack = new JSONArray();
            for (UnitPackData upData : mArrayUnitPack) {
                JSONObject objUnitPack = new JSONObject();

                objUnitPack.put(UnitPackColumns.PACKAGING_NO, upData.mnNo);
//                objUnitPack.put(UnitPackColumns.PRODUCT_NO, upData.mnNo);
                objUnitPack.put(UnitPackColumns.PACKAGING_TYPE, upData.mStrPackType);
                objUnitPack.put(UnitPackColumns.DIMENSION, upData.mStrDimen);
                objUnitPack.put(UnitPackColumns.THICKNESS, upData.mStrThickness);
                objUnitPack.put(UnitPackColumns.LAYERS, upData.mStrLayers);
                objUnitPack.put(UnitPackColumns.PRINTING_COLOR, upData.mStrPrintingColor);
                objUnitPack.put(UnitPackColumns.SEALING_TYPE, upData.mStrSealingType);

                arrayUnitPack.put(objUnitPack);
            }
            dataObject.put("unit_packaging", arrayUnitPack);

            // other info
            JSONArray arrayOtherInfo = new JSONArray();
            for (OtherInfoData oiData : mArrayOtherInfo) {
                JSONObject objOtherInfo = new JSONObject();

                objOtherInfo.put(OtherInfoColumns.OTHER_NO, oiData.mnNo);
                objOtherInfo.put(OtherInfoColumns.OTHER_INFO, oiData.mStrOtherInfo);

                arrayOtherInfo.put(objOtherInfo);
            }
            dataObject.put("other_information", arrayOtherInfo);

            // sampling
            JSONArray arraySampling = new JSONArray();
            for (SamplingData sData : mArraySampling) {
                JSONObject objSampling = new JSONObject();

                objSampling.put(SamplingColumns.SAMPLING_NO, sData.mnNo);
                objSampling.put(SamplingColumns.PRODUCT_NO, sData.mProduct.mnNo);
                objSampling.put(SamplingColumns.NUMBER_SAMPLING, sData.mStrNumSampling);
                objSampling.put(SamplingColumns.AQL, sData.mStrAql);
                objSampling.put(SamplingColumns.ACCEPT, sData.mStrNumAccept);
                objSampling.put(SamplingColumns.REJECT, sData.mStrNumReject);

                arraySampling.put(objSampling);
            }
            dataObject.put("sampling", arraySampling);

            UserData currentUser = UserData.currentUser(this);
//            String str = "asdfasdf";
//
//            if (currentUser != null) {
//                str = currentUser.mStrUsername;
//            }
//            Toast.makeText(this, str, Toast.LENGTH_SHORT).show();

            API_Manager.getInstance().uploadReport(
                    currentUser.mStrUsername,
                    dataObject,
                    mUploadResponseHandler
            );
        }
        catch(JSONException e) {
            mProgressDialog.dismiss();
            e.printStackTrace();
        }

        return true;
    }

    private void uploadLocationPhoto() {
        if (TextUtils.isEmpty(mLocationData.mStrPhoto)) {
            mProgressDialog.dismiss();
            return;
        }

        mProgressDialog.setMessage("正在上报现场照片...");

        UserData currentUser = UserData.currentUser(this);
        API_Manager.getInstance().uploadLocationPhoto(
                ReportDetailActivity.this,
                currentUser.mStrUsername,
                mStrReportID,
                mLocationData.mStrPhoto,
                mUploadResponseHandler
        );
    }

    private CollectImgData getUploadImageData(int index) {
        int nIndex = 0;
        CollectImgData collectImgData = null;

        // get image data for the current index
        for (CollectImgData ciData : mArrayPhotoPack) {
            if (nIndex == index) {
                collectImgData = ciData;
                break;
            }

            nIndex++;
        }

        if (collectImgData != null) {
            return collectImgData;
        }

        for (CollectImgData ciData : mArrayPhotoProduct) {
            if (nIndex == index) {
                collectImgData = ciData;
                break;
            }
            nIndex++;
        }

        if (collectImgData != null) {
            return collectImgData;
        }

        for (CollectImgData ciData : mArrayPhotoStructure) {
            if (nIndex == index) {
                collectImgData = ciData;
                break;
            }
            nIndex++;
        }

        if (collectImgData != null) {
            return collectImgData;
        }

        for (CollectImgData ciData : mArrayPhotoMark) {
            if (nIndex == index) {
                collectImgData = ciData;
                break;
            }
            nIndex++;
        }

        if (collectImgData != null) {
            return collectImgData;
        }

        for (CollectImgData ciData : mArrayPhotoDefect) {
            if (nIndex == index) {
                collectImgData = ciData;
                break;
            }
            nIndex++;
        }

        return collectImgData;
    }

    private void uploadCollectionPhoto() {

        int nImgIndex = mnCurCollectionIndex + 1;
        mProgressDialog.setMessage("正在上报采集照片..." + nImgIndex + "/" + mnTotalCollectionCount);

        CollectImgData ciData;

//        if (mnCurCollectionIndex > 0) {
//            ciData = getUploadImageData(mnCurCollectionIndex - 1);
//
//            ciData.mStatus = BaseData.DS_UPDATED;
//            ciData.mnUploaded = 1;
//            ciData.save(mStrReportID);
//        }

        ciData = getUploadImageData(mnCurCollectionIndex);

        mnCurCollectionIndex++;

//        if (ciData.mnUploaded == 0) {
            UserData currentUser = UserData.currentUser(this);
            API_Manager.getInstance().uploadCollectionPhoto(
                    ReportDetailActivity.this,
                    currentUser.mStrUsername,
                    mStrReportID,
                    ciData.mStrImgAddress,
                    ciData.mStrType,
                    ciData.mStrDesc,
                    mUploadResponseHandler
            );
//        }
//        else {
//            doUploadFunc();
//        }
    }

    private void uploadSigningImage(int type) {
        String strFilePath = "";

        if (type == SignActivity.TYPE_CLIENT) {
            mProgressDialog.setMessage("正在上传客户签名图片...");
            strFilePath = mStrClientImgPath;
        }
        else {
            mProgressDialog.setMessage("正在上传审核人签名图片...");
            strFilePath = mStrInspectorImgPath;
        }

        UserData currentUser = UserData.currentUser(this);
        API_Manager.getInstance().uploadSigningImage(
                ReportDetailActivity.this,
                currentUser.mStrUsername,
                mStrReportID,
                type,
                strFilePath,
                mUploadResponseHandler
        );
    }

    private void getbackReport() {
        mProgressDialog = ProgressDialog.show(this, "", "正在取回...");
        UserData currentUser = UserData.currentUser(this);

        API_Manager.getInstance().getbackReport(
                currentUser.mStrUsername,
                mStrReportID,
                new TextHttpResponseHandler() {

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String response, Throwable throwable) {
                        mProgressDialog.dismiss();
                        CommonUtils.createErrorAlertDialog(ReportDetailActivity.this, "Error", Config.MSG_NETWORK_ERROR).show();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String response) {
                        mProgressDialog.dismiss();

                        try {
                            String strResponseDecoded = URLDecoder.decode(response, "UTF-8");
                            JSONObject jsonObject = new JSONObject(strResponseDecoded);

                            Boolean bRes = jsonObject.getBoolean(API_Manager.WEBAPI_RETURN_RESULT);
                            if (!bRes) {
                                CommonUtils.createErrorAlertDialog(ReportDetailActivity.this, "取回失败！").show();
                                return;
                            }

                            // update status
                            mStrReportStatus = ReportData.REPORT_STATUS_MODIFY;
                            saveReportStatusToDB();
                            updateButtons();
                        } catch (UnsupportedEncodingException e) {
                            CommonUtils.createErrorAlertDialog(ReportDetailActivity.this, "取回失败！").show();
                            e.printStackTrace();
                        } catch (JSONException e) {
                            CommonUtils.createErrorAlertDialog(ReportDetailActivity.this, "取回失败！").show();
                            e.printStackTrace();
                        }
                    }
                }
        );
    }

    private void updateStatusToSubmitted() {
        // update status
        mStrReportStatus = ReportData.REPORT_STATUS_SUBMITTED;
        saveReportStatusToDB();
        updateButtons();
    }

    private void showUploadError() {
        if (mnUploadStatus == UPLOAD_STATUS_REPORT) {
            CommonUtils.createErrorAlertDialog(ReportDetailActivity.this, "上报失败！").show();
        }
        else if (mnUploadStatus == UPLOAD_STATUS_LOCATION_PHOTO) {
            CommonUtils.createErrorAlertDialog(ReportDetailActivity.this, "上报现场照片失败！").show();
        }
        else if (mnUploadStatus == UPLOAD_STATUS_COLLECTION_PHOTO) {
            CommonUtils.createErrorAlertDialog(ReportDetailActivity.this, "上报采集照片失败！").show();
        }
        else if (mnUploadStatus == UPLOAD_STATUS_CLIENT_SIGN) {
            CommonUtils.createErrorAlertDialog(ReportDetailActivity.this, "上传客户签名图片失败！").show();
        }
        else if (mnUploadStatus == UPLOAD_STATUS_INSPECTOR_SIGN) {
            CommonUtils.createErrorAlertDialog(ReportDetailActivity.this, "上传审核人签名图片失败！").show();
        }
    }

    private void doUploadFunc() {

        if (mnUploadStatus == UPLOAD_STATUS_REPORT) {
            new MyContentProvider().addSystemLog("上报核查报告", ReportDetailActivity.this);

            uploadLocationPhoto();
            mnUploadStatus = UPLOAD_STATUS_LOCATION_PHOTO;
        }
        else if (mnUploadStatus == UPLOAD_STATUS_LOCATION_PHOTO) {
            new MyContentProvider().addSystemLog("上报现场照片", ReportDetailActivity.this);

            mnTotalCollectionCount = 0;

            mnTotalCollectionCount += mArrayPhotoPack.size();
            mnTotalCollectionCount += mArrayPhotoProduct.size();
            mnTotalCollectionCount += mArrayPhotoStructure.size();
            mnTotalCollectionCount += mArrayPhotoMark.size();
            mnTotalCollectionCount += mArrayPhotoDefect.size();

            if (mnTotalCollectionCount == 0) {
                uploadSigningImage(SignActivity.TYPE_CLIENT);
                mnUploadStatus = UPLOAD_STATUS_CLIENT_SIGN;

                return;
            }

            mnCurCollectionIndex = 0;

            mnUploadStatus = UPLOAD_STATUS_COLLECTION_PHOTO;
            uploadCollectionPhoto();
        }
        else if (mnUploadStatus == UPLOAD_STATUS_COLLECTION_PHOTO) {
            if (mnCurCollectionIndex < mnTotalCollectionCount) {
                uploadCollectionPhoto();
            }
            else {
                new MyContentProvider().addSystemLog("上报核查图像", ReportDetailActivity.this);

                uploadSigningImage(SignActivity.TYPE_CLIENT);
                mnUploadStatus = UPLOAD_STATUS_CLIENT_SIGN;
            }
        }
        else if (mnUploadStatus == UPLOAD_STATUS_CLIENT_SIGN) {
            uploadSigningImage(SignActivity.TYPE_INSPECTOR);
            mnUploadStatus = UPLOAD_STATUS_INSPECTOR_SIGN;
        }
        else if (mnUploadStatus == UPLOAD_STATUS_INSPECTOR_SIGN) {
            mProgressDialog.dismiss();
            updateStatusToSubmitted();
        }
    }

    private TextHttpResponseHandler mUploadResponseHandler = new TextHttpResponseHandler() {
        @Override
        public void onFailure(int statusCode, Header[] headers, String response, Throwable throwable) {

            boolean bExit = true;

            if (mnUploadStatus == UPLOAD_STATUS_LOCATION_PHOTO) {
                if (mnUploadRetry < UPLOAD_MAX_RETRY) {
                    uploadLocationPhoto();
                    bExit = false;
                }
            }
            else if (mnUploadStatus == UPLOAD_STATUS_COLLECTION_PHOTO) {
                if (mnUploadRetry < UPLOAD_MAX_RETRY) {
                    uploadCollectionPhoto();
                    bExit = false;
                }
            }

            mnUploadRetry++;

            if (bExit) {
                mProgressDialog.dismiss();

                if (statusCode >= 300) {
                    CommonUtils.createErrorAlertDialog(ReportDetailActivity.this, throwable.getLocalizedMessage()).show();
                } else {
                    CommonUtils.createErrorAlertDialog(ReportDetailActivity.this, Config.MSG_NETWORK_ERROR).show();
                }
            }
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String response) {

            mnUploadRetry = 0;

            String strResponseDecoded = null;
            try {
                strResponseDecoded = URLDecoder.decode(response, "UTF-8");
                JSONObject jsonObject = new JSONObject(strResponseDecoded);

                Boolean bRes = jsonObject.getBoolean(API_Manager.WEBAPI_RETURN_RESULT);
                if (!bRes) {
                    mProgressDialog.dismiss();
                    showUploadError();

                    return;
                }

                doUploadFunc();

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();

                mProgressDialog.dismiss();
                showUploadError();
            }

        }
    };

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (mbEnableEdit) {
            mbNeedSave = true;
        }
    }

    private void removeEditFocus() {
        mEdtApprovalBy.clearFocus();
//        mEdtConfirmClient.clearFocus();
        mEdtAccessories.clearFocus();
        mEdtSamplingResult.clearFocus();
        mEdtWitness.clearFocus();
        mEdtAdditional.clearFocus();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        closeMenu();
        return false;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
