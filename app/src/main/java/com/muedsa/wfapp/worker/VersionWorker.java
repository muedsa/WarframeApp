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

/**
 * Created by muedsa on 2017/10/24.
 */

public class VersionWorker {
    public static final String WORKER_URL = "https://raw.githubusercontent.com/MUedsa/WarframeApp/master/version";
    private Handler handler;

    Runnable network = new Runnable() {
        private BufferedInputStream bis;
        @Override
        public void run() {
            String version = "";
            try {
                URL url = new URL(AlertWorker.WORKER_URL);
                URLConnection urlConnection = url.openConnection();
                urlConnection.connect();
                bis = new BufferedInputStream(urlConnection.getInputStream());
                int length;
                byte[] bytes = new byte[1024];
                while ((length = bis.read(bytes, 0, bytes.length)) != -1) {
                    version += new String(bytes);
                }
                version = version.trim();
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
            bundle.putInt("versionCode", Integer.parseInt(version));
            msg.setData(bundle);
            handler.sendMessage(msg);
        }
    };

    public VersionWorker(Handler handler){
        this.handler = handler;
    }

    public void run(){
        Log.d("WFA", "VersionWorkRun!");
        new Thread(network).start();
    }
}
