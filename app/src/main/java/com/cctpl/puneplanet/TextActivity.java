package com.cctpl.puneplanet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Locale;

public class TextActivity extends AppCompatActivity {

    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    String UserId,DocId,radioTxt,chipTxt,checkTxt,CoverImgUrl;
    
    EditText mTitle,mDescription;
    TextView mUploadBtn;
    ImageView mBtnSave,mBtnList,coverImg;
    Uri profileImgUri;
    StorageReference storageReference;
    ProgressDialog progressDialog;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        UserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        progressDialog = new ProgressDialog(this);
        
        mTitle = findViewById(R.id.title);
        mDescription = findViewById(R.id.description);
        mBtnSave = findViewById(R.id.btnSave);

        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            DocId = bundle.getString("DocId");
            RetrieveSaveDoc(DocId);
        }
        
        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Title = mTitle.getText().toString().trim();
                String Description = mDescription.getText().toString();

                if (TextUtils.isEmpty(Title)){
                    mTitle.setError("शीर्षक");
                }else if (TextUtils.isEmpty(Description)){
                    mDescription.setError("काही तरी लिहा..");
                }else {
                    SaveDoc(Title,Description);
                }
            }
        });

        findViewById(R.id.btnComplete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Title = mTitle.getText().toString().trim();
                String Description = mDescription.getText().toString();

                if (TextUtils.isEmpty(Title)){
                    mTitle.setError("शीर्षक");
                }else if (TextUtils.isEmpty(Description)){
                    mDescription.setError("काही तरी लिहा.");
                }else{
                    showBottomSheet();
                }

            }
        });

    }

    private void RetrieveSaveDoc(String docId) {
        firebaseFirestore.collection("Users").document(UserId).collection("SaveDocument")
                .document(docId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    String Title = task.getResult().getString("Title").toString();
                    String Description = task.getResult().getString("Description").toString();
                    mTitle.setText(Title);
                    mDescription.setText(Description);
                }
            }
        });
    }

    private void SaveDoc(String title, String description) {
        HashMap<String,Object> map = new HashMap<>();
        map.put("Title",title);
        map.put("Description",description);
        map.put("Status","SAVE");
        map.put("TimeStamp",System.currentTimeMillis());
        map.put("UserId",UserId);

        if (!TextUtils.isEmpty(DocId)){
            firebaseFirestore.collection("Users").document(UserId)
                    .collection("SaveDocument").document(DocId).update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(TextActivity.this, "Save Successfully..", Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            firebaseFirestore.collection("Users").document(UserId)
                    .collection("SaveDocument").add(map).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(TextActivity.this, "Save successfully..", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }


    }

    private void showBottomSheet() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.upload_bottom_sheet);
        bottomSheetDialog.show();

        coverImg = bottomSheetDialog.findViewById(R.id.coverImg);
        TextView btnSelectImg = bottomSheetDialog.findViewById(R.id.btnSelectImg);
        RadioGroup radioGroup = bottomSheetDialog.findViewById(R.id.radioGrp);
        ChipGroup chipGroup = bottomSheetDialog.findViewById(R.id.chipGroup);
        CheckBox checkBox = bottomSheetDialog.findViewById(R.id.checkbox);
        Button btnUpload = bottomSheetDialog.findViewById(R.id.btnUpload);
        if (checkBox.isChecked()){
            checkTxt = "Accept";
        }

        btnSelectImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadImg();
            }
        });


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                radioTxt = radioButton.getText().toString();
                Toast.makeText(TextActivity.this,radioTxt, Toast.LENGTH_SHORT).show();
            }
        });

        chipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {
                Chip chip = (Chip) group.findViewById(checkedId);
                chipTxt = chip.getText().toString();
                Toast.makeText(TextActivity.this, chipTxt, Toast.LENGTH_SHORT).show();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Title = mTitle.getText().toString().trim();
                String Description = mDescription.getText().toString();

                if (TextUtils.isEmpty(Title)){
                    mTitle.setError("शीर्षक");
                }else if (TextUtils.isEmpty(Description)){
                    mDescription.setError("काही तरी लिहा.");
                }else if (TextUtils.isEmpty(radioTxt)){
                    Toast.makeText(TextActivity.this, "प्रकार निवडा", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(chipTxt)){
                    Toast.makeText(TextActivity.this, "श्रेणी निवडा", Toast.LENGTH_SHORT).show();
                }else if (!checkBox.isChecked()){
                    checkBox.setError("अटी मान्य करा");
                }else{
                    UploadDoc(Title,Description,radioTxt,chipTxt);
                }
            }
        });

    }

    private void UploadDoc(String title, String description, String category, String subCategory) {
        HashMap<String ,Object> map = new HashMap<>();
        map.put("Title",title);
        map.put("Description",description);
        map.put("Status","Complete");
        map.put("TimeStamp",System.currentTimeMillis());
        map.put("UserId",UserId);
        map.put("Category",category);
        map.put("SubCategory",subCategory);
        map.put("Agreement","Accept");
        map.put("CoverImg",CoverImgUrl);

        firebaseFirestore.collection("Posts").add(map).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful()){
                    Toast.makeText(TextActivity.this, "साहित्य पुर्णपणे अपलोड केले गेले आहे", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(TextActivity.this, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void UploadImg() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setOutputCompressQuality(10)
                .setAspectRatio(1,1)
                .start(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                profileImgUri = result.getUri();
                coverImg.setImageURI(profileImgUri);
                AddImg();
                progressDialog.setMessage("Loading...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(this, "Error : " + error, Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void AddImg() {

        StorageReference profileImgPath = storageReference.child("CoverImg").child(System.currentTimeMillis() + ".jpg");

        profileImgPath.putFile(profileImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                profileImgPath.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {

                        CoverImgUrl = task.getResult().toString();
                        progressDialog.dismiss();
//                        HashMap<String,Object> map = new HashMap<>();
//                        map.put("ProfileImgUrl" , ProfileUri);

//                        firebaseFirestore.collection("Users").document(UserId).update(map)
//                                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Void> task) {
////                                        progressDialog.dismiss();
//                                        Toast.makeText(getApplicationContext(), "Upload Successfully..", Toast.LENGTH_SHORT).show();
////                                        progressBar.setVisibility(View.GONE);
////                                        Fragment fragment = new SettingFragment();
////                                        getFragmentManager().beginTransaction().replace(R.id.container,fragment).commit();
//                                    }
//                                }).addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Toast.makeText(getApplicationContext(), "Storage error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                            }
//                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

    }
}