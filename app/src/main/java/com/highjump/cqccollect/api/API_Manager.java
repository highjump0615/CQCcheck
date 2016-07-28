package com.highjump.cqccollect.api;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.highjump.cqccollect.utils.CommonUtils;
import com.highjump.cqccollect.utils.Config;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.HttpEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by highjump on 15-3-3.
 */
public class API_Manager {

    private static final String TAG = API_Manager.class.getSimpleName();

    public static String SERVER_PATH = "http://www.cqccms.com.cn/sasocheck/";
//    public static String SERVER_PATH = "http://192.168.1.107/cqc";
    public static String API_PATH;
    public static String API_FILE_PATH;
    public static String API_DOWNLOAD_PATH;

//    public static final String API_PATH = "http://192.168.1.115:8080/cqc/mobile_import.jsp";
//    public static final String API_FILE_PATH = "http://192.168.1.115:8080/cqc/mobile_import_image.jsp";

    public static final String IDENTIFY_ACTION = "identify";
    public static final String GETUSERINFO_ACTION = "get_user_info";
    public static final String SCHEDULED_TASK_ACTION = "scheduled_task";
    public static final String DOWNLOAD_REPORT_ACTION = "download_report";
    public static final String UPLOAD_REPORT_ACTION = "upload_report";
    public static final String GETBACK_REPORT_ACTION = "getback_report";
    public static final String UPLOAD_LOCATION_PHOTO_ACTION = "upload_location_photo";
    public static final String UPLOAD_COLLECT_IMAGE_ACTION = "upload_collect_image";
    public static final String UPLOAD_SIGNING_IMAGE_ACTION = "upload_signing_image";

    // WebAPI return objects.
    public static final String WEBAPI_RETURN_RESULT = "result";
    public static final String WEBAPI_RETURN_DATA = "data";

    private int mnTimeout = 60000;


    private static API_Manager mInstance = null;

    public static API_Manager getInstance() {
        if (mInstance == null) {
            mInstance = new API_Manager();
            setUrl();
        }

        return mInstance;
    }

    public static void setUrl() {
        String strPath = SERVER_PATH;

        if (!strPath.endsWith("/")) {
            strPath += "/";
        }

        API_PATH = strPath + "mobile_import.jsp";
        API_FILE_PATH = strPath + "mobile_import_image.jsp";
        API_DOWNLOAD_PATH = strPath + "download_attachment.jsp";
    }

    public void userIdentify(String user_name,
                             String user_password,
                             AsyncHttpResponseHandler responseHandler) {

        RequestParams params = new RequestParams();
        params.put("action", IDENTIFY_ACTION);
        params.put("username", user_name);

        JSONObject dataObject = new JSONObject();
        try {
            dataObject.put("username", user_name);
//            dataObject.put("password", user_password);

            String strMd5 = CommonUtils.getMD5EncryptedString(user_password + Config.PASSWORD_SECURITY);
            dataObject.put("md", strMd5);

            // location
            StringBuilder sb = new StringBuilder();
            sb.append(CommonUtils.mTencentGPSTracker.mLongitude)
                    .append(",")
                    .append(CommonUtils.mTencentGPSTracker.mLatitude);

            dataObject.put("location", sb.toString());

            String strDataEncoded = URLEncoder.encode(dataObject.toString(), "UTF-8");

            params.put("data", strDataEncoded);

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        sendToServiceByPost(API_PATH, params, responseHandler);
    }

    public void getUserInfo(String user_name,
                            AsyncHttpResponseHandler responseHandler) {

        RequestParams params = new RequestParams();
        params.put("action", GETUSERINFO_ACTION);
        params.put("username", user_name);

        JSONObject dataObject = new JSONObject();
        try {
            dataObject.put("username", user_name);
            String strDataEncoded = URLEncoder.encode(dataObject.toString(), "UTF-8");

            params.put("data", strDataEncoded);

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        sendToServiceByPost(API_PATH, params, responseHandler);
    }

    public void getScheduledTask(String user_name,
                                 AsyncHttpResponseHandler responseHandler) {

        RequestParams params = new RequestParams();
        params.put("action", SCHEDULED_TASK_ACTION);
        params.put("username", user_name);

        JSONObject dataObject = new JSONObject();
        try {
            dataObject.put("username", user_name);

            String strDataEncoded = URLEncoder.encode(dataObject.toString(), "UTF-8");

            params.put("data", strDataEncoded);

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        sendToServiceByPost(API_PATH, params, responseHandler);
    }

    public void downloadReport(String user_name,
                               String report_no,
                               String task_no,
                               AsyncHttpResponseHandler responseHandler) {

        RequestParams params = new RequestParams();
        params.put("action", DOWNLOAD_REPORT_ACTION);
        params.put("username", user_name);

        JSONObject dataObject = new JSONObject();
        try {
            dataObject.put("report_no", report_no);
            dataObject.put("task_no", task_no);

            String strDataEncoded = URLEncoder.encode(dataObject.toString(), "UTF-8");

            params.put("data", strDataEncoded);

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        sendToServiceByPost(API_PATH, params, responseHandler);
    }

    public void uploadReport(String user_name,
                             JSONObject report_obj,
                             AsyncHttpResponseHandler responseHandler) {

        RequestParams params = new RequestParams();
        params.put("action", UPLOAD_REPORT_ACTION);
        params.put("username", user_name);

        try {
            String strDataEncoded = URLEncoder.encode(report_obj.toString(), "UTF-8");

            params.put("data", strDataEncoded);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        sendToServiceByPost(API_PATH, params, responseHandler);
    }

    public void getbackReport(String user_name,
                              String report_no,
                              AsyncHttpResponseHandler responseHandler) {

        RequestParams params = new RequestParams();
        params.put("action", GETBACK_REPORT_ACTION);
        params.put("username", user_name);

        JSONObject dataObject = new JSONObject();
        try {
            dataObject.put("report_no", report_no);

            String strDataEncoded = URLEncoder.encode(dataObject.toString(), "UTF-8");

            params.put("data", strDataEncoded);

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        sendToServiceByPost(API_PATH, params, responseHandler);
    }

    private void compressImageFile(String file_path) {
        // do compress first
        Bitmap bitmap = BitmapFactory.decodeFile(file_path);// 此时返回bm为空

        try {
            FileOutputStream out = new FileOutputStream(file_path);
            byte[] bytesCompressBmp = CommonUtils.compressBitmap(bitmap, 1024 * 1024);
            out.write(bytesCompressBmp);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void uploadLocationPhoto(Context ctx,
                                    String user_name,
                                    String report_no,
                                    String file_path,
                                    AsyncHttpResponseHandler responseHandler) {

        compressImageFile(file_path);

        HttpEntity formEntity = null;

        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
        multipartEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

        Log.d(TAG, "upload filename = " + file_path);

        multipartEntityBuilder.addTextBody("action", UPLOAD_LOCATION_PHOTO_ACTION);
        multipartEntityBuilder.addTextBody("username", URLEncoder.encode(user_name));
        multipartEntityBuilder.addTextBody("report_no", URLEncoder.encode(report_no));

        multipartEntityBuilder.addBinaryBody("file", new File(file_path));

        formEntity = multipartEntityBuilder.build();


        AsyncHttpClient client = new AsyncHttpClient();

        client.setTimeout(mnTimeout);
        client.setResponseTimeout(mnTimeout);
        client.setConnectTimeout(mnTimeout);

        client.post(ctx, API_FILE_PATH, formEntity, "multipart/form-data", responseHandler);

//        RequestParams params = new RequestParams();
//        params.put("action", UPLOAD_LOCATION_PHOTO_ACTION);
//        params.put("username", user_name);
//        params.put("report_no", report_no);
//
////        if (file_path != null) {
////            try {
////                File fileImage = new File(file_path);
////                params.put("file", fileImage, "application/octet-stream");
////            } catch (FileNotFoundException e) {
////                e.printStackTrace();
////            }
////        }
//
//        sendToServiceByPost(API_PATH, params, responseHandler);
//
////        RequestParams params = new RequestParams();
////        params.put("action", SCHEDULED_TASK_ACTION);
////        params.put("username", user_name);
////
////        sendToServiceByPost(API_PATH, params, responseHandler);
    }

    public void uploadCollectionPhoto(Context ctx,
                                      String user_name,
                                      String report_no,
                                      String file_path,
                                      String image_type,
                                      String image_description,
                                      AsyncHttpResponseHandler responseHandler) {

        compressImageFile(file_path);

        HttpEntity formEntity = null;

        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
        multipartEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

        Log.d(TAG, "upload filename = " + file_path);

        multipartEntityBuilder.addTextBody("action", UPLOAD_COLLECT_IMAGE_ACTION);
        multipartEntityBuilder.addTextBody("username", URLEncoder.encode(user_name));
        multipartEntityBuilder.addTextBody("report_no", URLEncoder.encode(report_no));

        multipartEntityBuilder.addTextBody("image_type", URLEncoder.encode(image_type));
        multipartEntityBuilder.addTextBody("image_description", URLEncoder.encode(image_description));

        multipartEntityBuilder.addBinaryBody("file", new File(file_path));

        formEntity = multipartEntityBuilder.build();


        AsyncHttpClient client = new AsyncHttpClient();

        client.setTimeout(mnTimeout);
        client.setResponseTimeout(mnTimeout);
        client.setConnectTimeout(mnTimeout);

        client.post(ctx, API_FILE_PATH, formEntity, "multipart/form-data", responseHandler);
    }

    public void uploadSigningImage(Context ctx,
                                   String user_name,
                                   String report_no,
                                   int type,
                                   String file_path,
                                   AsyncHttpResponseHandler responseHandler) {

        HttpEntity formEntity = null;

        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
        multipartEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

        Log.d(TAG, "upload filename = " + file_path);

        multipartEntityBuilder.addTextBody("action", UPLOAD_SIGNING_IMAGE_ACTION);
        multipartEntityBuilder.addTextBody("username", URLEncoder.encode(user_name));
        multipartEntityBuilder.addTextBody("report_no", URLEncoder.encode(report_no));
        multipartEntityBuilder.addTextBody("type", URLEncoder.encode(Integer.toString(type)));

        multipartEntityBuilder.addBinaryBody("file", new File(file_path));

        formEntity = multipartEntityBuilder.build();


        AsyncHttpClient client = new AsyncHttpClient();

        client.setTimeout(mnTimeout);
        client.setResponseTimeout(mnTimeout);
        client.setConnectTimeout(mnTimeout);

        client.post(ctx, API_FILE_PATH, formEntity, "multipart/form-data", responseHandler);
    }


    private void sendToServiceByPost(String serviceAPIURL,
                                     RequestParams params,
                                     AsyncHttpResponseHandler responseHandler) {

        AsyncHttpClient client = new AsyncHttpClient();

        client.setTimeout(mnTimeout);
        client.setResponseTimeout(mnTimeout);
        client.setConnectTimeout(mnTimeout);

        client.post(serviceAPIURL, params, responseHandler);
    }
}
