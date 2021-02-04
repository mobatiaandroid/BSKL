package com.mobatia.bskl.fragment.messages.adapter;

import android.app.Dialog;
import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobatia.bskl.R;
import com.mobatia.bskl.constants.CacheDIRConstants;
import com.mobatia.bskl.constants.IntentPassValueConstants;
import com.mobatia.bskl.constants.JSONConstants;
import com.mobatia.bskl.constants.URLConstants;
import com.mobatia.bskl.fragment.messages.model.PushNotificationModel;

import java.util.ArrayList;

/**
 * Created by krishnaraj on 10/07/18.
 */

public class PushNotificationListAdapter extends RecyclerView.Adapter<PushNotificationListAdapter.MyViewHolder> implements CacheDIRConstants, IntentPassValueConstants,JSONConstants,URLConstants {

    private Context mContext;
    private ArrayList<PushNotificationModel> mMessageList;
    String read;
    int pos;
    Dialog dialog;
    CheckBox messageSelect;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView messageCntnt;
        LinearLayout deptLayout;
        View separator;

        public MyViewHolder(View view) {
            super(view);

            messageCntnt = view.findViewById(R.id.messageCntnt);
            deptLayout= view.findViewById(R.id.deptLayout);
            separator= view.findViewById(R.id.separator);
            messageSelect= view.findViewById(R.id.messageSelect);


        }
    }
    public PushNotificationListAdapter(Context mContext,ArrayList<PushNotificationModel> mMessageList, String read) {
        this.mContext = mContext;
        this.mMessageList = mMessageList;
        this.read=read;
    }
    @Override
    public PushNotificationListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(PushNotificationListAdapter.MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
