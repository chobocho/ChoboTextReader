package com.chobocho.dbmanager;

import androidx.annotation.NonNull;

class EbookItem {
    public final String title;
    public final String filename;


    public EbookItem(String title, String filename) {
        this.title = title;
        this.filename = filename + ".txt";
    }

    @NonNull
    @Override
    public String toString() {
        return title + " : " + filename;
    }
}
