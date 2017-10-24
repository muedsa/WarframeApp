package com.muedsa.wfapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.crashlytics.android.answers.Answers;
import com.muedsa.wfapp.R;
import com.muedsa.wfapp.adapter.TabAdapter;
import com.crashlytics.android.Crashlytics;
import com.muedsa.wfapp.service.AlertService;
import com.muedsa.wfapp.worker.ConfigWorker;
import com.muedsa.wfapp.worker.VersionWorker;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private Handler versionHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            int versionCode = bundle.getInt("versionCode");
            int currentVersion;
            try {
                currentVersion = MainActivity.this.getPackageManager().getPackageInfo(MainActivity.this.getPackageName(), 0).versionCode;
            } catch (PackageManager.NameNotFoundException e) {
                throw new RuntimeException("WTF!!!");
            }

            if(versionCode > 0 && versionCode > currentVersion){
                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("更新")
                        .setMessage("发现新版本，是否更新?")
                        .setNegativeButton("忽略更新", null)
                        .setPositiveButton("更新", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/MUedsa/WarframeApp/releases")));
                            }
                        })
                        .create();
                dialog.show();
            }
        }
    };
    private VersionWorker versionWorker;
    public int threadLock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Answers());
        Fabric.with(this, new Crashlytics());
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

        this.versionWorker = new VersionWorker(this.versionHandler);

    }

    @Override
    protected void onStart() {
        super.onStart();
        ConfigWorker configWorker = ConfigWorker.getInstance(getApplicationContext());
        Intent intent = new Intent();
        intent.setClass(this, AlertService.class);
        if(configWorker.isNotify()){
            startService(intent);
        }else{
            stopService(intent);
        }

        this.versionWorker.run();
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
            Intent intent = new Intent();
//            intent.setAction("android.intent.action.VIEW");
//            Uri content_url = Uri.parse("https://github.com/MUedsa/WarframeApp");
//            intent.setData(content_url);
            intent.setClass(MainActivity.this, SettingActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
