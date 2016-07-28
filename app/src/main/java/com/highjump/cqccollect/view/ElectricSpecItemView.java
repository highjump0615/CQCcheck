package com.highjump.cqccollect.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.highjump.cqccollect.R;
import com.highjump.cqccollect.model.ElectricSpecData;
import com.highjump.cqccollect.model.ProductData;

/**
 * Created by highjump on 15-2-7.
 */
public class ElectricSpecItemView extends FrameLayout {

    public TextView textNo;
    public TextView textProductName;
    public TextView textVoltage;
    public TextView textPower;
    public TextView textFreq;
    public TextView textClass;
    public TextView textIp;
    public TextView textCable;
    public TextView textSupply;
    public TextView textPlug;

    public ElectricSpecItemView(Context context) {
        this(context, null);
    }

    public ElectricSpecItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ElectricSpecItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.layout_detail_electric_spec_item, this);

        textNo = (TextView)findViewById(R.id.text_no);
        textProductName = (TextView)findViewById(R.id.text_product_name);
        textVoltage = (TextView)findViewById(R.id.text_rated_voltage);
        textPower = (TextView)findViewById(R.id.text_rated_power);
        textFreq = (TextView)findViewById(R.id.text_rated_freq);
        textClass = (TextView)findViewById(R.id.text_class);
        textIp = (TextView)findViewById(R.id.text_ip_grade);
        textCable = (TextView)findViewById(R.id.text_cable_spec);
        textSupply = (TextView)findViewById(R.id.text_supply);
        textPlug = (TextView)findViewById(R.id.text_plug);
    }

    public void initData(ElectricSpecData electricSpecData) {
        textNo.setText("" + electricSpecData.mProduct.mnNoInternal);
        textProductName.setText(electricSpecData.mProduct.mStrName);
        textVoltage.setText(electricSpecData.mStrRatedVoltage);
        textPower.setText(electricSpecData.mStrRatedPower);
        textFreq.setText(electricSpecData.mStrRatedFreq);
        textClass.setText(electricSpecData.mStrClass);
        textIp.setText(electricSpecData.mStrIpGrade);
        textCable.setText(electricSpecData.mStrCableSpec);
        textSupply.setText(electricSpecData.mStrSupply);
        textPlug.setText(electricSpecData.mStrPlug);
    }
}
