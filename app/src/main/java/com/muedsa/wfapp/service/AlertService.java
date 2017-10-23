package com.muedsa.wfapp.service;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.muedsa.wfapp.R;
import com.muedsa.wfapp.activity.MainActivity;
import com.muedsa.wfapp.language.Translation;
import com.muedsa.wfapp.model.Alert;
import com.muedsa.wfapp.worker.AlertWorker;
import com.muedsa.wfapp.worker.ConfigWorker;

import java.util.ArrayList;

public class AlertService extends IntentService {

    private Translation translation;
    private ConfigWorker configWorker;
    private AlertWorker alertWorker;
    private Handler alertHandler;

    private ArrayList<Alert> alerts;

    public AlertService(){
        super("AlertService");
        this.alerts = new ArrayList<>();

        this.alertHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Bundle data = msg.getData();
                ArrayList<Alert> results = data.getParcelableArrayList("alerts");
                for(Alert alert : results){
                    boolean isExist = true;
                    for(Alert temp : alerts){
                        if(alert.getId().equals(temp.getId())) {
                            isExist = false;
                        }
                    }
                    if(!isExist){
                        Integer startTime = Integer.parseInt(alert.getStartTime());
                        String planet = alert.getPlanet();
                        planet = translation.getPlanet(planet);
                        String mission = alert.getMission();
                        mission = translation.getMission(mission);
                        String faction = alert.getFaction();
                        faction = translation.getFaction(faction);
                        String awards = alert.getAwards();
                        String[][] t_awards = translation.getAwards(awards);
                        awards = "";
                        String moreWorth  = t_awards[t_awards.length-1][2];
                        for(int i=0; i<t_awards.length;i++){
                            awards += t_awards[i][0] + " " + t_awards[i][1] + "\n";
                        }
                        String[] items = configWorker.getNotficationItems();
                        isExist = false;
                        for (int i=0; i<items.length;i++){
                            if(items[i].equals(moreWorth)){
                                isExist = true;
                            }
                        }
                        if(isExist){
                            //moreWorth = moreWorth.toLowerCase().replaceAll(" ", "_");
                            moreWorth = t_awards[t_awards.length-1][1];
                            NotificationCompat.Builder mBuilder =
                                    new NotificationCompat.Builder(AlertService.this)
                                            .setSmallIcon(R.mipmap.ic_launcher)
                                            .setContentTitle(moreWorth)
                                            .setContentText(alert.getPlace()+ "(" + planet + ") " + mission + "(" + faction + ")");
                            Intent resultIntent = new Intent(AlertService.this, MainActivity.class);
                            TaskStackBuilder stackBuilder = TaskStackBuilder.create(AlertService.this);
                            stackBuilder.addParentStack(MainActivity.class);
                            stackBuilder.addNextIntent(resultIntent);
                            PendingIntent resultPendingIntent =
                                    stackBuilder.getPendingIntent(
                                            0,
                                            PendingIntent.FLAG_UPDATE_CURRENT
                                    );
                            mBuilder.setContentIntent(resultPendingIntent);
                            NotificationManager mNotificationManager =
                                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                            mNotificationManager.notify(startTime, mBuilder.build());
                        }
                    }
                }
                alerts = results;
            }
        };
        this.alertWorker = new AlertWorker(this.alertHandler);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        this.translation = Translation.getInstance(this, "zh_cn");
        this.configWorker = ConfigWorker.getInstance(this);
        while (true){
            try {
                Log.d("WFA", "AlertService");
                this.alertWorker.run();
                Thread.sleep(2 * 60 * 1000);
            }
            catch (Exception e){
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return Service.START_STICKY;
    }
}
