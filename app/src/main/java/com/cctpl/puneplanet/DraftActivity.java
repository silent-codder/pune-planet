package com.cctpl.puneplanet;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import com.cctpl.puneplanet.Adapter.SaveDocumentAdapter;
import com.cctpl.puneplanet.model.DocData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DraftActivity extends AppCompatActivity {

    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    String UserId;
    int DocNumber;
    RecyclerView recyclerView;

    SaveDocumentAdapter saveDocumentAdapter;
    List<DocData> docData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draft);
        recyclerView = findViewById(R.id.recycleView);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        UserId = firebaseAuth.getCurrentUser().getUid();

    }

    @Override
    protected void onStart() {
        super.onStart();
        docData = new ArrayList<>();
        saveDocumentAdapter = new SaveDocumentAdapter(docData);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView,new RecyclerView.State(), saveDocumentAdapter.getItemCount());
        recyclerView.setAdapter(saveDocumentAdapter);

        firebaseFirestore.collection("Users").document(UserId).collection("SaveDocument").orderBy("TimeStamp", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        DocNumber = value.size();
                        TextView draftCount = findViewById(R.id.draftCount);
                        draftCount.setText("ड्राफ्ट (" + String.valueOf(DocNumber) + ")");
                        for (DocumentChange doc : value.getDocumentChanges()){
                            if (doc.getType() == DocumentChange.Type.ADDED){
                                String DocId = doc.getDocument().getId();
                                DocData mDocData = doc.getDocument().toObject(DocData.class).withId(DocId);
                                docData.add(mDocData);
                                saveDocumentAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
    }
}