package com.highjump.cqccollect;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.highjump.cqccollect.model.BaseData;
import com.highjump.cqccollect.model.CollectImgData;
import com.highjump.cqccollect.utils.CommonUtils;

import java.util.ArrayList;


public class DialogPhotoActivity extends DetailDialogActivity {

    private static final String TAG = DialogPhotoActivity.class.getSimpleName();

    private ReportDetailActivity mDetailActivity;
    private ArrayList<CollectImgData> mArrayCollectImg;

    private ImageView mImgViewPhoto;
    private EditText mEditDesc;

    // variables to capture image
    private Uri mFileUri;
    private Uri mFileUriUsing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_photo);

        initControls();

        mDetailActivity = ReportDetailActivity.getInstance();

        // edit text
        mImgViewPhoto = (ImageView)findViewById(R.id.imgview_photo);
        mImgViewPhoto.setOnClickListener(this);
        mEditDesc = (EditText)findViewById(R.id.edit_desc);

        if (mDetailActivity.mstrCurPhotoType.contentEquals(CollectImgData.PHOTO_TYPE_PACK)) {
            mArrayCollectImg = mDetailActivity.mArrayPhotoPack;
        }
        else if (mDetailActivity.mstrCurPhotoType.contentEquals(CollectImgData.PHOTO_TYPE_PRODUCT)) {
            mArrayCollectImg = mDetailActivity.mArrayPhotoProduct;
        }
        else if (mDetailActivity.mstrCurPhotoType.contentEquals(CollectImgData.PHOTO_TYPE_STRUCT)) {
            mArrayCollectImg = mDetailActivity.mArrayPhotoStructure;
        }
        else if (mDetailActivity.mstrCurPhotoType.contentEquals(CollectImgData.PHOTO_TYPE_MARK)) {
            mArrayCollectImg = mDetailActivity.mArrayPhotoMark;
        }
        else if (mDetailActivity.mstrCurPhotoType.contentEquals(CollectImgData.PHOTO_TYPE_DEFECT)) {
            mArrayCollectImg = mDetailActivity.mArrayPhotoDefect;
        }

        showData();
    }

    private void showData() {
        if (mDetailActivity.mnCurCollectImg >= 0) {
            if (mArrayCollectImg == null) {
                return;
            }

            CollectImgData ciData = mArrayCollectImg.get(mDetailActivity.mnCurCollectImg);

            mFileUriUsing = Uri.parse(ciData.mStrImgAddress);
            mImgViewPhoto.setImageBitmap(CommonUtils.getBitmapFromUri(Uri.parse(ciData.mStrImgAddress), 1, 300));
            mEditDesc.setText(ciData.mStrDesc);
        }
        else {
            mButDelete.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        int id = v.getId();

        switch (id) {
            case R.id.imgview_photo:
                onTakePhoto();
                break;

            case R.id.but_save:
                saveData();
                finish();
                break;

            case R.id.but_delete:
                deleteData();
                finish();
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
            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
            startActivityForResult(intent, CommonUtils.CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
        } catch (ActivityNotFoundException anfe) {
            CommonUtils.createErrorAlertDialog(this, "Alert", "Your device doesn't support capturing images!").show();
        }
    }

    /**
     * Creating file uri to store photoImage/video
     */
    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(CommonUtils.getOutputMediaFile(this, type == CommonUtils.MEDIA_TYPE_IMAGE));
    }

    private void deleteData() {

        if (mDetailActivity.mnCurCollectImg < 0) {
            return;
        }

        CollectImgData collectImgData = mArrayCollectImg.get(mDetailActivity.mnCurCollectImg);
        collectImgData.mStatus += BaseData.DS_DELETED;
    }

    private void saveData() {

        CollectImgData collectImgData;

        if (mDetailActivity.mnCurCollectImg >= 0) {
            collectImgData = mArrayCollectImg.get(mDetailActivity.mnCurCollectImg);
        }
        else {
            collectImgData = new CollectImgData();
        }

        if (mFileUriUsing == null) {
            return;
        }

        collectImgData.mStrImgAddress = mFileUriUsing.getPath();
        collectImgData.mStrDesc = mEditDesc.getText().toString();
        collectImgData.mStrType = mDetailActivity.mstrCurPhotoType;
        collectImgData.mnUploaded = 0;

        if (mDetailActivity.mnCurCollectImg >= 0) {
            collectImgData.mStatus += BaseData.DS_UPDATED;
        }
        else {
            collectImgData.mStatus += BaseData.DS_ADDED;
            mArrayCollectImg.add(collectImgData);
        }
    }

    /**
     * Display room photo in ImageViews
     */
    private void refreshImageViews() {
        if (mFileUri != null) {
            mImgViewPhoto.setImageBitmap(CommonUtils.getBitmapFromUri(mFileUri, 1, 300));
        }

        mFileUriUsing = mFileUri;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.e(TAG, "onActivityResult : requestCode : " + requestCode);

        switch (requestCode) {
            case CommonUtils.CAMERA_CAPTURE_IMAGE_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    refreshImageViews();
                } else if (resultCode == RESULT_CANCELED) {
                    // User cancelled the photoImage capture
                    mFileUri = null;
                } else {
                    // Image capture failed, advise user
                    mFileUri = null;
                }
                break;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
