package com.mobatia.bskl.fragment.timetable.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mobatia.bskl.R;
import com.mobatia.bskl.fragment.timetable.model.FieldModel;

import java.util.ArrayList;

/**
 * Created by krishnaraj on 27/08/18.
 */

public class TimeTablePopUpRecycleBreakrAdapter extends RecyclerView.Adapter<TimeTablePopUpRecycleBreakrAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<FieldModel> timeTableList;
    String dept;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView dateTimeTextView, nameTextView, subjectNameTextView, periodTextView;


        public MyViewHolder(View view) {
            super(view);

            dateTimeTextView = view.findViewById(R.id.dateTimeTextView);
            nameTextView = view.findViewById(R.id.nameTextView);
            subjectNameTextView = view.findViewById(R.id.subjectNameTextView);
            periodTextView = view.findViewById(R.id.periodTextView);


        }
    }


    public TimeTablePopUpRecycleBreakrAdapter(Context mContext, ArrayList<FieldModel> timeTableList) {
        this.mContext = mContext;
        this.timeTableList = timeTableList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_timetable_popup, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.dateTimeTextView.setText("Monday - Thursday|"+timeTableList.get(position).getStarttime()+"-"+timeTableList.get(position).getEndtime());
        holder.nameTextView.setText("Friday|"+timeTableList.get(position).getFridyaStartTime()+"-"+timeTableList.get(position).getFridayEndTime());
        holder.nameTextView.setVisibility(View.VISIBLE);
        holder.periodTextView.setText(timeTableList.get(position).getSortname());
        holder.subjectNameTextView.setText(timeTableList.get(position).getSortname());
        if (timeTableList.size()>1)
        {
            if (position!=(timeTableList.size()-1))
            {
                holder.subjectNameTextView.setCompoundDrawablesWithIntrinsicBounds(null,null,null,mContext.getResources().getDrawable(R.drawable.listdividewhite));

            }
            else
            {
                holder.subjectNameTextView.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);

            }
        }
        else
        {
            holder.subjectNameTextView.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
        }

    }


    @Override
    public int getItemCount() {
        return timeTableList.size();
    }
}

