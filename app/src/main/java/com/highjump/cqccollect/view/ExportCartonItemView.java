package com.highjump.cqccollect.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.highjump.cqccollect.R;
import com.highjump.cqccollect.model.ElectricCompData;
import com.highjump.cqccollect.model.ExportCartonData;

/**
 * Created by highjump on 15-2-7.
 */
public class ExportCartonItemView extends FrameLayout {

    public TextView textPcs;
    public TextView textExtDimen;
    public TextView textThickness;
    public TextView textLayer;
    public TextView textSealingType;

    public ExportCartonItemView(Context context) {
        this(context, null);
    }

    public ExportCartonItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExportCartonItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.layout_detail_export_carton_item, this);

        textPcs = (TextView)findViewById(R.id.text_pcs_carton);
        textExtDimen = (TextView)findViewById(R.id.text_ext_dimen);
        textThickness = (TextView)findViewById(R.id.text_thickness);
        textLayer = (TextView)findViewById(R.id.text_layers);
        textSealingType = (TextView)findViewById(R.id.text_sealing_type);
    }

    public void initData(ExportCartonData ecData) {
        textPcs.setText(ecData.mStrPcsCarton);
        textExtDimen.setText(ecData.mStrExtDimen);
        textThickness.setText(ecData.mStrThickness);
        textLayer.setText(ecData.mStrLayers);
        textSealingType.setText(ecData.mStrSealingType);
    }
}
