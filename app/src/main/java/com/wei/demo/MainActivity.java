package com.wei.demo;

import android.content.Intent;
import android.os.Bundle;
import android.os.MemoryFile;
import android.os.PersistableBundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.wei.demo.adapter.MyListAdapter;
import com.wei.demo.adapter.MyViewPagerAdapter;
import com.wei.demo.anonation.ContentView;
import com.wei.demo.anonation.InJect;
import com.wei.demo.basepage.BasePager;
import com.wei.demo.basepage.ColumViewPager;
import com.wei.demo.basepage.CurvesPager;
import com.wei.demo.basepage.NewsPager;
import com.wei.demo.basepage.TestPager;
import com.wei.demo.view.MyViewPager;

import java.util.ArrayList;
@ContentView(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private MyViewPager viewpager;
    private TabLayout tablayout;
    private ArrayList<BasePager> pagerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InJect.inject(this);
//        setContentView(R.layout.activity_main);
        initView();
        setActionBar();
    }

    private void initView() {

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        viewpager = (MyViewPager) findViewById(R.id.viewpager);
        tablayout = (TabLayout) findViewById(R.id.tab_layout);
//设置背景为透明色
//        drawerLayout.setScrimColor(Color.TRANSPARENT);

        pagerList = new ArrayList<>();
        pagerList.add(new ColumViewPager(this));
        pagerList.add(new CurvesPager(this));
        pagerList.add(new NewsPager(this));
        pagerList.add(new TestPager(this));
        viewpager.setAdapter(new MyViewPagerAdapter(getApplicationContext(), pagerList));
        tablayout.setupWithViewPager(viewpager);
        initTab();

        //左侧菜单
        ListView left_listview = (ListView) findViewById(R.id.left_listview);
        left_listview.setAdapter(new MyListAdapter());

        viewpager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                pagerList.get(position).initData();
            }
        });

        left_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                drawerLayout.closeDrawers();
                startActivity(new Intent(getApplicationContext(),SencondActivity.class));
            }
        });
    }

    private void initTab() {
        tablayout.removeAllTabs();
        tablayout.addTab(tablayout.newTab().setText("柱状"));
        tablayout.addTab(tablayout.newTab().setText("分时"));
        tablayout.addTab(tablayout.newTab().setText("杂货铺"));
        tablayout.addTab(tablayout.newTab().setText("刷新"));
    }

    private void setActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getString(R.string.app_name));
        actionBar.setDisplayHomeAsUpEnabled(true);//home键可点击
        actionBar.setDisplayShowHomeEnabled(true);//显示home键
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, 0, 0);
        drawerToggle.syncState();//同步actionbar和drawerlayout同步
        drawerLayout.setDrawerListener(drawerToggle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            drawerToggle.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }
}
