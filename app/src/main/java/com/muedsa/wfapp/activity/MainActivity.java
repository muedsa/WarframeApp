package com.muedsa.wfapp.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.crashlytics.android.answers.Answers;
import com.muedsa.wfapp.R;
import com.muedsa.wfapp.adapter.TabAdapter;
import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {

    private ViewPager mViewPager;

    public int threadLock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Answers());
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        threadLock = 1;

        TabAdapter tabAdapter = new TabAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                threadLock = 0;
            }

            @Override
            public void onPageSelected(int position) {
                threadLock = 1;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state){
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        threadLock = 0;
                        break;
                    case ViewPager.SCROLL_STATE_IDLE:
                        threadLock = 1;
                }
            }
        });
        mViewPager.setAdapter(tabAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
//            Snackbar.make(mViewPager , "Todo:任务通知设置", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show();
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri content_url = Uri.parse("https://github.com/MUedsa/WarframeApp");
            intent.setData(content_url);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
