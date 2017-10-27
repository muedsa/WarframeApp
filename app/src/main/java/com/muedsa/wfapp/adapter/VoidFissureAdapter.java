package com.muedsa.wfapp.adapter;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.muedsa.wfapp.R;
import com.muedsa.wfapp.activity.MainActivity;
import com.muedsa.wfapp.fragment.VoidFissureFragment;
import com.muedsa.wfapp.language.Translation;
import com.muedsa.wfapp.model.VoidFissure;
import com.muedsa.wfapp.worker.AlertWorker;
import com.muedsa.wfapp.worker.VoidFissureWorker;

import java.util.ArrayList;
import java.util.Date;

public class VoidFissureAdapter extends RecyclerView.Adapter<VoidFissureAdapter.VoidFissureViewHolder> {

    private ArrayList<VoidFissure> voidFissures;
    private VoidFissureFragment voidFissureFragment;
    private Handler workerHandler;
    private Handler timerHandler;
    private Translation translation;
    private VoidFissureWorker voidFissureWorker;

    public VoidFissureAdapter(VoidFissureFragment voidFissureFragment){
        this.voidFissures = new ArrayList<>();
        this.voidFissureFragment = voidFissureFragment;
        this.workerHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Bundle bundle = msg.getData();
                VoidFissureAdapter.this.voidFissures = bundle.getParcelableArrayList("voidFissures");
                VoidFissureAdapter.this.notifyDataSetChanged();
            }
        };
        this.timerHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(VoidFissureAdapter.this.voidFissureFragment.getActivity() != null && ((MainActivity)VoidFissureAdapter.this.voidFissureFragment.getActivity()).threadLock == 1){
                    VoidFissureAdapter.this.notifyDataSetChanged();
                }
            }
        };
        this.translation = Translation.getInstance(this.voidFissureFragment.getActivity(), "zh_cn");
        this.voidFissureWorker = new VoidFissureWorker(this.workerHandler);
        this.voidFissureWorker.run();
        new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                while (true){
                    try {
                        Thread.sleep(1000/7);
                        VoidFissureAdapter.this.timerHandler.sendMessage(new Message());
                        i++;
                        if(i > 60*1000/(1000/7)){
                            VoidFissureAdapter.this.voidFissureWorker.run();
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
    public VoidFissureAdapter.VoidFissureViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_void_fissure, parent, false);
        VoidFissureAdapter.VoidFissureViewHolder holder = new VoidFissureAdapter.VoidFissureViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(VoidFissureViewHolder holder, int position) {
        VoidFissure voidFissure = this.voidFissures.get(position);
        String planet = voidFissure.getPlanet();
        planet = this.translation.getPlanet(planet);
        String modifier = voidFissure.getModifier();
        modifier = this.translation.getModifier(modifier);
        String mission = voidFissure.getMission();
        mission = this.translation.getMission(mission);
        Integer startTime = Integer.parseInt(voidFissure.getStartTime());
        Integer endTime = Integer.parseInt(voidFissure.getEndTime());
        Integer curTime = new Long(new Date().getTime()/1000).intValue();
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
        holder.tv1.setText(modifier);
        holder.tv2.setText(mission);
        holder.tv3.setText(voidFissure.getPlace() + "(" + planet + ")");
        holder.tv4.setText(text);
        holder.progressBar.setMax(max);
        holder.progressBar.setProgress(remain);
    }

    @Override
    public int getItemCount() {
        int count = 0;
        if(this.voidFissures != null){
            count = this.voidFissures.size();
        }
        return count;
    }

    class VoidFissureViewHolder extends RecyclerView.ViewHolder
    {
        TextView tv1;
        TextView tv2;
        TextView tv3;
        TextView tv4;
        ProgressBar progressBar;

        public VoidFissureViewHolder(View view)
        {
            super(view);
            tv1 = (TextView) view.findViewById(R.id.textView1_void_fissure);
            tv2 = (TextView) view.findViewById(R.id.textView2_void_fissure);
            tv3 = (TextView) view.findViewById(R.id.textView3_void_fissure);
            tv4 = (TextView) view.findViewById(R.id.textView4_void_fissure);
            progressBar = (ProgressBar) view.findViewById(R.id.progressBar_void_fissure);
        }


    }
}
