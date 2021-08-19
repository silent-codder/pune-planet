package com.cctpl.puneplanet.Fragments;

import android.app.DatePickerDialog;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cctpl.puneplanet.R;
import com.cctpl.puneplanet.TextActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;


public class AccountFragment extends Fragment {

    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    String UserId,Gender;

    EditText mFirstNameMarathi,mFirstNameEng,mLastNameMarathi,mLastNameEng,mNickName;
    TextView mDate;
    RadioGroup mRadioGrp;
    ImageView mBtnPickDate;
    Button mBtnUpload;
    Calendar calendar;
    int day,month,year;
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        UserId = firebaseAuth.getCurrentUser().getUid();

        mFirstNameMarathi = view.findViewById(R.id.firstName);
        mFirstNameEng = view.findViewById(R.id.firstNameInEnglish);
        mLastNameMarathi = view.findViewById(R.id.lastName);
        mLastNameEng = view.findViewById(R.id.lastNameInEnglish);
        mNickName = view.findViewById(R.id.nickName);
        mDate = view.findViewById(R.id.date);
        mBtnPickDate = view.findViewById(R.id.datePick);
        mRadioGrp = view.findViewById(R.id.radioGrp);
        mBtnUpload = view.findViewById(R.id.btnUpload);
        progressBar = view.findViewById(R.id.loader);

        calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);

        mRadioGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                Gender = radioButton.getText().toString();
                Toast.makeText(getContext(), Gender, Toast.LENGTH_SHORT).show();
            }
        });

        mBtnPickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {

                        month += 1;
                        mDate.setText(day + "-" + month + "-" + year);

                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        mBtnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBtnUpload.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                String firstNameInMarathi = mFirstNameMarathi.getText().toString();
                String firstNameInEng = mFirstNameEng.getText().toString();
                String lastNameInMarathi = mLastNameMarathi.getText().toString();
                String lastNameInEng = mLastNameEng.getText().toString();
                String nickName = mNickName.getText().toString();
                String birthDate = mDate.getText().toString();

                updateInfo(firstNameInMarathi,firstNameInEng,lastNameInMarathi,lastNameInEng,nickName,Gender,birthDate);
            }
        });


        return view;
    }

    private void updateInfo(String firstNameInMarathi, String firstNameInEng, String lastNameInMarathi, String lastNameInEng, String nickName,
                            String gender, String birthDate) {

        HashMap<String,Object> map = new HashMap<>();
        map.put("FirstNameInMarathi",firstNameInMarathi);
        map.put("FirstNameInEnglish",firstNameInEng);
        map.put("LastNameInMarathi",lastNameInMarathi);
        map.put("LastNameInEnglish",lastNameInEng);
        map.put("NickName",nickName);
        map.put("Gender",gender);
        map.put("BirthDate",birthDate);
        map.put("TimeStamp",System.currentTimeMillis());

        firebaseFirestore.collection("Users").document(UserId)
                .set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    mBtnUpload.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "माहिती अपलोड झाली", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mBtnUpload.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}