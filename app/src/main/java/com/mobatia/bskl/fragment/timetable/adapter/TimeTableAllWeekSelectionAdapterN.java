package com.mobatia.bskl.fragment.timetable.adapter;

import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobatia.bskl.R;
import com.mobatia.bskl.fragment.timetable.model.PeriodModel;
import com.ryanharter.android.tooltips.ToolTip;
import com.ryanharter.android.tooltips.ToolTipLayout;

import java.util.ArrayList;

public class TimeTableAllWeekSelectionAdapterN extends RecyclerView.Adapter<TimeTableAllWeekSelectionAdapterN.MyViewHolder> {

    private Context mContext;
    private ArrayList<PeriodModel> mPeriodList;
    RecyclerView timeTableAllRecycler;
    ToolTipLayout tipContainer;
    boolean isClick=false;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView periodTxt;
        TextView timeTxt;
        TextView tutor1;
        TextView tutor2;
        TextView tutor3;
        TextView tutor4;
        TextView tutor5;
        TextView timeBreak;
        TextView txtBreak;
        TextView countMTextView;
        TextView countTTextView;
        TextView countWTextView;
        TextView countThTextView;
        TextView countFTextView;
        LinearLayout timeLinear;
        LinearLayout tutorLinear;
        LinearLayout starLinearR;
        LinearLayout relSub;
        RelativeLayout llread;
        RelativeLayout countMRel;
        RelativeLayout countTRel;
        RelativeLayout countWRel;
        RelativeLayout countThRel;
        RelativeLayout countFRel;

        public MyViewHolder(View view) {
            super(view);

            countFTextView = (TextView) view.findViewById(R.id.countFTextView);
            countThTextView = (TextView) view.findViewById(R.id.countThTextView);
            countWTextView = (TextView) view.findViewById(R.id.countWTextView);
            countTTextView = (TextView) view.findViewById(R.id.countTTextView);
            countMTextView = (TextView) view.findViewById(R.id.countMTextView);
            periodTxt = (TextView) view.findViewById(R.id.periodTxt);
            timeTxt = (TextView) view.findViewById(R.id.timeTxt);
            tutor1 = (TextView) view.findViewById(R.id.tutor1);
            tutor2 = (TextView) view.findViewById(R.id.tutor2);
            tutor3 = (TextView) view.findViewById(R.id.tutor3);
            tutor4 = (TextView) view.findViewById(R.id.tutor4);
            tutor5 = (TextView) view.findViewById(R.id.tutor5);
            timeBreak = (TextView) view.findViewById(R.id.timeBreak);
            txtBreak = (TextView) view.findViewById(R.id.txtBreak);
            timeLinear = (LinearLayout) view.findViewById(R.id.timeLinear);
            tutorLinear = (LinearLayout) view.findViewById(R.id.tutorLinear);
            starLinearR = (LinearLayout) view.findViewById(R.id.starLinearR);
            relSub = (LinearLayout) view.findViewById(R.id.relSub);
            llread = (RelativeLayout) view.findViewById(R.id.llread);
            countMRel = (RelativeLayout) view.findViewById(R.id.countMRel);
            countTRel = (RelativeLayout) view.findViewById(R.id.countTRel);
            countWRel = (RelativeLayout) view.findViewById(R.id.countWRel);
            countThRel = (RelativeLayout) view.findViewById(R.id.countThRel);
            countFRel = (RelativeLayout) view.findViewById(R.id.countFRel);


        }
    }


    public TimeTableAllWeekSelectionAdapterN(Context mContext, ArrayList<PeriodModel> mPeriodList, RecyclerView timeTableAllRecycler, ToolTipLayout tipContainer) {
        this.mContext = mContext;
        this.mPeriodList = mPeriodList;
        this.timeTableAllRecycler = timeTableAllRecycler;
        this.tipContainer = tipContainer;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.all_selection_time_table_adapter, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.periodTxt.setText(mPeriodList.get(position).getSortname());
        holder.timeTxt.setVisibility(View.GONE);
       // holder.timeTxt.setText(AppUtils.timeParsingToAmPm(mPeriodList.get(position).getStarttime()));
        if (!(mPeriodList.get(position).getPeriod_id().equalsIgnoreCase(""))) {
            holder.timeLinear.setVisibility(View.VISIBLE);
            holder.tutorLinear.setVisibility(View.GONE);
            holder.relSub.setVisibility(View.GONE);
            holder.llread.setVisibility(View.GONE);
            holder.starLinearR.setVisibility(View.GONE);
            holder.timeBreak.setVisibility(View.INVISIBLE);
            //holder.timeBreak.setText(AppUtils.timeParsingToAmPm(mPeriodList.get(position).getStarttime()));
            holder.txtBreak.setText(mPeriodList.get(position).getSortname());
            if (mPeriodList.get(position).getCountBreak() == 1)
            {
                holder.timeLinear.setBackgroundResource(R.drawable.yellow_bar);
            } else if (mPeriodList.get(position).getCountBreak() == 2)
            {
                holder.timeLinear.setBackgroundResource(R.drawable.blue_bar);
            } else if (mPeriodList.get(position).getCountBreak() == 3)
            {
                holder.timeLinear.setBackgroundResource(R.drawable.violet_bar);
            } else
                {
                holder.timeLinear.setBackgroundResource(R.drawable.yellow_bar);

            }
        } else if (mPeriodList.get(position).getTimeTableDayModel().size() > 0)
        {
            holder.timeLinear.setVisibility(View.GONE);
            holder.tutorLinear.setVisibility(View.VISIBLE);
            holder.llread.setVisibility(View.VISIBLE);
            holder.starLinearR.setVisibility(View.VISIBLE);
            holder.relSub.setVisibility(View.VISIBLE);
            holder.tutor1.setText(mPeriodList.get(position).getMonday());
            holder.tutor2.setText(mPeriodList.get(position).getTuesday());
            holder.tutor3.setText(mPeriodList.get(position).getWednesday());
            holder.tutor4.setText(mPeriodList.get(position).getThursday());
            holder.tutor5.setText(mPeriodList.get(position).getFriday());
            if (mPeriodList.get(position).getCountM() > 1) {

                holder.countMRel.setVisibility(View.VISIBLE);
                holder.countMTextView.setVisibility(View.VISIBLE);
                holder.countMTextView.setText("+"+(mPeriodList.get(position).getCountM()-1));

            } else {

                holder.countMRel.setVisibility(View.GONE);
                holder.countMTextView.setVisibility(View.GONE);

            }
            if (mPeriodList.get(position).getCountT() > 1) {

                holder.countTRel.setVisibility(View.VISIBLE);
                holder.countTTextView.setVisibility(View.VISIBLE);
                holder.countTTextView.setText("+"+(mPeriodList.get(position).getCountT()-1));


            } else {

                holder.countTRel.setVisibility(View.GONE);
                holder.countTTextView.setVisibility(View.GONE);

            }
            if (mPeriodList.get(position).getCountW() > 1) {
                holder.countWRel.setVisibility(View.VISIBLE);
                holder.countWTextView.setVisibility(View.VISIBLE);
                holder.countWTextView.setText("+"+(mPeriodList.get(position).getCountW()-1));


            } else {
                holder.countWRel.setVisibility(View.GONE);
                holder.countWTextView.setVisibility(View.GONE);

            }
            if (mPeriodList.get(position).getCountTh() > 1) {
                holder.countThRel.setVisibility(View.VISIBLE);
                holder.countThTextView.setVisibility(View.VISIBLE);
                holder.countThTextView.setText("+"+(mPeriodList.get(position).getCountTh()-1));


            } else {
                holder.countThRel.setVisibility(View.GONE);
                holder.countThTextView.setVisibility(View.GONE);

            }
            if (mPeriodList.get(position).getCountF() > 1) {
                holder.countFRel.setVisibility(View.VISIBLE);
                holder.countFTextView.setVisibility(View.VISIBLE);
                holder.countFTextView.setText("+"+(mPeriodList.get(position).getCountF()-1));


            } else {
                holder.countFRel.setVisibility(View.GONE);
                holder.countFTextView.setVisibility(View.GONE);

            }

        } else {
            holder.tutor1.setText("");
            holder.tutor2.setText("");
            holder.tutor3.setText("");
            holder.tutor4.setText("");
            holder.tutor5.setText("");
            holder.countMRel.setVisibility(View.GONE);
            holder.countMTextView.setVisibility(View.GONE);
            holder.countTRel.setVisibility(View.GONE);
            holder.countTTextView.setVisibility(View.GONE);
            holder.countWRel.setVisibility(View.GONE);
            holder.countWTextView.setVisibility(View.GONE);
            holder.countThRel.setVisibility(View.GONE);
            holder.countThTextView.setVisibility(View.GONE);
            holder.countFRel.setVisibility(View.GONE);
            holder.countFTextView.setVisibility(View.GONE);
        }
        holder.tutor1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Quick and Easy intent selector in tooltip styles
                if (holder.tutor1.getText().toString().equalsIgnoreCase("")) {

                } else {
                    isClick=true;
                    System.out.println("mon:::" + mPeriodList.get(position).getTimeTableListM().size());
                    View itemView = LayoutInflater.from(mContext)
                            .inflate(R.layout.popup_timetable_activity,null,false);
                    RecyclerView recycler_view_timetable = itemView.findViewById(R.id.recycler_view_timetable);
                    recycler_view_timetable.setHasFixedSize(true);
                    //mainRecycleRel.setVisibility(View.GONE);
                    LinearLayoutManager llmAtime = new LinearLayoutManager(mContext);
                    llmAtime.setOrientation(LinearLayoutManager.VERTICAL);
                    recycler_view_timetable.setLayoutManager(llmAtime);
                    TimeTablePopUpRecyclerAdapter mTimeTablePopUpRecyclerAdapter = new TimeTablePopUpRecyclerAdapter(mContext, mPeriodList.get(position).getTimeTableListM());
                    mTimeTablePopUpRecyclerAdapter.notifyDataSetChanged();

                    recycler_view_timetable.setAdapter(mTimeTablePopUpRecyclerAdapter);
                    //  chromeHelpPopup.show(holder.tutor1);
                    ToolTip t=null;
                    if(position==0) {

                        t = new ToolTip.Builder(mContext)
                                .anchor(holder.tutor1)      // The view to which the ToolTip should be anchored
                                .gravity(Gravity.BOTTOM)      // The location of the view in relation to the anchor (LEFT, RIGHT, TOP, BOTTOM)
                                .color(mContext.getResources().getColor(R.color.ttpop))          // The color of the pointer arrow
                                .pointerSize(10) // The size of the pointer
                                .contentView(itemView)
                                // The actual contents of the ToolTip


                                .build();
                    }else
                    {
                        t = new ToolTip.Builder(mContext)
                                .anchor(holder.tutor1)      // The view to which the ToolTip should be anchored
                                .gravity(Gravity.TOP)      // The location of the view in relation to the anchor (LEFT, RIGHT, TOP, BOTTOM)
                                .color(mContext.getResources().getColor(R.color.ttpop))          // The color of the pointer arrow
                                .pointerSize(10) // The size of the pointer
                                .contentView(itemView)
                                // The actual contents of the ToolTip


                                .build();
                    }
                    tipContainer.addTooltip(t);


                }
            }
        });
        holder.tutor2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Quick and Easy intent selector in tooltip styles
                if (holder.tutor2.getText().toString().equalsIgnoreCase("")) {

                } else {
                    System.out.println("tue:::" + mPeriodList.get(position).getTimeTableListT().size());

                    View itemView = LayoutInflater.from(mContext)
                            .inflate(R.layout.popup_timetable_activity,null,false);
                    RecyclerView recycler_view_timetable = itemView.findViewById(R.id.recycler_view_timetable);
                    recycler_view_timetable.setHasFixedSize(true);
                    //mainRecycleRel.setVisibility(View.GONE);
                    LinearLayoutManager llmAtime = new LinearLayoutManager(mContext);
                    llmAtime.setOrientation(LinearLayoutManager.VERTICAL);
                    recycler_view_timetable.setLayoutManager(llmAtime);
                    TimeTablePopUpRecyclerAdapter mTimeTablePopUpRecyclerAdapter = new TimeTablePopUpRecyclerAdapter(mContext, mPeriodList.get(position).getTimeTableListT());
                    mTimeTablePopUpRecyclerAdapter.notifyDataSetChanged();

                    recycler_view_timetable.setAdapter(mTimeTablePopUpRecyclerAdapter);
                    //  chromeHelpPopup.show(holder.tutor1);
                    ToolTip t=null;
                    if(position==0)
                    {

                        t = new ToolTip.Builder(mContext)
                                .anchor(holder.tutor2)      // The view to which the ToolTip should be anchored
                                .gravity(Gravity.BOTTOM)      // The location of the view in relation to the anchor (LEFT, RIGHT, TOP, BOTTOM)
                                .color(mContext.getResources().getColor(R.color.ttpop))          // The color of the pointer arrow
                                .pointerSize(10) // The size of the pointer
                                .contentView(itemView)
                                // The actual contents of the ToolTip
                                .build();

                    }else
                    {
                        t = new ToolTip.Builder(mContext)
                                .anchor(holder.tutor2)      // The view to which the ToolTip should be anchored
                                .gravity(Gravity.TOP)      // The location of the view in relation to the anchor (LEFT, RIGHT, TOP, BOTTOM)
                                .color(mContext.getResources().getColor(R.color.ttpop))          // The color of the pointer arrow
                                .pointerSize(10) // The size of the pointer
                                .contentView(itemView)
                                // The actual contents of the ToolTip


                                .build();
                    }
                    tipContainer.addTooltip(t);
                }
            }
        });
        holder.tutor3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Quick and Easy intent selector in tooltip styles
                if (holder.tutor3.getText().toString().equalsIgnoreCase("")) {

                } else {
                    System.out.println("wed:::" + mPeriodList.get(position).getTimeTableListW().size());
                    View itemView = LayoutInflater.from(mContext)
                            .inflate(R.layout.popup_timetable_activity,null,false);
                    RecyclerView recycler_view_timetable = itemView.findViewById(R.id.recycler_view_timetable);
                    recycler_view_timetable.setHasFixedSize(true);
                    //mainRecycleRel.setVisibility(View.GONE);
                    LinearLayoutManager llmAtime = new LinearLayoutManager(mContext);
                    llmAtime.setOrientation(LinearLayoutManager.VERTICAL);
                    recycler_view_timetable.setLayoutManager(llmAtime);
                    TimeTablePopUpRecyclerAdapter mTimeTablePopUpRecyclerAdapter = new TimeTablePopUpRecyclerAdapter(mContext, mPeriodList.get(position).getTimeTableListW());
                    mTimeTablePopUpRecyclerAdapter.notifyDataSetChanged();

                    recycler_view_timetable.setAdapter(mTimeTablePopUpRecyclerAdapter);
                    //  chromeHelpPopup.show(holder.tutor1);
                    ToolTip t=null;
                    if(position==0) {

                        t = new ToolTip.Builder(mContext)
                                .anchor(holder.tutor3)      // The view to which the ToolTip should be anchored
                                .gravity(Gravity.BOTTOM)      // The location of the view in relation to the anchor (LEFT, RIGHT, TOP, BOTTOM)
                                .color(mContext.getResources().getColor(R.color.ttpop))          // The color of the pointer arrow
                                .pointerSize(10) // The size of the pointer
                                .contentView(itemView)
                                // The actual contents of the ToolTip


                                .build();
                    }else
                    {
                        t = new ToolTip.Builder(mContext)
                                .anchor(holder.tutor3)      // The view to which the ToolTip should be anchored
                                .gravity(Gravity.TOP)      // The location of the view in relation to the anchor (LEFT, RIGHT, TOP, BOTTOM)
                                .color(mContext.getResources().getColor(R.color.ttpop))          // The color of the pointer arrow
                                .pointerSize(10) // The size of the pointer
                                .contentView(itemView)
                                // The actual contents of the ToolTip


                                .build();
                    }
                    tipContainer.addTooltip(t);
                }
            }
        });
        holder.tutor4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Quick and Easy intent selector in tooltip styles
                if (holder.tutor4.getText().toString().equalsIgnoreCase("")) {

                } else {
                    View itemView = LayoutInflater.from(mContext)
                            .inflate(R.layout.popup_timetable_activity,null,false);
                    RecyclerView recycler_view_timetable = itemView.findViewById(R.id.recycler_view_timetable);
                    recycler_view_timetable.setHasFixedSize(true);
                    //mainRecycleRel.setVisibility(View.GONE);
                    LinearLayoutManager llmAtime = new LinearLayoutManager(mContext);
                    llmAtime.setOrientation(LinearLayoutManager.VERTICAL);
                    recycler_view_timetable.setLayoutManager(llmAtime);
                    TimeTablePopUpRecyclerAdapter mTimeTablePopUpRecyclerAdapter = new TimeTablePopUpRecyclerAdapter(mContext, mPeriodList.get(position).getTimeTableListTh());
                    mTimeTablePopUpRecyclerAdapter.notifyDataSetChanged();
                    recycler_view_timetable.setAdapter(mTimeTablePopUpRecyclerAdapter);
                    //  chromeHelpPopup.show(holder.tutor1);
                    ToolTip t=null;
                    if(position==0) {

                        t = new ToolTip.Builder(mContext)
                                .anchor(holder.tutor4)      // The view to which the ToolTip should be anchored
                                .gravity(Gravity.BOTTOM)      // The location of the view in relation to the anchor (LEFT, RIGHT, TOP, BOTTOM)
                                .color(mContext.getResources().getColor(R.color.ttpop))          // The color of the pointer arrow
                                .pointerSize(10) // The size of the pointer
                                .contentView(itemView)
                                // The actual contents of the ToolTip


                                .build();
                    }else
                    {
                        t = new ToolTip.Builder(mContext)
                                .anchor(holder.tutor4)      // The view to which the ToolTip should be anchored
                                .gravity(Gravity.TOP)      // The location of the view in relation to the anchor (LEFT, RIGHT, TOP, BOTTOM)
                                .color(mContext.getResources().getColor(R.color.ttpop))          // The color of the pointer arrow
                                .pointerSize(10) // The size of the pointer
                                .contentView(itemView)
                                // The actual contents of the ToolTip
                                .build();
                    }

                    tipContainer.addTooltip(t);
                }
            }
        });
        holder.tutor5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Quick and Easy intent selector in tooltip styles
                if (holder.tutor5.getText().toString().equalsIgnoreCase("")) {

                } else {
                    System.out.println("fri:" + mPeriodList.get(position).getTimeTableListF().size());
                    View itemView = LayoutInflater.from(mContext)
                            .inflate(R.layout.popup_timetable_activity,null,false);
                    RecyclerView recycler_view_timetable = itemView.findViewById(R.id.recycler_view_timetable);
                    recycler_view_timetable.setHasFixedSize(true);
                    //mainRecycleRel.setVisibility(View.GONE);
                    LinearLayoutManager llmAtime = new LinearLayoutManager(mContext);
                    llmAtime.setOrientation(LinearLayoutManager.VERTICAL);
                    recycler_view_timetable.setLayoutManager(llmAtime);
                    TimeTablePopUpRecyclerAdapter mTimeTablePopUpRecyclerAdapter = new TimeTablePopUpRecyclerAdapter(mContext, mPeriodList.get(position).getTimeTableListF());
                    mTimeTablePopUpRecyclerAdapter.notifyDataSetChanged();

                    recycler_view_timetable.setAdapter(mTimeTablePopUpRecyclerAdapter);
                    //  chromeHelpPopup.show(holder.tutor1);
                    ToolTip t=null;
                    if(position==0) {
                        t = new ToolTip.Builder(mContext)
                                .anchor(holder.tutor5)      // The view to which the ToolTip should be anchored
                                .gravity(Gravity.BOTTOM)      // The location of the view in relation to the anchor (LEFT, RIGHT, TOP, BOTTOM)
                                .color(mContext.getResources().getColor(R.color.ttpop))          // The color of the pointer arrow
                                .pointerSize(10) // The size of the pointer
                                .contentView(itemView)
                                // The actual contents of the ToolTip


                                .build();
                    }else
                    {
                        t = new ToolTip.Builder(mContext)
                                .anchor(holder.tutor5)      // The view to which the ToolTip should be anchored
                                .gravity(Gravity.TOP)      // The location of the view in relation to the anchor (LEFT, RIGHT, TOP, BOTTOM)
                                .color(mContext.getResources().getColor(R.color.ttpop))          // The color of the pointer arrow
                                .pointerSize(10) // The size of the pointer
                                .contentView(itemView)
                                // The actual contents of the ToolTip


                                .build();
                    }
                    tipContainer.addTooltip(t);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        System.out.println("period size" + mPeriodList.size());
        return mPeriodList.size();
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
