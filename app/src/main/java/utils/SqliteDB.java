package utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by kami on 15/10/10.
 */
public class SqliteDB extends SQLiteOpenHelper {

    public SqliteDB(Context context) {
        super(context, "db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("Create table course(" +
                "term text," +
                "json text)");
        db.execSQL("create table maillist(id int ,mail text)");
        ContentValues cv = new ContentValues();
        cv.put("id", 1);
        cv.put("mail", "");
        db.insert("maillist",null,cv);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
