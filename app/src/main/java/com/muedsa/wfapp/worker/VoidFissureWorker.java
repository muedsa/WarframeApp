package com.muedsa.wfapp.worker;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.muedsa.wfapp.model.Invasion;
import com.muedsa.wfapp.model.VoidFissure;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VoidFissureWorker {
    public static final String WORKER_URL = "https://deathsnacks.com/wf/data/activemissions.json";
    private Handler handler;

    Runnable network = new Runnable() {
        private BufferedInputStream bis;
        @Override
        public void run() {
            String results = "";
            try {
                URL url = new URL(VoidFissureWorker.WORKER_URL);
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
            ArrayList<VoidFissure> voidFissures = VoidFissureWorker.decode(results);
            bundle.putParcelableArrayList("voidFissures", voidFissures);
            msg.setData(bundle);
            handler.sendMessage(msg);
            Answers.getInstance().logCustom(new CustomEvent("VoidFissure Worker Run Success"));
        }
    };


    public VoidFissureWorker(Handler handler){
        this.handler = handler;
    }


    public static ArrayList<VoidFissure> decode(String data){
        ArrayList<VoidFissure> endata = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(data);
            for (int i=0;i <jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                VoidFissure voidFissure = new VoidFissure();
                voidFissure.setId(jsonObject.getJSONObject("_id").getString("id"));
                String noed = jsonObject.getString("Node");
                Pattern r = Pattern.compile("(.+)\\s\\((.+)\\)\\s\\((.+)\\)");
                Matcher m = r.matcher(noed);
                if(m.find()){
                    voidFissure.setPlace(m.group(1));
                    voidFissure.setPlanet(m.group(2));
                    voidFissure.setMission(m.group(3));
                }
                voidFissure.setModifier(jsonObject.getString("Modifier"));
                voidFissure.setStartTime(jsonObject.getJSONObject("Activation").getString("sec"));
                voidFissure.setEndTime(jsonObject.getJSONObject("Expiry").getString("sec"));
                endata.add(voidFissure);
            }
        }
        catch (Exception e){
            Log.d("WFA", e.getMessage());
        }
        return endata;
    }

    public void run(){
        Log.d("WFA", "VoidFissureWorkRun!");
        new Thread(network).start();
    }
}
