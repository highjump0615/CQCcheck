package com.highjump.cqccollect;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.highjump.cqccollect.model.LocationData;
import com.highjump.cqccollect.utils.CommonUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;


public class LocationActivity extends MenuActivity implements View.OnTouchListener {

    private static final String TAG = LocationActivity.class.getSimpleName();

    private EditText mEditVerifyDate;
    private EditText mEditVerifyPlace;

    private TextView mTxtLocation;
    private ImageView mImgViewPlace;
    private LocationData mLocationData;

    // variables to capture image
    private Uri mFileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        super.initView(getString(R.string.menu_location));

        mLocationData = CommonUtils.mLocationSelected;
        if (mLocationData == null) {
            mLocationData = new LocationData();
        }

        mTxtLocation = (TextView)findViewById(R.id.txt_location);
        mImgViewPlace = (ImageView)findViewById(R.id.imgview_place);

        mEditVerifyDate = (EditText)findViewById(R.id.edit_verify_date);
        mEditVerifyPlace = (EditText)findViewById(R.id.edit_verify_place);

        RelativeLayout layoutRoot = (RelativeLayout)findViewById(R.id.layout_root);
        layoutRoot.setOnTouchListener(this);

        Button button = (Button)findViewById(R.id.but_camera);
        button.setOnClickListener(this);

        button = (Button)findViewById(R.id.but_location);
        button.setOnClickListener(this);

        button = (Button)findViewById(R.id.but_save);
        button.setOnClickListener(this);

        showDateTime();
        showLocation();
    }

    private void showDateTime() {
        mEditVerifyDate.setText(mLocationData.mStrVerifyDate);
        mEditVerifyDate.setOnTouchListener(CommonUtils.getOnTouchListenerForDatePicker(this, mEditVerifyDate));
        mEditVerifyPlace.setText(mLocationData.mStrVerifyPlaceC);
    }

    private void showLocation() {

        String strLocation = "";
        if (mLocationData.mStrVerifyLocation != null) {
            strLocation = mLocationData.mStrVerifyLocation;
        }
        mTxtLocation.setText(strLocation);

        if (mLocationData.mStrPhoto != null) {
            mImgViewPlace.setImageBitmap(CommonUtils.getBitmapFromUri(Uri.parse(mLocationData.mStrPhoto), 1, 500));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (CommonUtils.mTencentGPSTracker!= null) {
            CommonUtils.mTencentGPSTracker.startLocation();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (CommonUtils.mTencentGPSTracker!= null) {
            CommonUtils.mTencentGPSTracker.stopLocation();
        }
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id) {
            case R.id.but_location:
                StringBuilder sb = new StringBuilder();
                sb.append(CommonUtils.mTencentGPSTracker.mLongitude)
                        .append(", ")
                        .append(CommonUtils.mTencentGPSTracker.mLatitude);

                mLocationData.mStrVerifyLocation = sb.toString();
                mLocationData.mStrLocateDate = CommonUtils.getFormattedDateString(new Date(), "yyyy-MM-dd HH:mm:ss");

                showLocation();

                break;

            case R.id.but_camera:
                onTakePhoto();
                break;

            case R.id.but_save:
                saveData();
                finish();
                break;

            default:
                super.onClick(v);
                break;
        }
    }

    private void saveData() {
        mLocationData.mStrVerifyDate = mEditVerifyDate.getText().toString();
        mLocationData.mStrVerifyPlaceC = mEditVerifyPlace.getText().toString();

        mLocationData.saveToDB();
    }

    @Override
    public void onBackPressed() {

//        Dialog alertBuilder = new AlertDialog.Builder(this)
//                .setTitle("")
//                .setMessage("是否将更改保存？")
//                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        saveData();
//                        finish();
//                    }
//                })
//                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        finish();
//                    }
//                })
//                .create();
//
//        alertBuilder.show();
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.e(TAG, "onActivityResult : requestCode : " + requestCode);

        switch (requestCode) {
            case CommonUtils.CAMERA_CAPTURE_IMAGE_REQUEST_CODE:
                if (resultCode == RESULT_OK) {

//                    //Check if data in not null and extract the Bitmap:
//                    if (data != null)
//                    {
//                        File destinationFile = CommonUtils.getOutputMediaFile(this, true);
//                        Log.d(TAG, "the destination for image file is: " + destinationFile );
//
//                        if (data.getExtras() != null)
//                        {
//                            Bundle bbb = data.getExtras();
//                            for(String key : bbb.keySet()){
//                                Object obj = bbb.get(key);   //later parse it as per your required type
//
//                                Log.d(TAG, "tag: " + key);
//                            }
//
//                            Bitmap bitmap = (Bitmap)data.getExtras().get("data");
//                            try
//                            {
//                                FileOutputStream out = new FileOutputStream(destinationFile);
//                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
//                                out.flush();
//                                out.close();
//
//                                mFileUri = Uri.fromFile(destinationFile);
//                                refreshImageViews();
//                            }
//                            catch (Exception e)
//                            {
//                                Log.e(TAG, "ERROR:" + e.toString());
//                            }
//                        }
//                    }
                    refreshImageViews();
                }
                else if (resultCode == RESULT_CANCELED) {
                    // User cancelled the photoImage capture
                    mFileUri = null;
                }
                else {
                    // Image capture failed, advise user
                    mFileUri = null;
                }
                break;
        }
    }

    private void onTakePhoto() {
        try {
            mFileUri = getOutputMediaFileUri(CommonUtils.MEDIA_TYPE_IMAGE);
            Log.e(TAG, "mFileUri = " + mFileUri.getPath());

            //create new Intent
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mFileUri);
            startActivityForResult(intent, CommonUtils.CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
        } catch (ActivityNotFoundException anfe) {
            CommonUtils.createErrorAlertDialog(this, "Alert", "Your device doesn't support capturing images!").show();
        }
    }

    /**
     * Display room photo in ImageViews
     */
    private void refreshImageViews() {

        if (mFileUri != null) {
            mLocationData.mStrPhoto = mFileUri.getPath();
            mLocationData.mStrPhotoDate = CommonUtils.getFormattedDateString(new Date(), "yyyy-MM-dd HH:mm:ss");
        }

        showLocation();
    }

    /**
     * Creating file uri to store photoImage/video
     */
    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(CommonUtils.getOutputMediaFile(this, type == CommonUtils.MEDIA_TYPE_IMAGE));
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        closeMenu();
        return false;
    }
}
