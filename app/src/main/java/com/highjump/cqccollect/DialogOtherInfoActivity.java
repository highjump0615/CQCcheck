package com.highjump.cqccollect;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.highjump.cqccollect.model.BaseData;
import com.highjump.cqccollect.model.OtherInfoData;


public class DialogOtherInfoActivity extends DetailDialogActivity {

    private ReportDetailActivity mDetailActivity;

    private EditText mEditOtherInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_other_info);

        initControls();

        mDetailActivity = ReportDetailActivity.getInstance();

        // edit text
        mEditOtherInfo = (EditText)findViewById(R.id.edit_other_info);

        showData();
    }

    private void showData() {
        if (mDetailActivity.mnCurOtherInfo >= 0) {
            OtherInfoData oiData = mDetailActivity.mArrayOtherInfo.get(mDetailActivity.mnCurOtherInfo);

            // product index
            mEditOtherInfo.setText(oiData.mStrOtherInfo);
        }
        else {
            mButDelete.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        int id = v.getId();

        switch (id) {
            case R.id.but_save:
                saveData();
                finish();
                break;

            case R.id.but_delete:
                deleteData();
                finish();
                break;
        }
    }

    private void saveData() {
        OtherInfoData otherInfoData;

        if (mDetailActivity.mnCurOtherInfo >= 0) {
            otherInfoData = mDetailActivity.mArrayOtherInfo.get(mDetailActivity.mnCurOtherInfo);
        }
        else {
            otherInfoData = new OtherInfoData();
        }

        otherInfoData.mStrOtherInfo = mEditOtherInfo.getText().toString();

        if (mDetailActivity.mnCurOtherInfo >= 0) {
            otherInfoData.mStatus += BaseData.DS_UPDATED;
        }
        else {
            otherInfoData.mStatus += BaseData.DS_ADDED;
            mDetailActivity.mArrayOtherInfo.add(otherInfoData);
        }
    }

    private void deleteData() {

        if (mDetailActivity.mnCurOtherInfo < 0) {
            return;
        }

        OtherInfoData otherInfoData = mDetailActivity.mArrayOtherInfo.get(mDetailActivity.mnCurOtherInfo);
        otherInfoData.mStatus += BaseData.DS_DELETED;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
