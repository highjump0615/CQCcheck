package com.highjump.cqccollect;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.highjump.cqccollect.api.API_Manager;
import com.highjump.cqccollect.model.UserData;
import com.highjump.cqccollect.provider.MyContentProvider;
import com.highjump.cqccollect.provider.UserColumns;
import com.highjump.cqccollect.utils.CommonUtils;
import com.highjump.cqccollect.utils.Config;
import com.highjump.cqccollect.utils.TencentGPSTracker;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by highjump on 15-1-19.
 */
public class LoginActivity extends Activity implements View.OnClickListener {

    private EditText mEditUsername;
    private EditText mEditPassword;

    private String mStrUsername;
    private String mStrPassword;

    private String mstrNotMatch = "用户名或口令不正确";
    private String mstrGetUserFail = "获取用户信息失败！";

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEditUsername = (EditText)findViewById(R.id.edit_username);
        mEditPassword = (EditText)findViewById(R.id.edit_password);

        Button button = (Button)findViewById(R.id.but_login);
        button.setOnClickListener(this);

        button = (Button)findViewById(R.id.but_seturl);
        button.setOnClickListener(this);

        // load preference
        SharedPreferences preferences = getSharedPreferences(Config.PREF_NAME, Context.MODE_PRIVATE);
        String strUrl = preferences.getString(Config.PREF_URL, API_Manager.SERVER_PATH);
        String strSecCode = preferences.getString(Config.PREF_SECCODE, Config.PASSWORD_SECURITY);

        CommonUtils.setCqcParam(strUrl, strSecCode);

        if (CommonUtils.mTencentGPSTracker == null) {
            CommonUtils.mTencentGPSTracker = new TencentGPSTracker(this);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (CommonUtils.mTencentGPSTracker!= null) {
            CommonUtils.mTencentGPSTracker.startLocation();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (CommonUtils.mTencentGPSTracker != null) {
            CommonUtils.mTencentGPSTracker.stopLocation();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.but_login:
                doLogin();
                break;

            case R.id.but_seturl:
                doSetUrl();
                break;
        }
    }

    private boolean isUserExisting(String username, String password) {
        boolean bRes = false;

        //
        // local authentication
        //
        final MyContentProvider contentProvider = new MyContentProvider();
        final String strMd5 = CommonUtils.getMD5EncryptedString(password + "fukye");

        String whereString = "";
        whereString += UserColumns.USERNAME + " ='" + username + "' AND " + UserColumns.MD + " = '" + strMd5 + "'";

        Cursor cursor = contentProvider.query(UserColumns.CONTENT_URI, null, whereString, null, null);
        if (cursor != null) {
            if (cursor.getCount() > 0 && cursor.moveToFirst()) {
                bRes = true;
            }
        }

        return bRes;
    }

    private void setCurrentUser(String strUsername) {
        new UserData(strUsername);

        // Save user phone and flag of signed into NSUserDefaults
        SharedPreferences preferences = getSharedPreferences(Config.PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Config.PREF_USERNAME, strUsername);
        editor.commit();

        CommonUtils.moveNextActivity(LoginActivity.this, MainActivity.class, true);

        new MyContentProvider().addSystemLog("用户身份认证", this);
    }

    private void doLocalLogin() {
        final String strUsername = mEditUsername.getText().toString();
        final String strPassword = mEditPassword.getText().toString();

        //
        // local authentication
        //
        if (isUserExisting(strUsername, strPassword)) {
            setCurrentUser(strUsername);
        }
        else {
            CommonUtils.createErrorAlertDialog(LoginActivity.this, mstrNotMatch).show();
        }
    }

    private void doLogin() {
        mStrUsername = mEditUsername.getText().toString();
        mStrPassword = mEditPassword.getText().toString();

        // Check data integrity
        if (TextUtils.isEmpty(mStrUsername)) {
            CommonUtils.createErrorAlertDialog(this, "请输入用户名").show();
            return;
        }

        if (TextUtils.isEmpty(mStrUsername)) {
            CommonUtils.createErrorAlertDialog(this, "请输入密码").show();
            return;
        }

        final MyContentProvider contentProvider = new MyContentProvider();

        //
        // online authentication
        //
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            return;
        }

        mProgressDialog = ProgressDialog.show(this, "", "正在进行身份认证...");

        API_Manager.getInstance().userIdentify(
                mStrUsername,
                mStrPassword,
                new TextHttpResponseHandler() {

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String response, Throwable throwable) {
                        mProgressDialog.dismiss();
//                        CommonUtils.createErrorAlertDialog(LoginActivity.this, "Error", throwable.getMessage()).show();

                        doLocalLogin();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String response) {
                        mProgressDialog.dismiss();

                        try {
                            String strResponseDecoded = URLDecoder.decode(response, "UTF-8");
                            JSONObject jsonObject = new JSONObject(strResponseDecoded);

                            Boolean bRes = jsonObject.getBoolean(API_Manager.WEBAPI_RETURN_RESULT);
                            if (!bRes) {
                                CommonUtils.createErrorAlertDialog(LoginActivity.this, mstrNotMatch).show();
                                return;
                            }

                            // add this user to local db
                            if (!isUserExisting(mStrUsername, mStrPassword)) {
                                mProgressDialog = ProgressDialog.show(LoginActivity.this, "", "正在获取用户信息...");

                                API_Manager.getInstance().getUserInfo(
                                        mStrUsername,
                                        mGetUserInfoResponseHandler
                                );
                            }
                            else {
                                setCurrentUser(mStrUsername);
                            }
                        }
                        catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        catch (JSONException e) {
                            e.printStackTrace();

                            doLocalLogin();
                        }
                    }
                }
        );
    }

    private TextHttpResponseHandler mGetUserInfoResponseHandler = new TextHttpResponseHandler() {
        @Override
        public void onFailure(int statusCode, Header[] headers, String response, Throwable throwable) {
            mProgressDialog.dismiss();
            CommonUtils.createErrorAlertDialog(LoginActivity.this, "Error", throwable.getMessage()).show();
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String response) {
            mProgressDialog.dismiss();

            try {
                MyContentProvider contentProvider = new MyContentProvider();

                String strResponseDecoded = URLDecoder.decode(response, "UTF-8");
                JSONObject jsonObject = new JSONObject(strResponseDecoded);

                Boolean bRes = jsonObject.getBoolean(API_Manager.WEBAPI_RETURN_RESULT);
                if (!bRes) {
                    CommonUtils.createErrorAlertDialog(LoginActivity.this, mstrGetUserFail).show();
                    return;
                }

                JSONObject dataObject = jsonObject.getJSONObject(API_Manager.WEBAPI_RETURN_DATA);
                if (dataObject == null) {
                    return;
                }

                final String strMd5 = CommonUtils.getMD5EncryptedString(mStrPassword + Config.PASSWORD_SECURITY);
                ContentValues contentValues = new ContentValues();

                contentValues.put(UserColumns.USERNAME, mStrUsername);
                contentValues.put(UserColumns.MD, strMd5);
                contentValues.put(UserColumns.NAME, dataObject.getString(UserColumns.NAME));
                contentValues.put(UserColumns.UNIT, dataObject.getString(UserColumns.UNIT));
                contentValues.put(UserColumns.ROLE, dataObject.getString(UserColumns.ROLE));

                contentProvider.insert(UserColumns.CONTENT_URI, contentValues);

                setCurrentUser(mStrUsername);
            }
            catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            catch (JSONException e) {
                e.printStackTrace();

                CommonUtils.createErrorAlertDialog(LoginActivity.this, mstrGetUserFail).show();
            }
        }
    };

    private void doSetUrl() {
        CommonUtils.showSetUrlDialog(this);
    }
}
