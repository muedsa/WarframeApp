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
import android.support.annotation.ArrayRes;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.muedsa.wfapp.R;
import com.muedsa.wfapp.activity.MainActivity;
import com.muedsa.wfapp.language.Translation;
import com.muedsa.wfapp.model.Alert;
import com.muedsa.wfapp.model.Invasion;
import com.muedsa.wfapp.worker.AlertWorker;
import com.muedsa.wfapp.worker.ConfigWorker;
import com.muedsa.wfapp.worker.InvasionWorker;

import java.util.ArrayList;

public class AlertService extends IntentService {

    private Translation translation;
    private ConfigWorker configWorker;
    private AlertWorker alertWorker;
    private Handler alertHandler;
    private ArrayList<Alert> alerts;
    private InvasionWorker invasionWorker;
    private Handler invasionHandler;
    private ArrayList<Invasion> invasions;

    public AlertService(){
        super("AlertService");
        this.alerts = new ArrayList<>();
        this.invasions  = new ArrayList<>();
        this.alertHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Bundle data = msg.getData();
                ArrayList<Alert> results = data.getParcelableArrayList("alerts");
                for(Alert alert : results){
                    boolean isExist = false;
                    for(Alert temp : alerts){
                        if(alert.getId().equals(temp.getId())) {
                            isExist = true;
                        }
                    }
                    if(!isExist){
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
                            mNotificationManager.notify(alert.getId().hashCode(), mBuilder.build());
                        }
                    }
                }
                alerts = results;
            }
        };
        this.alertWorker = new AlertWorker(this.alertHandler);

        this.invasionHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Bundle data = msg.getData();
                ArrayList<Invasion> results = data.getParcelableArrayList("invasions");
                for (Invasion invasion : results){
                    boolean isExist = false;
                    for(Invasion temp : invasions){
                        if(invasion.getId().equals(temp.getId())) {
                            isExist = true;
                        }
                    }
                    if(!isExist){
                        String awardsA = invasion.getAwardsA();
                        String[][] t_awardsA = AlertService.this.translation.getAwards(awardsA);
                        awardsA = "";
                        String moreWorthA  = t_awardsA[t_awardsA.length-1][2];
                        for(int i=0; i<t_awardsA.length;i++){
                            awardsA += t_awardsA[i][0] + " " + t_awardsA[i][1] + "\n";
                        }
                        String awardsB = invasion.getAwardsB();
                        String[][] t_awardsB = AlertService.this.translation.getAwards(awardsB);
                        awardsB = "";
                        String moreWorthB  = t_awardsB[t_awardsB.length-1][2];
                        for(int i=0; i<t_awardsB.length;i++){
                            awardsB += t_awardsB[i][0] + " " + t_awardsB[i][1] + "\n";
                        }
                        //moreWorthA = moreWorthA.toLowerCase().replaceAll(" ", "_");
                        //moreWorthB = moreWorthB.toLowerCase().replaceAll(" ", "_");
                        String[] items = configWorker.getNotficationItems();
                        boolean isExistA = false;
                        boolean isExistB = false;
                        for (int i=0; i<items.length;i++){
                            if(items[i].equals(moreWorthA)){
                                isExistA = true;
                            }
                            if(items[i].equals(moreWorthB)){
                                isExistB = true;
                            }
                        }
                        if(isExistA || isExistB){
                            String place = invasion.getPlace();
                            String planet = invasion.getPlanet();
                            planet = AlertService.this.translation.getPlanet(planet);
                            String factionA = invasion.getFactionA();
                            factionA = AlertService.this.translation.getFaction(factionA);
                            String factionB = invasion.getFactionB();
                            factionB = AlertService.this.translation.getFaction(factionB);
                            String type = invasion.getType();
                            type = AlertService.this.translation.getMission(type);
                            if(isExistA && !"cr".equals(moreWorthA)){
                                moreWorthA = t_awardsA[t_awardsA.length-1][1];
                                NotificationCompat.Builder mBuilder =
                                        new NotificationCompat.Builder(AlertService.this)
                                                .setSmallIcon(R.mipmap.ic_launcher)
                                                .setContentTitle(moreWorthA)
                                                .setContentText(place + "(" + planet + ") " + type + " " + factionA + " vs " + factionB);
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
                                mNotificationManager.notify(invasion.getId().hashCode(), mBuilder.build());
                            }
                            if(isExistB && !"cr".equals(moreWorthB)){
                                moreWorthB = t_awardsB[t_awardsB.length-1][1];
                                NotificationCompat.Builder mBuilder =
                                        new NotificationCompat.Builder(AlertService.this)
                                                .setSmallIcon(R.mipmap.ic_launcher)
                                                .setContentTitle(moreWorthB)
                                                .setContentText(place + "(" + planet + ") " + type + " " + factionA + " vs " + factionB);
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
                                mNotificationManager.notify(invasion.getId().hashCode(), mBuilder.build());
                            }
                        }
                    }
                }
                invasions = results;
            }
        };

        this.invasionWorker = new InvasionWorker(this.invasionHandler);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        this.translation = Translation.getInstance(this, "zh_cn");
        this.configWorker = ConfigWorker.getInstance(this);
        while (true){
            try {
                this.alertWorker.run();
                this.invasionWorker.run();
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
