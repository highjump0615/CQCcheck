package com.highjump.cqccollect;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.highjump.cqccollect.model.BaseData;
import com.highjump.cqccollect.model.InstrumentData;
import com.highjump.cqccollect.utils.CommonUtils;


public class DialogInstrumentActivity extends DetailDialogActivity {

    private static final String TAG = DialogInstrumentActivity.class.getSimpleName();

    private ReportDetailActivity mDetailActivity;

    private EditText mEditName;
    private EditText mEditModel;
    private EditText mEditRefNo;
    private EditText mEditValidity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_instrument);

        initControls();

        mDetailActivity = ReportDetailActivity.getInstance();

        // edit text
        mEditName = (EditText)findViewById(R.id.edit_instrument);
        mEditModel = (EditText)findViewById(R.id.edit_model);
        mEditRefNo = (EditText)findViewById(R.id.edit_refno);
        mEditValidity = (EditText)findViewById(R.id.edit_validity);
//        mEditValidity.setOnTouchListener(CommonUtils.getOnTouchListenerForDatePicker(this, mEditValidity));
        mEditValidity.setOnFocusChangeListener(CommonUtils.getOnFocusChangeListenerForDatePicker(this, mEditValidity));

        showData();
    }

    private void showData() {
        if (mDetailActivity.mnCurInstrument >= 0) {
            InstrumentData iData = mDetailActivity.mArrayInstrument.get(mDetailActivity.mnCurInstrument);

            // product index
            mEditName.setText(iData.mStrName);
            mEditModel.setText(iData.mStrModel);
            mEditRefNo.setText(iData.mStrRefNo);
            mEditValidity.setText(iData.mStrValidity);
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
        InstrumentData instrumentData;

        if (mDetailActivity.mnCurInstrument >= 0) {
            instrumentData = mDetailActivity.mArrayInstrument.get(mDetailActivity.mnCurInstrument);
        }
        else {
            instrumentData = new InstrumentData();
        }

        instrumentData.mStrName = mEditName.getText().toString();
        instrumentData.mStrModel = mEditModel.getText().toString();
        instrumentData.mStrRefNo = mEditRefNo.getText().toString();
        instrumentData.mStrValidity = mEditValidity.getText().toString();

        if (mDetailActivity.mnCurInstrument >= 0) {
            instrumentData.mStatus += BaseData.DS_UPDATED;
        }
        else {
            instrumentData.mStatus += BaseData.DS_ADDED;
            mDetailActivity.mArrayInstrument.add(instrumentData);
        }
    }

    private void deleteData() {

        if (mDetailActivity.mnCurInstrument < 0) {
            return;
        }

        InstrumentData instrumentData = mDetailActivity.mArrayInstrument.get(mDetailActivity.mnCurInstrument);
        instrumentData.mStatus += BaseData.DS_DELETED;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
