package com.cctpl.puneplanet.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.cctpl.puneplanet.Adapter.AllDocAdapter;
import com.cctpl.puneplanet.Adapter.ForYouDocAdapter;
import com.cctpl.puneplanet.R;
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

public class CategoryDocFragment extends Fragment {

    String Category;
    RecyclerView recyclerView;
    TextView textView;

    FirebaseFirestore firebaseFirestore;
    AllDocAdapter allDocAdapter;
    List<UploadDocData> uploadDocData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_doc, container, false);
        recyclerView = view.findViewById(R.id.recycleView);
        textView = view.findViewById(R.id.category);
        firebaseFirestore = FirebaseFirestore.getInstance();
        Bundle bundle = this.getArguments();
        if (bundle!=null){
            Category = bundle.getString("Category");
            textView.setText(Category);
        }


        uploadDocData = new ArrayList<>();
        allDocAdapter = new AllDocAdapter(uploadDocData);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView,new RecyclerView.State(), allDocAdapter.getItemCount());
        recyclerView.setAdapter(allDocAdapter);

        firebaseFirestore.collection("Posts").whereEqualTo("SubCategory",Category)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        int DocNumber = value.size();
                        if (DocNumber != 0){
                            view.findViewById(R.id.lottie).setVisibility(View.GONE);
                            view.findViewById(R.id.emptyText).setVisibility(View.GONE);
                        }
                        textView.setText(Category + " (" + String.valueOf(DocNumber) + ")");
                        for (DocumentChange doc : value.getDocumentChanges()){
                            if (doc.getType() == DocumentChange.Type.ADDED){
                                String DocId = doc.getDocument().getId();
                                UploadDocData mDocData = doc.getDocument().toObject(UploadDocData.class).withId(DocId);
                                uploadDocData.add(mDocData);
                                allDocAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });

        return view;
    }
}