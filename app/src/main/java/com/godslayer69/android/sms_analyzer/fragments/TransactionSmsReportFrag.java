package com.godslayer69.android.sms_analyzer.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.godslayer69.android.sms_analyzer.MainActivity;
import com.godslayer69.android.sms_analyzer.R;
import com.godslayer69.android.sms_analyzer.databaseHelper.SmsSqliteHelper;
import com.godslayer69.android.sms_analyzer.smsDataAnalysis.SmsDataParser;

/**
 * Created by root on 5/16/17.
 * Project: ELE_SMS_ANIRUDHA
 */

public class TransactionSmsReportFrag extends Fragment implements View.OnClickListener {

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String TAG = "TransactionalSmsReport";

    private TextView senderNameText, messageCountText, messageTypeText, analysisDetails,
            analysisCategorydetails, openMessageViewerLink;
    private Button expandCollapseCardViewBtn;
    //public static PieChart messageAnalysisPie;
    private RelativeLayout analysisReportLayout;

    private SmsDataParser smsDataParser;

    public TransactionSmsReportFrag() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static TransactionSmsReportFrag newInstance(int sectionNumber) {
        TransactionSmsReportFrag fragment = new TransactionSmsReportFrag();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sms_category, container, false);

        init(getContext(), rootView);

        return rootView;
    }

    private void init(Context c, View v){

        senderNameText = (TextView) v.findViewById(R.id.operator_name_text);
        messageCountText = (TextView) v.findViewById(R.id.sms_count_text);
        messageTypeText = (TextView) v.findViewById(R.id.sms_type_text);
        analysisDetails = (TextView) v.findViewById(R.id.sms_analysis_text);
        analysisCategorydetails = (TextView) v.findViewById(R.id.sms_analysis_details_text);
        openMessageViewerLink = (TextView) v.findViewById(R.id.opensmslink);

        openMessageViewerLink.setOnClickListener(this);

        //messageAnalysisPie = (PieChart) v.findViewById(R.id.sms_analysis_pie);
        //messageAnalysisPie.setVisibility(View.GONE);

        analysisReportLayout = (RelativeLayout) v.findViewById(R.id.categorisedmessageanalysisframe);
        analysisReportLayout.setVisibility(View.GONE);

        expandCollapseCardViewBtn = (Button) v.findViewById(R.id.expand_cardview_btn);
        expandCollapseCardViewBtn.setOnClickListener(this);

        smsDataParser = new SmsDataParser(c);
        smsDataParser.getSmsAnalysisReport(SmsSqliteHelper.TABLE_TRANSACTIONAL, senderNameText,
                messageCountText, messageTypeText, analysisDetails, analysisCategorydetails);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.expand_cardview_btn){
            if (analysisReportLayout.getVisibility() == View.GONE) {
                // it's collapsed - expand it
                analysisReportLayout.setVisibility(View.VISIBLE);
                expandCollapseCardViewBtn.setBackground(getResources().getDrawable(R.mipmap.ic_expand_cardview_up));
            } else {
                // it's expanded - collapse it
                analysisReportLayout.setVisibility(View.GONE);
                expandCollapseCardViewBtn.setBackground(getResources().getDrawable(R.mipmap.ic_expand_cardview_down));
            }
        }
        else if (v.getId() == R.id.opensmslink){
            
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
                Toast.makeText(getContext(), "Not supported on your os version", Toast.LENGTH_SHORT).show();
            }
            else {
                Intent smsIntent = new Intent(Intent.ACTION_MAIN);
                smsIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                smsIntent.setClassName("com.android.mms", "com.android.mms.ui.SearchActivity");
                smsIntent.putExtra("intent_extra_data_key", "OLAMNY");
                startActivity(smsIntent);
            }

        }
    }
}
