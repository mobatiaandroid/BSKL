package com.mobatia.bskl.activity.notification.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobatia.bskl.R;
import com.mobatia.bskl.fragment.messages.model.PushNotificationModel;

import java.util.ArrayList;

/**
 * Created by krishnaraj on 23/07/18.
 */

public class StudentRecyclerAdapter extends RecyclerView.Adapter<StudentRecyclerAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<PushNotificationModel> mReadArraylist;
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


    public StudentRecyclerAdapter(Context mContext, ArrayList<PushNotificationModel> mReadArraylist) {
        this.mContext = mContext;
        this.mReadArraylist = mReadArraylist;

    }

    @Override
    public StudentRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.student_image_name, parent, false);

        return new StudentRecyclerAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final StudentRecyclerAdapter.MyViewHolder holder, int position) {
        if(mReadArraylist.get(position).getStudent_name().equalsIgnoreCase(""))
        {
            holder.studName.setVisibility(View.GONE);
            holder.imageIcon.setVisibility(View.GONE);
        }
        else
        {
            holder.studName.setText(mReadArraylist.get(position).getStudent_name());
            holder.imageIcon.setImageResource(R.drawable.student);
        }



    }


    @Override
    public int getItemCount() {
        return mReadArraylist.size();
    }

}
