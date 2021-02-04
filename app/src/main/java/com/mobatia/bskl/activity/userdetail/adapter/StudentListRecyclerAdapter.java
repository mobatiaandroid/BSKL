package com.mobatia.bskl.activity.userdetail.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobatia.bskl.R;
import com.mobatia.bskl.activity.userdetail.model.StudentUserModel;
import com.mobatia.bskl.manager.AppUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by krishnaraj on 05/09/18.
 */

public class StudentListRecyclerAdapter extends RecyclerView.Adapter<StudentListRecyclerAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<StudentUserModel> mStudentList;
    String dept;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView studentName, studentAlumini;
        ImageView imgIcon;
        ImageView arrowImg;
        View seperator;
        View seperatorBottom;

        public MyViewHolder(View view) {
            super(view);

            studentName = view.findViewById(R.id.studentName);
            studentAlumini = view.findViewById(R.id.studentAlumini);
            imgIcon = view.findViewById(R.id.iconImg);
            arrowImg = view.findViewById(R.id.arrowImg);
            seperator = view.findViewById(R.id.seperator);
            seperatorBottom = view.findViewById(R.id.seperatorBottom);
        }
    }


    public StudentListRecyclerAdapter(Context mContext, ArrayList<StudentUserModel> mStudentList) {
        this.mContext = mContext;
        this.mStudentList = mStudentList;
    }

    @Override
    public StudentListRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.student_list_adapter, parent, false);

        return new StudentListRecyclerAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final StudentListRecyclerAdapter.MyViewHolder holder, int position) {
        if (mStudentList.get(position).getAlumini().equalsIgnoreCase("0")) {
            holder.studentAlumini.setVisibility(View.VISIBLE);
            holder.studentAlumini.setText(mStudentList.get(position).getClassAndSection());
            holder.imgIcon.setVisibility(View.VISIBLE);
            holder.arrowImg.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.studentAlumini.setVisibility(View.VISIBLE);
            holder.studentAlumini.setText("(Alumni)");
            holder.imgIcon.setVisibility(View.VISIBLE);
            holder.arrowImg.setVisibility(View.GONE);

        }

//            holder.studentName.setText(mStudentList.get(position).getStudName());
//            holder.imgIcon.setVisibility(View.VISIBLE);
//            // holder.listTxtClass.setText(mStudentList.get(position).getmClass());
//            if (!mStudentList.get(position).getPhoto().equals("")) {
//
//                Picasso.with(mContext).load(AppUtils.replace(mStudentList.get(position).getPhoto().toString())).placeholder(R.drawable.boy).fit().into(holder.imgIcon);
//            } else
//
//            {
//
//                holder.imgIcon.setImageResource(R.drawable.boy);
//            }
//        }
        holder.studentName.setText(mStudentList.get(position).getStudName());
        holder.imgIcon.setVisibility(View.VISIBLE);
        // holder.listTxtClass.setText(mStudentList.get(position).getmClass());
        if (!mStudentList.get(position).getPhoto().equals("")) {

            Picasso.with(mContext).load(AppUtils.replace(mStudentList.get(position).getPhoto().toString())).placeholder(R.drawable.boy).fit().into(holder.imgIcon);
        } else

        {

            holder.imgIcon.setImageResource(R.drawable.boy);
        }
        holder.seperator.setVisibility(View.GONE);

//        if (mStudentList.size()==1)
//        {
//            holder.seperator.setVisibility(View.GONE);
//
//        }
//        else {
//            if (position == 0) {
//                holder.seperator.setVisibility(View.VISIBLE);
//            } else {
//                holder.seperator.setVisibility(View.GONE);
//
//            }
//        }

    }

    @Override
    public int getItemCount() {
        return mStudentList.size();
    }
}
