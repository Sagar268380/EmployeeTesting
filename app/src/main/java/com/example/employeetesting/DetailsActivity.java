package com.example.employeetesting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DetailsActivity extends AppCompatActivity {

    TextView title,amount,startDate,endDate,company,date,description,specialInstruction;
    Button btnApply;
    String mTitle,mAmount,mStartdate,mEndDate,mCompany,mDate,mdescription,mSpecialInstruction;
    DatabaseReference databaseReference,ds1;
    FirebaseDatabase firebaseDatabase;
    String user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Details");

        title=findViewById(R.id.tv_title);
        amount=findViewById(R.id.tv_amount);
        startDate=findViewById(R.id.tv_totalTimeId);
        endDate=findViewById(R.id.tv_startTimeId);
        company=findViewById(R.id.tv_compId2);
        date=findViewById(R.id.tv_dat);
        btnApply=findViewById(R.id.btnApply);
        description=findViewById(R.id.tv_desc);
        specialInstruction=findViewById(R.id.tv_special_instruction);

        Intent intent=getIntent();
        mTitle=intent.getStringExtra("jobtitle");
        mAmount=intent.getStringExtra("jobamount");
        mStartdate=intent.getStringExtra("jobstarttime");
        mEndDate=intent.getStringExtra("jobendtime");
        mCompany=intent.getStringExtra("companyname");
        mDate=intent.getStringExtra("jobdate");


        firebaseDatabase= FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference().child("Jobs");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        user = ds.getKey();
                        ds1 = firebaseDatabase.getInstance().getReference().child("Jobs").child(user);

                        ds1.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshots) {
                                if (dataSnapshots.exists()) {
                                    for (DataSnapshot ds1 : dataSnapshots.getChildren()) {
                                        String jobs = ds1.getKey();
                                        mdescription = dataSnapshots.child(jobs).child("Job_Desc").getValue().toString();
                                       // mSpecialInstruction = dataSnapshot.child(jobs).child("Job_Special").getValue().toString();

                                        description.setText(mdescription);
                                        specialInstruction.setText(mSpecialInstruction);
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        title.setText(mTitle);
        amount.setText(mAmount);
        startDate.setText(mStartdate);
        endDate.setText(mEndDate);
        date.setText(mDate);
        company.setText(mCompany);
        description.setText(mdescription);
        specialInstruction.setText(mSpecialInstruction);

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1=new Intent(DetailsActivity.this,ApplyForJob.class);
                startActivity(intent1);
            }
        });
    }
}
