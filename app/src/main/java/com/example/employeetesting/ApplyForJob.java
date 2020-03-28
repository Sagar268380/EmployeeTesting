package com.example.employeetesting;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ApplyForJob extends AppCompatActivity {

EditText et_UserName,etLangauge,etPhoneNumber,etGooglePayNumber,etLocation;
Button btnApplyForJob;

String userName,userLangauge,userNumber,userGooglePayNumber,userLocation,mSpecial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_for_job);

        et_UserName=findViewById(R.id.et_user_name);
        etLangauge=findViewById(R.id.et_langauge);
        etPhoneNumber=findViewById(R.id.et_phone_number);
        etGooglePayNumber=findViewById(R.id.et_tez_number);
        etLocation=findViewById(R.id.et_location);

        btnApplyForJob=findViewById(R.id.btn_ApplyEmployee);

        Intent intent=getIntent();
        mSpecial=intent.getStringExtra("special");


        btnApplyForJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder=new AlertDialog.Builder(ApplyForJob.this);
                builder.setMessage(""+mSpecial);
                builder.setTitle("Special Instruction");
                builder.setIcon(R.drawable.apply_icon_for);
                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(ApplyForJob.this, "Successfully Apply ", Toast.LENGTH_SHORT).show();
                    }
                });

                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(ApplyForJob.this, "Cancel", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ApplyForJob.this,ApplyForJob.class));
                    }
                });
                AlertDialog alertDialog=builder.create();
                alertDialog.setCancelable(false);
                alertDialog.show();


            }
        });

    }
}
