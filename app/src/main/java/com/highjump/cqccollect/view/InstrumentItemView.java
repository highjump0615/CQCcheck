package com.highjump.cqccollect.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.highjump.cqccollect.R;
import com.highjump.cqccollect.model.DataMeasuredData;
import com.highjump.cqccollect.model.InstrumentData;

/**
 * Created by highjump on 15-2-7.
 */
public class InstrumentItemView extends FrameLayout {

    public TextView textName;
    public TextView textModel;
    public TextView textRefNo;
    public TextView textValidity;

    public InstrumentItemView(Context context) {
        this(context, null);
    }

    public InstrumentItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public InstrumentItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.layout_detail_instrument_item, this);

        textName = (TextView)findViewById(R.id.text_instrument);
        textModel = (TextView)findViewById(R.id.text_model);
        textRefNo = (TextView)findViewById(R.id.text_ref_no);
        textValidity = (TextView)findViewById(R.id.text_validity);
    }

    public void initData(InstrumentData instrumentData) {
        textName.setText(instrumentData.mStrName);
        textModel.setText(instrumentData.mStrModel);
        textRefNo.setText(instrumentData.mStrRefNo);
        textValidity.setText(instrumentData.mStrValidity);
    }
}
