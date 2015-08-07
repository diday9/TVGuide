package test.freelancer.com.fltest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Android 18 on 8/7/2015.
 */
public class SQLiteHelper extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "TVGuideManager";

    // Contacts table name
    private static final String TABLE_PROGRAM = "Programs";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_START_TIME = "start_time";
    private static final String KEY_END_TIME = "end_time";
    private static final String KEY_CHANNEL = "channel";
    private static final String KEY_RATING = "rating";



    public SQLiteHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_PROGRAM + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_START_TIME + " TEXT,"
                + KEY_END_TIME + " TEXT,"
                + KEY_CHANNEL + " TEXT,"
                + KEY_RATING + " TEXT"
                + ")";
        sqLiteDatabase.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROGRAM);

        // Create tables again
        onCreate(db);
    }

    public boolean insertProgram(String name, String start, String end, String ch, String rating){
        Log.d("DATABASE","insert Program info to database");
        SQLiteDatabase db = this.getWritableDatabase();


            ContentValues values = new ContentValues();
            values.put(KEY_NAME, name); // name
            values.put(KEY_START_TIME, start);
            values.put(KEY_END_TIME, end);
            values.put(KEY_CHANNEL, ch);
            values.put(KEY_START_TIME, rating);



        // Inserting Row
        db.insert(TABLE_PROGRAM, null, values);
        db.close(); // Closing database connection
        return true;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db,TABLE_PROGRAM);
        db.close();
        return numRows;
    }

    public void deleteAllPrograms(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_PROGRAM);
        db.close();
    }

    public ArrayList<ProgramInfo> getAll(){
        ArrayList<ProgramInfo> programList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_PROGRAM, null);

        res.moveToFirst();
        while(res.isAfterLast() == false){
            String name = res.getString(res.getColumnIndex(KEY_NAME));
            String start = res.getString(res.getColumnIndex(KEY_START_TIME));
            String end = res.getString(res.getColumnIndex(KEY_END_TIME));
            String channel = res.getString(res.getColumnIndex(KEY_CHANNEL));
            String rating = res.getString(res.getColumnIndex(KEY_RATING));
            programList.add(new ProgramInfo(name, start, end, channel, rating));
            res.moveToNext();
        }
        db.close();
        return programList;
    }
}
