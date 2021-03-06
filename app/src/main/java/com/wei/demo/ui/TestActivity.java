package com.wei.demo.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.wei.demo.R;
import com.wei.demo.adapter.MyRecyclerAdapter;
import com.wei.demo.recycleview.StickySectionDecoration;
import com.wei.demo.view.RefreshLayout;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ${wei} on 2017/8/11.
 */

public class TestActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        int test = getIntent().getIntExtra("test", -1);
        Log.e(TAG, "30行...onCreate: test = " + test);

        final RefreshLayout refreshLayout = (RefreshLayout) findViewById(R.id.refreshlayout);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        final ArrayList<String> list = getData();
        recyclerView.setAdapter(new MyRecyclerAdapter(list));

        recyclerView.addItemDecoration(new StickySectionDecoration(this, new StickySectionDecoration.Decorationcallback() {
            @Override
            public long getGroupId(int position) {
                return list.get(position).charAt(0);
            }

            @Override
            public String getGroupFirstLine(int position) {
                return list.get(position).substring(0, 1).toUpperCase();
            }
        }));

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Observable.timer(2000, TimeUnit.MILLISECONDS)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<Long>() {
                            @Override
                            public void accept(Long aLong) throws Exception {
                                refreshLayout.setRefreshing(false);
                                android.util.Log.e(TAG, "timer - accept:  = ");
                            }
                        });
            }
        });

    }

    private static final String TAG = "debug_TestActivity";
    int i = 97;

    private ArrayList<String> getData() {
        ArrayList<String> list = new ArrayList<>();
        for (int j = 0; j < 26; j++) {
            list.add(String.format("%s%s", (char) i, (char) i));
            list.add(String.format("%s%s", (char) i, ((char) (i + 1))));
            i++;
        }
        return list;
    }

    @Override
    public void onClick(View v) {

    }
}
