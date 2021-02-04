package com.mobatia.bskl.activity.notification.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobatia.bskl.R;

import java.util.ArrayList;

/**
 * Created by krishnaraj on 23/07/18.
 */

public class StudentUnReadRecyclerAdapter extends RecyclerView.Adapter<StudentUnReadRecyclerAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<String> mReadArraylist;
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


    public StudentUnReadRecyclerAdapter(Context mContext, ArrayList<String> mReadArraylist) {
        this.mContext = mContext;
        this.mReadArraylist = mReadArraylist;

    }

    @Override
    public StudentUnReadRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.student_image_name, parent, false);

        return new StudentUnReadRecyclerAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final StudentUnReadRecyclerAdapter.MyViewHolder holder, int position) {
//        holder.submenu.setText(mnNewsLetterModelArrayList.get(position).getSubmenu());
        if(mReadArraylist.get(position).equalsIgnoreCase(""))
        {
            holder.studName.setVisibility(View.GONE);
            holder.imageIcon.setVisibility(View.GONE);
        }
        else
        {
            holder.studName.setText(mReadArraylist.get(position));
            holder.imageIcon.setImageResource(R.drawable.student);
        }
    }


    @Override
    public int getItemCount() {
        return mReadArraylist.size();
    }

}
