package com.chobocho.dbmanager;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

public class BookManager {
    public static final String TAG = "[eBook] " + BookManager.class.getSimpleName();
    ArrayList<EbookItem> ebookList = null;
    final ArrayList<String> ebookTitleList = new ArrayList<>();
    final HashMap <String, String> ebookListHash = new HashMap<>();
    final Context mContext;

    public BookManager(Context context) {
        Log.i(TAG, "Init");
        mContext = context;
        init();
    }

    private void init() {
        IndexManager indexManager = new IndexManager(mContext);
        ebookList = indexManager.getEbookList();
        for (EbookItem book : ebookList) {
              ebookListHash.put(book.title, book.filename);
              ebookTitleList.add(book.title);
        }
    }

    public ArrayList<String> getEbookList() {
        return ebookTitleList;
    }

    public ArrayList<String> getEbookList(String query) {
        if (TextUtils.isEmpty(query)) {
            return getEbookList();
        }

        ArrayList<String> filteredList = new ArrayList<>();
        ebookTitleList.stream().filter(e -> e.toLowerCase().contains(query.toLowerCase())).forEach(filteredList::add);
        return filteredList;
    }

    public String getBookText(String title) {
        String booktitle = ebookListHash.get(title);
        return readBookFile(booktitle);
    }

    private String readBookFile(String filename) {
        return LoadTextFileAsString("text/" + filename);
    }

    private String LoadTextFileAsString(String filename) {
        String textData;

        try {
            InputStream textInputStream = mContext.getAssets().open(filename);
            byte[] rawData = new byte[textInputStream.available()];
            textInputStream.read(rawData);
            textInputStream.close();
            textData = new String(rawData, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return textData;
    }
}