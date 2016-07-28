package com.highjump.cqccollect;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.highjump.cqccollect.model.BaseData;
import com.highjump.cqccollect.model.DataMeasuredData;
import com.highjump.cqccollect.model.ProductData;

import java.util.ArrayList;


public class DialogDataMeasuredActivity extends DetailDialogActivity {

    private Spinner mSpinProduct;
    private ReportDetailActivity mDetailActivity;
    private ArrayList<String> mArrayStrProd;

    private EditText mEditNumSampling;
    private EditText mEditStrength;
    private EditText mEditEarthResist;
    private EditText mEditLeakCur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_data_measured);

        initControls();

        mDetailActivity = ReportDetailActivity.getInstance();

        // init product spinner
        mSpinProduct = (Spinner)findViewById(R.id.spin_product);

        mArrayStrProd = mDetailActivity.getProductNames();

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mArrayStrProd);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinProduct.setAdapter(spinnerArrayAdapter);

        // edit text
        mEditNumSampling = (EditText)findViewById(R.id.edit_num_sampling);
        mEditStrength = (EditText)findViewById(R.id.edit_strength);
        mEditEarthResist = (EditText)findViewById(R.id.edit_earth_resist);
        mEditLeakCur = (EditText)findViewById(R.id.edit_leak_cur);

        showData();
    }

    private void showData() {
        if (mDetailActivity.mnCurDataMeasured >= 0) {
            DataMeasuredData dmData = mDetailActivity.mArrayDataMeasured.get(mDetailActivity.mnCurDataMeasured);

            // product index
            mSpinProduct.setSelection(mDetailActivity.getProductIndex(dmData.mProduct));
            mEditNumSampling.setText(dmData.mStrNumberSampling);
            mEditStrength.setText(dmData.mStrElectricStrength);
            mEditEarthResist.setText(dmData.mStrEarthResist);
            mEditLeakCur.setText(dmData.mStrLeakCur);
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
                if (saveData()) {
                    finish();
                }
                break;

            case R.id.but_delete:
                deleteData();
                finish();
                break;
        }
    }

    private void deleteData() {

        if (mDetailActivity.mnCurDataMeasured < 0) {
            return;
        }

        DataMeasuredData dataMeasuredData = mDetailActivity.mArrayDataMeasured.get(mDetailActivity.mnCurDataMeasured);
        dataMeasuredData.mStatus += BaseData.DS_DELETED;
    }

    private boolean saveData() {

        int nProdIndex = mSpinProduct.getSelectedItemPosition();
        if (nProdIndex < 0) {
            Toast.makeText(this, "请选择产品", Toast.LENGTH_SHORT).show();
            return false;
        }

        ProductData productData = mDetailActivity.mArrayProduct.get(nProdIndex);

        DataMeasuredData dataMeasuredData;

        if (mDetailActivity.mnCurDataMeasured >= 0) {
            dataMeasuredData = mDetailActivity.mArrayDataMeasured.get(mDetailActivity.mnCurDataMeasured);
        }
        else {
            dataMeasuredData = new DataMeasuredData();
        }

        dataMeasuredData.mProduct = productData;
        dataMeasuredData.mStrNumberSampling= mEditNumSampling.getText().toString();
        dataMeasuredData.mStrElectricStrength= mEditStrength.getText().toString();
        dataMeasuredData.mStrEarthResist= mEditEarthResist.getText().toString();
        dataMeasuredData.mStrLeakCur= mEditLeakCur.getText().toString();

        if (mDetailActivity.mnCurDataMeasured >= 0) {
            dataMeasuredData.mStatus += BaseData.DS_UPDATED;
        }
        else {
            dataMeasuredData.mStatus += BaseData.DS_ADDED;
            mDetailActivity.mArrayDataMeasured.add(dataMeasuredData);
        }

        return true;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
