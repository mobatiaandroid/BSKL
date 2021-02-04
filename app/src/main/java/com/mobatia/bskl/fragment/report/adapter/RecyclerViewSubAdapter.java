package com.mobatia.bskl.fragment.report.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobatia.bskl.R;
import com.mobatia.bskl.activity.pdf.PDF_View_New;
import com.mobatia.bskl.activity.web_view.LoadUrlWebViewActivityNew;
import com.mobatia.bskl.fragment.report.model.DataModel;

import java.util.ArrayList;

/**
 * Created by krishnaraj on 23/07/18.
 */

public class RecyclerViewSubAdapter extends RecyclerView.Adapter<RecyclerViewSubAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<DataModel>mDataModel;
    String dept;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView termName;
        LinearLayout clickLinear;


        public MyViewHolder(View view) {
            super(view);
            termName = view.findViewById(R.id.termname);
            clickLinear = view.findViewById(R.id.clickLinear);
            ArrayList<DataModel> mDataModel= new ArrayList<>();



        }
    }


    public RecyclerViewSubAdapter(Context mContext, ArrayList<DataModel> mDataModel) {
        this.mContext = mContext;
        this.mDataModel = mDataModel;

    }

    @Override
    public RecyclerViewSubAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_adapter_sub, parent, false);

        return new RecyclerViewSubAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewSubAdapter.MyViewHolder holder, final int position) {
       holder.termName.setText(mDataModel.get(position).getReporting_cycle());
        holder.clickLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (mDataModel.get(position).getFile().endsWith(".pdf")) {
//					Intent intent = new Intent(mContext, PdfReaderActivity.class);
//					intent.putExtra("pdf_url", mDataModel.get(position).getFile());
//					mContext.startActivity(intent);Intent intent = new Intent(mContext, PdfReaderActivity.class);
                     System.out.println("PDF URL"+mDataModel.get(position).getFile());
                    Intent intent = new Intent(mContext, PDF_View_New.class);
                    intent.putExtra("fileName",mDataModel.get(position).getReporting_cycle());
					intent.putExtra("pdf_url", mDataModel.get(position).getFile());
					mContext.startActivity(intent);
				}
				else
				{
					Intent intent = new Intent(mContext, LoadUrlWebViewActivityNew.class);
					intent.putExtra("url",mDataModel.get(position).getFile());
					mContext.startActivity(intent);
				}

            }
        });


    }


    @Override
    public int getItemCount() {
        return mDataModel.size();
    }

}
