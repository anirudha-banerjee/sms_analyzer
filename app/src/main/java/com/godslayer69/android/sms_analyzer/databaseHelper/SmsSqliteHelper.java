package com.godslayer69.android.sms_analyzer.databaseHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by root on 5/14/17.
 * Project: ELE_SMS_ANIRUDHA
 */

public class SmsSqliteHelper extends SQLiteOpenHelper {

    public static final String TABLE_TRANSACTIONAL = "mtransaction";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_ADDRESS = "_address";
    public static final String COLUMN_DATE_SENT = "_datesent";
    public static final String COLUMN_BODY = "_body";

    public static final String TABLE_OPERATION = "moperation";
    public static final String OCOLUMN_ID = "_id_operation";
    public static final String OCOLUMN_ADDRESS = "_address_operator";
    public static final String OCOLUMN_DATE_SENT = "_datesent_operation";
    public static final String OCOLUMN_BODY = "_body_operation";

    public static final String TABLE_PROMOTION = "mpromotion";
    public static final String PCOLUMN_ID = "_id_promotion";
    public static final String PCOLUMN_ADDRESS = "_address_promoter";
    public static final String PCOLUMN_DATE_SENT = "_datesent_promotion";
    public static final String PCOLUMN_BODY = "_body_promotion";

    public static final String VTABLE_TRANSCTIONAL = "vtransaction";
    public static final String VTABLE_OPERATION = "voperation";
    public static final String VTABLE_PROMOTION = "vpromotion";

    private static final String DATABASE_NAME = "elesmsanirudha.db";
    private static final int DATABASE_VERSION = 1;

    public SmsSqliteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    //Sql statements
    private static final String TDATABASE_CREATE = "create table if not exists "
            + TABLE_TRANSACTIONAL + "( " + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_ADDRESS
            + " text not null, " + COLUMN_DATE_SENT + " text not null, " + COLUMN_BODY
            + " text not null);";

    private static final String ODATABASE_CREATE = "create table if not exists "
            + TABLE_OPERATION + "( " + OCOLUMN_ID
            + " integer primary key autoincrement, " + OCOLUMN_ADDRESS
            + " text not null, " + OCOLUMN_DATE_SENT + " text not null, " + OCOLUMN_BODY
            + " text not null);";

    private static final String PDATABASE_CREATE = "create table if not exists "
            + TABLE_PROMOTION + "( " + PCOLUMN_ID
            + " integer primary key autoincrement, " + PCOLUMN_ADDRESS
            + " text not null, " + PCOLUMN_DATE_SENT + " text not null, " + PCOLUMN_BODY
            + " text not null);";

    //Virtual table sql statements
    private static final String VTDATABASE_CREATE = "CREATE VIRTUAL TABLE if not exists " + VTABLE_TRANSCTIONAL
            + " USING fts4 " + "(content='" + TABLE_TRANSACTIONAL + "', " + COLUMN_BODY + ")";

    private static final String VODATABASE_CREATE = "CREATE VIRTUAL TABLE if not exists " + VTABLE_OPERATION
            + " USING fts4 " + "(content='" + TABLE_OPERATION + "', " + OCOLUMN_BODY + ")";

    private static final String VPDATABASE_CREATE = "CREATE VIRTUAL TABLE if not exists " + VTABLE_PROMOTION
            + " USING fts4 " + "(content='" + TABLE_PROMOTION + "', " + PCOLUMN_BODY + ")";


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TDATABASE_CREATE);
        db.execSQL(ODATABASE_CREATE);
        db.execSQL(PDATABASE_CREATE);
        db.execSQL(VTDATABASE_CREATE);
        db.execSQL(VODATABASE_CREATE);
        db.execSQL(VPDATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(SmsSqliteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTIONAL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OPERATION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROMOTION);
        db.execSQL("DROP TABLE IF EXISTS " + VTABLE_TRANSCTIONAL);
        db.execSQL("DROP TABLE IF EXISTS " + VTABLE_OPERATION);
        db.execSQL("DROP TABLE IF EXISTS " + VTABLE_PROMOTION);
        onCreate(db);
    }

}
