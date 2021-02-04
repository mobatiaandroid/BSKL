package com.mobatia.bskl.activity.userdetail.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mobatia.bskl.R;
import com.mobatia.bskl.activity.userdetail.model.RelationshipModel;
import com.mobatia.bskl.activity.userdetail.model.StudentDetailModel;
import com.mobatia.bskl.fragment.settings.model.PassportDetailModel;

import java.util.ArrayList;

/**
 * Created by krishnaraj on 23/07/18.
 */

public class PassportDetailRecyclerAdapter extends RecyclerView.Adapter<PassportDetailRecyclerAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<PassportDetailModel> mStudentDetailModel;
    private ArrayList<RelationshipModel> mRelationshipModel;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;
        TextView valueTextView;
        LinearLayout namelayout;

        public MyViewHolder(View view) {
            super(view);
            nameTextView = view.findViewById(R.id.nameTextView);
            valueTextView = view.findViewById(R.id.valueTextView);
            namelayout = view.findViewById(R.id.namelayout);


        }
    }


    public PassportDetailRecyclerAdapter(Context mContext, ArrayList<PassportDetailModel> youtubeArraylist) {
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
        System.out.println("working passport");
        if(mStudentDetailModel.get(position).getValue().equalsIgnoreCase("No Data") ||mStudentDetailModel.get(position).getValue().equalsIgnoreCase("No data") ||mStudentDetailModel.get(position).getValue().equalsIgnoreCase("Nodata"))
        {
            holder.valueTextView.setTextColor(mContext.getResources().getColor(R.color.red));
        }
        else
        {
            holder.valueTextView.setTextColor(mContext.getResources().getColor(R.color.black));
        }
        System.out.println("Expired Value"+mStudentDetailModel.get(position).getIs_expired());
        if (mStudentDetailModel.get(position).getIs_expired().equalsIgnoreCase("1"))
        {
            if(mStudentDetailModel.get(position).getTitle().equalsIgnoreCase("Expiry Date"))
            {
                holder.valueTextView.setTextColor(mContext.getResources().getColor(R.color.red));
            }

        }
        else
        {
            if(mStudentDetailModel.get(position).getTitle().equalsIgnoreCase("Expiry Date"))
            {
                if(mStudentDetailModel.get(position).getValue().equalsIgnoreCase("No Data") ||mStudentDetailModel.get(position).getValue().equalsIgnoreCase("No data") ||mStudentDetailModel.get(position).getValue().equalsIgnoreCase("Nodata"))
                {
                    holder.valueTextView.setTextColor(mContext.getResources().getColor(R.color.red));
                }
            }


        }

        if(mStudentDetailModel.get(position).getTitle().equalsIgnoreCase("passport_expired") || mStudentDetailModel.get(position).getTitle().equalsIgnoreCase("visa_expired"))
        {
            holder.valueTextView.setVisibility(View.GONE);
            holder.nameTextView.setVisibility(View.GONE);
            holder.namelayout.setVisibility(View.GONE);
        }
        else
        {
            holder.valueTextView.setVisibility(View.VISIBLE);
            holder.nameTextView.setVisibility(View.VISIBLE);
            holder.namelayout.setVisibility(View.VISIBLE);
        }
        holder.valueTextView.setText(mStudentDetailModel.get(position).getValue());
        holder.nameTextView.setText(mStudentDetailModel.get(position).getTitle());


    }


    @Override
    public int getItemCount() {
        return mStudentDetailModel.size();
    }

}
