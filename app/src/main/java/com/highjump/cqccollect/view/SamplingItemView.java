package com.highjump.cqccollect.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.highjump.cqccollect.R;
import com.highjump.cqccollect.model.SamplingData;

/**
 * Created by highjump on 15-2-7.
 */
public class SamplingItemView extends FrameLayout {

    public TextView textProdName;
    public TextView textNumTotal;
    public TextView textNumSampling;
    public TextView textAQL;
    public TextView textAccept;
    public TextView textReject;

    public SamplingItemView(Context context) {
        this(context, null);
    }

    public SamplingItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SamplingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.layout_detail_sampling_item, this);

        textProdName = (TextView)findViewById(R.id.text_product_name);
        textNumTotal = (TextView)findViewById(R.id.text_number_total);
        textNumSampling = (TextView)findViewById(R.id.text_number_sampling);
        textAQL = (TextView)findViewById(R.id.text_aql);
        textAccept = (TextView)findViewById(R.id.text_number_accept);
        textReject = (TextView)findViewById(R.id.text_number_reject);
    }

    public void initData(SamplingData sData) {
        textProdName.setText(sData.mProduct.mStrName);
        textNumTotal.setText(sData.mProduct.mStrQuantity);
        textNumSampling.setText(sData.mStrNumSampling);
        textAQL.setText(sData.mStrAql);
        textAccept.setText(sData.mStrNumAccept);
        textReject.setText(sData.mStrNumReject);
    }
}
