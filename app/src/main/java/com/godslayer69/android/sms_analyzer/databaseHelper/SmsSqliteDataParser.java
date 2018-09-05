package com.godslayer69.android.sms_analyzer.databaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by root on 5/14/17.
 * Project: ELE_SMS_ANIRUDHA
 */

public class SmsSqliteDataParser {

    private final String TAG = "SqliteSmsDataParser";

    //Database entries
    private SQLiteDatabase database;
    private SmsSqliteHelper dbHelper;
    private String[] allTransactionColumns = { SmsSqliteHelper.COLUMN_ID,
            SmsSqliteHelper.COLUMN_ADDRESS, SmsSqliteHelper.COLUMN_DATE_SENT, SmsSqliteHelper.COLUMN_BODY };

    private String[] allOperationColumns = { SmsSqliteHelper.OCOLUMN_ID,
            SmsSqliteHelper.OCOLUMN_ADDRESS, SmsSqliteHelper.OCOLUMN_DATE_SENT, SmsSqliteHelper.OCOLUMN_BODY };

    private String[] allPromotionColumns = { SmsSqliteHelper.PCOLUMN_ID,
            SmsSqliteHelper.PCOLUMN_ADDRESS, SmsSqliteHelper.PCOLUMN_DATE_SENT, SmsSqliteHelper.PCOLUMN_BODY };

    private int paymentMessageCount = 0;
    private int rechargeMessageCount = 0;
    private int transactionSuccessCount = 0;
    private int offerMessageCount = 0;
    private int offersMessageCount = 0;
    private int bookNowMessageCount = 0;
    private int rideNowMessageCount = 0;
    private int rechargeNowMessageCount = 0;
    private Context context;

    public SmsSqliteDataParser(Context context) {
        dbHelper = new SmsSqliteHelper(context);
        this.context = context;
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void createSmsEntries(ArrayList<SmsSqliteModel> smsSqliteModelArrayList) {

        //Check for tablename
        for (int i=0;i<smsSqliteModelArrayList.size();i++){

            SmsSqliteModel model = smsSqliteModelArrayList.get(i);
            String tableName = model.getSmsAddress();
            ContentValues values = new ContentValues();

            if (tableName.contains("OLAMNY")){

                if (!CheckIsDataAlreadyInDB(SmsSqliteHelper.TABLE_TRANSACTIONAL, SmsSqliteHelper.COLUMN_DATE_SENT
                        , model.getSmsDateSent())){

                    //Insert values
                    values.put(SmsSqliteHelper.COLUMN_ADDRESS, model.getSmsAddress());
                    values.put(SmsSqliteHelper.COLUMN_DATE_SENT, model.getSmsDateSent());
                    values.put(SmsSqliteHelper.COLUMN_BODY, model.getSmsBody());

                    long insertId = database.insert(SmsSqliteHelper.TABLE_TRANSACTIONAL, null,
                            values);

                    Log.d(TAG, "sms entry insert id: " + insertId);

                    createVirtualSmsEntries(tableName);
                }
            }
            else if (tableName.contains("OLACAB")){

                if (!CheckIsDataAlreadyInDB(SmsSqliteHelper.TABLE_OPERATION, SmsSqliteHelper.OCOLUMN_DATE_SENT
                        , model.getSmsDateSent())){

                    //Insert values
                    values.put(SmsSqliteHelper.OCOLUMN_ADDRESS, model.getSmsAddress());
                    values.put(SmsSqliteHelper.OCOLUMN_DATE_SENT, model.getSmsDateSent());
                    values.put(SmsSqliteHelper.OCOLUMN_BODY, model.getSmsBody());

                    long insertId = database.insert(SmsSqliteHelper.TABLE_OPERATION, null,
                            values);

                    Log.d(TAG, "sms entry insert id: " + insertId);

                    createVirtualSmsEntries(tableName);

                }
            }
            else if (tableName.contains("OLACBS")){

                if (!CheckIsDataAlreadyInDB(SmsSqliteHelper.TABLE_PROMOTION, SmsSqliteHelper.PCOLUMN_DATE_SENT,
                        model.getSmsDateSent())){

                    //Insert values
                    values.put(SmsSqliteHelper.PCOLUMN_ADDRESS, model.getSmsAddress());
                    values.put(SmsSqliteHelper.PCOLUMN_DATE_SENT, model.getSmsDateSent());
                    values.put(SmsSqliteHelper.PCOLUMN_BODY, model.getSmsBody());

                    long insertId = database.insert(SmsSqliteHelper.TABLE_PROMOTION, null,
                            values);

                    Log.d(TAG, "sms entry insert id: " + insertId);

                    createVirtualSmsEntries(tableName);

                }

            }
        }
    }

    private void createVirtualSmsEntries(String tableName) {

        if (tableName.contains("OLAMNY")){

            //Insert values
            String VTDATABASE_INSERT = "INSERT INTO " + SmsSqliteHelper.VTABLE_TRANSCTIONAL +
                    "(docid, " + SmsSqliteHelper.COLUMN_BODY + ") SELECT " + SmsSqliteHelper.COLUMN_ID +
                    ", " + SmsSqliteHelper.COLUMN_BODY + " FROM " + SmsSqliteHelper.TABLE_TRANSACTIONAL;

            database.execSQL(VTDATABASE_INSERT);

            Log.d(TAG, "virtual sms entry inserted");
        }
        else if (tableName.contains("OLACAB")){

            //Insert values
            String VODATABASE_INSERT = "INSERT INTO " + SmsSqliteHelper.VTABLE_OPERATION +
                    "(docid, " + SmsSqliteHelper.OCOLUMN_BODY + ") SELECT " + SmsSqliteHelper.OCOLUMN_ID +
                    " , " + SmsSqliteHelper.OCOLUMN_BODY + " FROM " + SmsSqliteHelper.TABLE_OPERATION;

            database.execSQL(VODATABASE_INSERT);

            Log.d(TAG, "virtual sms entry inserted");
        }
        else if (tableName.contains("OLACBS")){

            //Insert values
            String VPDATABASE_INSERT = "INSERT INTO " + SmsSqliteHelper.VTABLE_PROMOTION +
                    "(docid, " + SmsSqliteHelper.PCOLUMN_BODY + ") SELECT " + SmsSqliteHelper.PCOLUMN_ID +
                    " , " + SmsSqliteHelper.PCOLUMN_BODY + " FROM " + SmsSqliteHelper.TABLE_PROMOTION;

            database.execSQL(VPDATABASE_INSERT);

            Log.d(TAG, "virtual sms entry inserted");

        }

    }

    public void deleteSmsEntry(String tableName, SmsSqliteModel smsDataModel) {
        long id = smsDataModel.getSmsID();
        Log.d(TAG, "Delete entry with id: " + id);

        //Check for table name
        if (tableName.equalsIgnoreCase(SmsSqliteHelper.TABLE_TRANSACTIONAL)){

            int rowsDeleted = database.delete(SmsSqliteHelper.TABLE_TRANSACTIONAL, SmsSqliteHelper.COLUMN_ID
                    + " = ? ", new String[] {String.valueOf(id)});

            Log.d(TAG, "rows deleted: " + rowsDeleted);

        }
        else if (tableName.equalsIgnoreCase(SmsSqliteHelper.TABLE_OPERATION)){

            int rowsDeleted = database.delete(SmsSqliteHelper.TABLE_OPERATION, SmsSqliteHelper.OCOLUMN_ID
                    + " = ? ", new String[] {String.valueOf(id)});

            Log.d(TAG, "rows deleted: " + rowsDeleted);
        }
        else if (tableName.equalsIgnoreCase(SmsSqliteHelper.TABLE_PROMOTION)){

            int rowsDeleted = database.delete(SmsSqliteHelper.TABLE_PROMOTION, SmsSqliteHelper.PCOLUMN_ID
                    + " = ? ", new String[] {String.valueOf(id)});

            Log.d(TAG, "rows deleted: " + rowsDeleted);
        }
    }

    public long getKeywordEntries(String tableName) {

        transactionSuccessCount = 0;
        paymentMessageCount = 0;
        rechargeMessageCount = 0;
        offerMessageCount = 0;
        offersMessageCount = 0;
        bookNowMessageCount = 0;
        rideNowMessageCount = 0;
        rechargeNowMessageCount = 0;

        //Check for table name
        if (tableName.equalsIgnoreCase(SmsSqliteHelper.VTABLE_TRANSCTIONAL)){

            int totalCount = 0;

            //Run query
            Cursor cursor = database.query(SmsSqliteHelper.VTABLE_TRANSCTIONAL, null,
                    SmsSqliteHelper.VTABLE_TRANSCTIONAL + " MATCH ? ",
                    new String[] {"transaction"}, null, null, null);

            Log.d(TAG, "All Transaction Entries: " + cursor.getCount());
            transactionSuccessCount = cursor.getCount();
            totalCount += cursor.getCount();

            Cursor cursor2 = database.query(SmsSqliteHelper.VTABLE_TRANSCTIONAL, null,
                    SmsSqliteHelper.VTABLE_TRANSCTIONAL + " MATCH ? ",
                    new String[] {"payment"}, null, null, null);

            Log.d(TAG, "All Payment Entries: " + cursor2.getCount());
            paymentMessageCount = cursor2.getCount();
            totalCount += cursor2.getCount();

            Cursor cursor3 = database.query(SmsSqliteHelper.VTABLE_TRANSCTIONAL, null,
                    SmsSqliteHelper.VTABLE_TRANSCTIONAL + " MATCH ? ",
                    new String[] {"recharge"}, null, null, null);

            Log.d(TAG, "All Recharge Entries: " + cursor3.getCount());
            rechargeMessageCount = cursor3.getCount();
            totalCount += cursor3.getCount();

            cursor.close();
            cursor2.close();
            cursor3.close();

            return totalCount;

        }
        else if (tableName.equalsIgnoreCase(SmsSqliteHelper.VTABLE_OPERATION)){

            int totalCount = 0;

            Cursor cursor = database.query(SmsSqliteHelper.VTABLE_OPERATION, null,
                    SmsSqliteHelper.VTABLE_OPERATION + " MATCH ? ", new String[] {"on the way"}, null, null, null);

            Log.d(TAG, "All Operation Entries: " + cursor.getCount());
            totalCount += cursor.getCount();
            cursor.close();
            return totalCount;

        }
        else if (tableName.equalsIgnoreCase(SmsSqliteHelper.VTABLE_PROMOTION)){

            int totalCount = 0;

            Cursor cursor = database.query(SmsSqliteHelper.VTABLE_PROMOTION, null,
                    SmsSqliteHelper.VTABLE_PROMOTION + " MATCH ? ", new String[] {"offer"}, null, null, null);

            offerMessageCount += cursor.getCount();

            Cursor cursor2 = database.query(SmsSqliteHelper.VTABLE_PROMOTION, null,
                    SmsSqliteHelper.VTABLE_PROMOTION + " MATCH ? ", new String[] {"offers"}, null, null, null);

            offersMessageCount += cursor2.getCount();

            Cursor cursor3 = database.query(SmsSqliteHelper.VTABLE_PROMOTION, null,
                    SmsSqliteHelper.VTABLE_PROMOTION + " MATCH ? ", new String[] {"recharge now"}, null, null, null);

            rechargeNowMessageCount += cursor3.getCount();

            Cursor cursor4 = database.query(SmsSqliteHelper.VTABLE_PROMOTION, null,
                    SmsSqliteHelper.VTABLE_PROMOTION + " MATCH ? ", new String[] {"ride now"}, null, null, null);

            rideNowMessageCount += cursor4.getCount();

            Cursor cursor5 = database.query(SmsSqliteHelper.VTABLE_PROMOTION, null,
                    SmsSqliteHelper.VTABLE_PROMOTION + " MATCH ? ", new String[] {"book now"}, null, null, null);

            bookNowMessageCount += cursor5.getCount();

            cursor.close();
            cursor2.close();
            cursor3.close();
            cursor4.close();
            cursor5.close();

            return totalCount;

        }
        else {
            Log.e(TAG, "Table name not found");
            return 0;
        }

    }

    /**

    public List<SmsSqliteModel> messageList(){

    }**/

    public int getTransactionSuccessCount(){
        if (transactionSuccessCount != 0){
            return transactionSuccessCount;
        }
        else {
            return 0;
        }
    }

    public int getPaymentMessageCount() {
        if (paymentMessageCount != 0){
            return paymentMessageCount;
        }
        else {
            return 0;
        }
    }

    public int getRechargeMessageCount(){
        if (rechargeMessageCount != 0){
            return rechargeMessageCount;
        }
        else {
            return 0;
        }
    }

    public int getOfferMessageCount() {
        return offerMessageCount;
    }

    public int getOffersMessageCount() {
        return offersMessageCount;
    }

    public int getRechargeNowMessageCount() {
        return rechargeNowMessageCount;
    }

    public int getRideNowMessageCount() {
        return rideNowMessageCount;
    }

    public int getBookNowMessageCount() {
        return bookNowMessageCount;
    }

    public long getRowCount(String[] tableNames){

        long rowCount = 0;

        if (tableNames != null){

            for (String tableName: tableNames){

                if (tableName.equalsIgnoreCase(SmsSqliteHelper.TABLE_TRANSACTIONAL)){

                    rowCount  += DatabaseUtils.queryNumEntries(database, tableName);

                }
                else if (tableName.equalsIgnoreCase(SmsSqliteHelper.TABLE_OPERATION)){

                    rowCount += DatabaseUtils.queryNumEntries(database, tableName);

                }
                else if (tableName.equalsIgnoreCase(SmsSqliteHelper.TABLE_PROMOTION)){

                    rowCount += DatabaseUtils.queryNumEntries(database, tableName);

                }
                else {
                    rowCount = 0;
                }

            }
            Log.d(TAG, "Total row count: " + rowCount);

            return rowCount;
        }
        else {
            Log.e(TAG, "No Table Name Found");
            return 0;
        }

    }

    private boolean CheckIsDataAlreadyInDB(String TableName, String dbfield, String fieldValue) {

        String Query = "Select * from " + TableName + " where " + dbfield + " = " + fieldValue;
        Cursor cursor = database.rawQuery(Query, null);

        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    private SmsSqliteModel parseToSmsModel(Cursor cursor) {
        SmsSqliteModel smsModel = new SmsSqliteModel();
        smsModel.setSmsID(cursor.getLong(0));
        smsModel.setSmsAddress(cursor.getString(1));
        smsModel.setSmsDateSent(cursor.getString(2));
        smsModel.setSmsBody(cursor.getString(3));
        return smsModel;
    }

}
