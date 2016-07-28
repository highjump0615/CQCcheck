package com.highjump.cqccollect.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.highjump.cqccollect.R;
import com.highjump.cqccollect.model.ElectricCompData;
import com.highjump.cqccollect.model.InstrumentData;

/**
 * Created by highjump on 15-2-7.
 */
public class ElectricCompItemView extends FrameLayout {

    public TextView textName;
    public TextView textTradeMark;
    public TextView textModel;
    public TextView textSpec;
    public TextView textConformityMark;
    public TextView textSameY;
    public TextView textSameN;

    public ElectricCompItemView(Context context) {
        this(context, null);
    }

    public ElectricCompItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ElectricCompItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.layout_detail_electric_comp_item, this);

        textName = (TextView)findViewById(R.id.text_component_name);
        textTradeMark = (TextView)findViewById(R.id.text_trade_mark);
        textModel = (TextView)findViewById(R.id.text_model);
        textSpec = (TextView)findViewById(R.id.text_spec);
        textConformityMark = (TextView)findViewById(R.id.text_conformity_mark);
        textSameY = (TextView)findViewById(R.id.text_same_y);
        textSameN = (TextView)findViewById(R.id.text_same_n);
    }

    public void initData(ElectricCompData ecData) {
        textName.setText(ecData.mStrName);
        textTradeMark.setText(ecData.mStrTradeMark);
        textModel.setText(ecData.mStrModel);
        textSpec.setText(ecData.mStrSpec);
        textConformityMark.setText(ecData.mStrConformityMark);

        if (ecData.mnSameAs == 0) {
            textSameN.setText("N");
            textSameY.setText("");
        }
        else {
            textSameN.setText("");
            textSameY.setText("Y");
        }
    }
}
