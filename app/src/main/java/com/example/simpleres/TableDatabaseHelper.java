package com.example.simpleres;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static java.lang.Integer.parseInt;

class TableDatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Table.db";
    private static final String TABLE_TABLE_INFO = "tableClass";
    private static final String KEY_ID = "id"; //AKA TABLE NUMBER
    private static final String KEY_STATUS = "status";
    private static final String KEY_NAME = "name";

    TableDatabaseHelper(Context context){
        super(context, DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        String CREATE_TABLE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_TABLE_INFO + "("
                + KEY_ID + " INTEGER PRIMARY KEY UNIQUE,"
                + KEY_STATUS + " TEXT,"
                + KEY_NAME + " TEXT"
                + ")";
        System.out.println("Executing SQLite: \n"+CREATE_TABLE_TABLE);
        db.execSQL(CREATE_TABLE_TABLE);
        System.out.println("Table "+TABLE_TABLE_INFO+" Created" );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TABLE_INFO);
        onCreate(db);
    }

    //add an entry to database
    void addTableClass (TableClass tableClass){
        System.out.println(DATABASE_NAME+" connection opened");

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, tableClass.getTableNumber());
        values.put(KEY_STATUS, tableClass.getTableStatus());
        values.put(KEY_NAME, tableClass.getTableName());
        try {
            db.insert(TABLE_TABLE_INFO, null, values);
        }
        catch (Exception e){
            System.out.println("Table Already Exists in Database");
        }
        db.close();
        System.out.println(DATABASE_NAME+" connection closed");
    }

    //retrieves TableClass info from database from the table number or "id"/ sorts entries by TABLE NUMBER in list in ascending order
    TableClass getTableClass(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        //System.out.println("Retrieving tableClass from database");
        @SuppressLint("Recycle") Cursor cursor = db.query(TABLE_TABLE_INFO, new String[]{KEY_ID, KEY_STATUS, KEY_NAME}, KEY_ID + "=?",
                new String[]{String.valueOf(id)},null,null,KEY_ID +" ASC",null);

        if (cursor!=null)
            cursor.moveToFirst();

        assert cursor != null;
        TableClass tableClass = new TableClass(parseInt(cursor.getString(0)), cursor.getString(1),
                cursor.getString(2));
        db.close();
        return tableClass;
    }

    //used to change values of existing entries in the database
    void updateTableInfo(TableClass tableClass){
        SQLiteDatabase db = this.getWritableDatabase();
        System.out.println("updating table" + tableClass.getTableNumber() + " with the following information:\n"+
                "Status: "+tableClass.getTableStatus()+"\n"+
                "TableName: "+tableClass.getTableName()
                );
        ContentValues values = new ContentValues();
        values.put(KEY_STATUS, tableClass.getTableStatus());
        values.put(KEY_NAME, tableClass.getTableName());
        db.update(TABLE_TABLE_INFO, values, KEY_ID + "=?",
                new String[]{String.valueOf(tableClass.getTableNumber())});
    }
}

