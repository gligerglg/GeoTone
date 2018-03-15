package apps.gligerglg.geotone;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gayan Lakshitha on 3/9/2018.
 */

public class DBPlaceHelper extends SQLiteOpenHelper {

    private static final String TABLE_NAME = "easylec_place";
    private static final String ID = "id";
    private static final String TITLE = "title";
    private static final String RADIUS = "radius";
    private static final String LAT = "lat";
    private static final String LON = "lon";
    private static final String VIBRATE = "vibrate";
    private static final String SOUNDCUT = "soundcut";
    private static final String AIRPLANE = "airplane";



    public DBPlaceHelper(Context context) {
        super(context, "easylecV2", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "CREATE TABLE " + TABLE_NAME + "(" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TITLE + " VARCHAR(25), " +
                LAT + " DOUBLE, " +
                LON + " DOUBLE, " +
                RADIUS + " INT, " +
                VIBRATE + " INT, " +
                SOUNDCUT + " INT, " +
                AIRPLANE + " INT)";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void addPlace(Place place)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TITLE,place.getTitle());
        values.put(LAT,place.getLatitude());
        values.put(LON,place.getLongitude());
        values.put(RADIUS,place.getRadius());
        values.put(VIBRATE,place.isVibrateOn());
        values.put(SOUNDCUT,place.isSoundCutOn());
        values.put(AIRPLANE,place.isAirplaneModeOn());
        db.insert(TABLE_NAME,null,values);
        db.close();
    }

    public Place getPlace(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + ID + " = " + id + " LIMIT 1",null);
        cursor.moveToFirst();
        Place place = new Place();
        place.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(ID))));
        place.setTitle(cursor.getString(cursor.getColumnIndex(TITLE)));
        place.setLatitude(Double.parseDouble(cursor.getString(cursor.getColumnIndex(LAT))));
        place.setLongitude(Double.parseDouble(cursor.getString(cursor.getColumnIndex(LON))));
        place.setRadius(Integer.parseInt(cursor.getString(cursor.getColumnIndex(RADIUS))));
        place.setVibrateOn(Integer.parseInt(cursor.getString(cursor.getColumnIndex(VIBRATE))));
        place.setSoundCutOn(Integer.parseInt(cursor.getString(cursor.getColumnIndex(SOUNDCUT))));
        place.setAirplaneModeOn(Integer.parseInt(cursor.getString(cursor.getColumnIndex(AIRPLANE))));
        db.close();
        return place;
    }

    public List<Place> getAllPlaces()
    {
        List<Place> placesList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME,null);
        if (cursor.moveToFirst()) {
            do {
                Place place = new Place();
                place.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(ID))));
                place.setTitle(cursor.getString(cursor.getColumnIndex(TITLE)));
                place.setLatitude(Double.parseDouble(cursor.getString(cursor.getColumnIndex(LAT))));
                place.setLongitude(Double.parseDouble(cursor.getString(cursor.getColumnIndex(LON))));
                place.setRadius(Integer.parseInt(cursor.getString(cursor.getColumnIndex(RADIUS))));
                place.setVibrateOn(Integer.parseInt(cursor.getString(cursor.getColumnIndex(VIBRATE))));
                place.setSoundCutOn(Integer.parseInt(cursor.getString(cursor.getColumnIndex(SOUNDCUT))));
                place.setAirplaneModeOn(Integer.parseInt(cursor.getString(cursor.getColumnIndex(AIRPLANE))));
                placesList.add(place);
            } while (cursor.moveToNext());
        }
        db.close();
        return placesList;
    }

    public void updatePlace(Place place)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TITLE,place.getTitle());
        values.put(LAT,place.getLatitude());
        values.put(LON,place.getLongitude());
        values.put(RADIUS,place.getRadius());
        values.put(VIBRATE,place.isVibrateOn());
        values.put(SOUNDCUT,place.isSoundCutOn());
        values.put(AIRPLANE,place.isAirplaneModeOn());
        db.update(TABLE_NAME,values,ID + " = ?",new String[]{String.valueOf(place.getId())});
        db.close();
    }

    public void deletePlace(Place place)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME,ID + " = ?",new String[]{String.valueOf(place.getId())});
        db.close();
    }

    public int getPlaceCount()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME,null);
        cursor.close();
        return cursor.getCount();
    }
}
