package com.highjump.cqccollect;

import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

import com.highjump.cqccollect.adapter.OperationAdapter;
import com.highjump.cqccollect.model.LogData;
import com.highjump.cqccollect.model.ReportData;
import com.highjump.cqccollect.provider.MyContentProvider;
import com.highjump.cqccollect.provider.ProcessLogColumns;
import com.highjump.cqccollect.provider.ReportColumns;
import com.highjump.cqccollect.utils.CommonUtils;

import java.util.ArrayList;


public class OperationActivity extends MenuActivity implements View.OnTouchListener {

    private OperationAdapter mAdapter;
    private ListView mListView;

    private ArrayList<LogData> mDataList = new ArrayList<LogData>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operation);

        super.initView(getString(R.string.menu_operation));

        // init list
        mListView = (ListView)findViewById(R.id.list_operation);
        mAdapter = new OperationAdapter(OperationActivity.this, mDataList);
        mListView.setAdapter(mAdapter);
        mListView.setOnTouchListener(this);

        setBottomBarUserInfo();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
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

    private void loadData() {
        mDataList.clear();

        MyContentProvider contentProvider = new MyContentProvider();

        Cursor cursor = contentProvider.query(ProcessLogColumns.CONTENT_URI,
                new String[]{ProcessLogColumns.PROCESS_TIME, ProcessLogColumns.PROCESS},
                null, null, ProcessLogColumns.SORT_ORDER_DEFAULT);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                LogData data = new LogData();
                data.time = cursor.getString(cursor.getColumnIndex(ProcessLogColumns.PROCESS_TIME));
                data.operation = cursor.getString(cursor.getColumnIndex(ProcessLogColumns.PROCESS));

                mDataList.add(data);
            }

            mAdapter.notifyDataSetChanged();
        }
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
