package com.godslayer69.android.sms_analyzer;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.godslayer69.android.sms_analyzer.smsDataAnalysis.SmsDataParser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button startMsgAnalysis, viewAnalysisReport;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    private SmsDataParser smsDataParser;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startMsgAnalysis = findViewById(R.id.startSmsAnalysis);
        viewAnalysisReport = findViewById(R.id.viewSmsAnalysis);

        startMsgAnalysis.setOnClickListener(this);
        viewAnalysisReport.setOnClickListener(this);

        //Initialize sms data resolver
        smsDataParser = new SmsDataParser(this);
    }

    @Override
    public void onClick(View v) {

        //Check for button id
        if (v.getId() == R.id.startSmsAnalysis){
            //Check for permissions to read sms
            checkSmsReadPermission();
        }
        else if (v.getId() == R.id.viewSmsAnalysis){
            //Open report page
            //Switch to list view
            Intent intent = new Intent(MainActivity.this, SmsCategoryActivity.class);
            startActivity(intent);
        }
        else {
            Log.e(TAG, "View Id Didn't Match");
        }

    }

    private void checkSmsReadPermission(){
        int hasReadSmsPermission = ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_SMS);
        if (hasReadSmsPermission != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.READ_SMS)) {
                showMessageOKCancel("You need to allow this for the app to read messages",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[] {Manifest.permission.READ_SMS},
                                        REQUEST_CODE_ASK_PERMISSIONS);
                            }
                        });
                return;
            }
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[] {Manifest.permission.WRITE_CONTACTS},
                    REQUEST_CODE_ASK_PERMISSIONS);
            return;
        }
        readSmsInbox();
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    readSmsInbox();
                } else {
                    // Permission Denied
                    Toast.makeText(MainActivity.this, "READ_SMS Permission Denied", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void readSmsInbox(){
        //Start reading sms inbox
        smsDataParser.readSmsInbox();

        //Switch to list view
        Intent intent = new Intent(this, SmsCategoryActivity.class);
        startActivity(intent);

    }

}
