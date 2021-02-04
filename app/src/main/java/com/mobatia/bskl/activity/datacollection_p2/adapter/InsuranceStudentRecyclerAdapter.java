package com.mobatia.bskl.activity.datacollection_p2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobatia.bskl.R;
import com.mobatia.bskl.activity.datacollection_p2.model.InsuranceDetailModel;
import com.mobatia.bskl.activity.datacollection_p2.model.KinModel;
import com.mobatia.bskl.activity.datacollection_p2.model.StudentModelNew;
import com.mobatia.bskl.appcontroller.AppController;
import com.mobatia.bskl.fragment.report.model.StudentModel;
import com.mobatia.bskl.manager.PreferenceManager;

import java.util.ArrayList;

public class InsuranceStudentRecyclerAdapter extends RecyclerView.Adapter<InsuranceStudentRecyclerAdapter.MyViewHolder> {

    Context mContext;
    ArrayList<StudentModelNew> mInsuranceDetailList = new ArrayList<>();
    int row_index = -1;

    public InsuranceStudentRecyclerAdapter(Context mContext, ArrayList<StudentModelNew> mInsuranceDetailList) {
        this.mContext = mContext;
        this.mInsuranceDetailList = mInsuranceDetailList;
    }

    @NonNull
    @Override
    public InsuranceStudentRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_second_fragment_new, viewGroup, false);

        return new InsuranceStudentRecyclerAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final InsuranceStudentRecyclerAdapter.MyViewHolder myViewHolder, final int i) {
        if (PreferenceManager.getInsuranceStudentList(mContext).get(i).isConfirmed())
        {
            myViewHolder.mainRelative.setBackgroundResource(R.drawable.rect_background_grey);
            myViewHolder.ConfirmButton.setVisibility(View.GONE);
        }
        else
        {
            myViewHolder.mainRelative.setBackgroundResource(R.drawable.rect_data_collection_red);
            myViewHolder.ConfirmButton.setVisibility(View.VISIBLE);
        }
        myViewHolder.studentNameTxt.setText(PreferenceManager.getInsuranceStudentList(mContext).get(i).getmName());
        myViewHolder.classTxt.setText(PreferenceManager.getInsuranceStudentList(mContext).get(i).getmClass());


    }



    @Override
    public int getItemCount() {
        return AppController.mStudentDataArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView studentNameTxt,classTxt;
        Button ConfirmButton;
        RelativeLayout mainRelative;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mainRelative = itemView.findViewById(R.id.mainRelative);
            studentNameTxt =itemView.findViewById(R.id.studentNameTxt);
            classTxt = itemView.findViewById(R.id.classTxt);
            ConfirmButton = itemView.findViewById(R.id.confirmBtn);
        }
    }
}
