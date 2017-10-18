package com.muedsa.wfapp.language;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Translation {

    private static Translation that;

    private JSONObject translation;
    private JSONObject planet;
    private JSONObject mission;
    private JSONObject modifier;
    private JSONObject faction;
    private JSONObject awards;

    protected Translation(Context context, String language){
        try{
            InputStreamReader inputStreamReader = new InputStreamReader(context.getAssets().open("language/" + language + ".json"), "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null){
                stringBuilder.append(line);
            }
            bufferedReader.close();
            inputStreamReader.close();
            this.translation = new JSONObject(stringBuilder.toString());
            this.planet = this.translation.getJSONObject("planet");
            this.mission = this.translation.getJSONObject("mission");
            this.modifier = this.translation.getJSONObject("modifier");
            this.faction = this.translation.getJSONObject("faction");
            this.awards = this.translation.getJSONObject("awards");
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("WFA", e.getMessage());
        }
    }

    public static Translation getInstance(Context context, String language){
        if(that == null){
            that = new Translation(context, language);
        }
        return that;
    }


    public String getPlanet(String en){
        String planet = en;
        try {
            if(this.planet != null)
                planet = this.planet.getString(en);
        } catch (JSONException e) {
            //e.printStackTrace();
            //Log.d("WFA", e.getMessage());
        }
        return planet;
    }

    public String getMission(String en){
        String mission = en;
        try {
            if(this.mission != null)
                mission = this.mission.getString(en);
        } catch (JSONException e) {
            //e.printStackTrace();
            //Log.d("WFA", e.getMessage());
        }
        return mission;
    }

    public String getModifier(String en){
        String modifier = en;
        try {
            if(this.modifier != null)
                modifier = this.modifier.getString(en);
        } catch (JSONException e) {
            //e.printStackTrace();
            //Log.d("WFA", e.getMessage());
        }
        return modifier;
    }

    public String getFaction(String en){
        String faction = en;
        try {
            if(this.faction != null)
                faction = this.faction.getString(en);
        } catch (JSONException e) {
            //e.printStackTrace();
            //Log.d("WFA", e.getMessage());
        }
        return faction;
    }
    public String[][] getAwards(String en){
        String[] awards = en.split(" - ");
        String[][] t_awards = new String[awards.length][3];
        if(this.awards != null){
            for(int i=0;i<awards.length;i++){
                String award = awards[i];
                award = award.replaceAll(",", "");
                String pattern = "(\\d+)\\s?(.+)";
                Pattern r = Pattern.compile(pattern);
                Matcher m = r.matcher(award);
                String num = "";
                String name = "";
                String en_name = "";
                try {
                    if(m.find()){
                        num = m.group(1);
                        name = m.group(2);
                        en_name = name;
                        name = this.awards.getString(name);
                    }else{
                        name = award;
                        en_name = name;
                        name = this.awards.getString(name);
                    }
                } catch (JSONException e) {
                    //e.printStackTrace();
                    //Log.d("WFA", e.getMessage());
                }
                t_awards[i] = new String[]{num, name, en_name};
            }
        }

        return t_awards;
    }
}
