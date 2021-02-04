package com.mobatia.bskl.activity.userdetail.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mobatia.bskl.R;
import com.mobatia.bskl.activity.userdetail.model.RelationshipModel;
import com.mobatia.bskl.activity.userdetail.model.StudentDetailModel;

import java.util.ArrayList;

/**
 * Created by krishnaraj on 23/07/18.
 */

public class StudentDetailRecyclerAdapter extends RecyclerView.Adapter<StudentDetailRecyclerAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<StudentDetailModel> mStudentDetailModel;
    private ArrayList<RelationshipModel> mRelationshipModel;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;
        TextView valueTextView;

        public MyViewHolder(View view) {
            super(view);
            nameTextView = view.findViewById(R.id.nameTextView);
            valueTextView = view.findViewById(R.id.valueTextView);


        }
    }


    public StudentDetailRecyclerAdapter(Context mContext, ArrayList<StudentDetailModel> youtubeArraylist) {
        this.mContext = mContext;
        this.mStudentDetailModel = youtubeArraylist;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_student_detail, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        System.out.println("working");
        holder.valueTextView.setText(mStudentDetailModel.get(position).getValue());
        holder.nameTextView.setText(mStudentDetailModel.get(position).getTitle());


    }


    @Override
    public int getItemCount() {
        return mStudentDetailModel.size();
    }

}
