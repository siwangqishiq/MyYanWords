package com.xinlan.myyanwords;

import android.content.Context;
import android.database.Cursor;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by panyi on 2017/7/14.
 */

public class WordsDao {
    private Context context;
    private DbHelper mDbHelper;

    public static final String ID = "id";
    public static final String WORD = "word";
    public static final String TRANSLATE = "translate";
    public static final String EXTRA = "extra";
    public static final String TIME = "time";

    public WordsDao(Context context) {
        this.context = context;
        try {
            mDbHelper = new DbHelper(context);
            mDbHelper.opendatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }

    public List<Word> getWordsList() throws UnsupportedEncodingException {
        List<Word> list = new ArrayList<Word>();
        Cursor cursor = mDbHelper.getDb().query("words", new String[]{ID, WORD, TRANSLATE, EXTRA, TIME}, null, null, null, null, null);
        while (cursor.moveToNext()) {
            Word word = new Word();
            word.setId(cursor.getInt(cursor.getColumnIndex(ID)));
            word.setWord(readString(cursor, WORD));
            word.setTranslate(readString(cursor, TRANSLATE));
            word.setExtra(cursor.getString(cursor.getColumnIndex(EXTRA)));
            word.setTime(cursor.getLong(cursor.getColumnIndex(TIME)));
            list.add(word);
            //System.out.println(word);
        }//end while
        cursor.close();

        return list;
    }

    public static String readString(Cursor cursor, String key) {
        if (cursor == null)
            return "";
        try {
            String str = new String(cursor.getBlob(cursor.getColumnIndex(key)), "GBK");
            return str.trim();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
}
