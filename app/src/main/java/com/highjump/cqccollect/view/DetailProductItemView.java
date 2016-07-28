package com.highjump.cqccollect.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.highjump.cqccollect.R;
import com.highjump.cqccollect.model.ProductData;

/**
 * Created by highjump on 15-2-7.
 */
public class DetailProductItemView extends FrameLayout {

    public TextView mTextNo;
    public TextView mTextName;
    public TextView mTextQuantity;
    public TextView mTextDesc;
    public TextView mTextPackManner;

    public DetailProductItemView(Context context) {
        this(context, null);
    }

    public DetailProductItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DetailProductItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.layout_detail_prod_info_item, this);

        mTextNo = (TextView)findViewById(R.id.text_no);
        mTextName = (TextView)findViewById(R.id.text_name);
        mTextQuantity = (TextView)findViewById(R.id.text_quantity);
        mTextDesc = (TextView)findViewById(R.id.text_desc);
        mTextPackManner = (TextView)findViewById(R.id.text_pack_manner);
    }

    public void initData(ProductData productData) {
        mTextNo.setText("" + productData.mnNoInternal);
        mTextName.setText(productData.mStrName);
        mTextQuantity.setText(productData.mStrQuantity);
        mTextDesc.setText(productData.mStrDescription);
        mTextPackManner.setText(productData.mStrPackManner);
    }
}
