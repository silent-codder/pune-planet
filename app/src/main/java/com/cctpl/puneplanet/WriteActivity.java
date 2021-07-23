package com.cctpl.puneplanet;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cctpl.puneplanet.Adapter.SaveDocumentAdapter;
import com.cctpl.puneplanet.Adapter.UserUploadDocAdapter;
import com.cctpl.puneplanet.model.DocData;
import com.cctpl.puneplanet.model.UploadDocData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class WriteActivity extends AppCompatActivity {

    Button mBtnDraft;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    String UserId;
    RecyclerView recyclerView;
    TextView mDocCount;
    int DocNumber;

    List<UploadDocData> uploadDocData;
    UserUploadDocAdapter userUploadDocAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);
        mBtnDraft = findViewById(R.id.btnDraft);
        mDocCount = findViewById(R.id.docCount);
        recyclerView = findViewById(R.id.recycleView);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        UserId = firebaseAuth.getCurrentUser().getUid();

        firebaseFirestore.collection("Users").document(UserId).collection("SaveDocument").orderBy("TimeStamp", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        DocNumber = value.size();
                        TextView draftCount = findViewById(R.id.draftCount);
                        mBtnDraft.setText("ड्राफ्ट (" + String.valueOf(DocNumber) + ")");
                    }
                });

        findViewById(R.id.btnNew).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WriteActivity.this,TextActivity.class));
            }
        });

        findViewById(R.id.btnDraft).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WriteActivity.this, DraftActivity.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        uploadDocData = new ArrayList<>();
        userUploadDocAdapter = new UserUploadDocAdapter(uploadDocData);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView,new RecyclerView.State(), userUploadDocAdapter.getItemCount());
        recyclerView.setAdapter(userUploadDocAdapter);

        firebaseFirestore.collection("Posts").orderBy("TimeStamp", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        DocNumber = value.size();
                        mDocCount.setText("प्रकाशित साहित्य (" + String.valueOf(DocNumber) + ")");
                        for (DocumentChange doc : value.getDocumentChanges()){
                            if (doc.getType() == DocumentChange.Type.ADDED){
                                String DocId = doc.getDocument().getId();
                                UploadDocData mDocData = doc.getDocument().toObject(UploadDocData.class).withId(DocId);
                                uploadDocData.add(mDocData);
                                userUploadDocAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
    }
}