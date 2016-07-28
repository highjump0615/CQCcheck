package com.highjump.cqccollect.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.highjump.cqccollect.R;
import com.highjump.cqccollect.model.ExportCartonData;
import com.highjump.cqccollect.model.UnitPackData;

/**
 * Created by highjump on 15-2-7.
 */
public class UnitPackItemView extends FrameLayout {

    public TextView textPackType;
    public TextView textDimen;
    public TextView textThickness;
    public TextView textLayer;
    public TextView textColor;
    public TextView textSealingType;

    public UnitPackItemView(Context context) {
        this(context, null);
    }

    public UnitPackItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UnitPackItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.layout_detail_unit_pack_item, this);

        textPackType = (TextView)findViewById(R.id.text_pack_type);
        textDimen = (TextView)findViewById(R.id.text_dimen);
        textThickness = (TextView)findViewById(R.id.text_thickness);
        textLayer = (TextView)findViewById(R.id.text_layers);
        textColor = (TextView)findViewById(R.id.text_print_color);
        textSealingType = (TextView)findViewById(R.id.text_sealing_type);
    }

    public void initData(UnitPackData upData) {
        textPackType.setText(upData.mStrPackType);
        textDimen.setText(upData.mStrDimen);
        textThickness.setText(upData.mStrThickness);
        textLayer.setText(upData.mStrLayers);
        textColor.setText(upData.mStrPrintingColor);
        textSealingType.setText(upData.mStrSealingType);
    }
}
