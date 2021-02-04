package com.mobatia.bskl.fragment.settings.adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobatia.bskl.R;
import com.mobatia.bskl.fragment.settings.model.TriggerDataModel;

import java.util.ArrayList;

public class TriggerRecyclerAdapter extends RecyclerView.Adapter<TriggerRecyclerAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<TriggerDataModel>mTriggerModelArrayList;
    String dept;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView categoryTypeTxt;
        ImageView checkBoxImg;


        public MyViewHolder(View view) {
            super(view);
            categoryTypeTxt = view.findViewById(R.id.categoryTypeTxt);
            checkBoxImg = view.findViewById(R.id.checkBoxImg);




        }
    }


    public TriggerRecyclerAdapter(Context mContext, ArrayList<TriggerDataModel>mTriggerModelArrayList) {
        this.mContext = mContext;
        this.mTriggerModelArrayList = mTriggerModelArrayList;

    }

    @Override
    public TriggerRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_trigger_check_recycler, parent, false);

        return new TriggerRecyclerAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final TriggerRecyclerAdapter.MyViewHolder holder, final int position)
    {

        holder.categoryTypeTxt.setText(mTriggerModelArrayList.get(position).getCategoryName());
        if (mTriggerModelArrayList.get(position).isCheckedCategory())
        {
            holder.checkBoxImg.setImageResource(R.drawable.check_box_header_tick);
        }
        else
        {
            holder.checkBoxImg.setImageResource(R.drawable.check_box_header);
        }



    }


    @Override
    public int getItemCount() {
        return mTriggerModelArrayList.size();
    }

}