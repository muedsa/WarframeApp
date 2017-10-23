package com.muedsa.wfapp.worker;

import android.content.Context;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.FileInputStream;
import java.io.FileOutputStream;


public class ConfigWorker {

    public static final String CONFIG_NAME = "notfication.json";

    private static ConfigWorker that;

    Context context;

    private boolean isNotify;
    private JSONArray items;

    private ConfigWorker(Context context){
        this.context = context;
        this.init(this.readFromFile(CONFIG_NAME));
    }

    public static ConfigWorker getInstance(Context context){
        if(ConfigWorker.that == null){
            ConfigWorker.that = new ConfigWorker(context);
        }
        return ConfigWorker.that;
    }

    public void openNotfication(){
        this.isNotify = true;
        this.save();
    }

    public void closeNotfication(){
        this.isNotify = false;
        this.save();
    }

    public void addNotficationItem(String item){
        if(this.items.length() > 0){
            boolean isExist = false;
            for (int i=0; i< this.items.length(); i++){
                String temp = this.items.optString(i);
                if(temp.equals(item)){
                    isExist = true;
                    break;
                }
            }
            if (!isExist){
                this.items.put(item);
                this.save();
            }
        }else{
            this.items.put(item);
            this.save();
        }
    }

    public void removeNotficationItem(String item){
        for (int i=0; i< this.items.length(); i++){
            String temp = this.items.optString(i);
            if(temp.equals(item)){
                this.items.remove(i);
            }
        }
        this.save();
    }

    public boolean isNotify(){
        //this.init(this.readFromFile(CONFIG_NAME));
        return this.isNotify;
    }

    public String[] getNotficationItems(){
        String[] items = new String[this.items.length()];
        for(int i=0; i < this.items.length(); i++){
            String item = this.items.optString(i);
            if(item != null){
                items[i] = item;
            }
        }
        return items;
    }



    private void save(){
        JSONObject jsonObject = new JSONObject();
        if(this.items == null){
            this.items = new JSONArray();
        }
        try {
            jsonObject.put("switch", this.isNotify);
            jsonObject.put("items", this.items);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("WFA", e.getMessage());
        }
        this.saveToFile(CONFIG_NAME, jsonObject.toString());
    }

    private String readFromFile(String filename){
        StringBuilder stringBuilder = new StringBuilder("");
        try {
            FileInputStream fileInputStream = this.context.openFileInput(filename);
            byte[] temp = new byte[1024];

            int len = 0;
            while ((len = fileInputStream.read(temp)) > 0) {
                stringBuilder.append(new String(temp, 0, len));
            }
            fileInputStream.close();
        }
        catch (Exception e){
            e.printStackTrace();
            Log.d("WFA", e.getMessage());
        }
        return stringBuilder.toString();
    }

    private void saveToFile(String filename, String content) {
        try {
            FileOutputStream output = this.context.openFileOutput(filename, Context.MODE_PRIVATE);
            output.write(content.getBytes());
            output.close();
        }
        catch (Exception e){
            e.printStackTrace();
            Log.d("WFA", e.getMessage());
        }

    }

    private void init(String content){
        this.isNotify = false;
        if(content != null && !"".equals(content)){
            try{
                JSONObject jsonObject = new JSONObject(content);
                this.isNotify = jsonObject.optBoolean("switch", false);
                this.items = jsonObject.optJSONArray("items");
            }
            catch (Exception e){
                e.printStackTrace();
                Log.d("WFA", e.getMessage());
            }
        }
        if (this.items == null){
            this.items = new JSONArray();
        }
    }
}
