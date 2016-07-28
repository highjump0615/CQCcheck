package com.highjump.cqccollect.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.highjump.cqccollect.R;
import com.highjump.cqccollect.model.OtherInfoData;
import com.highjump.cqccollect.model.UnitPackData;

/**
 * Created by highjump on 15-2-7.
 */
public class OtherInfoItemView extends FrameLayout {

    public TextView textNo;
    public TextView textOtherInfo;

    public OtherInfoItemView(Context context) {
        this(context, null);
    }

    public OtherInfoItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OtherInfoItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.layout_detail_other_info_item, this);

        textNo = (TextView)findViewById(R.id.text_no);
        textOtherInfo = (TextView)findViewById(R.id.text_other_info);
    }

    public void initData(OtherInfoData oiData, int nIndex) {
        textNo.setText("" + nIndex);
        textOtherInfo.setText(oiData.mStrOtherInfo);
    }
}
