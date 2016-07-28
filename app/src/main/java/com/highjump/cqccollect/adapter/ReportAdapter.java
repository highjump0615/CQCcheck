package com.highjump.cqccollect.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.highjump.cqccollect.R;
import com.highjump.cqccollect.model.ReportData;

import java.util.ArrayList;

/**
 * Created by highjump on 15-1-20.
 */
public class ReportAdapter extends BaseAdapter {

    private ArrayList<ReportData> mValues;

    private Context mContext;
    private static LayoutInflater mInflater = null;

    public ReportAdapter(Context ctx, ArrayList<ReportData> values) {
        mValues = values;
        mContext = ctx;
        mInflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mValues.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        if (vi == null)
        {
            vi = mInflater.inflate(R.layout.layout_report_item, null);
        }

        // fill data
        ReportData data = mValues.get(position);

        TextView textView = (TextView)vi.findViewById(R.id.text_no);
        textView.setText(Integer.toString(position + 1));
        textView = (TextView)vi.findViewById(R.id.text_id);
        textView.setText(data.mStrId);

        String strTitle = "";
        if (data.mStrTime != null && !TextUtils.isEmpty(data.mStrTime)) {
            strTitle = data.mStrTime + " ";
        }
        strTitle += data.mStrPlace;
        textView = (TextView)vi.findViewById(R.id.text_title);
        textView.setText(strTitle);

        textView = (TextView)vi.findViewById(R.id.text_state);
        textView.setText(data.mStrStatus);

//        LinearLayout layoutLinear = (LinearLayout) vi.findViewById(R.id.layout_root);
//        if (position % 2 == 0) {
//            layoutLinear.setBackgroundColor(Color.WHITE);
//        }
//        else {
//            Resources res = mContext.getResources();
//            layoutLinear.setBackgroundColor(res.getColor(R.color.report_item_back));
//        }

        return vi;
    }
}
