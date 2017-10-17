package com.muedsa.wfapp.worker;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.muedsa.wfapp.model.Alert;

import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class AlertWorker {
    public static final String WORKER_URL = "http://deathsnacks.com/wf/data/alerts_raw.txt";
    private Handler handler;

    Runnable network = new Runnable() {
        private BufferedInputStream bis;
        @Override
        public void run() {
            String results = "";
            try {
                URL url = new URL(AlertWorker.WORKER_URL);
                URLConnection urlConnection = url.openConnection();
                urlConnection.connect();
                bis = new BufferedInputStream(urlConnection.getInputStream());
                int length;
                byte[] bytes = new byte[1024];
                while ((length = bis.read(bytes, 0, bytes.length)) != -1) {
                    results += new String(bytes);
                }
                results = results.trim();
            }
            catch (Exception e){
                Log.e("WFA", e.getMessage());
            }
            finally {
                try {
                    if (this.bis != null) {
                        this.bis.close();
                    }
                } catch (Exception e2) {
                    Log.e("WFA", e2.getMessage());
                }
            }
            Message msg = new Message();
            Bundle bundle = new Bundle();
            ArrayList<Alert> alerts = AlertWorker.decode(results);
            bundle.putParcelableArrayList("alerts", alerts);
            msg.setData(bundle);
            handler.sendMessage(msg);
            Answers.getInstance().logCustom(new CustomEvent("Alert Worker Run Success"));
        }
    };

    public AlertWorker(Handler handler){
        this.handler = handler;
    }

    public static ArrayList<Alert> decode(String data){
        ArrayList<Alert> encodeData = new ArrayList<>();
        if (data != null){
            String[] alerts = data.split("\n");
            for(String str : alerts){
                String[] arr = str.split("\\|");
                if(arr.length > 9){
                    Alert alert = new Alert();
                    alert.setId(arr[0]);
                    alert.setPlace(arr[1]);
                    alert.setPlanet(arr[2]);
                    alert.setMission(arr[3]);
                    alert.setFaction(arr[4]);
                    alert.setLevel(arr[5]+"-"+arr[6]);
                    alert.setStartTime(arr[7]);
                    alert.setEndTime(arr[8]);
                    alert.setAwards(arr[9]);
                    if(arr.length > 10){
                        alert.setArchwing("Archwing");
                    }else{
                        alert.setArchwing("NoArchwing");
                    }
                    encodeData.add(alert);
                }
            }
        }
        return encodeData;
    }

    public void run(){
        new Thread(network).start();
    }
}