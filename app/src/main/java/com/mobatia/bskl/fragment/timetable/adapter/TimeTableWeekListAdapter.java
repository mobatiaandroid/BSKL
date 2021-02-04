package com.mobatia.bskl.fragment.timetable.adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobatia.bskl.R;
import com.mobatia.bskl.fragment.timetable.model.WeekModel;

import java.util.ArrayList;

public class TimeTableWeekListAdapter  extends RecyclerView.Adapter<TimeTableWeekListAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<WeekModel> mWeekList;
    int weekPosition=0;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView weekTxt;
        ImageView lineImage;
        ImageView downArrowImage;

        public MyViewHolder(View view) {
            super(view);

            weekTxt = (TextView) view.findViewById(R.id.weekTxt);
            lineImage = (ImageView) view.findViewById(R.id.lineImage);
            downArrowImage = (ImageView) view.findViewById(R.id.downArrowImage);


        }
    }


    public TimeTableWeekListAdapter(Context mContext, ArrayList<WeekModel> mWeekList, int weekPosition) {
        this.mContext = mContext;
        this.mWeekList = mWeekList;
        this.weekPosition=weekPosition;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_weeklist_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        holder.weekTxt.setText(mWeekList.get(position).getWeekName());
        if (mWeekList.get(position).getPositionSelected()!=-1)
        {
            holder.weekTxt.setTextColor(mContext.getResources().getColor(R.color.timetableblue));
            holder.lineImage.setVisibility(View.VISIBLE);
            holder.downArrowImage.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.weekTxt.setTextColor(mContext.getResources().getColor(R.color.dark_grey1));
            holder.lineImage.setVisibility(View.INVISIBLE);
            holder.downArrowImage.setVisibility(View.INVISIBLE);

        }



    }


    @Override
    public int getItemCount() {
        return mWeekList.size();
    }
}
