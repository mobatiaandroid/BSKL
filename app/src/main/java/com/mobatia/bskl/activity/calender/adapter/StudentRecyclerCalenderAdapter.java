package com.mobatia.bskl.activity.calender.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobatia.bskl.R;
import com.mobatia.bskl.fragment.calendar.model.StudentDetailModel;

import java.util.ArrayList;

/**
 * Created by krishnaraj on 26/07/18.
 */

public class StudentRecyclerCalenderAdapter extends RecyclerView.Adapter<StudentRecyclerCalenderAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<StudentDetailModel> mStudentDetail;
    String dept;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageIcon;
        TextView studName;
        public MyViewHolder(View view) {
            super(view);
            imageIcon = view.findViewById(R.id.imagicon);
            studName = view.findViewById(R.id.studName);



        }
    }


    public StudentRecyclerCalenderAdapter(Context mContext, ArrayList<StudentDetailModel> mStudentDetail) {
        this.mContext = mContext;
        this.mStudentDetail = mStudentDetail;

    }

    @Override
    public StudentRecyclerCalenderAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.student_image_name, parent, false);
        return new StudentRecyclerCalenderAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final StudentRecyclerCalenderAdapter.MyViewHolder holder, int position) {
        if(mStudentDetail.get(position).getStudentName().equalsIgnoreCase(""))
        {
            holder.studName.setVisibility(View.GONE);
            holder.imageIcon.setVisibility(View.GONE);
        }
        else
        {
            holder.studName.setText(mStudentDetail.get(position).getStudentName());
            holder.imageIcon.setImageResource(R.drawable.student);
        }



    }


    @Override
    public int getItemCount() {
        return mStudentDetail.size();
    }

}
