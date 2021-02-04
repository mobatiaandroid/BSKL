package com.mobatia.bskl.fragment.report.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobatia.bskl.R;
import com.mobatia.bskl.fragment.report.model.StudentModel;
import com.mobatia.bskl.manager.AppUtils;
import com.mobatia.bskl.manager.PreferenceManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by krishnaraj on 18/07/18.
 */

public class StudentSpinnerAdapter extends RecyclerView.Adapter<StudentSpinnerAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<StudentModel> mStudentList;
    String dept;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mTitleTxt,listTxtClass;
        ImageView imgIcon;
        RelativeLayout relSub;

        public MyViewHolder(View view) {
            super(view);

            mTitleTxt= view.findViewById(R.id.listTxtTitle);
            listTxtClass= view.findViewById(R.id.listTxtClass);
            imgIcon= view.findViewById(R.id.imagicon);
            relSub= view.findViewById(R.id.relSub);
        }
    }


    public StudentSpinnerAdapter(Context mContext,ArrayList<StudentModel> mStudentList) {
        this.mContext = mContext;
        this.mStudentList = mStudentList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_student_list_adapter, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.mTitleTxt.setText(mStudentList.get(position).getmName());
        holder.imgIcon.setVisibility(View.VISIBLE);
        if(PreferenceManager.getTimeTableGroup(mContext).equalsIgnoreCase("1"))
        {
            if (mStudentList.get(position).getType().equalsIgnoreCase("primary"))
            {

            }
            else
            {
                holder.relSub.setAlpha(0.2f);
            }
        }
        else if(PreferenceManager.getTimeTableGroup(mContext).equalsIgnoreCase("2"))
        {
            if (mStudentList.get(position).getType().equalsIgnoreCase("secondary"))
            {

            }
            else
            {
                holder.relSub.setAlpha(0.2f);
            }
        }
        else if(PreferenceManager.getTimeTableGroup(mContext).equalsIgnoreCase("3"))
        {
            if (mStudentList.get(position).getType().equalsIgnoreCase("secondary")||mStudentList.get(position).getType().equalsIgnoreCase("primary") )
            {

            }
            else
            {
                holder.relSub.setAlpha(0.5f);
            }
        }
        if(mStudentList.get(position).getAlumini().equalsIgnoreCase("1"))
        {
            holder.listTxtClass.setTextColor(mContext.getResources().getColor(R.color.grey));
            holder.listTxtClass.setText("Alumini");
        }
        else
        {
            holder.listTxtClass.setText(mStudentList.get(position).getmClass());

        }
        if (!mStudentList.get(position).getmPhoto().equals("")) {

            Picasso.with(mContext).load(AppUtils.replace(mStudentList.get(position).getmPhoto().toString())).placeholder(R.drawable.boy).fit().into(holder.imgIcon);
        }
        else

        {

            holder.imgIcon.setImageResource(R.drawable.boy);
        }
    }


    @Override
    public int getItemCount() {
        return mStudentList.size();
    }
}
