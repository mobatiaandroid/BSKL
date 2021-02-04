package com.mobatia.bskl.activity.userdetail.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobatia.bskl.R;
import com.mobatia.bskl.activity.home.model.OwnDetailsModel;

import java.util.ArrayList;

/**
 * Created by krishnaraj on 23/07/18.
 */

public class RelationRecyclerAdapter extends RecyclerView.Adapter<RelationRecyclerAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<OwnDetailsModel> mRelationshipModel;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nameValueTextView;
        TextView emailValueTextView;
        TextView phoneValueTextView;
        LinearLayout namelayout;
        LinearLayout emaillayout;
        LinearLayout phonelayout;

        public MyViewHolder(View view) {
            super(view);
            nameValueTextView = view.findViewById(R.id.nameValueTextView);
            emailValueTextView = view.findViewById(R.id.emailValueTextView);
            phoneValueTextView = view.findViewById(R.id.phoneValueTextView);
            namelayout = view.findViewById(R.id.namelayout);
            emaillayout = view.findViewById(R.id.emaillayout);
            phonelayout = view.findViewById(R.id.phonelayout);


        }
    }

    public RelationRecyclerAdapter(Context mContext, ArrayList<OwnDetailsModel> youtubeArraylist) {
        this.mContext = mContext;
        this.mRelationshipModel = youtubeArraylist;

    }

    @Override
    public RelationRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.detail_layout, parent, false);

        return new RelationRecyclerAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RelationRecyclerAdapter.MyViewHolder holder, int position) {

        if (mRelationshipModel.get(position).getFullName().equalsIgnoreCase(""))
        {
            holder.namelayout.setVisibility(View.GONE);
        }
        else
        {
            holder.namelayout.setVisibility(View.VISIBLE);
            if (mRelationshipModel.get(position).getRelationShip().equalsIgnoreCase(""))
            {
                holder.nameValueTextView.setText(mRelationshipModel.get(position).getFullName());

            }
            else
            {
                holder.nameValueTextView.setText(mRelationshipModel.get(position).getFullName() +"( "+mRelationshipModel.get(position).getRelationShip()+" )");
            }

            if(mRelationshipModel.get(position).getFullName().equalsIgnoreCase("No Data"))
            {
                holder.nameValueTextView.setTextColor(mContext.getResources().getColor(R.color.red));
            }
            else
            {
                holder.nameValueTextView.setTextColor(mContext.getResources().getColor(R.color.black));
            }
        }
        if (mRelationshipModel.get(position).getEmail().equalsIgnoreCase(""))
        {
            holder.emaillayout.setVisibility(View.GONE);
        }
        else {
            holder.emaillayout.setVisibility(View.VISIBLE);
            holder.emailValueTextView.setText(mRelationshipModel.get(position).getEmail());
            if(mRelationshipModel.get(position).getEmail().equalsIgnoreCase("No Data"))
            {
                holder.emailValueTextView.setTextColor(mContext.getResources().getColor(R.color.red));
            }
            else
            {
                holder.emailValueTextView.setTextColor(mContext.getResources().getColor(R.color.black));
            }
        }

        if (mRelationshipModel.get(position).getContactNo().equalsIgnoreCase(""))
        {
            holder.phonelayout.setVisibility(View.GONE);
        }
        else {
            holder.phonelayout.setVisibility(View.VISIBLE);
            holder.phoneValueTextView.setText(mRelationshipModel.get(position).getContactNo());
            if(mRelationshipModel.get(position).getContactNo().equalsIgnoreCase("No Data"))
            {
                holder.phoneValueTextView.setTextColor(mContext.getResources().getColor(R.color.red));
            }
            else
            {
                holder.phoneValueTextView.setTextColor(mContext.getResources().getColor(R.color.black));
            }
        }

    }


    @Override
    public int getItemCount() {
        return mRelationshipModel.size();
    }

}
