package com.chobocho.dbmanager;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;


public class IndexManager {
    public static final String TAG = "[eBook] " + IndexManager.class.getSimpleName();
    final ArrayList<EbookItem> ebookList;

    private Context mContext = null;

    public IndexManager(Context context) {
        Log.i(TAG, "Init");
        mContext = context;
        String jsonString = LoadJsonFileAsString();
        ebookList = loadEbookList(jsonString);
    }

    private String LoadJsonFileAsString() {
        String jsonData = null;
        String jsonFileName = "index.json";

        try {
            InputStream jsonInputStream = mContext.getAssets().open(jsonFileName);
            byte[] rawData = new byte[jsonInputStream.available()];
            jsonInputStream.read(rawData);
            jsonInputStream.close();
            jsonData = new String(rawData, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return jsonData;
    }

    private ArrayList<EbookItem> loadEbookList(String passData) {
        ArrayList<EbookItem> ebooks = new ArrayList<>();

        if (TextUtils.isEmpty(passData)) {
            Log.i(TAG, "PassData is empty");
            return ebooks;
        }

        try {
            JSONObject jsonObject = new JSONObject(passData);
            JSONArray jsonArray = jsonObject.getJSONArray("data");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                String title = object.getString("id");
                String filename = object.getString("filename");

                EbookItem ebookItem = new EbookItem(title, filename);
                ebooks.add(ebookItem);
                // Log.i(TAG, ebookItem.toString());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ebooks;
    }

    ArrayList<EbookItem> getEbookList() {
        return ebookList;
    }
}