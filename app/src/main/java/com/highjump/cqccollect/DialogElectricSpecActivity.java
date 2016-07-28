package com.highjump.cqccollect;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.highjump.cqccollect.model.BaseData;
import com.highjump.cqccollect.model.ElectricSpecData;
import com.highjump.cqccollect.model.ProductData;

import java.util.ArrayList;


public class DialogElectricSpecActivity extends DetailDialogActivity {

    private Spinner mSpinProduct;
    private ReportDetailActivity mDetailActivity;
    private ArrayList<String> mArrayStrProd;

    private EditText mEditVoltage;
    private EditText mEditPower;
    private EditText mEditFreq;
    private EditText mEditClass;
    private EditText mEditIp;
    private EditText mEditCable;
    private EditText mEditSupply;
    private EditText mEditPlug;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_electric_spec);

        initControls();

        mDetailActivity = ReportDetailActivity.getInstance();

        // init product spinner
        mSpinProduct = (Spinner)findViewById(R.id.spin_product);

        mArrayStrProd = mDetailActivity.getProductNames();

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mArrayStrProd);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinProduct.setAdapter(spinnerArrayAdapter);

        // edit text
        mEditVoltage = (EditText)findViewById(R.id.edit_voltage);
        mEditPower = (EditText)findViewById(R.id.edit_power);
        mEditFreq = (EditText)findViewById(R.id.edit_freq);
        mEditClass = (EditText)findViewById(R.id.edit_class);
        mEditIp = (EditText)findViewById(R.id.edit_ip);
        mEditCable = (EditText)findViewById(R.id.edit_cable);
        mEditSupply = (EditText)findViewById(R.id.edit_supply);
        mEditPlug = (EditText)findViewById(R.id.edit_plug);

        showData();
    }

    private void showData() {
        if (mDetailActivity.mnCurElectricSpec >= 0) {
            ElectricSpecData esData = mDetailActivity.mArrayElectricSpec.get(mDetailActivity.mnCurElectricSpec);

            // product index
            mSpinProduct.setSelection(mDetailActivity.getProductIndex(esData.mProduct));
            mEditVoltage.setText(esData.mStrRatedVoltage);
            mEditPower.setText(esData.mStrRatedPower);
            mEditFreq.setText(esData.mStrRatedFreq);
            mEditClass.setText(esData.mStrClass);
            mEditIp.setText(esData.mStrIpGrade);
            mEditCable.setText(esData.mStrCableSpec);
            mEditSupply.setText(esData.mStrSupply);
            mEditPlug.setText(esData.mStrPlug);
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

        if (mDetailActivity.mnCurElectricSpec < 0) {
            return;
        }

        ElectricSpecData electricSpecData = mDetailActivity.mArrayElectricSpec.get(mDetailActivity.mnCurElectricSpec);
        electricSpecData.mStatus += BaseData.DS_DELETED;
    }

    private boolean saveData() {

        int nProdIndex = mSpinProduct.getSelectedItemPosition();
        if (nProdIndex < 0) {
            Toast.makeText(this, "请选择产品", Toast.LENGTH_SHORT).show();
            return false;
        }

        ProductData productData = mDetailActivity.mArrayProduct.get(nProdIndex);

        ElectricSpecData electricSpecData;

        if (mDetailActivity.mnCurElectricSpec >= 0) {
            electricSpecData = mDetailActivity.mArrayElectricSpec.get(mDetailActivity.mnCurElectricSpec);
        }
        else {
            electricSpecData = new ElectricSpecData();
        }

        electricSpecData.mProduct = productData;
        electricSpecData.mStrRatedVoltage = mEditVoltage.getText().toString();
        electricSpecData.mStrRatedPower = mEditPower.getText().toString();
        electricSpecData.mStrRatedFreq = mEditFreq.getText().toString();
        electricSpecData.mStrClass = mEditClass.getText().toString();
        electricSpecData.mStrIpGrade = mEditIp.getText().toString();
        electricSpecData.mStrCableSpec = mEditCable.getText().toString();
        electricSpecData.mStrSupply = mEditSupply.getText().toString();
        electricSpecData.mStrPlug = mEditPlug.getText().toString();

        if (mDetailActivity.mnCurElectricSpec >= 0) {
            electricSpecData.mStatus += BaseData.DS_UPDATED;
        }
        else {
            electricSpecData.mStatus += BaseData.DS_ADDED;
            mDetailActivity.mArrayElectricSpec.add(electricSpecData);
        }

        return true;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
