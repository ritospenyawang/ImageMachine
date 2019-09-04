package com.example.imagemachine;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MachineDB extends SQLiteOpenHelper {
    //Database Info
    private static final String DATABASE_NAME = "machineDB";
    private static final int DATABASE_VERSION = 1;

    //Table Names
    private static final String TABLE_MACHINE = "machines";

    //Machines Column Names
    private static final String KEY_MACHINES_ID = "id";
    private static final String KEY_MACHINES_ID_FK = "machineID";
    private static final String KEY_MACHINES_NAME = "name";
    private static final String KEY_MACHINES_TYPE = "type";
    private static final String KEY_MACHINES_QR = "qr";
    private static final String KEY_MACHINES_DATE = "date";

    private static MachineDB sInstance;

    private MachineDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private MachineDB(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public static synchronized MachineDB getInstance(Context context){
        if(sInstance == null){
            sInstance = new MachineDB(context.getApplicationContext());
        }
        return sInstance;
    }

    //Configure database settings
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    //Called when database is created for the first time
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_MACHINES_TABLE = "CREATE TABLE " + TABLE_MACHINE + "(" +
                KEY_MACHINES_ID + " INTEGER PRIMARY KEY," +
                KEY_MACHINES_ID_FK + " INTEGER," +
                KEY_MACHINES_NAME + " TEXT," +
                KEY_MACHINES_TYPE + " TEXT," +
                KEY_MACHINES_QR + " INTEGER," +
                KEY_MACHINES_DATE + " DATE" +
                ")";
        db.execSQL(CREATE_MACHINES_TABLE);
    }

    //Called when database needs to be upgraded
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion != newVersion){
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_MACHINE);
            onCreate(db);
        }
    }

    //Function to add new machine to database
    public void addMachine(Machine m){
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try{
            ContentValues cv = new ContentValues();
            cv.put(KEY_MACHINES_ID_FK, m.ID);
            cv.put(KEY_MACHINES_NAME, m.name);
            cv.put(KEY_MACHINES_TYPE, m.type);
            cv.put(KEY_MACHINES_QR, m.qr);
            cv.put(KEY_MACHINES_DATE, m.date);

            db.insertOrThrow(TABLE_MACHINE, null, cv);
            db.setTransactionSuccessful();
        }catch (Exception e){
            Log.d("TAG", "Error while trying to add data to database");
        }finally {
            Log.d("TAG", "Added 1 new data to database");
            db.endTransaction();
        }
    }

    //Function to update existing machine in database (currently not working)
    public void updateMachine(Machine m){
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try{
            ContentValues cv = new ContentValues();
            cv.put(KEY_MACHINES_NAME, m.name);
            cv.put(KEY_MACHINES_TYPE, m.type);
            cv.put(KEY_MACHINES_QR, m.qr);
            cv.put(KEY_MACHINES_DATE, m.date);

            db.update(TABLE_MACHINE, cv, KEY_MACHINES_ID + " = " + m.ID, null);
            db.setTransactionSuccessful();
        }catch (Exception e){
            Log.d("TAG", "Error while trying to update data to database");
        }finally {
            Log.d("TAG", "Updated 1 data to database");
            db.endTransaction();
        }
    }

    //Function to get all machines in database
    public List<Machine> getAllMachine(String orderBy){
        List<Machine> machines = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.query(TABLE_MACHINE, null, null, null, null, null, orderBy);

        try{
            if(c.moveToFirst()){
                do{
                    Machine newMachine = new Machine();
                    newMachine.ID = c.getInt(c.getColumnIndex(KEY_MACHINES_ID_FK));
                    newMachine.name = c.getString(c.getColumnIndex(KEY_MACHINES_NAME));
                    newMachine.type = c.getString(c.getColumnIndex(KEY_MACHINES_TYPE));
                    newMachine.qr = c.getInt(c.getColumnIndex(KEY_MACHINES_QR));
                    newMachine.date = c.getString(c.getColumnIndex(KEY_MACHINES_DATE));
                    machines.add(newMachine);
                }while (c.moveToNext());
            }
        }catch (Exception e){
            Log.d("TAG", "Error while trying to get data from database");
        }finally {
            if(c != null && !c.isClosed()){
                c.close();
            }
        }
        return machines;
    }

    //Function to get 1 machine in database
    public Machine getMachine(int machineID){
        Machine machine = new Machine();
        SQLiteDatabase db = getReadableDatabase();
        String cond = "machineID = " + machineID;
        Cursor c = db.query(TABLE_MACHINE, null, cond, null, null, null, null);
        try{
            if(c.moveToFirst()){
                machine.ID = c.getInt(c.getColumnIndex(KEY_MACHINES_ID_FK));
                machine.name = c.getString(c.getColumnIndex(KEY_MACHINES_NAME));
                machine.type = c.getString(c.getColumnIndex(KEY_MACHINES_TYPE));
                machine.qr = c.getInt(c.getColumnIndex(KEY_MACHINES_QR));
                machine.date = c.getString(c.getColumnIndex(KEY_MACHINES_DATE));
            }
        }catch (Exception e){
            Log.d("TAG", "Error while trying to get data from database");
        }finally {
            if(c != null && !c.isClosed()){
                c.close();
            }
        }
        return machine;
    }
}
