package com.highjump.cqccollect;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.highjump.cqccollect.adapter.ReportAdapter;

import com.highjump.cqccollect.model.ReportData;
import com.highjump.cqccollect.model.UserData;
import com.highjump.cqccollect.provider.MyContentProvider;
import com.highjump.cqccollect.provider.ReportColumns;
import com.highjump.cqccollect.utils.CommonUtils;
import com.highjump.cqccollect.utils.TencentGPSTracker;

import java.util.ArrayList;


public class MainActivity extends MenuActivity implements AdapterView.OnItemClickListener, View.OnTouchListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private ReportAdapter mAdapter;
    private ListView mListView;

    private ProgressDialog mProgressDialog;

    private ArrayList<ReportData> mDataList = new ArrayList<ReportData>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        super.initView(getString(R.string.menu_report));

        // init list
        mListView = (ListView)findViewById(R.id.list_report);
        mAdapter = new ReportAdapter(MainActivity.this, mDataList);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
        mListView.setOnTouchListener(this);

        if (CommonUtils.mTencentGPSTracker == null) {
            CommonUtils.mTencentGPSTracker = new TencentGPSTracker(this);
        }

        setBottomBarUserInfo();
    }

    @Override
    protected void onResume() {
        super.onResume();

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
    protected void onStart() {
        super.onStart();

        if (CommonUtils.mTencentGPSTracker!= null) {
            CommonUtils.mTencentGPSTracker.startLocation();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (CommonUtils.mTencentGPSTracker != null) {
            CommonUtils.mTencentGPSTracker.stopLocation();
        }
    }

//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public void onBackPressed() {
        if (isTaskRoot()) {
            CommonUtils.showExitDialog(this);
        }
        else {
            super.onBackPressed();
        }
    }

    private void loadData() {
        mDataList.clear();

        int nCount = 0;

        MyContentProvider contentProvider = new MyContentProvider();

        Cursor cursor = contentProvider.query(ReportColumns.CONTENT_URI,
                new String[]{ReportColumns.NO, ReportColumns.VERIFICATION_DATE, ReportColumns.VERIFICATION_PLACE_C, ReportColumns.REPORT_STATUS},
                null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                nCount++;

                ReportData data = new ReportData();
                data.mStrId = cursor.getString(cursor.getColumnIndex(ReportColumns.NO));
                data.mStrTime = cursor.getString(cursor.getColumnIndex(ReportColumns.VERIFICATION_DATE));
                data.mStrPlace = cursor.getString(cursor.getColumnIndex(ReportColumns.VERIFICATION_PLACE_C));
                data.mStrStatus = cursor.getString(cursor.getColumnIndex(ReportColumns.REPORT_STATUS));

                mDataList.add(data);
            }

            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            return;
        }

        ReportData data = mDataList.get(position);

        Intent intent = new Intent(this, ReportDetailActivity.class);
        intent.putExtra(ReportDetailActivity.SELECTED_REPORT_ID, data.mStrId);
        startActivity(intent);

        mProgressDialog = ProgressDialog.show(this, "", "正在加载...");
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
