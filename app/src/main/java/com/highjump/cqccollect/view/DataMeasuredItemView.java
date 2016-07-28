package com.highjump.cqccollect.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.highjump.cqccollect.R;
import com.highjump.cqccollect.model.DataMeasuredData;
import com.highjump.cqccollect.model.ElectricSpecData;

/**
 * Created by highjump on 15-2-7.
 */
public class DataMeasuredItemView extends FrameLayout {

    public TextView textNo;
    public TextView textProductName;
    public TextView textNumSampling;
    public TextView textElecStrength;
    public TextView textEarthResist;
    public TextView textLeakCur;

    public DataMeasuredItemView(Context context) {
        this(context, null);
    }

    public DataMeasuredItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DataMeasuredItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.layout_detail_tech_data_item, this);

        textNo = (TextView)findViewById(R.id.text_no);
        textProductName = (TextView)findViewById(R.id.text_product_name);
        textNumSampling = (TextView)findViewById(R.id.text_number_sampling);
        textElecStrength = (TextView)findViewById(R.id.text_electric_strength);
        textEarthResist = (TextView)findViewById(R.id.text_earth_resist);
        textLeakCur = (TextView)findViewById(R.id.text_leak_cur);
    }

    public void initData(DataMeasuredData dataMeasuredData) {
        textNo.setText("" + dataMeasuredData.mProduct.mnNoInternal);
        textProductName.setText(dataMeasuredData.mProduct.mStrName);
        textNumSampling.setText(dataMeasuredData.mStrNumberSampling);
        textElecStrength.setText(dataMeasuredData.mStrElectricStrength);
        textEarthResist.setText(dataMeasuredData.mStrEarthResist);
        textLeakCur.setText(dataMeasuredData.mStrLeakCur);
    }
}
