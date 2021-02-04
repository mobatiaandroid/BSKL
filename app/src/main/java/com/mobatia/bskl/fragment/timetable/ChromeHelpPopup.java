package com.mobatia.bskl.fragment.timetable;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.mobatia.bskl.R;
import com.mobatia.bskl.fragment.timetable.adapter.TimeTablePopUpRecyclerAdapter;
import com.mobatia.bskl.fragment.timetable.model.DayModel;

import java.util.ArrayList;


public class ChromeHelpPopup {

    protected WindowManager mWindowManager;
    ArrayList<DayModel> timeTableDayModel = new ArrayList<>();
    protected Context mContext;
    protected PopupWindow mWindow;

    //    private TextView mHelpTextView;
    private ImageView mUpImageView;
    private ImageView mDownImageView;
    private RelativeLayout mainRecycleRel;
    private RecyclerView recycler_view_timetable;
    protected View mView;

    protected Drawable mBackgroundDrawable = null;
    protected ShowListener showListener;
    int size, position;

    public ChromeHelpPopup(Context context, String text, int viewResource) {
        mContext = context;
        mWindow = new PopupWindow(context);

        mWindowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);

        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        setContentView(layoutInflater.inflate(viewResource, null));

//        mHelpTextView = (TextView) mView.findViewById(R.id.text);
        mUpImageView = mView.findViewById(R.id.arrow_up);
        mDownImageView = mView.findViewById(R.id.arrow_down);
        recycler_view_timetable = mView.findViewById(R.id.recycler_view_timetable);
        mainRecycleRel = mView.findViewById(R.id.mainRecycleRel);
        recycler_view_timetable.setHasFixedSize(true);
        //mainRecycleRel.setVisibility(View.GONE);
        LinearLayoutManager llmAtime = new LinearLayoutManager(mContext);
        llmAtime.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_view_timetable.setLayoutManager(llmAtime);


//        mHelpTextView.setMovementMethod(ScrollingMovementMethod.getInstance());
//        mHelpTextView.setSelected(true);
    }

    public ChromeHelpPopup(Context context) {
        this(context, "", R.layout.popup_timetable_activity);

    }

    public View ChromeHelpPopup(Context context, ArrayList<DayModel> timeTableDayModel, int size, int position) {
        //this(context);
        this.timeTableDayModel = timeTableDayModel;
        this.size = size;

        this.position = position;
        preShow();
        return mView;
    }

    public void show(View anchor) {
        preShow();

        int[] location = new int[2];

        anchor.getLocationOnScreen(location);

        Rect anchorRect = new Rect(location[0], location[1], location[0]
                + anchor.getWidth(), location[1] + anchor.getHeight());

        anchor.measure(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        int rootHeight = anchor.getMeasuredHeight();
        int rootWidth = anchor.getMeasuredWidth();

        final int screenWidth = mWindowManager.getDefaultDisplay().getWidth();
        final int screenHeight = mWindowManager.getDefaultDisplay().getHeight();

        int yPos = anchorRect.top - rootHeight;

        boolean onTop = true;

            if (anchorRect.top < screenHeight) {//anchorRect.top < screenHeight / 2
                yPos = anchorRect.bottom;
                onTop = false;
            } /*else {
                yPos = anchorRect.top;
                onTop = true;
            }*/


        int whichArrow, requestedX;

        whichArrow = ((onTop) ? R.id.arrow_down : R.id.arrow_up);
        requestedX = anchorRect.centerX();

        View arrow = whichArrow == R.id.arrow_up ? mUpImageView
                : mDownImageView;
        View hideArrow = whichArrow == R.id.arrow_up ? mDownImageView
                : mUpImageView;

        final int arrowWidth = arrow.getMeasuredWidth();

        arrow.setVisibility(View.VISIBLE);

        ViewGroup.MarginLayoutParams param = (ViewGroup.MarginLayoutParams) arrow
                .getLayoutParams();

        hideArrow.setVisibility(View.INVISIBLE);

        int xPos = 0;

        // ETXTREME RIGHT CLIKED
        if (anchorRect.left + rootWidth > screenWidth / 6) {
            xPos = (screenWidth / 6 - rootWidth / 6);
        }
        // ETXTREME LEFT CLIKED
        else if (anchorRect.left - (rootWidth / 6) < 0) {
            xPos = anchorRect.left;
        }
        // INBETWEEN
        else {
            xPos = (anchorRect.centerX() - (rootWidth / 6));
        }

        param.leftMargin = (requestedX - xPos) - (arrowWidth / 6);
//
//        if (onTop) {
//            mHelpTextView.setMaxHeight(anchorRect.top - anchorRect.height());
//
//        } else {
//            mHelpTextView.setMaxHeight(screenHeight - yPos);
//        }

        mWindow.showAtLocation(anchor, Gravity.NO_GRAVITY, xPos, yPos);
//        mView.setAnimation(AnimationUtils.loadAnimation(mContext,
//                R.anim.float_anim));

    }

    protected void preShow() {
        if (mView == null)
            throw new IllegalStateException("view undefined");


        if (showListener != null) {
            showListener.onPreShow();
            showListener.onShow();
        }

        if (mBackgroundDrawable == null)
            mWindow.setBackgroundDrawable(new BitmapDrawable());
        else
            mWindow.setBackgroundDrawable(mBackgroundDrawable);

        mWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        mWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        mWindow.setTouchable(true);
        mWindow.setFocusable(true);
        mWindow.setOutsideTouchable(true);

        mWindow.setContentView(mView);
        setText(timeTableDayModel);
    }

    public void setBackgroundDrawable(Drawable background) {
        mBackgroundDrawable = background;
    }

    public void setContentView(View root) {
        mView = root;

        mWindow.setContentView(root);
    }

    public void setContentView(int layoutResID) {
        LayoutInflater inflator = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        setContentView(inflator.inflate(layoutResID, null));
    }

    public void setOnDismissListener(PopupWindow.OnDismissListener listener) {
        mWindow.setOnDismissListener(listener);
    }

    public void dismiss() {
        mWindow.dismiss();
        if (showListener != null) {
            showListener.onDismiss();
        }
    }

    public void setText(ArrayList<DayModel> timeTableDayModel) {
//        mHelpTextView.setText(text);

        TimeTablePopUpRecyclerAdapter mTimeTablePopUpRecyclerAdapter = new TimeTablePopUpRecyclerAdapter(mContext, timeTableDayModel);
        recycler_view_timetable.setAdapter(mTimeTablePopUpRecyclerAdapter);
    }

    public static interface ShowListener {
        void onPreShow();

        void onDismiss();

        void onShow();
    }

    public void setShowListener(ShowListener showListener) {
        this.showListener = showListener;
    }
}

