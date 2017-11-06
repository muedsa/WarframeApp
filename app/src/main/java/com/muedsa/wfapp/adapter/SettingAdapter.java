package com.muedsa.wfapp.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import com.muedsa.wfapp.R;
import com.muedsa.wfapp.language.Translation;
import com.muedsa.wfapp.worker.ConfigWorker;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Iterator;

public class SettingAdapter extends RecyclerView.Adapter<SettingAdapter.SettingViewHolder>{

    private JSONObject awards;
    private ArrayList<String> keys;
    private View.OnClickListener onClickListener;
    private ConfigWorker configWorker;

    public SettingAdapter(Activity activity){
        this.awards = Translation.getInstance(activity.getApplicationContext(), "zh_cn").getAwards();
        this.configWorker = ConfigWorker.getInstance(activity.getApplicationContext());
        this.keys = new ArrayList<>();
        Iterator<String> keys = this.awards.keys();
        while (keys.hasNext()){
            this.keys.add(keys.next());
        }
        this.onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((CheckedTextView) v).toggle();
                if(((CheckedTextView) v).isChecked()){
                    configWorker.addNotficationItem((String)v.getTag());
                }else{
                    configWorker.removeNotficationItem((String)v.getTag());
                }
            }
        };
    }

    @Override
    public SettingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_setting, parent, false);
        SettingAdapter.SettingViewHolder holder = new SettingAdapter.SettingViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(SettingViewHolder holder, int position) {
        String key = this.keys.get(position);
        String value = key;
        try {
            value = this.awards.getString(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        holder.checkedTextView.setChecked(false);
        String[] items = this.configWorker.getNotficationItems();
        for (int i=0;i < items.length; i++){
            if (items[i].equals(key)){
                holder.checkedTextView.setChecked(true);
            }
        }
        holder.checkedTextView.setText(value + "(" + key + ")");
        holder.checkedTextView.setTag(key);
        holder.checkedTextView.setOnClickListener(this.onClickListener);
    }

    @Override
    public int getItemCount() {
        int count = 0;
        if(this.awards != null){
            count = this.awards.length();
        }
        return count;
    }

    class SettingViewHolder extends RecyclerView.ViewHolder {

        CheckedTextView checkedTextView;

        public SettingViewHolder(View view){
            super(view);
            this.checkedTextView = (CheckedTextView)view.findViewById(R.id.checkedTextView_settting);
        }
    }
}
