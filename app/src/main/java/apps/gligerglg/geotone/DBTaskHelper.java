package apps.gligerglg.geotone;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gayan Lakshitha on 3/8/2018.
 */

public class DBTaskHelper extends SQLiteOpenHelper {

    private static final String TABLE_NAME = "easylec_task";
    private static final String ID = "id";
    private static final String TITLE = "title";
    private static final String DESCRIPTION = "desc";
    private static final String LAT = "lat";
    private static final String LON = "lon";
    private static final String RADIUS = "radius";
    private static final String DATE = "date";
    private static final String TIME = "time";

    public DBTaskHelper(Context context) {
        super(context, "easylecV2", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "CREATE TABLE " + TABLE_NAME + "(" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TITLE + " VARCHAR(25), " +
                DESCRIPTION + " VARCHAR(75), " +
                LAT + " DOUBLE, " +
                LON + " DOUBLE, " +
                RADIUS + " INT, " +
                DATE + " TEXT, " +
                TIME + " TEXT)";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void addTask(Task task)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TITLE,task.getTitle());
        values.put(DESCRIPTION,task.getDescription());
        values.put(LAT,task.getLatitude());
        values.put(LON,task.getLongitude());
        values.put(RADIUS,task.getRadius());
        values.put(DATE,task.getDate());
        values.put(TIME,task.getTime());
        db.insert(TABLE_NAME,null,values);
        db.close();
    }

    public Task getTask(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + ID + " = " + id + " LIMIT 1",null);
        cursor.moveToFirst();
        Task task = new Task();
        task.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(ID))));
        task.setTitle(cursor.getString(cursor.getColumnIndex(TITLE)));
        task.setDescription(cursor.getString(cursor.getColumnIndex(DESCRIPTION)));
        task.setLatitude(Double.parseDouble(cursor.getString(cursor.getColumnIndex(LAT))));
        task.setLongitude(Double.parseDouble(cursor.getString(cursor.getColumnIndex(LON))));
        task.setRadius(Integer.parseInt(cursor.getString(cursor.getColumnIndex(RADIUS))));
        task.setDate(cursor.getString(cursor.getColumnIndex(DATE)));
        task.setTime(cursor.getString(cursor.getColumnIndex(TIME)));
        db.close();
        return task;
    }

    public List<Task> getAllTasks()
    {
        List<Task> taskList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME,null);
        if (cursor.moveToFirst()) {
            do {
                Task task = new Task();
                task.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(ID))));
                task.setTitle(cursor.getString(cursor.getColumnIndex(TITLE)));
                task.setDescription(cursor.getString(cursor.getColumnIndex(DESCRIPTION)));
                task.setLatitude(Double.parseDouble(cursor.getString(cursor.getColumnIndex(LAT))));
                task.setLongitude(Double.parseDouble(cursor.getString(cursor.getColumnIndex(LON))));
                task.setRadius(Integer.parseInt(cursor.getString(cursor.getColumnIndex(RADIUS))));
                task.setDate(cursor.getString(cursor.getColumnIndex(DATE)));
                task.setTime(cursor.getString(cursor.getColumnIndex(TIME)));
                taskList.add(task);
            } while (cursor.moveToNext());
        }
        db.close();
        return taskList;
    }

    public void updateTask(Task task)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TITLE,task.getTitle());
        values.put(DESCRIPTION,task.getDescription());
        values.put(LAT,task.getLatitude());
        values.put(LON,task.getLongitude());
        values.put(RADIUS,task.getRadius());
        values.put(DATE,task.getDate());
        values.put(TIME,task.getTime());
        db.update(TABLE_NAME,values,ID + " = ?",new String[]{String.valueOf(task.getId())});
        db.close();
    }

    public void deleteTask(Task task)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME,ID + " = ?",new String[]{String.valueOf(task.getId())});
        db.close();
    }

    public int getTaskCount()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME,null);
        cursor.close();
        return cursor.getCount();
    }
}
