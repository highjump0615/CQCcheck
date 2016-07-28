package com.highjump.cqccollect;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by highjump on 15-2-7.
 */
public class DetailDialogActivity extends Activity implements View.OnClickListener {

    public Button mButCancel;
    public Button mButDelete;
    public Button mButSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void initControls() {
        mButCancel = (Button)findViewById(R.id.but_cancel);
        mButCancel.setOnClickListener(this);

        mButDelete = (Button)findViewById(R.id.but_delete);
        mButDelete.setOnClickListener(this);

        mButSave = (Button)findViewById(R.id.but_save);
        mButSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.but_cancel:
                finish();
                break;
        }
    }
}
