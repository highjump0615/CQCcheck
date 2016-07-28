package com.highjump.cqccollect;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.highjump.cqccollect.model.ElectricCompData;
import com.highjump.cqccollect.model.UserData;
import com.highjump.cqccollect.utils.CommonUtils;

import java.util.Date;


public class DialogConfirmReportActivity extends Activity implements View.OnClickListener {

    private final int SIGN_ACTIVITY_REQEUST = 1;

    private ReportDetailActivity mDetailActivity;

    private Button mButCancel;
    private Button mButOk;

//    private EditText mEditClientName;
    private ImageView mImgViewClientName;

    private EditText mEditClientConfirmDate;
//    private EditText mEditInspectorName;
    private ImageView mImgViewInspectorName;

    private EditText mEditVerifyStartDate;
    private EditText mEditVerifyEndDate;

    private EditText mEditInspectDate;

    private RadioButton mRadioPass;
    private RadioButton mRadioFail;

    private String mStrClientImgPath = "";
    private String mStrConfirmClientDate = "";
    private String mStrVerifyDate = "";
    private String mStrVerifyEndDate = "";
    private String mStrInspectorImgPath = "";
    private String mStrInspectorDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_confirm_report);

        mButCancel = (Button)findViewById(R.id.but_cancel);
        mButCancel.setOnClickListener(this);

        mButOk = (Button)findViewById(R.id.but_ok);
        mButOk.setOnClickListener(this);

        mDetailActivity = ReportDetailActivity.getInstance();

        // get data
        mStrConfirmClientDate = mDetailActivity.mStrConfirmClientDate;
        mStrInspectorDate = mDetailActivity.mStrInspectorDate;
        mStrVerifyDate = mDetailActivity.mLocationData.mStrVerifyDate;
        mStrVerifyEndDate = mDetailActivity.mStrVerifyEndDate;
        mStrClientImgPath = mDetailActivity.mStrClientImgPath;
        mStrInspectorImgPath = mDetailActivity.mStrInspectorImgPath;

        // edit text
//        mEditClientName = (EditText)findViewById(R.id.edit_client_name);
        mImgViewClientName = (ImageView)findViewById(R.id.imgview_client_name);
        mImgViewClientName.setOnClickListener(this);

        mEditClientConfirmDate = (EditText)findViewById(R.id.edit_client_confirm_date);
//        mEditClientConfirmDate.setOnTouchListener(CommonUtils.getOnTouchListenerForDatePicker(this, mEditClientConfirmDate));
        mEditClientConfirmDate.setOnFocusChangeListener(CommonUtils.getOnFocusChangeListenerForDatePicker(this, mEditClientConfirmDate));

        mEditVerifyStartDate = (EditText)findViewById(R.id.edit_verify_start_date);
        mEditVerifyStartDate.setText(mStrVerifyDate);
        mEditVerifyStartDate.setOnFocusChangeListener(CommonUtils.getOnFocusChangeListenerForDatePicker(this, mEditVerifyStartDate));

        mEditVerifyEndDate = (EditText)findViewById(R.id.edit_verify_end_date);
        if (TextUtils.isEmpty(mStrVerifyEndDate)) {
            mEditVerifyEndDate.setText(mStrVerifyDate);
        }
        else {
            mEditVerifyEndDate.setText(mStrVerifyEndDate);
        }
        mEditVerifyEndDate.setOnFocusChangeListener(CommonUtils.getOnFocusChangeListenerForDatePicker(this, mEditVerifyEndDate));

//        mEditInspectorName = (EditText)findViewById(R.id.edit_inspector_name);
        mImgViewInspectorName = (ImageView)findViewById(R.id.imgview_inspector_name);
        mImgViewInspectorName.setOnClickListener(this);

        mEditInspectDate = (EditText)findViewById(R.id.edit_inspect_date);
//        mEditInspectDate.setOnTouchListener(CommonUtils.getOnTouchListenerForDatePicker(this, mEditInspectDate));
        mEditInspectDate.setOnFocusChangeListener(CommonUtils.getOnFocusChangeListenerForDatePicker(this, mEditInspectDate));

        mRadioPass = (RadioButton)findViewById(R.id.rad_pass);
        mRadioPass.setChecked(true);
        mRadioFail = (RadioButton)findViewById(R.id.rad_fail);

        showData();
    }

    private void showData() {
        // client sign
        mImgViewClientName.setImageBitmap(CommonUtils.getBitmapFromUri(Uri.parse(mStrClientImgPath), 1, 200));

        // client date
//        mEditClientName.setText(mDetailActivity.mStrClientName);
        if (TextUtils.isEmpty(mStrConfirmClientDate)) {
            mEditClientConfirmDate.setText(CommonUtils.getFormattedDateString(new Date(), "yyyy-MM-dd"));
        }
        else {
            mEditClientConfirmDate.setText(mStrConfirmClientDate);
        }

        // inspector sign
        mImgViewInspectorName.setImageBitmap(CommonUtils.getBitmapFromUri(Uri.parse(mStrInspectorImgPath), 1, 200));

        // inspector
        if (TextUtils.isEmpty(mDetailActivity.mStrInspectorC)) {
            UserData currentUser = UserData.currentUser(this);
//            mEditInspectorName.setText(currentUser.mStrName);
        }
        else {
//            mEditInspectorName.setText(mDetailActivity.mStrInspectorC);
        }

        // inspector date
        if (TextUtils.isEmpty(mStrInspectorDate)) {
            mEditInspectDate.setText(CommonUtils.getFormattedDateString(new Date(), "yyyy-MM-dd"));
        }
        else {
            mEditInspectDate.setText(mStrInspectorDate);
        }
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id) {
            case R.id.but_cancel:
                finish();
                break;

            case R.id.but_ok:
                confirmData();
                break;

            case R.id.imgview_client_name:
                gotoSignActivity(SignActivity.TYPE_CLIENT);
                break;

            case R.id.imgview_inspector_name:
                gotoSignActivity(SignActivity.TYPE_INSPECTOR);
                break;
        }
    }

    private void gotoSignActivity(int type) {
        Intent intent = new Intent(this, SignActivity.class);
        intent.putExtra(SignActivity.SIGN_TYPE, type);

        if (type == SignActivity.TYPE_CLIENT) {
            intent.putExtra(SignActivity.SIGN_IMG_PATH, mDetailActivity.mStrClientImgPath);
        }
        else if (type == SignActivity.TYPE_INSPECTOR) {
            intent.putExtra(SignActivity.SIGN_IMG_PATH, mDetailActivity.mStrInspectorImgPath);
        }

        startActivityForResult(intent, SIGN_ACTIVITY_REQEUST);
    }

    private void confirmData() {

//        mStrClientName = mEditClientName.getText().toString();
        mStrConfirmClientDate = mEditClientConfirmDate.getText().toString();
//        mStrInspectorC = mEdi®tInspectorName.getText().toString();
        mStrInspectorDate = mEditInspectDate.getText().toString();

        // Check data integrity
//        if (TextUtils.isEmpty(mDetailActivity.mStrClientName)) {
//            Toast.makeText(this, "请输入客户姓名", Toast.LENGTH_SHORT).show();
//            return;
//        }
        if (TextUtils.isEmpty(mStrClientImgPath)) {
            Toast.makeText(this, "请输入客户签名", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(mStrConfirmClientDate)) {
            Toast.makeText(this, "请输入客户确认时间", Toast.LENGTH_SHORT).show();
            return;
        }

        mStrVerifyDate = mEditVerifyStartDate.getText().toString();
        if (TextUtils.isEmpty(mStrVerifyDate)) {
            Toast.makeText(this, "请输入核查开始日期", Toast.LENGTH_SHORT).show();
            return;
        }

        mStrVerifyEndDate = mEditVerifyEndDate.getText().toString();
        if (TextUtils.isEmpty(mStrVerifyEndDate)) {
            Toast.makeText(this, "请输入核查结束日期", Toast.LENGTH_SHORT).show();
            return;
        }

//        if (TextUtils.isEmpty(mDetailActivity.mStrInspectorC)) {
//            Toast.makeText(this, "请输入审核人姓名", Toast.LENGTH_SHORT).show();
//            return;
//        }
        if (TextUtils.isEmpty(mStrInspectorImgPath)) {
            Toast.makeText(this, "请输入审核人签名", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(mStrInspectorDate)) {
            Toast.makeText(this, "请输入审核日期", Toast.LENGTH_SHORT).show();
            return;
        }

        // save data
        if (mRadioPass.isChecked()) {
            mDetailActivity.mnConclusion = 1;
        }
        else {
            mDetailActivity.mnConclusion = 0;
        }

        mDetailActivity.mStrConfirmClientDate = mStrConfirmClientDate;
        mDetailActivity.mStrInspectorDate = mStrInspectorDate;
        mDetailActivity.mStrClientImgPath = mStrClientImgPath;
        mDetailActivity.mStrInspectorImgPath = mStrInspectorImgPath;
        mDetailActivity.mLocationData.mStrVerifyDate = mStrVerifyDate;
        mDetailActivity.mStrVerifyEndDate = mStrVerifyEndDate;
        mDetailActivity.mStrInspectorImgPath = mStrInspectorImgPath;

        mDetailActivity.doConfirmReport();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == SIGN_ACTIVITY_REQEUST) {
            int nSignType = 0;
            String strFilePath = "";

            if (data.hasExtra(SignActivity.SIGN_TYPE)) {
                nSignType = data.getIntExtra(SignActivity.SIGN_TYPE, 0);
            }
            if (data.hasExtra(SignActivity.SIGN_IMG_PATH)) {
                strFilePath = data.getStringExtra(SignActivity.SIGN_IMG_PATH);
            }

            if (nSignType == SignActivity.TYPE_CLIENT) {
                mStrClientImgPath = strFilePath;
                mImgViewClientName.setImageBitmap(CommonUtils.getBitmapFromUri(Uri.parse(strFilePath), 1, 200));
            }
            else if (nSignType == SignActivity.TYPE_INSPECTOR) {
                mStrInspectorImgPath = strFilePath;
                mImgViewInspectorName.setImageBitmap(CommonUtils.getBitmapFromUri(Uri.parse(strFilePath), 1, 200));
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
