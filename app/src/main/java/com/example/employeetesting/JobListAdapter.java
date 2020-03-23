package com.example.employeetesting;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class JobListAdapter extends RecyclerView.Adapter<JobListAdapter.JobListViewHolder>{

    Context mCtx;
    List<JobListModel> jobListModelList;

    public JobListAdapter(Context mCtx, List<JobListModel> jobListModelList) {
        this.mCtx = mCtx;
        this.jobListModelList = jobListModelList;
    }

    @NonNull
    @Override
    public JobListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.item_active_job_list_view, parent, false);
        JobListViewHolder jobListViewHolder = new JobListViewHolder(view);
        return jobListViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull JobListViewHolder holder, final int position) {

        JobListModel jobModel=jobListModelList.get(position);
        holder.txtCompantName.setText(jobModel.getCompany_name());
      //  holder.txtJobspecial.setText(jobModel.getJob_Special());
        holder.txtJobStartTime.setText(jobModel.getJob_Start_Time());
        holder.txtJobEndTime.setText(jobModel.getJob_End_Time());
        holder.txtJobDate.setText(jobModel.getJob_Date());
        //holder.txtBookingRadius.setText(jobModel.getJob_Booking_Radius());
        holder.txtAmount.setText(jobModel.getJob_Amount()+"/Per Day");
        holder.txtJobTitle.setText(jobModel.getJobTitle());
        //holder.txtJobDesc.setText(jobModel.getJob_Desc());
        //holder.txtEmployeeId.setText(jobModel.getUserId());

        final  int[] img={
                R.drawable.back,
                R.drawable.back2,
                R.drawable.back3,
                R.drawable.back4,
                R.drawable.back5,
                R.drawable.back6,
                R.drawable.back7,
                R.drawable.back8,
                R.drawable.back9
        };

      // holder.cardView.setCardBackgroundColor(Color.rgb(new Random().nextInt(256),new Random().nextInt(256),new Random().nextInt(256)));

        holder.linearLayout.setBackgroundResource(img[new Random().nextInt(img.length)]);
       //int [] androidColors=mCtx.getResources().getIntArray(R.array.androidcolor);
        //int randomAndroidColor=androidColors[new Random().nextInt(androidColors.length)];
       // holder.linearLayout.setCardBackgroundRe(randomAndroidColor);

                holder.btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String jobStartTime=jobListModelList.get(position).getJob_Start_Time();
                String jobEndTime=jobListModelList.get(position).getJob_End_Time();
                String jobDate=jobListModelList.get(position).getJob_Date();
                String jobAmout=jobListModelList.get(position).getJob_Amount()+"/";
                String jobTitle=jobListModelList.get(position).getJobTitle();
                String jobcompany=jobListModelList.get(position).getCompany_name();

               // Intent in=new Intent(mCtx,ApplyForJob.class);


                Intent intent=new Intent(mCtx,DetailsActivity.class);
                intent.putExtra("jobstarttime",jobStartTime);
                intent.putExtra("jobendtime",jobEndTime);
                intent.putExtra("jobdate",jobDate);
                intent.putExtra("jobamount",jobAmout);
                intent.putExtra("jobtitle",jobTitle);
                intent.putExtra("companyname",jobcompany);
              //  mCtx.startActivity(intent);
                      mCtx.startActivity(intent);
            }
        });

               holder.setItemClickListner(new ItemClickListner() {
            @Override
            public void onItemClickListner(View v, int position) {

                String jobStartTime=jobListModelList.get(position).getJob_Start_Time();
                String jobEndTime=jobListModelList.get(position).getJob_End_Time();
                String jobDate=jobListModelList.get(position).getJob_Date();
                String jobAmout=jobListModelList.get(position).getJob_Amount()+"/per day";
                String jobTitle=jobListModelList.get(position).getJobTitle();
                String jobcompany=jobListModelList.get(position).getCompany_name();

                Intent intent=new Intent(mCtx,DetailsActivity.class);
                intent.putExtra("jobstarttime",jobStartTime);
                intent.putExtra("jobendtime",jobEndTime);
                intent.putExtra("jobdate",jobDate);
                intent.putExtra("jobamount",jobAmout);
                intent.putExtra("jobtitle",jobTitle);
                intent.putExtra("companyname",jobcompany);
                mCtx.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return jobListModelList.size();
    }


    public class JobListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtCompantName,txtJobTitle,txtAmount,txtJobStartTime,txtJobEndTime,txtJobDate;//,txtJobDesc,txtJobEndTime,txtJobspecial,txtEmployeeId,txtBookingRadius,;
        ItemClickListner itemClickListner;
        public CardView cardView;

        LinearLayout linearLayout;
        TextView btnApply;
        public JobListViewHolder(@NonNull View itemView) {
            super(itemView);

            linearLayout=itemView.findViewById(R.id.lin_firstId);
            cardView = itemView.findViewById(R.id.card_view);
            btnApply=itemView.findViewById(R.id.btnApply);
            txtCompantName=itemView.findViewById(R.id.tv_compId2);
            txtAmount=itemView.findViewById(R.id.tv_amount);
            txtJobTitle=itemView.findViewById(R.id.tv_title);
            //txtBookingRadius=itemView.findViewById(R.id.tv_job_booking_radius);
            //txtJobDesc=itemView.findViewById(R.id.tv_desc);
            txtJobDate=itemView.findViewById(R.id.tv_date);
            txtJobEndTime=itemView.findViewById(R.id.tv_startTimeId);
            txtJobStartTime=itemView.findViewById(R.id.tv_totalTimeId);
            //txtJobspecial=itemView.findViewById(R.id.tv_bleck_pant);
            //txtEmployeeId=itemView.findViewById(R.id.tv_employer_userId);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            this.itemClickListner.onItemClickListner(view,getLayoutPosition());
        }

        public void setItemClickListner(ItemClickListner ic){
            this.itemClickListner=ic;
        }

    }
}
