package com.highjump.cqccollect;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.highjump.cqccollect.model.BaseData;
import com.highjump.cqccollect.model.ExportCartonData;


public class DialogExportCartonActivity extends DetailDialogActivity {

    private ReportDetailActivity mDetailActivity;

    private EditText mEditPcs;
    private EditText mEditExtDimen;
    private EditText mEditThickness;
    private EditText mEditLayer;
    private EditText mEditSealType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_export_carton);

        initControls();

        mDetailActivity = ReportDetailActivity.getInstance();

        // edit text
        mEditPcs = (EditText)findViewById(R.id.edit_pcs);
        mEditExtDimen = (EditText)findViewById(R.id.edit_ext_dimen);
        mEditThickness = (EditText)findViewById(R.id.edit_thickness);
        mEditLayer = (EditText)findViewById(R.id.edit_layer);
        mEditSealType = (EditText)findViewById(R.id.edit_seal_type);

        showData();
    }

    private void showData() {
        if (mDetailActivity.mnCurExportCarton >= 0) {
            ExportCartonData ecData = mDetailActivity.mArrayExportCarton.get(mDetailActivity.mnCurExportCarton);

            // product index
            mEditPcs.setText(ecData.mStrPcsCarton);
            mEditExtDimen.setText(ecData.mStrExtDimen);
            mEditThickness.setText(ecData.mStrThickness);
            mEditLayer.setText(ecData.mStrLayers);
            mEditSealType.setText(ecData.mStrSealingType);
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
        ExportCartonData exportCartonData;

        if (mDetailActivity.mnCurExportCarton >= 0) {
            exportCartonData = mDetailActivity.mArrayExportCarton.get(mDetailActivity.mnCurExportCarton);
        }
        else {
            exportCartonData = new ExportCartonData();
        }

        exportCartonData.mStrPcsCarton = mEditPcs.getText().toString();
        exportCartonData.mStrExtDimen = mEditExtDimen.getText().toString();
        exportCartonData.mStrThickness = mEditThickness.getText().toString();
        exportCartonData.mStrLayers = mEditLayer.getText().toString();
        exportCartonData.mStrSealingType = mEditSealType.getText().toString();

        if (mDetailActivity.mnCurExportCarton >= 0) {
            exportCartonData.mStatus += BaseData.DS_UPDATED;
        }
        else {
            exportCartonData.mStatus += BaseData.DS_ADDED;
            mDetailActivity.mArrayExportCarton.add(exportCartonData);
        }
    }

    private void deleteData() {

        if (mDetailActivity.mnCurExportCarton < 0) {
            return;
        }

        ExportCartonData exportCartonData = mDetailActivity.mArrayExportCarton.get(mDetailActivity.mnCurExportCarton);
        exportCartonData.mStatus += BaseData.DS_DELETED;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
