/**
 *
 */
package com.mobatia.bskl.activity.home.adapter;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.res.TypedArray;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.mobatia.bskl.R;
//import com.mobatia.bskl.manager.PreferenceManager;
//
//
///**
// * @author RIJO K JOSE
// *
// */
//public class HomeListAdapter extends BaseAdapter{
//
//    private Context mContext;
//    private String[] mListItemArray;
//    private TypedArray mListImgArray;
//    private int customLayout;
//    private boolean mDisplayListImage;
//
//    public HomeListAdapter(Context context, String[] listItemArray,
//                           TypedArray listImgArray, int customLayout, boolean displayListImage) {
//        this.mContext = context;
//        this.mListItemArray = listItemArray;
//        this.mListImgArray = listImgArray;
//        this.customLayout = customLayout;
//        this.mDisplayListImage = displayListImage;
//    }
//
//    public HomeListAdapter(Context context, String[] listItemArray,
//                           int customLayout, boolean displayListImage) {
//        this.mContext = context;
//        this.mListItemArray = listItemArray;
//        this.customLayout = customLayout;
//        this.mDisplayListImage = displayListImage;
//    }
//
//    /*
//     * (non-Javadoc)
//     *
//     * @see android.widget.Adapter#getCount()
//     */
//    @Override
//    public int getCount() {
//        // TODO Auto-generated method stub
//        return mListItemArray.length;
//    }
//
//    /*
//     * (non-Javadoc)
//     *
//     * @see android.widget.Adapter#getItem(int)
//     */
//    @Override
//    public Object getItem(int position) {
//        // TODO Auto-generated method stub
//        return mListItemArray[position];
//    }
//
//    /*
//     * (non-Javadoc)
//     *
//     * @see android.widget.Adapter#getItemId(int)
//     */
//    @Override
//    public long getItemId(int position) {
//        // TODO Auto-generated method stub
//        return position;
//    }
//
//    /*
//     * (non-Javadoc)
//     *
//     * @see android.widget.Adapter#getView(int, android.view.View,
//     * android.view.ViewGroup)
//     */
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//
//
//        ViewHolder holder;
//
//        if (convertView == null) {
//            LayoutInflater mInflater = (LayoutInflater) mContext
//                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
//            convertView = mInflater.inflate(customLayout, null);
//            holder = new ViewHolder();
//            holder.txtTitle = convertView.findViewById(R.id.listTxtView);
//            holder.imgView = convertView.findViewById(R.id.listImg);
//            convertView.setTag(holder);
//        }
//        else {
//            holder = (ViewHolder) convertView.getTag();
//        }
//
////        TextView txtTitle = (TextView) convertView
////                .findViewById(R.id.listTxtView);
////        ImageView imgView = (ImageView) convertView.findViewById(R.id.listImg);
//        holder.txtTitle.setText(mListItemArray[position]);
//        if (mDisplayListImage) {
//            holder.imgView.setVisibility(View.VISIBLE);
//            holder.imgView.setImageDrawable(mListImgArray.getDrawable(position));
//        } else {
//            holder.imgView.setVisibility(View.GONE);
//        }
//        if(PreferenceManager.getIsVisible(mContext).equalsIgnoreCase("1"))
//        {
//            if (PreferenceManager.getReportMailMerge(mContext).equalsIgnoreCase("1"))
//            {
//                if(mListItemArray[position].equalsIgnoreCase("Home") || mListItemArray[position].equalsIgnoreCase("Reports")|| mListItemArray[position].equalsIgnoreCase("Contact us"))
//                {
//                    holder.txtTitle.setTextColor(mContext.getResources().getColor(R.color.white));
//
//                }
//                else
//                {
//                    holder.txtTitle.setTextColor(mContext.getResources().getColor(R.color.logout_user));
//
//                }
//            }
//            else
//            {
//                if(mListItemArray[position].equalsIgnoreCase("Home") || mListItemArray[position].equalsIgnoreCase("Contact us"))
//                {
//                    holder.txtTitle.setTextColor(mContext.getResources().getColor(R.color.white));
//
//                }
//                else
//                {
//                    holder.txtTitle.setTextColor(mContext.getResources().getColor(R.color.logout_user));
//
//                }
//            }
//
////            if(mListItemArray[position].equalsIgnoreCase("Home") || mListItemArray[position].equalsIgnoreCase("Reports")|| mListItemArray[position].equalsIgnoreCase("Contact us"))
////            {
////                holder.txtTitle.setTextColor(mContext.getResources().getColor(R.color.white));
////
////            }
////            else
////            {
////                holder.txtTitle.setTextColor(mContext.getResources().getColor(R.color.logout_user));
////
////            }
//        }
//        else
//        {
//
//           if(PreferenceManager.getReportMailMerge(mContext).equalsIgnoreCase("0"))
//            {
//                if(mListItemArray[position].equalsIgnoreCase("Home") || mListItemArray[position].equalsIgnoreCase("Messages")|| mListItemArray[position].equalsIgnoreCase("Calendar")|| mListItemArray[position].equalsIgnoreCase("BSKL News")|| mListItemArray[position].equalsIgnoreCase("Social Media")|| mListItemArray[position].equalsIgnoreCase("Timetable")|| mListItemArray[position].equalsIgnoreCase("Contact us"))
//                {
//                    holder.txtTitle.setTextColor(mContext.getResources().getColor(R.color.white));
//
//                }
//                else
//                {
//                    holder.txtTitle.setTextColor(mContext.getResources().getColor(R.color.logout_user));
//
//                }
//            }
//           else
//           {
//               holder.txtTitle.setTextColor(mContext.getResources().getColor(R.color.white));
//           }
//
//
//        }
//        return convertView;
//    }
//
//
//    static class ViewHolder {
//        private TextView txtTitle;
//        private ImageView imgView;
//    }
//
//}
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobatia.bskl.R;
import com.mobatia.bskl.manager.PreferenceManager;


/**
 * @author RIJO K JOSE
 *
 */
public class HomeListAdapter extends BaseAdapter{

    private Context mContext;
    private String[] mListItemArray;
    private TypedArray mListImgArray;
    private int customLayout;
    private boolean mDisplayListImage;

    public HomeListAdapter(Context context, String[] listItemArray,
                           TypedArray listImgArray, int customLayout, boolean displayListImage) {
        this.mContext = context;
        this.mListItemArray = listItemArray;
        this.mListImgArray = listImgArray;
        this.customLayout = customLayout;
        this.mDisplayListImage = displayListImage;
    }

    public HomeListAdapter(Context context, String[] listItemArray,
                           int customLayout, boolean displayListImage) {
        this.mContext = context;
        this.mListItemArray = listItemArray;
        this.customLayout = customLayout;
        this.mDisplayListImage = displayListImage;
    }

    /*
     * (non-Javadoc)
     *
     * @see android.widget.Adapter#getCount()
     */
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mListItemArray.length;
    }

    /*
     * (non-Javadoc)
     *
     * @see android.widget.Adapter#getItem(int)
     */
    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mListItemArray[position];
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
    public View getView(int position, View convertView, ViewGroup parent) {


        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) mContext
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(customLayout, null);
            holder = new ViewHolder();
            holder.txtTitle = convertView.findViewById(R.id.listTxtView);
            holder.imgView = convertView.findViewById(R.id.listImg);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

//        TextView txtTitle = (TextView) convertView
//                .findViewById(R.id.listTxtView);
//        ImageView imgView = (ImageView) convertView.findViewById(R.id.listImg);
        holder.txtTitle.setText(mListItemArray[position]);
        if (mDisplayListImage) {
            holder.imgView.setVisibility(View.VISIBLE);
            holder.imgView.setImageDrawable(mListImgArray.getDrawable(position));
        } else {
            holder.imgView.setVisibility(View.GONE);
        }
        if(PreferenceManager.getIsVisible(mContext).equalsIgnoreCase("1"))
        {
            if (PreferenceManager.getReportMailMerge(mContext).equalsIgnoreCase("1"))
            {
                if(mListItemArray[position].equalsIgnoreCase("Home") || mListItemArray[position].equalsIgnoreCase("Reports")|| mListItemArray[position].equalsIgnoreCase("Contact us"))
                {
                    holder.txtTitle.setTextColor(mContext.getResources().getColor(R.color.white));

                }
                else
                {
                    holder.txtTitle.setTextColor(mContext.getResources().getColor(R.color.logout_user));

                }
            }
            else
            {
                if(mListItemArray[position].equalsIgnoreCase("Home") || mListItemArray[position].equalsIgnoreCase("Contact us"))
                {
                    holder.txtTitle.setTextColor(mContext.getResources().getColor(R.color.white));

                }
                else
                {
                    holder.txtTitle.setTextColor(mContext.getResources().getColor(R.color.logout_user));

                }
            }

//            if(mListItemArray[position].equalsIgnoreCase("Home") || mListItemArray[position].equalsIgnoreCase("Reports")|| mListItemArray[position].equalsIgnoreCase("Contact us"))
//            {
//                holder.txtTitle.setTextColor(mContext.getResources().getColor(R.color.white));
//
//            }
//            else
//            {
//                holder.txtTitle.setTextColor(mContext.getResources().getColor(R.color.logout_user));
//
//            }
        }

        else
        {
            if (PreferenceManager.getIsApplicant(mContext).equalsIgnoreCase("1"))
            {
                if(mListItemArray[position].equalsIgnoreCase("Home") || mListItemArray[position].equalsIgnoreCase("Contact us"))
                {
                    holder.txtTitle.setTextColor(mContext.getResources().getColor(R.color.white));

                }
                else
                {
                    holder.txtTitle.setTextColor(mContext.getResources().getColor(R.color.logout_user));

                }
            }
            else
            {
//|| mListItemArray[position].equalsIgnoreCase("Timetable")
                if(PreferenceManager.getReportMailMerge(mContext).equalsIgnoreCase("0"))
                {
                    if(mListItemArray[position].equalsIgnoreCase("Home") || mListItemArray[position].equalsIgnoreCase("Messages")|| mListItemArray[position].equalsIgnoreCase("Calendar")|| mListItemArray[position].equalsIgnoreCase("BSKL News")|| mListItemArray[position].equalsIgnoreCase("Social Media")|| mListItemArray[position].equalsIgnoreCase("Contact us"))
                    {
                        holder.txtTitle.setTextColor(mContext.getResources().getColor(R.color.white));

                    }
                    else
                    {
                        holder.txtTitle.setTextColor(mContext.getResources().getColor(R.color.logout_user));

                    }
                }
                else
                {
                    holder.txtTitle.setTextColor(mContext.getResources().getColor(R.color.white));
                }


            }
        }

        return convertView;
    }


    static class ViewHolder {
        private TextView txtTitle;
        private ImageView imgView;
    }

}