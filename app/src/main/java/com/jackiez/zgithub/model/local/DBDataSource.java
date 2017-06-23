package com.jackiez.zgithub.model.local;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by zsigui on 17-5-19.
 */

public class DBDataSource {

    public SQLHelper mHelper;

    public void init(Context context) {
        mHelper = new SQLHelper(context);
        SQLiteDatabase db = mHelper.getReadableDatabase();
        db.beginTransaction();
        Cursor c = db.query(true, "", new String[]{}, "", new String[]{}, "", null, null, null);
        if (c.moveToFirst()) {
            do {
            }
            while (c.moveToNext());
        }
        db.endTransaction();
        testInterface(name -> {
            Log.d("", name);
        }, "test");
    }




    public class SQLHelper extends SQLiteOpenHelper {

        public SQLHelper(Context context) {
            this(context, "db_github", null, 0);
        }

        public SQLHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        public SQLHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version,
                         DatabaseErrorHandler errorHandler) {
            super(context, name, factory, version, errorHandler);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

    public void testInterface(ICallback callback, String msg) {
        callback.show(msg);
    }

    public interface ICallback {
        void show(String name);
    }

}
