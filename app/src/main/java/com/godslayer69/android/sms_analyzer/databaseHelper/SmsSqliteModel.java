package com.godslayer69.android.sms_analyzer.databaseHelper;

/**
 * Created by root on 5/14/17.
 * Project: ELE_SMS_ANIRUDHA
 */

public class SmsSqliteModel {

    public long smsID;
    public String smsAddress;
    public String smsDateSent;
    public String smsBody;

    long getSmsID() {
        return smsID;
    }

    void setSmsID(long smsID) {
        this.smsID = smsID;
    }

    String getSmsAddress() {
        return smsAddress;
    }

    public void setSmsAddress(String smsAddress) {
        this.smsAddress = smsAddress;
    }

    String getSmsDateSent() {
        return smsDateSent;
    }

    public void setSmsDateSent(String smsDateSent) {
        this.smsDateSent = smsDateSent;
    }

    String getSmsBody() {
        return smsBody;
    }

    public void setSmsBody(String smsBody) {
        this.smsBody = smsBody;
    }

}
