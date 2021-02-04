package com.mobatia.bskl.fragment.calendar.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobatia.bskl.R;
import com.mobatia.bskl.constants.CacheDIRConstants;
import com.mobatia.bskl.constants.IntentPassValueConstants;
import com.mobatia.bskl.fragment.calendar.model.ListViewSpinnerModel;

import java.util.ArrayList;

/**
 * Created by krishnaraj on 09/08/18.
 */

public class ListViewSpinnerAdapter extends BaseAdapter implements
        CacheDIRConstants, IntentPassValueConstants {

    private Context mContext;
    private ArrayList<String> mAboutusLists;
    private ArrayList<ListViewSpinnerModel> mTermsCalendarModelArrayList;
    private View view;
    private TextView mTitleTxt;
    private ImageView mImageView;
    private String mTitle;
    private String mTabId;

    public ListViewSpinnerAdapter(Context context,
                                 ArrayList<String> arrList, String title, String tabId) {
        this.mContext = context;
        this.mAboutusLists = arrList;
        this.mTitle = title;
        this.mTabId = tabId;
    }

    public ListViewSpinnerAdapter(Context context,
                                 ArrayList<ListViewSpinnerModel> arrList) {
        this.mContext = context;
        this.mTermsCalendarModelArrayList = arrList;

    }
    /*
     * (non-Javadoc)
     *
     * @see android.widget.Adapter#getCount()
     */
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mTermsCalendarModelArrayList.size();
    }

    /*
     * (non-Javadoc)
     *
     * @see android.widget.Adapter#getItem(int)
     */
    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mTermsCalendarModelArrayList.get(position);
    }

    /*
     * (non-Javadoc)
     *
     * @see android.widget.Adapter#getItemId(int)
     */
    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    /*
     * (non-Javadoc)
     *
     * @see android.widget.Adapter#getView(int, android.view.View,
     * android.view.ViewGroup)
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflate = LayoutInflater.from(mContext);
            view = inflate.inflate(R.layout.custom_aboutus_list_adapter, null);
        } else {
            view = convertView;
        }
        try {
            mTitleTxt = view.findViewById(R.id.listTxtTitle);
            mTitleTxt.setText(mTermsCalendarModelArrayList.get(position).getTitle());
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

}

