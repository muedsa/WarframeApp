package com.muedsa.wfapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.CompoundButton;
import android.widget.Switch;
import com.muedsa.wfapp.R;
import com.muedsa.wfapp.adapter.SettingAdapter;
import com.muedsa.wfapp.service.AlertService;
import com.muedsa.wfapp.worker.ConfigWorker;

public class SettingActivity extends AppCompatActivity {

    private ConfigWorker configWorker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        this.configWorker = ConfigWorker.getInstance(this.getApplicationContext());
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView_setting);
        recyclerView.setAdapter(new SettingAdapter(this));
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        Switch s = (Switch) findViewById(R.id.switch_setting);
        s.setChecked(this.configWorker.isNotify());
        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Intent intent = new Intent();
                intent.setClass(SettingActivity.this, AlertService.class);
                if(isChecked){
                    configWorker.openNotfication();
                    startService(intent);
                }else {
                    configWorker.closeNotfication();
                    stopService(intent);
                }
            }
        });
    }
}