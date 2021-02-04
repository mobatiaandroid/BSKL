package com.mobatia.bskl.activity.userdetail.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobatia.bskl.R;
import com.mobatia.bskl.activity.home.model.KinDetailsModel;

import java.util.ArrayList;

/**
 * Created by krishnaraj on 23/07/18.
 */

public class NextOfKinRecyclerAdapter extends RecyclerView.Adapter<NextOfKinRecyclerAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<KinDetailsModel> mRelationshipModel;


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


    public NextOfKinRecyclerAdapter(Context mContext, ArrayList<KinDetailsModel> youtubeArraylist) {
        this.mContext = mContext;
        this.mRelationshipModel = youtubeArraylist;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.detail_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        if (mRelationshipModel.get(position).getFullName().equalsIgnoreCase(""))
        {
            holder.namelayout.setVisibility(View.VISIBLE);
            holder.nameValueTextView.setText("No Data");
            holder.emailValueTextView.setTextColor(mContext.getResources().getColor(R.color.red));
        }
        else
        {
            holder.namelayout.setVisibility(View.VISIBLE);
            holder.nameValueTextView.setTextColor(mContext.getResources().getColor(R.color.black));
            if (mRelationshipModel.get(position).getRelationShip().equalsIgnoreCase(""))
            {
                holder.nameValueTextView.setText(mRelationshipModel.get(position).getFullName());

            }
            else
            {
                holder.nameValueTextView.setText(mRelationshipModel.get(position).getFullName() +"( "+mRelationshipModel.get(position).getRelationShip()+" )");
            }
        }
        if (mRelationshipModel.get(position).getEmial().equalsIgnoreCase(""))
        {
            holder.emaillayout.setVisibility(View.VISIBLE);
            holder.emailValueTextView.setText("No Data");
            holder.emailValueTextView.setTextColor(mContext.getResources().getColor(R.color.red));
        }
        else {
            holder.emaillayout.setVisibility(View.VISIBLE);
            holder.emailValueTextView.setText(mRelationshipModel.get(position).getEmial());
            holder.emailValueTextView.setTextColor(mContext.getResources().getColor(R.color.black));
        }

        if (mRelationshipModel.get(position).getContactNumber().equalsIgnoreCase(""))
        {
            holder.phonelayout.setVisibility(View.VISIBLE);
            holder.phoneValueTextView.setText("No Data");
            holder.phoneValueTextView.setTextColor(mContext.getResources().getColor(R.color.red));
        }
        else {
            holder.phonelayout.setVisibility(View.VISIBLE);
            holder.phoneValueTextView.setTextColor(mContext.getResources().getColor(R.color.black));
            holder.phoneValueTextView.setText(mRelationshipModel.get(position).getContactNumber());
        }



    }


    @Override
    public int getItemCount() {
        return mRelationshipModel.size();
    }

}
