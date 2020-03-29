package com.example.employeetesting;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.icu.text.CaseMap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.protobuf.compiler.PluginProtos;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RegisterActivity extends AppCompatActivity {


    EditText et_f_name, et_l_name, et_post_code, et_phone_number,
             et_account_number, et_address,
            et_email,et_paytm_number,et_tez_number,et_IFSC_CODE,et_bank_name;
    ProgressDialog progressDialog;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    DatabaseReference databaseReference;
    String userID;
    Button btnRegister;

    private ImageView userImage;

    private Uri imageUri = null;
    private StorageReference storageReference;

    private static final int ImageBack=1;

    private Uri filepath1,filepath2,filepath3;
    EditText et_AdharNumber;
    TextView adharCard,passbook;
    Uri profileUrl,adharUrl,passbookUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("Uploading file");
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Register");
        userImage = findViewById(R.id.ProfileImage);
        et_f_name = findViewById(R.id.et_f_name);
        et_l_name = findViewById(R.id.et_l_name);
        et_phone_number = findViewById(R.id.et_phone_number);
        et_email = findViewById(R.id.et_email);
        et_address=findViewById(R.id.et_address);
        et_paytm_number=findViewById(R.id.et_paytm_number);
        et_tez_number=findViewById(R.id.et_tez_number);
        et_account_number = findViewById(R.id.et_account_number);
        et_IFSC_CODE=findViewById(R.id.IFSCCODE);
        et_bank_name=findViewById(R.id.et_bank_name);
        btnRegister=findViewById(R.id.btn_registerEmployee);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = fAuth.getCurrentUser().getUid();
        et_AdharNumber=findViewById(R.id.et_aadhar_number);
        adharCard=findViewById(R.id.adharCard);
        passbook=findViewById(R.id.passbook);
        storageReference = FirebaseStorage.getInstance().getReference();

        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent();
                in.setType("image/*");
                in.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(in,"Select Profile"),1);
            }
        });
        adharCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent();
                in.setType("image/*");
                in.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(in,"Select Adhar Card Image"),2);

            }
        });
        passbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent();
                in.setType("image/*");
                in.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(in,"Select Passbook Image"),3);
            }
        });


     /*  userImage.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View view) {
                                             if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                                                 if (ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                                     ActivityCompat.requestPermissions(RegisterActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                                                     //Toast.makeText(RegisterActivity.this, "Permission Denied", Toast.LENGTH_LONG).show();
                                                 } else {
                                                     choseImage(view);
                                                 }
                                             } else {
                                                 //choseImage();
                                             }
                                         }
                                     }
        );*/

        btnRegister.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(et_f_name.getText().toString().isEmpty()|| et_l_name.getText().toString().isEmpty()
                        || et_account_number.getText().toString().isEmpty()|| et_paytm_number.getText().toString().isEmpty() || et_tez_number.getText().toString().isEmpty()
                        || et_bank_name.getText().toString().isEmpty()|| et_email.getText().toString().isEmpty() || et_IFSC_CODE.getText().toString().isEmpty()
                        ||et_phone_number.getText().toString().isEmpty()|| et_address.getText().toString().isEmpty() || et_AdharNumber.getText().toString().isEmpty())
                {
                    Toast.makeText(RegisterActivity.this, "Fill the required Details", Toast.LENGTH_SHORT).show();
                    return;
                }
                else
                    {
                        progressDialog = new ProgressDialog(RegisterActivity.this);
                        progressDialog.setTitle("Uploading");
                        progressDialog.show();
                        if (filepath1 != null)
                        {
                            StorageReference reference = storageReference.child("Images/" + UUID.randomUUID().toString());
                            reference.putFile(filepath1).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                    Toast.makeText(RegisterActivity.this, "Profile done", Toast.LENGTH_SHORT).show();
                                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            profileUrl=uri;
                                        }
                                    });
                                 }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle unsuccessful uploads
                                    // ...
                                    Toast.makeText(RegisterActivity.this, "in error "+exception, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        if (filepath2 != null)
                        {
                            StorageReference reference = storageReference.child("Images/" + UUID.randomUUID().toString());
                            reference.putFile(filepath2).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Toast.makeText(RegisterActivity.this, "Adhar Done", Toast.LENGTH_LONG).show();
                                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            adharUrl=uri;
                                        }
                                    });
                                }
                            });
                        }
                        if (filepath3 != null)
                        {
                            StorageReference reference = storageReference.child("Images/" + UUID.randomUUID().toString());
                            reference.putFile(filepath3).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Toast.makeText(RegisterActivity.this, "Passbook Done", Toast.LENGTH_SHORT).show();
                                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri)
                                        {
                                            passbookUrl=uri;

                                            DocumentReference docRef = fStore.collection("users").document(userID);
                                            //DatabaseReference reference= FirebaseDatabase.getInstance().getReference("users").child(userID);
                                            Map<String,Object> user = new HashMap<>();

                                            user.put("first",et_f_name.getText().toString());
                                            user.put("last",et_l_name.getText().toString());
                                            user.put("email",et_email.getText().toString());
                                            user.put("accountNumber",et_account_number.getText().toString());
                                            user.put("PaytmNumber",et_paytm_number.getText().toString());
                                            user.put("Address", et_address.getText().toString());
                                            user.put("IFSCCODE",et_IFSC_CODE.getText().toString());
                                            user.put("BankName",et_bank_name.getText().toString());
                                            user.put("TezNumber",et_tez_number.getText().toString());
                                            user.put("PhoneNumber",et_phone_number.getText().toString());
                                            user.put("AdharNumber",et_AdharNumber.getText().toString());
                                            user.put("ProfileUrl",passbookUrl.toString());
                                            user.put("AdharUrl",adharUrl.toString());
                                            user.put("PassbookUrl",passbookUrl.toString());
                                            // FileUplode();
                                            docRef.set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        progressDialog.dismiss();
                                                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                                                        finish();
                                                    }
                                                }
                                            });
                                        }
                                    });

                                }
                            });
                        }

                }
            }
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            Bitmap bitmap;
            filepath1 = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath1);
                userImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        else if (requestCode == 2 && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            Bitmap bitmap;
            filepath2 = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (requestCode == 3 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Bitmap bitmap;
            filepath3 = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath3);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    /*

      public void  choseImage(View view){
                 CropImage.activity()
               .setGuidelines(CropImageView.Guidelines.ON)
               .setAspectRatio(1, 1)
               .start(RegisterActivity.this);
   }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        progressDialog.show();
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode==RESULT_OK){
                Uri ImageData=result.getUri();

                final StorageReference Imagename = storageReference.child("image"+ImageData.getLastPathSegment());

                Imagename.putFile(ImageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Imagename.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                progressDialog.dismiss();
                                DatabaseReference imagestore=FirebaseDatabase.getInstance().getReference().child("Image").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                HashMap<String,String> hashMap=new HashMap<>();
                                hashMap.put("image", String.valueOf(uri));

                                imagestore.setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                      //  Toast.makeText(RegisterActivity.this, "Finally Complete", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    }
                });
            }
        }
    }*/
}
