package com.godslayer69.android.sms_analyzer.smsDataAnalysis;


import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Telephony;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.godslayer69.android.sms_analyzer.databaseHelper.SmsSqliteDataParser;
import com.godslayer69.android.sms_analyzer.databaseHelper.SmsSqliteHelper;
import com.godslayer69.android.sms_analyzer.databaseHelper.SmsSqliteModel;

import java.util.ArrayList;

/**
 * Created by root on 5/15/17.
 * Project: ELE_SMS_ANIRUDHA
 */

public class SmsDataParser {

    private final String TAG = "SmsDataParser";
    private Context context;
    private SmsSqliteDataParser sqliteDataParser;

    public SmsDataParser(Context c) {
        context = c;
    }

    public void readSmsInbox(){

        //Choose columns to show
        final String[] projection = {Telephony.Sms._ID, Telephony.Sms.ADDRESS, Telephony.Sms.DATE_SENT,
                Telephony.Sms.BODY};

        //Selection Args
        final String olaMnySelection = "%OLAMNY%";
        final String olaCabSelection = "%OLACAB%";
        final String olaPromoSelection = "%OLACBS%";

        Toast.makeText(context, "Analysing Your Messages.... This Can Take A Few Moments",
                Toast.LENGTH_SHORT).show();

        Cursor cursor = context.getContentResolver().query(Uri.parse("content://sms/inbox"), projection,
                Telephony.Sms.ADDRESS + " LIKE ? OR " + Telephony.Sms.ADDRESS + " LIKE ? OR " +
                        Telephony.Sms.ADDRESS + " LIKE ? ",
                new String[] {olaCabSelection, olaMnySelection, olaPromoSelection}, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) { //check the result to prevent exception
                ArrayList<SmsSqliteModel> smsModelArrayList = new ArrayList<>();
                sqliteDataParser = new SmsSqliteDataParser(context);

                do {
                    SmsSqliteModel sqliteModel = new SmsSqliteModel();

                    for(int i=1;i<cursor.getColumnCount();i++)
                    {
                        String columnName = cursor.getColumnName(i);

                        switch (columnName){
                            case Telephony.Sms.ADDRESS:
                                sqliteModel.setSmsAddress(cursor.getString(i));
                                break;
                            case Telephony.Sms.DATE_SENT:
                                sqliteModel.setSmsDateSent(cursor.getString(i));
                                break;
                            case Telephony.Sms.BODY:
                                sqliteModel.setSmsBody(cursor.getString(i));
                                break;
                            default:
                                Log.e(TAG, "Column: " + cursor.getColumnName(i) + " not needed");
                                break;
                        }
                    }
                    Log.d(TAG, "sms data: " + cursor.getCount());

                    // use msgData
                    smsModelArrayList.add(sqliteModel);
                } while (cursor.moveToNext());

                //Close connections
                cursor.close();
                //Send msg data to db data parser
                sqliteDataParser.open();
                sqliteDataParser.createSmsEntries(smsModelArrayList);
                sqliteDataParser.close();

            } else {
                Log.e("ELE_SMS: ", "no sms data");

                cursor.close();
            }
        }

    }

    public void getSmsAnalysisReport(String table, TextView senderNameText,
                                     TextView messageCountText, TextView messageTypeText,
                                     TextView analysisDetails, TextView analysisCategoryDetails){

        sqliteDataParser = new SmsSqliteDataParser(context);
        sqliteDataParser.open();

        //Get message counts
        long messageRowCount = sqliteDataParser.getRowCount(new String[] {table});

        Log.d(TAG, "Message row count: " + messageRowCount);

        long totalRowCount = sqliteDataParser.getRowCount(new String[] {SmsSqliteHelper.TABLE_TRANSACTIONAL,
                SmsSqliteHelper.TABLE_OPERATION, SmsSqliteHelper.TABLE_PROMOTION});

        Log.d(TAG, "Total row count: " + totalRowCount);

        if (table.equalsIgnoreCase(SmsSqliteHelper.TABLE_TRANSACTIONAL)){

            senderNameText.setText("OLAMNY");
            messageCountText.setText(String.valueOf(messageRowCount));
            messageTypeText.setText("Transaction");

            //AnalysisChartUtil chartUtil = new AnalysisChartUtil(context, messageRowCount, totalRowCount,
                    //TransactionSmsReportFrag.messageAnalysisPie);

            long tAnalysisReport = sqliteDataParser.getKeywordEntries(SmsSqliteHelper.VTABLE_TRANSCTIONAL);

            analysisDetails.setText("Total " + tAnalysisReport + " messages were found " +
                    "related to OLA Money Successful Transaction");

            int successTransactionCount = sqliteDataParser.getTransactionSuccessCount();
            int paymentTransactionCount = sqliteDataParser.getPaymentMessageCount();
            int rechargeTransactionCount = sqliteDataParser.getRechargeMessageCount();

            analysisCategoryDetails.setText(successTransactionCount + " total transaction done through OLA Money\n" +
                    paymentTransactionCount + " total payments done through OLA MONEY\n" +
                    rechargeTransactionCount + " total recharges done through OLA MONEY");

        }
        else if (table.equalsIgnoreCase(SmsSqliteHelper.TABLE_OPERATION)){

            senderNameText.setText("OLACAB");
            messageCountText.setText(String.valueOf(messageRowCount));
            messageTypeText.setText("Operation");

            //AnalysisChartUtil chartUtil = new AnalysisChartUtil(context, messageRowCount, totalRowCount,
                    //OperationSmsReportFrag.messageAnalysisPie);

            long oAnalysisReport = sqliteDataParser.getKeywordEntries(SmsSqliteHelper.VTABLE_OPERATION);

            analysisDetails.setText("Total " + oAnalysisReport + " messages were found " +
                    "related to OLA Cab Confirmation");

        }
        else if (table.equalsIgnoreCase(SmsSqliteHelper.TABLE_PROMOTION)){


            senderNameText.setText("OLACBS");
            messageCountText.setText(String.valueOf(messageRowCount));
            messageTypeText.setText("Promotions");

            long pAnalysisReport = sqliteDataParser.getKeywordEntries(SmsSqliteHelper.VTABLE_PROMOTION);

            if (pAnalysisReport == 0){
                analysisDetails.setVisibility(View.GONE);
            }
            else {
                analysisDetails.setText("Total " + pAnalysisReport + " messages were found " +
                        "related to OLA Promotions");
            }

            int offerMessageCount = sqliteDataParser.getOfferMessageCount();
            int offersMessageCount = sqliteDataParser.getOffersMessageCount();
            int rechargeNowMessageCount = sqliteDataParser.getRechargeNowMessageCount();
            int rideNowMessageCount = sqliteDataParser.getRideNowMessageCount();
            int bookNowMessageCount = sqliteDataParser.getBookNowMessageCount();
            int totalOfferMessageCount = offerMessageCount + offersMessageCount;

            analysisCategoryDetails.setText(totalOfferMessageCount + " total offer messages\n" +
                    rechargeNowMessageCount + " total recharge now offers\n" +
                    rideNowMessageCount + " total ride now offers\n" +
                    bookNowMessageCount + " total book now offers");

        }
        else {

            Log.e(TAG, "Table " + table + " not found");

        }

        sqliteDataParser.close();

    }
}
