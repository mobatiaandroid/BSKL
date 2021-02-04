package com.mobatia.bskl.fragment.timetable.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mobatia.bskl.R;
import com.mobatia.bskl.fragment.timetable.model.DayModel;
import com.mobatia.bskl.manager.AppUtils;

import java.util.ArrayList;

/**
 * Created by krishnaraj on 27/08/18.
 */

public class TimeTablePopUpRecyclerAdapter extends RecyclerView.Adapter<TimeTablePopUpRecyclerAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<DayModel> timeTableList;
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


    public TimeTablePopUpRecyclerAdapter(Context mContext, ArrayList<DayModel> timeTableList) {
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
        holder.dateTimeTextView.setText(timeTableList.get(position).getDay()+" | "+AppUtils.timeParsingToAmPm(timeTableList.get(position).getStarttime())+" - "+AppUtils.timeParsingToAmPm(timeTableList.get(position).getEndtime()));
        if (timeTableList.get(position).getStaff().equalsIgnoreCase(""))
        {
            holder.nameTextView.setVisibility(View.GONE);

        }
        else
        {
            holder.nameTextView.setText(timeTableList.get(position).getStaff());
            holder.nameTextView.setVisibility(View.VISIBLE);
        }
        if (position==0)
        {
            holder.periodTextView.setText(timeTableList.get(position).getSortname());
        }
        else
        {
            holder.periodTextView.setText("");
        }
        holder.subjectNameTextView.setText(timeTableList.get(position).getSubject_name());
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

