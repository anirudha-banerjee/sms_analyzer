package com.godslayer69.android.sms_analyzer.fragments.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.godslayer69.android.sms_analyzer.R;

/**
 * Created by root on 5/20/17.
 * Project: ELE_SMS_ANIRUDHA
 */

public class MessageViewerListAdapter extends RecyclerView.Adapter<MessageViewerListAdapter.MessageViewHolder> {

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mMessageBodyText, mMessageDateText;

        public MessageViewHolder(View v) {
            super(v);

            mMessageBodyText = (TextView) v.findViewById(R.id.message_body_text);
            mMessageDateText = (TextView) v.findViewById(R.id.message_date_text);

        }
    }

    public MessageViewerListAdapter(){}

    @Override
    public MessageViewerListAdapter.MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_viewer_list_item,
                parent, false);

        return new MessageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MessageViewerListAdapter.MessageViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
