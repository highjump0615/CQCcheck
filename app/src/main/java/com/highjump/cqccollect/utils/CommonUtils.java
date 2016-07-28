package com.highjump.cqccollect.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;

import com.highjump.cqccollect.R;
import com.highjump.cqccollect.api.API_Manager;
import com.highjump.cqccollect.model.LocationData;
import com.highjump.cqccollect.model.UserData;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by highjump on 15-1-30.
 */
public class CommonUtils {

    private static final String TAG = CommonUtils.class.getSimpleName();

    public static final int MEDIA_TYPE_IMAGE = 0;
    public static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;

    public static TencentGPSTracker mTencentGPSTracker = null;

    /* Common Data */
    public static LocationData mLocationSelected;

    /**
     * Create error AlertDialog.
     */
    public static Dialog createErrorAlertDialog(final Context context, String message) {
        return createErrorAlertDialog(context, "", message);
    }

    /**
     * Create error AlertDialog.
     */
    public static Dialog createErrorAlertDialog(final Context context, String title, String message) {
        return new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null).create();
    }

    public static String getMD5EncryptedString(String encTarget) {
        return getMD5EncryptedString(encTarget.getBytes(), encTarget.length());
    }

    public static String getMD5EncryptedString(byte[] data, int length) {
        MessageDigest mdEnc;
        try {
            mdEnc = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Exception while encrypting to md5");
            e.printStackTrace();
            return null;
        }

        // Encryption algorithm
        mdEnc.update(data, 0, length);
        String md5 = new BigInteger(1, mdEnc.digest()).toString(16);
        while (md5.length() < 32) {
            md5 = "0" + md5;
        }
        return md5;
    }

    /**
     * Move to destination activity class with animate transition.
     */
    public static void moveNextActivity(Activity source, Class<?> destinationClass, boolean removeSource)
    {
        Intent intent = new Intent(source, destinationClass);

        if (removeSource)
        {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        }

        source.startActivity(intent);

        if (removeSource)
        {
            source.finish();
        }

//        source.overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
    }

    /*
     * returning photoImage / video
     */
    public static File getOutputMediaFile(Context context, boolean isImageType) {
        File mediaStorageDir = getMyApplicationDirectory(context, Config.IMAGE_DIRECTORY_NAME);

        if (mediaStorageDir == null) return null;

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(new Date());
        File mediaFile;

        if (isImageType) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator /*+ "IMG_"*/ + timeStamp + ".jpg");
        } else {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator /*+ "VID_"*/ + timeStamp + ".3gp");
        }

        return mediaFile;
    }

    /**
     * Get path of images to be saved
     */
    public static File getMyApplicationDirectory(Context context, String directoryName) {
        String appName = context.getString(R.string.app_name);

        // External sdcard location
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), appName);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "Oops! Failed create " + appName + " directory");
                return null;
            }
        }

        mediaStorageDir = new File(Environment.getExternalStorageDirectory() + File.separator + appName, directoryName);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "Oops! Failed create " + directoryName + " directory");
                return null;
            }
        }

        return mediaStorageDir;
    }

    /**
     * Get bitmap from internal image file.
     */
    public static Bitmap getBitmapFromUri(Uri fileUri, int sampleSize, float imgSize) {
//        // bitmap factory
//        BitmapFactory.Options options = new BitmapFactory.Options();
//
//        // downsizing photoImage as it throws OutOfMemory Exception for larger
//        // images
//        options.inSampleSize = sampleSize;
//        options.inMutable = true;
//
//        return BitmapFactory.decodeFile(fileUri.getPath(), options);

        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(), newOpts);// 此时返回bm为空

        if (bitmap != null)
            bitmap.recycle();

        newOpts.inJustDecodeBounds = false;
        newOpts.inPurgeable = true;
        newOpts.inInputShareable = true;

        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = imgSize;// 这里设置高度为800f
        float ww = imgSize;// 这里设置宽度为480f
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w >= h && w > ww)
        {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        }
        else if (w < h && h > hh)
        {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(fileUri.getPath(), newOpts);

        return bitmap;
    }

    public static byte[] compressBitmap(Bitmap origin, int nTargetSize) {
        int nRatio = 90;

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        origin.compress(Bitmap.CompressFormat.JPEG, 100, stream);

        int nSize = stream.toByteArray().length;

        if (nTargetSize > 0) {
            while (nSize > nTargetSize) {
                stream.reset();
                origin.compress(Bitmap.CompressFormat.JPEG, nRatio, stream);
                nRatio -= 10;

                nSize = stream.toByteArray().length;
            }
        }

        return stream.toByteArray();
    }

    /**
     * @param date
     * @return
     */
    public static String getFormattedDateString(Date date, String format) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    /**
     * Hide always Soft Keyboard
     *
     * @param context is current Activity
     */
    public static void hideKeyboard(Context context, EditText editText) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (editText != null) {
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
            editText.clearFocus();
            //editText.setInputType(0);
        }
    }

    public static String convertChinaFormatTime(String date) {
        if (TextUtils.isEmpty(date)) return "";
        String[] strContent = date.split("-");

        if (strContent.length == 3) {
            return String.format("%s年%s月%s日", strContent[0], strContent[1], strContent[2]);
        }

        return "";
    }

    public static Date getDateFromString(String dateString) {
        Date date = new Date();

        if (!TextUtils.isEmpty(dateString)) {
            try {
                date = SimpleDateFormat.getDateInstance(SimpleDateFormat.LONG, Locale.CHINA).parse(
                        convertChinaFormatTime(dateString));
            } catch (ParseException e) {
                if (Config.DEBUG) e.printStackTrace();
            }
        }

        return date;
    }

    private static void showDatePickerDialog(final Context context, final EditText editText) {
        CommonUtils.hideKeyboard(context, editText);
        final String originalText = editText.getText().toString();

        Calendar currentTime = Calendar.getInstance();
        currentTime.setTime(getDateFromString(originalText));

        int year = currentTime.get(Calendar.YEAR);
        int month = currentTime.get(Calendar.MONTH);
        int day = currentTime.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                context,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        if("清空".equals(editText.getText().toString())) {
                            editText.setText("");
                        } else {
                            editText.setText(CommonUtils.getFormattedDateString(new GregorianCalendar(year, monthOfYear, dayOfMonth).getTime(), "yyyy-MM-dd"));
                        }
                    }
                }, year, month, day);
        datePickerDialog.setTitle("请选择日期");
        datePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                editText.setText(originalText);
            }
        });
        datePickerDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "清空", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
                    editText.setText("");
                else
                    editText.setText("清空");
            }
        });
        datePickerDialog.show();
    }

    /**
     * Make new OnTouchListener to show DatePickerDialog
     */
    public static View.OnTouchListener getOnTouchListenerForDatePicker(final Context context, final EditText editText) {
        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    showDatePickerDialog(context, editText);
                    return true;
                }

                return false;
            }
        };
    }

    /**
     * Make new OnTouchListener to show DatePickerDialog
     */
    public static View.OnFocusChangeListener getOnFocusChangeListenerForDatePicker(final Context context, final EditText editText) {
        return new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDatePickerDialog(context, editText);
                }
            }
        };
    }

    public static void showSetUrlDialog(final Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setTitle(activity.getString(R.string.menu_setparam));

        // Get the layout inflater
        LayoutInflater inflater = activity.getLayoutInflater();
        View viewDialog = inflater.inflate(R.layout.layout_dialog_setparam, null);

        final EditText editUrl = (EditText)viewDialog.findViewById(R.id.edit_url);
        editUrl.setText(API_Manager.SERVER_PATH);

        final EditText editSecCode = (EditText)viewDialog.findViewById(R.id.edit_sec_code);
        editSecCode.setText(Config.PASSWORD_SECURITY);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(viewDialog)
                // Add action buttons
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        setCqcParam(editUrl.getText().toString(), editSecCode.getText().toString());

                        // Save user phone and flag of signed into NSUserDefaults
                        SharedPreferences preferences = activity.getSharedPreferences(Config.PREF_NAME, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString(Config.PREF_URL, API_Manager.SERVER_PATH);
                        editor.putString(Config.PREF_SECCODE, Config.PASSWORD_SECURITY);
                        editor.commit();

                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });

        builder.show();
    }

    public static void setCqcParam(String url, String seccode) {
        API_Manager.SERVER_PATH = url;
        API_Manager.setUrl();

        Config.PASSWORD_SECURITY = seccode;
    }

    public static void showExitDialog(final Activity activity) {
        Dialog alertBuilder = new AlertDialog.Builder(activity)
                .setTitle("")
                .setMessage("确定要退出吗？")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        activity.finish();
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .create();

        alertBuilder.show();
    }
}
