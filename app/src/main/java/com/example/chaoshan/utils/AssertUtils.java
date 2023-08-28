package com.example.chaoshan.utils;

import android.content.Context;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class AssertUtils {

    public static String initMaps(String name, Context context) {
        // 解析Json数据
        StringBuilder newstringBuilder = new StringBuilder();
        InputStream inputStream;
        try {
            inputStream = context.getResources().getAssets().open(name);
            InputStreamReader isr = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(isr);
            String jsonLine;
            while ((jsonLine = reader.readLine()) != null) {
                newstringBuilder.append(jsonLine);
            }
            reader.close();
            isr.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return newstringBuilder .toString();
       /* Gson gson = new Gson();
        Type MapBeanListType = new TypeToken<ArrayList<MapBean>>(){}.getType();
        mapBeanList=gson.fromJson(str, MapBeanListType);*/
    }

}
