package com.highjump.cqccollect.view;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.highjump.cqccollect.R;
import com.highjump.cqccollect.model.CollectImgData;
import com.highjump.cqccollect.utils.CommonUtils;

/**
 * Created by highjump on 15-2-7.
 */
public class CollectImgItemView extends FrameLayout {

    public ImageView mImgviewPhoto;
    public TextView mTextDesc;

    public LinearLayout mLayoutParent;

    public CollectImgItemView(Context context) {
        this(context, null);
    }

    public CollectImgItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CollectImgItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.layout_detail_photo_item, this);

        mImgviewPhoto = (ImageView)findViewById(R.id.imgview_photo);
        mTextDesc = (TextView)findViewById(R.id.text_desc);
    }

    public void initData(CollectImgData ciData) {
        mImgviewPhoto.setImageBitmap(CommonUtils.getBitmapFromUri(Uri.parse(ciData.mStrImgAddress), 1, 300));
        mTextDesc.setText(ciData.mStrDesc);
    }
}
