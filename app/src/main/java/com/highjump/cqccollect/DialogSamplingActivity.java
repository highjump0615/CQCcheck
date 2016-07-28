package com.highjump.cqccollect;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.highjump.cqccollect.model.BaseData;
import com.highjump.cqccollect.model.ProductData;
import com.highjump.cqccollect.model.SamplingData;

import java.util.ArrayList;


public class DialogSamplingActivity extends DetailDialogActivity {

    private Spinner mSpinProduct;
    private ReportDetailActivity mDetailActivity;
    private ArrayList<String> mArrayStrProd;

    private EditText mEditNumSampling;
    private EditText mEditAql;
    private EditText mEditAccept;
    private EditText mEditReject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_sampling);

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
        mEditAql = (EditText)findViewById(R.id.edit_aql);
        mEditAccept = (EditText)findViewById(R.id.edit_accept);
        mEditReject = (EditText)findViewById(R.id.edit_reject);

        showData();
    }

    private void showData() {
        if (mDetailActivity.mnCurSampling >= 0) {
            SamplingData sData = mDetailActivity.mArraySampling.get(mDetailActivity.mnCurSampling);

            // product index
            mSpinProduct.setSelection(mDetailActivity.getProductIndex(sData.mProduct));
            mEditNumSampling.setText(sData.mStrNumSampling);
            mEditAql.setText(sData.mStrAql);
            mEditAccept.setText(sData.mStrNumAccept);
            mEditReject.setText(sData.mStrNumReject);
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

        if (mDetailActivity.mnCurSampling < 0) {
            return;
        }

        SamplingData samplingData = mDetailActivity.mArraySampling.get(mDetailActivity.mnCurSampling);
        samplingData.mStatus += BaseData.DS_DELETED;
    }

    private boolean saveData() {

        int nProdIndex = mSpinProduct.getSelectedItemPosition();
        if (nProdIndex < 0) {
            Toast.makeText(this, "请选择产品", Toast.LENGTH_SHORT).show();
            return false;
        }

        ProductData productData = mDetailActivity.mArrayProduct.get(nProdIndex);

        SamplingData samplingData;

        if (mDetailActivity.mnCurSampling >= 0) {
            samplingData = mDetailActivity.mArraySampling.get(mDetailActivity.mnCurSampling);
        }
        else {
            samplingData = new SamplingData();
        }

        samplingData.mProduct = productData;
        samplingData.mStrNumSampling = mEditNumSampling.getText().toString();
        samplingData.mStrAql = mEditAql.getText().toString();
        samplingData.mStrNumAccept = mEditAccept.getText().toString();
        samplingData.mStrNumReject = mEditReject.getText().toString();

        if (mDetailActivity.mnCurSampling >= 0) {
            samplingData.mStatus += BaseData.DS_UPDATED;
        }
        else {
            samplingData.mStatus += BaseData.DS_ADDED;
            mDetailActivity.mArraySampling.add(samplingData);
        }

        return true;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
