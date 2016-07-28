package com.highjump.cqccollect;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.highjump.cqccollect.model.UserData;
import com.highjump.cqccollect.utils.CommonUtils;

import java.util.Date;

/**
 * Created by highjump on 15-1-20.
 */
public class MenuActivity extends Activity implements View.OnClickListener {

    private LinearLayout mLayoutMenu;
    private TextView mTxtPageTitle;

    private Button mButMenuTask;
    private Button mButMenuReport;
    private Button mButMenuOperation;

    // bottom bar
    private TextView mTextUnit;
    private TextView mTextUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void initView(String title) {
        ImageView imageView = (ImageView)findViewById(R.id.imgview_menu);
        imageView.setOnClickListener(this);

        mLayoutMenu = (LinearLayout)findViewById(R.id.layout_menu);
        mTxtPageTitle = (TextView)findViewById(R.id.text_page_title);
        mTxtPageTitle.setText(title);

        mButMenuTask = (Button)findViewById(R.id.but_menu_task);
        mButMenuTask.setOnClickListener(this);

        mButMenuReport = (Button)findViewById(R.id.but_menu_report);
        mButMenuReport.setOnClickListener(this);

        Button button = (Button)findViewById(R.id.but_menu_location);
        button.setOnClickListener(this);
        if (this instanceof LocationActivity) {
            button.setEnabled(false);
        }

        mButMenuOperation = (Button)findViewById(R.id.but_menu_operation);
        mButMenuOperation.setOnClickListener(this);

        button = (Button)findViewById(R.id.but_menu_seturl);
        button.setOnClickListener(this);

        enableMenuItem(true);

        TextView textView = (TextView)findViewById(R.id.text_today);

        if (textView != null) {
            textView.setText(CommonUtils.getFormattedDateString(new Date(), "yyyy年MM月dd日"));
        }
    }

    public void enableMenuItem(boolean bEnabled) {

        mButMenuTask.setEnabled(bEnabled);
        mButMenuReport.setEnabled(bEnabled);
        mButMenuOperation.setEnabled(bEnabled);

        if (bEnabled) {
            if (this instanceof TaskActivity) {
//                mButMenuTask.setEnabled(false);
            }
            if (this instanceof ReportDetailActivity || this instanceof MainActivity) {
                mButMenuReport.setEnabled(false);
            }
            if (this instanceof OperationActivity) {
                mButMenuOperation.setEnabled(false);
            }
        }
    }

    private void showMenu() {
        if (mLayoutMenu.getVisibility() == View.VISIBLE) {
            mLayoutMenu.setVisibility(View.GONE);
        }
        else {
            mLayoutMenu.setVisibility(View.VISIBLE);
        }
    }

    public void closeMenu() {
        if (mLayoutMenu.getVisibility() == View.VISIBLE) {
            mLayoutMenu.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.imgview_menu:
                showMenu();
                break;

            case R.id.but_menu_report:
                showMenu();
                if (this instanceof MainActivity) {
                    break;
                }

                CommonUtils.moveNextActivity(this, MainActivity.class, true);
                break;

            case R.id.but_menu_task:
                showMenu();
                if (this instanceof TaskActivity) {
                    break;
                }

                CommonUtils.moveNextActivity(this, TaskActivity.class, true);
                break;

            case R.id.but_menu_location:
                showMenu();
                if (this instanceof LocationActivity) {
                    break;
                }

                CommonUtils.moveNextActivity(this, LocationActivity.class, false);
                break;

            case R.id.but_menu_operation:
                showMenu();
                if (this instanceof OperationActivity) {
                    break;
                }

                CommonUtils.moveNextActivity(this, OperationActivity.class, true);
                break;

            case R.id.but_menu_seturl:
                showMenu();
                CommonUtils.showSetUrlDialog(this);
                break;
        }
    }

    protected void setBottomBarUserInfo() {
        // set current user
        UserData currentUser = UserData.currentUser(this);

        mTextUnit = (TextView)findViewById(R.id.text_unit);
        mTextUnit.setText(currentUser.mStrUnit);

        mTextUsername = (TextView)findViewById(R.id.text_username);
        mTextUsername.setText(currentUser.mStrName);
    }
}
