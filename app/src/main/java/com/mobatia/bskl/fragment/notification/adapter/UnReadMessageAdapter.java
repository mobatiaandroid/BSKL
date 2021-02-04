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

public class UnReadMessageAdapter extends RecyclerView.Adapter<UnReadMessageAdapter.MyViewHolder> implements IntentPassValueConstants, JSONConstants, StatusConstants, URLConstants {

    private Context mContext;
    private ArrayList<PushNotificationModel> mMessageUnreadList;
    TextView markRead;
    ImageView mCheckbox;
    ImageView msgBoxRead;
    TextView msgUnread;
    TextView markUnRead;
    RecyclerView msgUnReadList;
    RelativeLayout unreadClick;
    LinearLayout readLinear;
    private ClickListener listener;
    private WeakReference<ClickListener> listenerRef;
    OnBottomReachedListener onBottomReachedListener;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        TextView unreadMessage;
        TextView unreadTxt;
        ImageView msgBoxUnRead;
        ImageView fav;
        LinearLayout starLinear;
        CardView cardView;
        TextView viewFirst;
        TextView viewTxt;
        RelativeLayout unreadClick;

        public MyViewHolder(View view) {
            super(view);
            listenerRef = new WeakReference<>(listener);
            unreadMessage = view.findViewById(R.id.unreadMessage);
            unreadTxt = view.findViewById(R.id.unreadTxt);
            msgBoxUnRead = view.findViewById(R.id.msgBoxUnRead);
            viewFirst = view.findViewById(R.id.viewFirst);
            viewTxt = view.findViewById(R.id.viewTxt);
            fav = view.findViewById(R.id.starUR);
            cardView = view.findViewById(R.id.card_view);
            starLinear = view.findViewById(R.id.starLinear);
            unreadClick = view.findViewById(R.id.unreadClick);
            starLinear.setOnClickListener(this);
            msgBoxUnRead.setOnClickListener(this);
            unreadClick.setOnLongClickListener(this);
            unreadClick.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == starLinear.getId())
            {
                fav.setBackgroundResource(R.drawable.star_yellow);

                if (AppController.mMessageUnreadList.get(getAdapterPosition()).getFavourite().equalsIgnoreCase("0"))

                {
                    fav.setBackgroundResource(R.drawable.star_yellow);
                    AppController.mMessageUnreadList.get(getAdapterPosition()).setFavourite("1");
                    NotificationFragmentPagination.callfavouriteStatusApi("1", AppController.mMessageUnreadList.get(getAdapterPosition()).getPushid());
                } else {
                    fav.setBackgroundResource(R.drawable.star_gray);
                    AppController.mMessageUnreadList.get(getAdapterPosition()).setFavourite("0");
                    NotificationFragmentPagination.callfavouriteStatusApi("0", AppController.mMessageUnreadList.get(getAdapterPosition()).getPushid());

                }
            } else if (v.getId() == msgBoxUnRead.getId()) {
                if (AppController.mMessageUnreadList.get(getAdapterPosition()).getChecked()) {
                    AppController.mMessageUnreadList.get(getAdapterPosition()).setChecked(false);

                } else {
                    AppController.mMessageUnreadList.get(getAdapterPosition()).setChecked(true);

                }
                int marked_count = 0;
                for (int i = 0; i < AppController.mMessageUnreadList.size(); i++) {
                    if (AppController.mMessageUnreadList.get(i).getChecked()) {

                        marked_count++;
                    }
                }


            }
            listenerRef.get().onPositionClicked(getAdapterPosition());

        }

        @Override
        public boolean onLongClick(View v) {
            if (v == unreadClick) {
                listenerRef.get().onLongClicked(getAdapterPosition());
            }

            return false;
        }
    }
    public void setOnBottomReachedListener(OnBottomReachedListener onBottomReachedListener){

        this.onBottomReachedListener = onBottomReachedListener;
    }

    public UnReadMessageAdapter(Context mContext, ArrayList<PushNotificationModel> mMessageUnreadList, final ImageView checkBox, final TextView markRead, final TextView markUnRead, final TextView msgUnread, ClickListener listener) {
        this.mContext = mContext;
        this.mMessageUnreadList = mMessageUnreadList;
        this.mCheckbox = checkBox;
        this.listener = listener;
        this.markRead = markRead;
        this.markUnRead = markUnRead;
        this.msgUnread = msgUnread;

    }

    public UnReadMessageAdapter(Context mContext, ArrayList<PushNotificationModel> mMessageUnreadList, ImageView msgBoxread) {
        this.mContext = mContext;
        this.mMessageUnreadList = mMessageUnreadList;
        this.msgBoxRead = msgBoxread;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_unread_list, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        onBottomReachedListener.onBottomReached(position);
        // System.out.println("Message Unread:::" + mMessageUnreadList.get(position).getMessage());
        holder.unreadMessage.setText(AppController.mMessageUnreadList.get(position).getDay());
        holder.unreadTxt.setText(AppController.mMessageUnreadList.get(position).getTitle());
        if (AppController.isVisibleUnreadBox) {
            holder.msgBoxUnRead.setVisibility(View.VISIBLE);
            holder.viewFirst.setVisibility(View.GONE);
            holder.viewTxt.setVisibility(View.VISIBLE);

        } else {
            holder.msgBoxUnRead.setVisibility(View.INVISIBLE);
            holder.viewFirst.setVisibility(View.VISIBLE);
            holder.viewTxt.setVisibility(View.GONE);
        }

        holder.msgBoxUnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppController.mMessageUnreadList.get(position).getChecked())
                {
                    holder.msgBoxUnRead.setBackgroundResource(R.drawable.check_box_white_2);
                    AppController.mMessageUnreadList.get(position).setChecked(false);
                    AppController.click_count=0;

                } else {
                    holder.msgBoxUnRead.setBackgroundResource(R.drawable.check_box_white_tick_2);
                    AppController.mMessageUnreadList.get(position).setChecked(true);
                    AppController.click_count=1;


                }

                int marked_count = 0;
                for (int i = 0; i < AppController.mMessageUnreadList.size(); i++) {
                    if (AppController.mMessageUnreadList.get(i).getChecked()) {

                        marked_count++;
                    }
                }

                if (marked_count == AppController.mMessageUnreadList.size()) {
                    mCheckbox.setVisibility(View.VISIBLE);
                    msgUnread.setVisibility(View.VISIBLE);
                    mCheckbox.setBackgroundResource(R.drawable.check_box_header_tick);

                } else if (marked_count == 0) {
                    mCheckbox.setVisibility(View.GONE);
                    msgUnread.setVisibility(View.GONE);
                    AppController.isVisibleUnreadBox = false;
                    markRead.setVisibility(View.GONE);
                    notifyDataSetChanged();
                    // notifyItemRangeChanged(0, getItemCount());



                } else if (marked_count==1){

                    AppController.click_count=0;
                    notifyDataSetChanged();
                    //notifyItemRangeChanged(0, getItemCount());


                }
                else {
                    markRead.setVisibility(View.VISIBLE);
                    mCheckbox.setBackgroundResource(R.drawable.check_box_header);

                }
                if (marked_count >= 1)
                {
                    AppController.click_count=0;

                    markRead.setVisibility(View.VISIBLE);

                }
            }


        });
        if (AppController.mMessageUnreadList.get(position).getFavourite().equalsIgnoreCase("1")) {
            holder.fav.setBackgroundResource(R.drawable.star_yellow);
        } else {
            holder.fav.setBackgroundResource(R.drawable.star_gray);

        }

        if (AppController.mMessageUnreadList.get(position).getChecked()) {
            holder.msgBoxUnRead.setBackgroundResource(R.drawable.check_box_white_tick_2);

        } else {
            holder.msgBoxUnRead.setBackgroundResource(R.drawable.check_box_white_2);
        }

        holder.unreadClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppController.mMessageUnreadList.get(position).setStatus("1");
                Intent mIntent;

                if (AppController.mMessageUnreadList.get(position).getType().equalsIgnoreCase("image") || AppController.mMessageUnreadList.get(position).getType().equalsIgnoreCase("text")||AppController.mMessageUnreadList.get(position).getType().equalsIgnoreCase("")) {
                    String pushNotificationDetail = "<!DOCTYPE html>\n" +
                            "<html>\n" +
                            "<head>\n" +
                            "<style>\n" +
                            "\n" +
                            "@font-face {\n" +
                            "font-family: SourceSansPro-Semibold;" +
                            "src: url(SourceSansPro-Semibold.ttf);" +

                            "font-family: SourceSansPro-Regular;" +
                            "src: url(SourceSansPro-Regular.ttf);" +
                            "}" +
                            ".title {" +
                            "font-family: SourceSansPro-Regular;" +
                            "font-size:16px;" +
                            "text-align:left;" +
                            "color:	#46C1D0;" +
                            "}" +
                            ".description {" +
                            "font-family: SourceSansPro-Semibold;" +
                            "text-align:justify;" +
                            "font-size:14px;" +
                            "color: #A9A9A9;" +
                            "}" + ".date {" +
                            "font-family: SourceSansPro-Semibold;" +
                            "text-align:right;" +
                            "font-size:12px;" +

                            "color: #000000;" +
                            "}" +
                            "</style>\n" + "</head>" +
                            "<body>";
                    if (!AppController.mMessageUnreadList.get(position).getUrl().equalsIgnoreCase("")) {
                        pushNotificationDetail = pushNotificationDetail + "<center><img src='" + AppController.mMessageUnreadList.get(position).getUrl() + "'width='100%', height='auto'>";
                    }
                    DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    DateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy hh:mm a");
                    String mdate = AppController.mMessageUnreadList.get(position).getDate();
                    Date date = null;
                    try {
                        date = inputFormat.parse(mdate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    String outputDateStr = outputFormat.format(date);
                    pushNotificationDetail = pushNotificationDetail +
                            "<p class='description'>" + AppController.mMessageUnreadList.get(position).getMessage();
                    pushNotificationDetail = pushNotificationDetail + "<p class='date'>" +/*AppController.mMessageUnreadList.get(position).getDate()*/outputDateStr + "</p>" +

                            "</body>\n</html>";/*+"<p class='description'>"+pushNotificationArrayList.get(position).getDay() + "-" + pushNotificationArrayList.get(position).getMonthString() + "-" + pushNotificationArrayList.get(position).getYear()+" "+pushNotificationArrayList.get(position).getPushTime()+"</p>"+*/
                    mIntent = new Intent(mContext, ImageActivity.class);
                    mIntent.putExtra("webViewComingDetail", pushNotificationDetail);
                    mIntent.putExtra("title", AppController.mMessageUnreadList.get(position).getTitle());
                    mIntent.putExtra("isfromUnread", true);
                    mIntent.putExtra("isfromRead", false);

                    mIntent.putExtra(PASS_ARRAY_LIST, AppController.mMessageUnreadList);
                    mIntent.putExtra(POSITION, position);

                    mContext.startActivity(mIntent);

                }
                if (AppController.mMessageUnreadList.get(position).getType().equalsIgnoreCase("audio")) {
                    mIntent = new Intent(mContext, AudioPlayerViewActivity.class);
                    mIntent.putExtra(POSITION, position);
                    mIntent.putExtra("isfromUnread", true);
                    mIntent.putExtra("isfromRead", false);

                    mIntent.putExtra("title", AppController.mMessageUnreadList.get(position).getTitle());
                    mIntent.putExtra(PASS_ARRAY_LIST, AppController.mMessageUnreadList);
                    mContext.startActivity(mIntent);
                }
                if (AppController.mMessageUnreadList.get(position).getType().equalsIgnoreCase("video")) {
                    mIntent = new Intent(mContext, VideoWebViewActivity.class);
                    mIntent.putExtra(POSITION, position);
                    mIntent.putExtra("isfromUnread", true);
                    mIntent.putExtra("isfromRead", false);

                    mIntent.putExtra("title", AppController.mMessageUnreadList.get(position).getTitle());
                    mIntent.putExtra(PASS_ARRAY_LIST, AppController.mMessageUnreadList);
                    mContext.startActivity(mIntent);
                }
            }
        });

    }


    @Override
    public int getItemCount() {
        // System.out.println("Unread list size" + mMessageUnreadList.size());
        return AppController.mMessageUnreadList.size();
    }


}
