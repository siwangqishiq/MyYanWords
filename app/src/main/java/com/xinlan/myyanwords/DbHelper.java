package com.xinlan.myyanwords;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by panyi on 2017/7/14.
 */

public class DbHelper extends SQLiteOpenHelper {
    private static String DB_NAME = "words.db";//the extension may be .sqlite or .db
    private Context mContext;
    public SQLiteDatabase mDataBase;

    private String DB_PATH = "/data/data/" + BuildConfig.APPLICATION_ID + "/databases/";

    public DbHelper(Context context) throws IOException{
        super(context, DB_NAME, null, 1);
        this.mContext = context;

        boolean dbexist = checkdatabase();
        if (dbexist) {
            opendatabase();
        } else {
            createdatabase();
        }
    }

    public void deleteDirDb(){
        String myPath = DB_PATH + DB_NAME;
        File dbfile = new File(myPath);
        dbfile.deleteOnExit();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int ver, int i1) {

    }

    private boolean checkdatabase() {
        boolean checkdb = false;
        try {
            String myPath = DB_PATH + DB_NAME;
            File dbfile = new File(myPath);
            checkdb = dbfile.exists();
        } catch (SQLiteException e) {
            //System.out.println("Database doesn't exist");
        }
        return checkdb;
    }

    public void opendatabase() throws SQLException {
        String mypath = DB_PATH + DB_NAME;
        mDataBase = SQLiteDatabase.openDatabase(mypath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public void createdatabase() throws IOException {
        boolean dbexist = checkdatabase();
        if (dbexist) {
        } else {
            this.getReadableDatabase();
            try {
                copydatabase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    private void copydatabase() throws IOException {
        InputStream myinput = mContext.getAssets().open(DB_NAME);
        String outfilename = DB_PATH + DB_NAME;

        OutputStream myoutput = new FileOutputStream(outfilename);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = myinput.read(buffer)) > 0) {
            myoutput.write(buffer, 0, length);
        }

        //Close the streams
        myoutput.flush();
        myoutput.close();
        myinput.close();
    }

    public SQLiteDatabase getDb(){
        if(mDataBase == null){
            opendatabase();
        }

        return mDataBase;
    }

    public synchronized void close() {
        if (mDataBase != null) {
            mDataBase.close();
            mDataBase = null;
        }
        super.close();
    }
}//end class
