package com.wei.demo.ui;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.wei.demo.R;
import com.wei.demo.view.progress.CircleProgressView;
import com.yanzhenjie.album.Album;

import java.io.File;
import java.util.ArrayList;


/**
 * @author wei
 * @date 2017/3/12
 * <p>
 * 5.0新特性
 */
public class NewControlActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "debug_NewControl";
    private Toolbar toolbar;
    private CircleProgressView progressView;
    private ArrayList<String> mDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_control);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        progressView = (CircleProgressView) findViewById(R.id.progressview);
        setSupportActionBar(toolbar);

        initView();
    }

    private void initView() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        FloatingActionButton fab_left = (FloatingActionButton) findViewById(R.id.fab_left);
        fab.setOnClickListener(this);
        fab_left.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_left:
                if (mDatas != null && mDatas.size() > 0) {
                    String path = mDatas.get(0);
                    Log.e(TAG, "63行...onClick: path = " + path);
//                    Log.e(TAG, "64行...onClick:  = " + path.substring(path.lastIndexOf("/") + 1));
                    deleteBitmap(this, path);
                }
                break;
            case R.id.fab:
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        final float[] value = {0.01f};
                        while (value[0] < 1) {
                            try {
                                sleep(300L);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progressView.setData(value[0] += 0.02);
                                    Log.e(TAG, "54行...run: value = " + value[0]);
                                }
                            });
                        }
                    }
                }.start();

//                Album.album(this)
//                        .requestCode(1001)//请求码
//                        .camera(true)//是否显示相机
//                        .selectCount(4)//可选择数量
//                        .columnCount(4)
//                        .start();

                break;
        }
    }


    public void deleteBitmap(Context context, String path) {
        if (!TextUtils.isEmpty(path)) {
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(path))));//刷新图库

            ContentResolver resolver = context.getContentResolver();
            Uri contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            Cursor cursor = MediaStore.Images.Media.query(resolver, contentUri, new String[]{MediaStore.Images.Media._ID},
                    MediaStore.Images.Media.DATA + "=?", new String[]{path}, null);
            if (cursor.moveToFirst()) {
                String where = MediaStore.Images.Media.DATA + "='" + path + "'";
                int count = resolver.delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, where, null);
                Log.e(TAG, "211.deleteBitmap.count = " + count);
            }
            File file = new File(path);
            if (file.exists()) {
                boolean result = file.delete();
                Log.e(TAG, "215.deleteBitmap.result = " + result);
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            mDatas = Album.parseResult(data);
            for (int i = 0; i < mDatas.size(); i++) {
                Log.e(TAG, "92行...onActivityResult:  = " + mDatas.get(i));
            }
        }
    }
}
