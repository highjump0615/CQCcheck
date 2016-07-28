package com.highjump.cqccollect;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.highjump.cqccollect.utils.CommonUtils;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * Created by highjump on 15-4-5.
 */
public class SignActivity extends Activity implements View.OnTouchListener, View.OnClickListener {

    public final static int TYPE_CLIENT = 0;
    public final static int TYPE_INSPECTOR = 1;

    public final static String SIGN_TYPE = "sign_type";
    public final static String SIGN_IMG_PATH = "sign_img_path";

    private int mnType;
    private String mstrImgPath;

    private ImageView mImgView;
    private Bitmap mBaseBitmap;
    private Canvas mCanvas;
    private Paint mPaint;

    int mnStartX = 0;
    int mnStartY = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        Intent intent = getIntent();
        if (intent.hasExtra(SIGN_TYPE)) {
            mnType = intent.getIntExtra(SIGN_TYPE, 0);
        }
        if (intent.hasExtra(SIGN_IMG_PATH)) {
            mstrImgPath = intent.getStringExtra(SIGN_IMG_PATH);
        }

        mImgView = (ImageView) this.findViewById(R.id.imgview_sign);

        Button button = (Button)this.findViewById(R.id.but_delete);
        button.setOnClickListener(this);

        button = (Button)this.findViewById(R.id.but_ok);
        button.setOnClickListener(this);

        TextView textView = (TextView)this.findViewById(R.id.text_title);
        if (mnType == TYPE_CLIENT) {
            textView.setText("客户签名");
        }
        else if (mnType == TYPE_INSPECTOR) {
            textView.setText("审核人签名");
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (mBaseBitmap != null) {
            return;
        }

        int nWidth = mImgView.getWidth();
        int nHeight = mImgView.getHeight();

        if (TextUtils.isEmpty(mstrImgPath)) {
            // 创建一张空白图片
            mBaseBitmap = Bitmap.createBitmap(nWidth, nHeight, Bitmap.Config.ARGB_8888);
        }
        else {
            Bitmap bitmap = CommonUtils.getBitmapFromUri(Uri.parse(mstrImgPath), 1, nWidth);
            mBaseBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        }

        // 创建一张画布
        mCanvas = new Canvas(mBaseBitmap);

        if (TextUtils.isEmpty(mstrImgPath)) {
            // 画布背景为灰色
            mCanvas.drawColor(Color.WHITE);
        }

        // 创建画笔
        mPaint = new Paint();
        // 画笔颜色为红色
        mPaint.setColor(Color.BLACK);
        // 宽度5个像素
        mPaint.setStrokeWidth(5);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);                    // set the dither to true
        mPaint.setStyle(Paint.Style.STROKE);       // set to STOKE
        mPaint.setStrokeJoin(Paint.Join.ROUND);    // set the join to round you want
        mPaint.setStrokeCap(Paint.Cap.ROUND);      // set the paint cap to round too
        // 先将灰色背景画上
        mCanvas.drawBitmap(mBaseBitmap, new Matrix(), mPaint);

        mImgView.setImageBitmap(mBaseBitmap);



        mImgView.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 获取手按下时的坐标
                mnStartX = (int) event.getX();
                mnStartY = (int) event.getY();
                break;

            case MotionEvent.ACTION_MOVE:
                // 获取手移动后的坐标
                int stopX = (int) event.getX();
                int stopY = (int) event.getY();
                // 在开始和结束坐标间画一条线
                mCanvas.drawLine(mnStartX, mnStartY, stopX, stopY, mPaint);
                // 实时更新开始坐标
                mnStartX = (int) event.getX();
                mnStartY = (int) event.getY();
                mImgView.setImageBitmap(mBaseBitmap);

                break;
        }

        return true;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.but_delete:
                mCanvas.drawColor(Color.WHITE);
                mImgView.setImageBitmap(mBaseBitmap);
                break;

            case R.id.but_ok:
                saveToFile();

                break;
        }
    }

    public void saveToFile() {
        try {
            File file;
            if (TextUtils.isEmpty(mstrImgPath)) {
                file = CommonUtils.getOutputMediaFile(this, true);
            }
            else {
                file = new File(mstrImgPath);
            }

            OutputStream stream = new FileOutputStream(file);
            mBaseBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            stream.close();

            // 模拟一个广播，通知系统sdcard被挂载
//            Intent intent = new Intent();
//            intent.setAction(Intent.ACTION_MEDIA_MOUNTED);
//            intent.setData(Uri.fromFile(Environment.getExternalStorageDirectory()));
//            sendBroadcast(intent);

            Intent intentResult = new Intent();
            intentResult.putExtra(SignActivity.SIGN_TYPE, mnType);
            intentResult.putExtra(SignActivity.SIGN_IMG_PATH, file.getPath());
            setResult(RESULT_OK, intentResult);
            finish();
        }
        catch (Exception e) {
            Toast.makeText(this, "保存图片失败", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
