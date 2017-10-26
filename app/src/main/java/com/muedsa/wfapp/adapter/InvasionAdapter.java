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
import com.muedsa.wfapp.fragment.InvasionFragment;
import com.muedsa.wfapp.language.Translation;
import com.muedsa.wfapp.model.Invasion;
import com.muedsa.wfapp.worker.InvasionWorker;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class InvasionAdapter extends RecyclerView.Adapter<InvasionAdapter.InvasionViewHolder>{

    private InvasionFragment invasionFragment;
    private ArrayList<Invasion> invasions;
    private Handler workHandler;
    private InvasionWorker invasionWorker;
    private Translation translation;


    public InvasionAdapter(InvasionFragment invasionFragment){
        this.invasions = new ArrayList<>();
        this.invasionFragment = invasionFragment;
        this.workHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Bundle bundle = msg.getData();
                InvasionAdapter.this.invasions = bundle.getParcelableArrayList("invasions");
                InvasionAdapter.this.notifyDataSetChanged();
            }
        };
        this.translation = Translation.getInstance(this.invasionFragment.getActivity(), "zh_cn");
        this.invasionWorker = new InvasionWorker(this.workHandler);
        this.invasionWorker.run();
    }


    @Override
    public InvasionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_invasion, parent, false);
        InvasionAdapter.InvasionViewHolder holder = new InvasionAdapter.InvasionViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(InvasionViewHolder holder, int position) {
        Invasion invasion = this.invasions.get(position);
        String place = invasion.getPlace();
        String planet = invasion.getPlanet();
        planet = this.translation.getPlanet(planet);
        String factionA = invasion.getFactionA();
        factionA = this.translation.getFaction(factionA);
        String factionB = invasion.getFactionB();
        factionB = this.translation.getFaction(factionB);
        String type = invasion.getType();
        type = this.translation.getMission(type);
        String percentage = invasion.getPercentage();
        int index = (int)Float.parseFloat(percentage);
        String awardsA = invasion.getAwardsA();
        String[][] t_awardsA = this.translation.getAwards(awardsA);
        awardsA = "";
        String moreWorthA  = t_awardsA[t_awardsA.length-1][2];
        for(int i=0; i<t_awardsA.length;i++){
            awardsA += t_awardsA[i][0] + " " + t_awardsA[i][1] + "\n";
        }
        moreWorthA = moreWorthA.toLowerCase().replaceAll(" ", "_");
        String awardsB = invasion.getAwardsB();
        String[][] t_awardsB = this.translation.getAwards(awardsB);
        awardsB = "";
        String moreWorthB  = t_awardsB[t_awardsB.length-1][2];
        for(int i=0; i<t_awardsB.length;i++){
            awardsB += t_awardsB[i][0] + " " + t_awardsB[i][1] + "\n";
        }
        moreWorthB = moreWorthB.toLowerCase().replaceAll(" ", "_");
        Bitmap bitmapA = null;
        Bitmap bitmapB = null;
        try {
            InputStream inputStreamA = this.invasionFragment.getActivity().getAssets().open("image/" + moreWorthA + ".png");
            bitmapA = BitmapFactory.decodeStream(inputStreamA);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            InputStream inputStreamB = this.invasionFragment.getActivity().getAssets().open("image/" + moreWorthB + ".png");
            bitmapB = BitmapFactory.decodeStream(inputStreamB);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(bitmapA != null){
            holder.imgView1.setImageBitmap(bitmapA);

        }
        if(bitmapB != null){
            holder.imgView2.setImageBitmap(bitmapB);
        }
        holder.tv1.setText(factionA);
        holder.tv2.setText(factionB);
        holder.tv3.setText(awardsA);
        holder.tv4.setText(awardsB);
        holder.tv5.setText(type + " - " + place + "(" + planet + ")");
        holder.tv6.setText(percentage+"%");
        holder.progressBar.setProgress(index);
    }

    @Override
    public int getItemCount() {
        int count = 0;
        if(this.invasions != null){
            count = this.invasions.size();
        }
        return count;
    }

    class InvasionViewHolder extends RecyclerView.ViewHolder
    {
        TextView tv1;
        TextView tv2;
        TextView tv3;
        TextView tv4;
        TextView tv5;
        TextView tv6;
        ImageView imgView1;
        ImageView imgView2;
        ProgressBar progressBar;

        public InvasionViewHolder(View view)
        {
            super(view);
            tv1 = (TextView) view.findViewById(R.id.textView1_invasion);
            tv2 = (TextView) view.findViewById(R.id.textView2_invasion);
            tv3 = (TextView) view.findViewById(R.id.textView3_invasion);
            tv4 = (TextView) view.findViewById(R.id.textView4_invasion);
            tv5 = (TextView) view.findViewById(R.id.textView5_invasion);
            tv6 = (TextView) view.findViewById(R.id.textView6_invasion);
            imgView1 = (ImageView) view.findViewById(R.id.imageView1_invason);
            imgView2 = (ImageView) view.findViewById(R.id.imageView2_invason);
            progressBar = (ProgressBar) view.findViewById(R.id.progressBar_invasion);
        }
    }
}
