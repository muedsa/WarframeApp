package com.muedsa.wfapp.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.muedsa.wfapp.R;
import com.muedsa.wfapp.activity.MainActivity;
import com.muedsa.wfapp.fragment.AlertFragment;
import com.muedsa.wfapp.language.Translation;
import com.muedsa.wfapp.model.Alert;
import com.muedsa.wfapp.worker.AlertWorker;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;

public class AlertAdapter extends RecyclerView.Adapter<AlertAdapter.AlertViewHolder> {

    private AlertWorker alertWorker;
    private Handler workerHandler;
    private Handler timerHandler;
    private ArrayList<Alert> alerts;
    private AlertFragment alertFragment;
    private Translation translation;

    public AlertAdapter(AlertFragment alertFragment){
        this.alerts = new ArrayList<>();
        this.alertFragment = alertFragment;

        this.workerHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Bundle bundle = msg.getData();
                AlertAdapter.this.alerts = bundle.getParcelableArrayList("alerts");
                AlertAdapter.this.notifyDataSetChanged();
            }
        };
        this.timerHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(((MainActivity)AlertAdapter.this.alertFragment.getActivity()).threadLock == 1){
                    AlertAdapter.this.notifyDataSetChanged();
                }
            }
        };
        this.translation = Translation.getInstance(this.alertFragment.getActivity(), "zh_cn");
        this.alertWorker = new AlertWorker(workerHandler);
        this.alertWorker.run();
        new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                while (true){
                    try {
                        Thread.sleep(1000/7);
                        AlertAdapter.this.timerHandler.sendMessage(new Message());
                        i++;
                        if(i > 60*1000/(1000/7)){
                            AlertAdapter.this.alertWorker.run();
                            i = 0;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override
    public AlertViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_alert, parent, false);
        AlertViewHolder holder = new AlertViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(AlertViewHolder holder, int position) {
        Alert alert = alerts.get(position);
        String planet = alert.getPlanet();
        planet = this.translation.getPlanet(planet);
        String mission = alert.getMission();
        mission = this.translation.getMission(mission);
        String faction = alert.getFaction();
        faction = this.translation.getFaction(faction);
        String awards = alert.getAwards();
        String[][] t_awards = this.translation.getAwards(awards);
        awards = "";
        String moreWorth  = t_awards[t_awards.length-1][2];
        for(int i=0; i<t_awards.length;i++){
            awards += t_awards[i][0] + " " + t_awards[i][1] + "\n";
        }
        moreWorth = moreWorth.toLowerCase().replaceAll(" ", "_");
        Bitmap bitmap = null;
        try {
            InputStream inputStream = this.alertFragment.getActivity().getAssets().open("image/" + moreWorth + ".png");
            bitmap = BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(bitmap != null){
            holder.imgView.setImageBitmap(bitmap);
        }
        holder.tv1.setText(alert.getPlace() + "(" + planet +")");
        holder.tv2.setText(mission + "(" + faction +")");
        holder.tv3.setText("level:" + alert.getLevel() + ("Archwing".equals(alert.getArchwing())?"(Archwing)":""));
        holder.tv4.setText(awards);
        Integer startTime = Integer.parseInt(alert.getStartTime());
        Integer endTime = Integer.parseInt(alert.getEndTime());
        Integer curTime = new Long(new Date().getTime()/1000).intValue();
        //Integer index = curTime - startTime;
        Integer max = endTime - startTime;
        Integer remain = endTime - curTime;
        Integer hour = remain / (60 * 60);
        Integer minute = remain / 60;
        Integer second = remain % 60;
        String text = "";
        if(startTime > curTime){
            max = 3 * 60;
            remain = 3 * 60 - (startTime - curTime);
            text = "即将开始:" + (startTime - curTime) + "秒";
        }else if(curTime > endTime){
            text = "已结束";
        }else{
            if(hour > 0){
                text += hour + "小时";
                minute -= hour * 60;
            }
            if(minute > 0){
                text += minute + "分";
            }
            if(second > 0){
                text += second + "秒";
            }
        }
        holder.tv5.setText(text);
        holder.progressBar.setMax(max);
        holder.progressBar.setProgress(remain);
    }

    @Override
    public int getItemCount() {
        int count = 0;
        if(this.alerts != null){
            count = this.alerts.size();
        }
        return count;
    }

    class AlertViewHolder extends RecyclerView.ViewHolder
    {

        ImageView imgView;
        TextView tv1;
        TextView tv2;
        TextView tv3;
        TextView tv4;
        TextView tv5;
        ProgressBar progressBar;

        public AlertViewHolder(View view)
        {
            super(view);
            imgView = (ImageView) view.findViewById(R.id.imageView);
            tv1 = (TextView) view.findViewById(R.id.textView1);
            tv2 = (TextView) view.findViewById(R.id.textView2);
            tv3 = (TextView) view.findViewById(R.id.textView3);
            tv4 = (TextView) view.findViewById(R.id.textView4);
            tv5 = (TextView) view.findViewById(R.id.textView5);
            progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        }
    }
}
