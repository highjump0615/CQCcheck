package com.highjump.cqccollect.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.highjump.cqccollect.R;
import com.highjump.cqccollect.model.LogData;
import com.highjump.cqccollect.model.ReportData;

import java.util.ArrayList;

/**
 * Created by highjump on 15-1-20.
 */
public class OperationAdapter extends BaseAdapter {

    private Context mContext;
    private static LayoutInflater mInflater = null;

    private ArrayList<LogData> mValues;

    public OperationAdapter(Context ctx, ArrayList<LogData> values) {
        mContext = ctx;
        mInflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mValues = values;
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
            vi = mInflater.inflate(R.layout.layout_detail_operation_item, null);
        }

        // fill data
        LogData data = mValues.get(position);

        TextView textView = (TextView)vi.findViewById(R.id.text_time);
        textView.setText(data.time);

        textView = (TextView)vi.findViewById(R.id.text_process);
        textView.setText(data.operation);

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
