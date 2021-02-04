package com.mobatia.bskl.activity.socialmedia.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mobatia.bskl.R;
import com.mobatia.bskl.fragment.socialmedia.model.YoutubeModel;
import com.mobatia.bskl.manager.AppUtils;

import java.util.ArrayList;

/**
 * Created by krishnaraj on 23/07/18.
 */

public class YoutubeRecyclerAdapter extends RecyclerView.Adapter<YoutubeRecyclerAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<YoutubeModel> mYoutubeArraylist;
    String dept;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView itemImage;
        TextView titleTextView;
        TextView dateTextView;

        public MyViewHolder(View view) {
            super(view);
            itemImage = view.findViewById(R.id.itemImage);
            titleTextView = view.findViewById(R.id.titleTextView);
            dateTextView = view.findViewById(R.id.dateTextView);


        }
    }


    public YoutubeRecyclerAdapter(Context mContext, ArrayList<YoutubeModel> youtubeArraylist) {
        this.mContext = mContext;
        this.mYoutubeArraylist = youtubeArraylist;

    }

    @Override
    public YoutubeRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_social_youtube_recycleritem, parent, false);

        return new YoutubeRecyclerAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final YoutubeRecyclerAdapter.MyViewHolder holder, int position) {

        holder.dateTextView.setText(mYoutubeArraylist.get(position).getPublishedAt());
        holder.titleTextView.setText(mYoutubeArraylist.get(position).getTitle());
        Glide.with(mContext).load(AppUtils.replace(mYoutubeArraylist.get(position).getThumbnailsHigh())).centerCrop().into( holder.itemImage);


    }


    @Override
    public int getItemCount() {
        return mYoutubeArraylist.size();
    }

}
