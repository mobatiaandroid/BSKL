package com.mobatia.bskl.fragment.notification.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobatia.bskl.R;
import com.mobatia.bskl.activity.notification.AudioPlayerViewActivity;
import com.mobatia.bskl.activity.notification.ImageActivity;
import com.mobatia.bskl.activity.notification.VideoWebViewActivity;
import com.mobatia.bskl.appcontroller.AppController;
import com.mobatia.bskl.constants.ClickListener;
import com.mobatia.bskl.constants.IntentPassValueConstants;
import com.mobatia.bskl.constants.JSONConstants;
import com.mobatia.bskl.constants.StatusConstants;
import com.mobatia.bskl.constants.URLConstants;
import com.mobatia.bskl.fragment.messages.model.PushNotificationModel;
import com.mobatia.bskl.fragment.notification.NotificationFragmentPagination;
import com.mobatia.bskl.recyclerviewmanager.OnBottomReachedListener;

import java.lang.ref.WeakReference;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by krishnaraj on 12/07/18.
 */

public class ReadMessageAdapter extends RecyclerView.Adapter<ReadMessageAdapter.MyViewHolder> implements IntentPassValueConstants,URLConstants,JSONConstants,StatusConstants {
    TextView markUnRead;
    ImageView checkBoxRead;
    TextView markRead;
    TextView msgReadSelect;
    ImageView msgBoxRead;
    TextView viewFirst;
    TextView viewTxt;
    private Context mContext;
    private ArrayList<PushNotificationModel> mMessageUnReadList;
    OnBottomReachedListener onBottomReachedListener;

    private ClickListener listener;
    CardView cardView;
    private WeakReference<ClickListener> listenerRefRead;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

        TextView readMessage;
        TextView readTxt;
        ImageView msgBoxRead;
        ImageView fav;
        TextView viewFirst;
        TextView viewTxt;
        LinearLayout starLinearR;
        RelativeLayout llReadClick;
        public MyViewHolder(View view) {
            super(view);
            listenerRefRead = new WeakReference<>(listener);
            readMessage = view.findViewById(R.id.readMessage);
            readTxt = view.findViewById(R.id.readTxt);
            msgBoxRead = view.findViewById(R.id.msgBoxRead);
            cardView= view.findViewById(R.id.card_view);
            llReadClick= view.findViewById(R.id.llread);
            viewFirst = view.findViewById(R.id.viewFirst);
            viewTxt = view.findViewById(R.id.viewTxt);
            fav = view.findViewById(R.id.starR);
            starLinearR= view.findViewById(R.id.starLinearR);
            starLinearR.setOnClickListener(this);
            msgBoxRead.setOnClickListener(this);
            llReadClick.setOnLongClickListener(this);
            llReadClick.setOnClickListener(this);
            cardView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            if (v.getId() == starLinearR.getId() ) {
                if (AppController.mMessageReadList.get(getAdapterPosition()).getFavourite().equalsIgnoreCase("0"))

                {
                    fav.setBackgroundResource(R.drawable.star_yellow);
                    AppController.mMessageReadList.get(getAdapterPosition()).setFavourite("1");
                    NotificationFragmentPagination.callfavouriteStatusApi("1",AppController.mMessageReadList.get(getAdapterPosition()).getPushid());
                }
                else
                {
                    fav.setBackgroundResource(R.drawable.star_gray);
                    AppController.mMessageReadList.get(getAdapterPosition()).setFavourite("0");
                    NotificationFragmentPagination.callfavouriteStatusApi("0",AppController.mMessageReadList.get(getAdapterPosition()).getPushid());
                }

            }
            else   if (v.getId() == msgBoxRead.getId() ){

                listenerRefRead.get().onPositionClicked(getAdapterPosition());

            }




        }

        @Override
        public boolean onLongClick(View v) {
            if(v==llReadClick)
            {

                listenerRefRead.get().onLongClicked(getAdapterPosition());
            }
            return false;
        }
    }
    public void setOnBottomReachedListener(OnBottomReachedListener onBottomReachedListener){

        this.onBottomReachedListener = onBottomReachedListener;
    }
    public ReadMessageAdapter(Context mContext, ArrayList<PushNotificationModel> mMessageReadList, final ImageView checkBoxRead, final TextView markUnRead, final TextView markRead, final TextView msgReadSelect, ClickListener listener) {
        this.mContext = mContext;
        this.markUnRead=markUnRead;
        this.checkBoxRead=checkBoxRead;
        this.markRead=markRead;
        this.msgReadSelect=msgReadSelect;
        this.listener=listener;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_read_adapter, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        onBottomReachedListener.onBottomReached(position);
        holder.readMessage.setText(AppController.mMessageReadList.get(position).getDay());

        holder.readTxt.setText(AppController.mMessageReadList.get(position).getTitle());
        if (AppController.isVisibleReadBox) {
            holder.msgBoxRead.setVisibility(View.VISIBLE);
            holder.viewFirst.setVisibility(View.GONE);
            holder.viewTxt.setVisibility(View.VISIBLE);

        } else {
            holder.msgBoxRead.setVisibility(View.INVISIBLE);
            holder.viewFirst.setVisibility(View.VISIBLE);
            holder.viewTxt.setVisibility(View.GONE);

        }

        holder.msgBoxRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppController.isSelectedRead=false;
                if(AppController.mMessageReadList.get(position).isMarked())
                {
                    holder.msgBoxRead.setBackgroundResource(R.drawable.check_box_white_2);
                    AppController.mMessageReadList.get(position).setMarked(false);
                    AppController.click_count_read=0;
                }
                else
                {
                    holder.msgBoxRead.setBackgroundResource(R.drawable.check_box_white_tick_2);
                    AppController.mMessageReadList.get(position).setMarked(true);
                    AppController.click_count_read=1;


                }

                int marked_count = 0;
                for (int i = 0; i < AppController.mMessageReadList.size(); i++) {
                    if (AppController.mMessageReadList.get(i).isMarked()) {

                        marked_count++;
                    }
                }
                if (marked_count == AppController.mMessageReadList.size()) {
                    checkBoxRead.setVisibility(View.VISIBLE);
                    msgReadSelect.setVisibility(View.VISIBLE);
                    checkBoxRead.setBackgroundResource(R.drawable.check_box_header_tick);

                } else if (marked_count==0){
                    checkBoxRead.setVisibility(View.GONE);
                    msgReadSelect.setVisibility(View.GONE);
                    AppController.isVisibleReadBox=false;
                    markUnRead.setVisibility(View.GONE);
                    AppController.click_count_read=0;
                notifyDataSetChanged();
                    //notifyItemRangeChanged(0, getItemCount());

                }
                else if (marked_count==1){

                    AppController.click_count_read=0;
                    notifyDataSetChanged();
                   // notifyItemRangeChanged(0, getItemCount());


                }
                else
                {
                   //AppController.click_count_read++;
                    markUnRead.setVisibility(View.VISIBLE);
                    checkBoxRead.setBackgroundResource(R.drawable.check_box_header);


                }
                if (marked_count>=1)
                {
                    AppController.click_count_read=0;
                    markUnRead.setVisibility(View.VISIBLE);

                }
            }
        });
        if(AppController.mMessageReadList.get(position).getFavourite().equalsIgnoreCase("1"))
        {
            holder.fav.setBackgroundResource(R.drawable.star_yellow);
        }
        else
        {
            holder.fav.setBackgroundResource(R.drawable.star_gray);

        }


        if (AppController.mMessageReadList.get(position).isMarked())
        {
            holder.msgBoxRead.setBackgroundResource(R.drawable.check_box_white_tick_2);

        }
        else
        {
            holder.msgBoxRead.setBackgroundResource(R.drawable.check_box_white_2);

        }

        holder.llReadClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent mIntent;
                if (AppController.mMessageReadList.get(position).getType().equalsIgnoreCase("image")|| AppController.mMessageReadList.get(position).getType().equalsIgnoreCase("text")||AppController.mMessageReadList.get(position).getType().equalsIgnoreCase(""))
                {
                    String pushNotificationDetail= "<!DOCTYPE html>\n" +
                            "<html>\n" +
                            "<head>\n" +
                            "<style>\n" +
                            "\n" +
                            "@font-face {\n" +
                            "font-family: SourceSansPro-Semibold;"+
                            "src: url(SourceSansPro-Semibold.ttf);"+

                            "font-family: SourceSansPro-Regular;"+
                            "src: url(SourceSansPro-Regular.ttf);"+
                            "}"+
                            ".title {"+
                            "font-family: SourceSansPro-Regular;"+
                            "font-size:16px;"+
                            "text-align:left;"+
                            "color:	#46C1D0;"+
                            "}"+
                            ".description {"+
                            "font-family: SourceSansPro-Semibold;"+
                            "text-align:justify;"+
                            "font-size:14px;"+
                            "color: #000000;"+
                            "}"+".date {"+
                            "font-family: SourceSansPro-Semibold;"+
                            "text-align:right;"+
                            "font-size:12px;"+

                            "color: #A9A9A9;"+
                            "}"+
                            "</style>\n"+"</head>"+
                            "<body>";
                    if (!AppController.mMessageReadList.get(position).getUrl().equalsIgnoreCase("")) {
                        pushNotificationDetail = pushNotificationDetail + "<center><img src='" + AppController.mMessageReadList.get(position).getUrl() + "'width='100%', height='auto'>";
                    }
                    DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    DateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy hh:mm a");
                    String mdate =AppController.mMessageReadList.get(position).getDate();
                    Date date = null;
                    try {
                        date = inputFormat.parse(mdate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    String outputDateStr = outputFormat.format(date);
                    pushNotificationDetail=pushNotificationDetail+
                            "<p class='description'>"+AppController.mMessageReadList.get(position).getMessage();
                    pushNotificationDetail=pushNotificationDetail+"<p class='date'>"+/*AppController.mMessageReadList.get(position).getDate()*/outputDateStr +"</p>"+

                    "</body>\n</html>";/*+"<p class='description'>"+pushNotificationArrayList.get(position).getDay() + "-" + pushNotificationArrayList.get(position).getMonthString() + "-" + pushNotificationArrayList.get(position).getYear()+" "+pushNotificationArrayList.get(position).getPushTime()+"</p>"+*/
                    mIntent = new Intent(mContext, ImageActivity.class);
                    mIntent.putExtra("webViewComingDetail", pushNotificationDetail);
                    mIntent.putExtra("title", AppController.mMessageReadList.get(position).getTitle());
                    mIntent.putExtra("date", AppController.mMessageReadList.get(position).getDate());
                    mIntent.putExtra("isfromUnread", false);
                    mIntent.putExtra("isfromRead", true);

                    mIntent.putExtra(POSITION, position);
                    mIntent.putExtra(PASS_ARRAY_LIST, AppController.mMessageReadList);

                    mContext.startActivity(mIntent);

                }
                if (AppController.mMessageReadList.get(position).getType().equalsIgnoreCase("audio")) {
                    mIntent = new Intent(mContext, AudioPlayerViewActivity.class);
                    mIntent.putExtra("title", AppController.mMessageReadList.get(position).getTitle());
                    mIntent.putExtra("date", AppController.mMessageReadList.get(position).getDate());
                    mIntent.putExtra(POSITION, position);
                    mIntent.putExtra("isfromUnread", false);
                    mIntent.putExtra("isfromRead", true);

                    mIntent.putExtra(PASS_ARRAY_LIST, AppController.mMessageReadList);
                    mContext.startActivity(mIntent);
                }
                if (AppController.mMessageReadList.get(position).getType().equalsIgnoreCase("video")) {
                    mIntent = new Intent(mContext, VideoWebViewActivity.class);
                    mIntent.putExtra(POSITION, position);
                    mIntent.putExtra("title", AppController.mMessageReadList.get(position).getTitle());
                    mIntent.putExtra("date", AppController.mMessageReadList.get(position).getDate());
                    mIntent.putExtra("isfromUnread", false);
                    mIntent.putExtra("isfromRead", true);

                    mIntent.putExtra(PASS_ARRAY_LIST, AppController.mMessageReadList);
                    mContext.startActivity(mIntent);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return AppController.mMessageReadList.size();
    }

}
