package com.highjump.cqccollect;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import com.highjump.cqccollect.model.BaseData;
import com.highjump.cqccollect.model.ElectricCompData;


public class DialogElectricCompActivity extends DetailDialogActivity {

    private ReportDetailActivity mDetailActivity;

    private EditText mEditName;
    private EditText mEditTradeMark;
    private EditText mEditModel;
    private EditText mEditSpec;
    private EditText mEditConformityMark;

    private RadioButton mRadioSameY;
    private RadioButton mRadioSameN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_electric_comp);

        initControls();

        mDetailActivity = ReportDetailActivity.getInstance();

        // edit text
        mEditName = (EditText)findViewById(R.id.edit_name);
        mEditTradeMark = (EditText)findViewById(R.id.edit_trade_mark);
        mEditModel = (EditText)findViewById(R.id.edit_model);
        mEditSpec = (EditText)findViewById(R.id.edit_spec);
        mEditConformityMark = (EditText)findViewById(R.id.edit_conform_mark);

        mRadioSameY = (RadioButton)findViewById(R.id.rad_yes);
        mRadioSameY.setChecked(true);
        mRadioSameN = (RadioButton)findViewById(R.id.rad_no);

        showData();
    }

    private void showData() {
        if (mDetailActivity.mnCurElectricComp >= 0) {
            ElectricCompData ecData = mDetailActivity.mArrayElectricComp.get(mDetailActivity.mnCurElectricComp);

            // product index
            mEditName.setText(ecData.mStrName);
            mEditTradeMark.setText(ecData.mStrTradeMark);
            mEditModel.setText(ecData.mStrModel);
            mEditSpec.setText(ecData.mStrSpec);
            mEditConformityMark.setText(ecData.mStrConformityMark);

            if (ecData.mnSameAs == 0) {
                mRadioSameN.setChecked(true);
            }
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
        ElectricCompData electricCompData;

        if (mDetailActivity.mnCurElectricComp >= 0) {
            electricCompData = mDetailActivity.mArrayElectricComp.get(mDetailActivity.mnCurElectricComp);
        }
        else {
            electricCompData = new ElectricCompData();
        }

        electricCompData.mStrName = mEditName.getText().toString();
        electricCompData.mStrTradeMark = mEditTradeMark.getText().toString();
        electricCompData.mStrModel = mEditModel.getText().toString();
        electricCompData.mStrSpec = mEditSpec.getText().toString();
        electricCompData.mStrConformityMark = mEditConformityMark.getText().toString();

        if (mRadioSameY.isChecked()) {
            electricCompData.mnSameAs = 1;
        }
        else {
            electricCompData.mnSameAs = 0;
        }

        if (mDetailActivity.mnCurElectricComp >= 0) {
            electricCompData.mStatus += BaseData.DS_UPDATED;
        }
        else {
            electricCompData.mStatus += BaseData.DS_ADDED;
            mDetailActivity.mArrayElectricComp.add(electricCompData);
        }
    }

    private void deleteData() {

        if (mDetailActivity.mnCurElectricComp < 0) {
            return;
        }

        ElectricCompData electricCompData = mDetailActivity.mArrayElectricComp.get(mDetailActivity.mnCurElectricComp);
        electricCompData.mStatus += BaseData.DS_DELETED;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
