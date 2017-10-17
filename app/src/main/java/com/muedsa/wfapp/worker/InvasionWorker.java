package com.muedsa.wfapp.worker;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.muedsa.wfapp.model.Alert;
import com.muedsa.wfapp.model.Invasion;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class InvasionWorker {
    public static final String WORKER_URL = "https://deathsnacks.com/wf/data/invasion_raw.txt";
    private Handler handler;

    Runnable network = new Runnable() {
        private BufferedReader bufferedReader;
        @Override
        public void run() {
            String results = "";
            try {
                URL url = new URL(InvasionWorker.WORKER_URL);
                URLConnection urlConnection = url.openConnection();
                urlConnection.connect();
                urlConnection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String line;
                int i = 0;
                while ((line = bufferedReader.readLine())  != null){
                    if(i > 0){
                        results += line + "\n";
                    }
                    i++;
                }
                results = results.trim();
            }
            catch (Exception e){
                Log.e("WFA", e.getMessage());
            }
            finally {
                try {
                    if (this.bufferedReader != null) {
                        this.bufferedReader.close();
                    }
                } catch (Exception e2) {
                    Log.e("WFA", e2.getMessage());
                }
            }
            Message msg = new Message();
            Bundle bundle = new Bundle();
            ArrayList<Invasion> invasions = InvasionWorker.decode(results);
            bundle.putParcelableArrayList("invasions", invasions);
            msg.setData(bundle);
            handler.sendMessage(msg);
            Answers.getInstance().logCustom(new CustomEvent("Invasion Worker Run Success"));
        }
    };

    public InvasionWorker(Handler handler){
        this.handler = handler;
    }


    public static ArrayList<Invasion> decode(String data){
        ArrayList<Invasion> encodeData = new ArrayList<>();
        if (data != null && !"".equals(data)){
            String[] invasions = data.split("\n");
            for (String str : invasions){
                String[] arr = str.split("\\|");
                if(arr.length > 18){
                    Invasion invasion = new Invasion();
                    invasion.setId(arr[0]);
                    invasion.setPlace(arr[1]);
                    invasion.setPlanet(arr[2]);
                    invasion.setFactionA(arr[3]);
                    invasion.setAwardsA(arr[5]);
                    invasion.setFactionB(arr[8]);
                    invasion.setAwardsB(arr[10]);
                    invasion.setPercentage(arr[16]);
                    invasion.setType(arr[18]);
                    encodeData.add(invasion);
                }
            }
        }
        return  encodeData;
    }

    public void run(){
        Log.d("WFA", "InvasionWorkRun!");
        new Thread(network).start();
    }
}
