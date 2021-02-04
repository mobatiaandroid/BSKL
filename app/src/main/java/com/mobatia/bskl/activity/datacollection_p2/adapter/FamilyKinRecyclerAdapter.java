package com.mobatia.bskl.activity.datacollection_p2.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobatia.bskl.R;
import com.mobatia.bskl.activity.datacollection_p2.model.KinModel;
import com.mobatia.bskl.appcontroller.AppController;

import java.util.ArrayList;

public class FamilyKinRecyclerAdapter extends RecyclerView.Adapter<FamilyKinRecyclerAdapter.MyViewHolder> {

    Context mContext;
    ArrayList<KinModel> arrayList = new ArrayList<>();
    int row_index = -1;

    public FamilyKinRecyclerAdapter(Context mContext, ArrayList<KinModel> arrayList) {
        this.mContext = mContext;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_own_detail, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        myViewHolder.Name.setText(AppController.kinArrayShow.get(i).getName()+" "+AppController.kinArrayShow.get(i).getLast_name());
        myViewHolder.ContactType.setText(AppController.kinArrayShow.get(i).getRelationship());

        if (AppController.kinArrayShow.get(i).isConfirmed()) {
            myViewHolder.ConfirmButton.setVisibility(View.GONE);
        }else{
            myViewHolder.ConfirmButton.setVisibility(View.VISIBLE);
        }

        if (AppController.kinArrayShow.get(i).isConfirmed()) {
            myViewHolder.layout.setBackgroundResource(R.drawable.rect_background_grey);
        }else{
            myViewHolder.layout.setBackgroundResource(R.drawable.rect_data_collection_red);
        }

//        if (arrayList.get(i).getNewData().equalsIgnoreCase("YES")){
//            myViewHolder.ConfirmButton.setVisibility(View.GONE);
//            myViewHolder.layout.setBackgroundResource(R.drawable.rect_background_grey);
//        }else {
//            myViewHolder.ConfirmButton.setVisibility(View.VISIBLE);
//            myViewHolder.layout.setBackgroundResource(R.drawable.rect_data_collection_red);
//        }
//        myViewHolder.layout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                row_index=i;
//                notifyDataSetChanged();
//            }
//        });
//        if (row_index==i){
//            myViewHolder.layout.setBackground(mContext.getResources().getDrawable(R.drawable.rect_background_grey));
//        }else {
////            myViewHolder.layout.setBackground(mContext.getResources().getDrawable(R.drawable.rect_background_grey));
//        }
    }



    @Override
    public int getItemCount() {
        return AppController.kinArrayShow.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView Name,ContactType;
        Button ConfirmButton;
        RelativeLayout layout;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.ownDetailViewRelative);
            Name =itemView.findViewById(R.id.nameOwnDetailTxt);
            ContactType = itemView.findViewById(R.id.contactTypeOwnDetailTxt);
            ConfirmButton = itemView.findViewById(R.id.confirmBtn);
        }
    }
}
