package com.highjump.cqccollect;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.highjump.cqccollect.model.BaseData;
import com.highjump.cqccollect.model.UnitPackData;


public class DialogUnitPackActivity extends DetailDialogActivity {

    private ReportDetailActivity mDetailActivity;

    private EditText mEditTypePack;
    private EditText mEditDimen;
    private EditText mEditThickness;
    private EditText mEditLayer;
    private EditText mEditColor;
    private EditText mEditSealType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_unit_pack);

        initControls();

        mDetailActivity = ReportDetailActivity.getInstance();

        // edit text
        mEditTypePack = (EditText)findViewById(R.id.edit_type_pack);
        mEditDimen = (EditText)findViewById(R.id.edit_dimen);
        mEditThickness = (EditText)findViewById(R.id.edit_thickness);
        mEditLayer = (EditText)findViewById(R.id.edit_layer);
        mEditColor = (EditText)findViewById(R.id.edit_color);
        mEditSealType = (EditText)findViewById(R.id.edit_seal_type);

        showData();
    }

    private void showData() {
        if (mDetailActivity.mnCurUnitPack >= 0) {
            UnitPackData upData = mDetailActivity.mArrayUnitPack.get(mDetailActivity.mnCurUnitPack);

            // product index
            mEditTypePack.setText(upData.mStrPackType);
            mEditDimen.setText(upData.mStrDimen);
            mEditThickness.setText(upData.mStrThickness);
            mEditLayer.setText(upData.mStrLayers);
            mEditColor.setText(upData.mStrPrintingColor);
            mEditSealType.setText(upData.mStrSealingType);
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
        UnitPackData unitPackData;

        if (mDetailActivity.mnCurUnitPack >= 0) {
            unitPackData = mDetailActivity.mArrayUnitPack.get(mDetailActivity.mnCurUnitPack);
        }
        else {
            unitPackData = new UnitPackData();
        }

        unitPackData.mStrPackType = mEditTypePack.getText().toString();
        unitPackData.mStrDimen = mEditDimen.getText().toString();
        unitPackData.mStrThickness = mEditThickness.getText().toString();
        unitPackData.mStrLayers = mEditLayer.getText().toString();
        unitPackData.mStrPrintingColor = mEditColor.getText().toString();
        unitPackData.mStrSealingType = mEditSealType.getText().toString();

        if (mDetailActivity.mnCurUnitPack >= 0) {
            unitPackData.mStatus += BaseData.DS_UPDATED;
        }
        else {
            unitPackData.mStatus += BaseData.DS_ADDED;
            mDetailActivity.mArrayUnitPack.add(unitPackData);
        }
    }

    private void deleteData() {

        if (mDetailActivity.mnCurUnitPack < 0) {
            return;
        }

        UnitPackData unitPackData = mDetailActivity.mArrayUnitPack.get(mDetailActivity.mnCurUnitPack);
        unitPackData.mStatus += BaseData.DS_DELETED;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
