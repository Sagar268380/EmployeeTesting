package com.example.employeetesting;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class ImageFor extends AppCompatActivity {

    Button front, back, uploadp;
    int ImageBack = 1, CAMER = 2;
    StorageReference storageReference;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    ProgressDialog progressDialog;
    String s = "front", s1 = "back";
    boolean d = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imagefor);

        progressDialog = new ProgressDialog(ImageFor.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("Uploading file");

        front = findViewById(R.id.front);
        back = findViewById(R.id.back);

        storageReference = FirebaseStorage.getInstance().getReference().child("Imaolder");

        front.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // UploadData(view);
                choseImage();

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choseImage();
            }
        });
        Log.d("s1", "s1 " + d);

    }


    private void choseImage() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .start(ImageFor.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        progressDialog.show();
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri ImageData = result.getUri();

                final StorageReference Imagename = storageReference.child("image" + ImageData.getLastPathSegment());
                Imagename.putFile(ImageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(ImageFor.this, "Uplaod", Toast.LENGTH_SHORT).show();

                        Imagename.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                progressDialog.dismiss();
                                imageFo image = new imageFo(String.valueOf(uri));

                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("ImeUrl");
                                databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(image);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
                    }

                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        int curretprogress = (int) (100 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        progressDialog.setProgress(curretprogress);
                    }
                });

            }

        } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
            //Exception error = result.getError();
        }
    }
}


   /* public void UploadData(View view){
         Intent intent=new Intent();
         intent.setAction(intent.ACTION_GET_CONTENT);
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .start(ImageFor.this);
         intent.setType("image/*");
         startActivityForResult(intent,ImageBack);

    }*/


   // @Override
   /* protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        progressDialog.show();
        if(requestCode==ImageBack){
            if(resultCode==RESULT_OK && data!=null){
                Uri ImageData =data.getData();

                final StorageReference Imagename=storageReference.child("image"+ImageData.getLastPathSegment());
                Imagename.putFile(ImageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                     public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(ImageFor.this, "Uplaod", Toast.LENGTH_SHORT).show();

                        Imagename.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                progressDialog.dismiss();
                                  imageFo image=new imageFo(String.valueOf(uri));

                                  DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("ImeUrl");
                                  databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(image);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                            }
                        });
                    }

                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                      int curretprogress=(int) (100*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                         progressDialog.setProgress(curretprogress);
                    }
                });
            }
        }
    }*/


