package com.highjump.cqccollect;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.highjump.cqccollect.adapter.TaskAdapter;
import com.highjump.cqccollect.api.API_Manager;
import com.highjump.cqccollect.model.TaskData;
import com.highjump.cqccollect.model.UserData;
import com.highjump.cqccollect.provider.AttachmentColumns;
import com.highjump.cqccollect.provider.MyContentProvider;
import com.highjump.cqccollect.provider.ProductColumns;
import com.highjump.cqccollect.provider.ReportColumns;
import com.highjump.cqccollect.provider.UserColumns;
import com.highjump.cqccollect.utils.CommonUtils;
import com.highjump.cqccollect.utils.Config;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;


public class TaskActivity extends MenuActivity implements AdapterView.OnItemClickListener, View.OnTouchListener {

    private TaskAdapter mAdapter;
    private ListView mListView;

    private ProgressDialog mProgressDialog;

    private ArrayList<TaskData> mListTask = new ArrayList<TaskData>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        super.initView(getString(R.string.menu_task));

        // init list
        mListView = (ListView)findViewById(R.id.list_task);
        mAdapter = new TaskAdapter(TaskActivity.this, mListTask);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
        mListView.setOnTouchListener(this);

        setBottomBarUserInfo();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        loadData();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        int id = v.getId();

        switch (id) {
            case R.id.but_menu_task:
                loadData();
                break;

            default:
                break;
        }
    }

    private void loadData() {

        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            return;
        }

        mProgressDialog = ProgressDialog.show(this, "", "正在加载任务...");

        UserData currentUser = UserData.currentUser(this);

        API_Manager.getInstance().getScheduledTask(
                currentUser.mStrUsername,
                new TextHttpResponseHandler() {

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String response, Throwable throwable) {
                        mProgressDialog.dismiss();
                        CommonUtils.createErrorAlertDialog(TaskActivity.this, "Error", Config.MSG_NETWORK_ERROR).show();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String response) {
                        mProgressDialog.dismiss();

                        try {
//                            String strTrimmed = response.trim();
                            String strResponseDecoded = URLDecoder.decode(response, "UTF-8");
                            JSONObject jsonObject = new JSONObject(strResponseDecoded);

                            Boolean bRes = jsonObject.getBoolean(API_Manager.WEBAPI_RETURN_RESULT);
                            if (!bRes) {
                                CommonUtils.createErrorAlertDialog(TaskActivity.this, "联网查询失败！").show();
                                return;
                            }

                            JSONArray dataArray = jsonObject.getJSONArray(API_Manager.WEBAPI_RETURN_DATA);
                            if (dataArray == null) {
                                return;
                            }

                            MyContentProvider contentProvider = new MyContentProvider();

                            mListTask.clear();

                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject taskObject = (JSONObject) dataArray.get(i);

                                TaskData tData = new TaskData();
                                tData.mStrTaskNo = taskObject.getString("task_no");
                                tData.mStrReportNo = taskObject.getString("report_no");

                                if (!taskObject.isNull("verification_place")) {
                                    tData.mStrVerifyPlace = taskObject.getString("verification_place");
                                }

                                if (!taskObject.isNull("applicant")) {
                                    tData.mStrApplicant = taskObject.getString("applicant");
                                }

                                tData.mStrDateSchedule = taskObject.getString("schedule_date");

                                // check whether it is already existing
                                String whereString = "";
                                whereString += ReportColumns.NO + " ='" + tData.mStrReportNo + "'";

                                Cursor cursor = contentProvider.query(ReportColumns.CONTENT_URI, null, whereString, null, null);
                                if (cursor != null) {
                                    if (cursor.getCount() > 0 && cursor.moveToFirst()) {
                                        tData.mbDownloaded = true;
                                    }
                                }

                                mListTask.add(tData);
                            }

                            mAdapter.notifyDataSetChanged();
                        }
                        catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
    }

    @Override
    public void onBackPressed() {
        if (isTaskRoot()) {
            CommonUtils.showExitDialog(this);
        }
        else {
            super.onBackPressed();
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final TaskData data = mListTask.get(position);

        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            return;
        }

        if (data.mbDownloaded) {
            Intent intent = new Intent(this, ReportDetailActivity.class);
            intent.putExtra(ReportDetailActivity.SELECTED_REPORT_ID, data.mStrReportNo);
            startActivity(intent);

            mProgressDialog = ProgressDialog.show(this, "", "正在加载...");
            return;
        }

        mProgressDialog = ProgressDialog.show(this, "", "正在下载任务...");

        UserData currentUser = UserData.currentUser(this);

        API_Manager.getInstance().downloadReport(
                currentUser.mStrUsername,
                data.mStrReportNo,
                data.mStrTaskNo,
                new TextHttpResponseHandler() {

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String response, Throwable throwable) {
                        mProgressDialog.dismiss();
                        CommonUtils.createErrorAlertDialog(TaskActivity.this, "Error", Config.MSG_NETWORK_ERROR).show();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String response) {
                        mProgressDialog.dismiss();

                        try {
                            String strResponseDecoded = URLDecoder.decode(response, "UTF-8");
                            JSONObject jsonObject = new JSONObject(strResponseDecoded);

                            Boolean bRes = jsonObject.getBoolean(API_Manager.WEBAPI_RETURN_RESULT);
                            if (!bRes) {
                                CommonUtils.createErrorAlertDialog(TaskActivity.this, "下载失败！").show();
                                return;
                            }

                            JSONObject dataObject = jsonObject.getJSONObject(API_Manager.WEBAPI_RETURN_DATA);
                            if (dataObject == null) {
                                return;
                            }

                            MyContentProvider contentProvider = new MyContentProvider();

                            // save product data
                            JSONArray productArray = dataObject.getJSONArray("product");
                            for (int i = 0; i < productArray.length(); i++) {
                                JSONObject prodObject = (JSONObject) productArray.get(i);

                                ContentValues contentValueProd = new ContentValues();
                                contentValueProd.put(ProductColumns.PRODUCT_NO, prodObject.getInt(ProductColumns.PRODUCT_NO));
                                contentValueProd.put(ProductColumns.REPORT_NO, dataObject.getString(ReportColumns.NO));
                                contentValueProd.put(ProductColumns.PRODUCT_NAME, prodObject.getString(ProductColumns.PRODUCT_NAME));
                                contentValueProd.put(ProductColumns.QUANTITY, prodObject.getString(ProductColumns.QUANTITY));
                                contentValueProd.put(ProductColumns.PRODUCT_DESCRIPTION, prodObject.getString(ProductColumns.PRODUCT_DESCRIPTION));
                                contentValueProd.put(ProductColumns.PACKAGE_MANNER, prodObject.getString(ProductColumns.PACKAGE_MANNER));

                                contentProvider.insert(ProductColumns.CONTENT_URI, contentValueProd);
                            }

                            // save attachment data
                            if (dataObject.has("attachment")) {
                                JSONArray attachArray = dataObject.getJSONArray("attachment");
                                for (int i = 0; i < attachArray.length(); i++) {
                                    JSONObject attachObject = (JSONObject) attachArray.get(i);

                                    ContentValues contentValueAttach = new ContentValues();
                                    contentValueAttach.put(AttachmentColumns.ATTACHMENT_NO, attachObject.getInt(AttachmentColumns.ATTACHMENT_NO));
                                    contentValueAttach.put(AttachmentColumns.FILE_NAME, attachObject.getString(AttachmentColumns.FILE_NAME));
                                    contentValueAttach.put(AttachmentColumns.REPORT_NO, dataObject.getString(ReportColumns.NO));

                                    contentProvider.insert(AttachmentColumns.CONTENT_URI, contentValueAttach);
                                }
                            }

                            // save report data
                            ContentValues contentValues = new ContentValues();

                            contentValues.put(ReportColumns.NO, dataObject.getString(ReportColumns.NO));
                            contentValues.put(ReportColumns.APPLICATION_NO, dataObject.getString(ReportColumns.APPLICATION_NO));
                            contentValues.put(ReportColumns.TASK_NO, dataObject.getString(ReportColumns.TASK_NO));
                            contentValues.put(ReportColumns.VERIFICATION_PLACE_C, dataObject.getString(ReportColumns.VERIFICATION_PLACE_C));
                            contentValues.put(ReportColumns.VERIFICATION_PLACE_E, dataObject.getString(ReportColumns.VERIFICATION_PLACE_E));
                            contentValues.put(ReportColumns.APPLICANT_C, dataObject.getString(ReportColumns.APPLICANT_C));
                            contentValues.put(ReportColumns.APPLICANT_E, dataObject.getString(ReportColumns.APPLICANT_E));
                            contentValues.put(ReportColumns.APPLICANT_ADDRESS_C, dataObject.getString(ReportColumns.APPLICANT_ADDRESS_C));
                            contentValues.put(ReportColumns.APPLICANT_ADDRESS_E, dataObject.getString(ReportColumns.APPLICANT_ADDRESS_E));
                            contentValues.put(ReportColumns.APPLICANT_TEL, dataObject.getString(ReportColumns.APPLICANT_TEL));
                            contentValues.put(ReportColumns.APPLICANT_EMAIL, dataObject.getString(ReportColumns.APPLICANT_EMAIL));
                            contentValues.put(ReportColumns.MANUFACTURER_C, dataObject.getString(ReportColumns.MANUFACTURER_C));
                            contentValues.put(ReportColumns.MANUFACTURER_E, dataObject.getString(ReportColumns.MANUFACTURER_E));
                            contentValues.put(ReportColumns.MANUFACTURER_ADDRESS_C, dataObject.getString(ReportColumns.MANUFACTURER_ADDRESS_C));
                            contentValues.put(ReportColumns.MANUFACTURER_ADDRESS_E, dataObject.getString(ReportColumns.MANUFACTURER_ADDRESS_E));
                            contentValues.put(ReportColumns.FACTORY_C, dataObject.getString(ReportColumns.FACTORY_C));
                            contentValues.put(ReportColumns.FACTORY_E, dataObject.getString(ReportColumns.FACTORY_E));
                            contentValues.put(ReportColumns.FACTORY_ADDRESS_C, dataObject.getString(ReportColumns.FACTORY_ADDRESS_C));
                            contentValues.put(ReportColumns.FACTORY_ADDRESS_E, dataObject.getString(ReportColumns.FACTORY_ADDRESS_E));
                            contentValues.put(ReportColumns.EXPORTER_C, dataObject.getString(ReportColumns.EXPORTER_C));
                            contentValues.put(ReportColumns.EXPORTER_E, dataObject.getString(ReportColumns.EXPORTER_E));
                            contentValues.put(ReportColumns.EXPORTER_ADDRESS_C, dataObject.getString(ReportColumns.EXPORTER_ADDRESS_C));
                            contentValues.put(ReportColumns.EXPORTER_ADDRESS_E, dataObject.getString(ReportColumns.EXPORTER_ADDRESS_E));
                            contentValues.put(ReportColumns.IMPORTER_C, dataObject.getString(ReportColumns.IMPORTER_C));
                            contentValues.put(ReportColumns.IMPORTER_E, dataObject.getString(ReportColumns.IMPORTER_E));
                            contentValues.put(ReportColumns.IMPORTER_ADDRESS_C, dataObject.getString(ReportColumns.IMPORTER_ADDRESS_C));
                            contentValues.put(ReportColumns.IMPORTER_ADDRESS_E, dataObject.getString(ReportColumns.IMPORTER_ADDRESS_E));
                            contentValues.put(ReportColumns.COMMERCIAL_INVOICE_NO, dataObject.getString(ReportColumns.COMMERCIAL_INVOICE_NO));
                            contentValues.put(ReportColumns.REPORT_TYPE, dataObject.getString(ReportColumns.REPORT_TYPE));

                            contentProvider.insert(ReportColumns.CONTENT_URI, contentValues);

                            data.mbDownloaded = true;

                            mAdapter.notifyDataSetChanged();
                            new MyContentProvider().addSystemLog("核查报告下载", TaskActivity.this);
                        }
                        catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        catch (JSONException e) {
                            e.printStackTrace();

                            CommonUtils.createErrorAlertDialog(TaskActivity.this, "Error", "查询核查任务失败！").show();
                        }
                    }
                }
        );
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
