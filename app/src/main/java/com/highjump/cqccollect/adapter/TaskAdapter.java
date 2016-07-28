package com.highjump.cqccollect.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.highjump.cqccollect.R;
import com.highjump.cqccollect.model.TaskData;

import java.util.ArrayList;

/**
 * Created by highjump on 15-1-20.
 */
public class TaskAdapter extends BaseAdapter {

    private Context mContext;
    private static LayoutInflater mInflater = null;

    private ArrayList<TaskData> mValues;

    public TaskAdapter(Context ctx, ArrayList<TaskData> values) {
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
            vi = mInflater.inflate(R.layout.layout_task_item, null);
        }

        // fill data
        TaskData data = mValues.get(position);

        TextView textView = (TextView)vi.findViewById(R.id.text_no);
        textView.setText(Integer.toString(position + 1));
//        textView.setText(data.mStrTaskNo);

        textView = (TextView)vi.findViewById(R.id.text_report_id);
        textView.setText(data.mStrReportNo);

        String strTask = "";
        if (!TextUtils.isEmpty(data.mStrDateSchedule)) {
            strTask += data.mStrDateSchedule;
        }
        if (!TextUtils.isEmpty(data.mStrVerifyPlace)) {
            strTask += " " + data.mStrVerifyPlace;
        }
        textView = (TextView)vi.findViewById(R.id.text_task);
        textView.setText(strTask);

        textView = (TextView)vi.findViewById(R.id.text_download);
        if (data.mbDownloaded) {
            textView.setVisibility(View.INVISIBLE);
        }
        else {
            textView.setVisibility(View.VISIBLE);
        }

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
