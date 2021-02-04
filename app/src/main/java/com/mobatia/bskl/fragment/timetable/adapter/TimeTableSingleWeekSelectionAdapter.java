package com.mobatia.bskl.fragment.timetable.adapter;

import android.content.Context;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobatia.bskl.R;
import com.mobatia.bskl.fragment.timetable.model.DayModel;
import com.mobatia.bskl.manager.AppUtils;

import java.util.ArrayList;

public class TimeTableSingleWeekSelectionAdapter extends RecyclerView.Adapter<TimeTableSingleWeekSelectionAdapter.MyViewHolder> {

    private Context mContext;
    private TextView timeTxt;
    private TextView timeAPTxt;
    private TextView subjectNameTxt;
    private TextView tutorNameTxt;
    private TextView subjectTxt;
    int breakPos=0;

  private     ArrayList<DayModel> mRangeModel;



    public class MyViewHolder extends RecyclerView.ViewHolder {
     //   TextView weekTxt;
        private TextView timeTxt;
        private TextView timeAPTxt;
        private TextView subjectNameTxt;
        private TextView tutorNameTxt;
        private TextView subjectTxt;
        private TextView breakTxt;
        private CardView card_view;
        private LinearLayout relSub;
        private LinearLayout starLinear;
        private RelativeLayout llreadbreak;
        private RelativeLayout llread;

        public MyViewHolder(View view) {
            super(view);

            //weekTxt = (TextView) view.findViewById(R.id.weekTxt);
            timeTxt = (TextView) view.findViewById(R.id.timeTxt);
            timeAPTxt = (TextView) view.findViewById(R.id.timeAPTxt);
            subjectNameTxt = (TextView) view.findViewById(R.id.subjectNameTxt);
            tutorNameTxt = (TextView) view.findViewById(R.id.tutorNameTxt);
            subjectTxt = (TextView) view.findViewById(R.id.subjectTxt);
            breakTxt = (TextView) view.findViewById(R.id.breakTxt);
            card_view = (CardView) view.findViewById(R.id.card_view);
            relSub = (LinearLayout) view.findViewById(R.id.relSub);
            starLinear = (LinearLayout) view.findViewById(R.id.starLinear);
            llreadbreak = (RelativeLayout) view.findViewById(R.id.llreadbreak);
            llread = (RelativeLayout) view.findViewById(R.id.llread);



        }
    }


    public TimeTableSingleWeekSelectionAdapter(Context mContext,ArrayList<DayModel> mRangeModel) {
        this.mContext = mContext;
        this.mRangeModel = mRangeModel;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_selection_time_table_adapter, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

       /* DateFormat dateFormat = new SimpleDateFormat("hh.mm aa");
        String dateString = dateFormat.format(mRangeModel.get(position).getStarttime());*/
    /*    String pattern = " hh.mm aa";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(mRangeModel.get(position).getStarttime());*/
        if (mRangeModel.get(position).getIsBreak()==1) {


            holder.llread.setVisibility(View.GONE);
            holder.llreadbreak.setVisibility(View.VISIBLE);
            holder.starLinear.setVisibility(View.INVISIBLE);
            holder.breakTxt.setText(mRangeModel.get(position).getSortname());
            holder.timeTxt.setTextColor(mContext.getResources().getColor(R.color.white));
            holder.timeTxt.setText(AppUtils.timeParsingToAmPm(mRangeModel.get(position).getStarttime()));

            breakPos = breakPos + 1;
            if (breakPos == 1) {
                holder.relSub.setBackgroundColor(mContext.getResources().getColor(R.color.timetableblue));
            } else if (breakPos == 2) {
                holder.relSub.setBackgroundColor(mContext.getResources().getColor(R.color.late));
            } else if (breakPos == 3) {
                holder.relSub.setBackgroundColor(mContext.getResources().getColor(R.color.authorisedabsence));
            } else {
                holder.relSub.setBackgroundColor(mContext.getResources().getColor(R.color.authorisedabsence));

            }
        }
        else
        {
            holder.llread.setVisibility(View.VISIBLE);
            holder.timeAPTxt.setVisibility(View.GONE);
            holder.llreadbreak.setVisibility(View.GONE);
            holder.starLinear.setVisibility(View.GONE);
            holder.timeTxt.setText(AppUtils.timeParsingToAmPm(mRangeModel.get(position).getStarttime()));
            holder.tutorNameTxt.setText(mRangeModel.get(position).getStaff());
            holder.subjectTxt.setText(mRangeModel.get(position).getSortname());
            holder.subjectNameTxt.setText(mRangeModel.get(position).getSubject_name());
        }




    }



    @Override
    public int getItemCount() {
        System.out.println("rangemodel size"+mRangeModel.size());
        return mRangeModel.size();
    }

}
