package com.cctpl.puneplanet.SubFragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cctpl.puneplanet.Adapter.AllCategoryAdapter;
import com.cctpl.puneplanet.Adapter.AllCategoryAdapter2;
import com.cctpl.puneplanet.Adapter.AllDocAdapter;
import com.cctpl.puneplanet.Adapter.ForYouDocAdapter;
import com.cctpl.puneplanet.Adapter.ForYouFavDocAdapter;
import com.cctpl.puneplanet.Adapter.UserUploadDocAdapter;
import com.cctpl.puneplanet.R;
import com.cctpl.puneplanet.model.AllCategory;
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
import java.util.Random;


public class ForYouFragment extends Fragment {

    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    ForYouDocAdapter forYouDocAdapter;
    ForYouFavDocAdapter forYouFavDocAdapter;
    AllDocAdapter allDocAdapter;
    List<UploadDocData> uploadDocData;
    List<UploadDocData> uploadFavDocData;
    List<UploadDocData> allDocData;
    RecyclerView recyclerView1,recyclerView2,recyclerView3,recyclerView4;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_for_you, container, false);
        recyclerView1 = view.findViewById(R.id.recycleView1);
        recyclerView2 = view.findViewById(R.id.recycleView);
        recyclerView3 = view.findViewById(R.id.recycleView3);
        recyclerView4 = view.findViewById(R.id.recycleView4);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        loadDataForYou();
        loadDataForYouFav();
        loadAllData();

        return view;
    }

    private void loadAllData() {
        allDocData = new ArrayList<>();
        allDocAdapter = new AllDocAdapter(allDocData);
        recyclerView4.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView4.getLayoutManager().smoothScrollToPosition(recyclerView4,new RecyclerView.State(), allDocAdapter.getItemCount());
        recyclerView4.setAdapter(allDocAdapter);

        firebaseFirestore.collection("Posts")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

//                        DocNumber = value.size();
//                        mDocCount.setText("प्रकाशित साहित्य (" + String.valueOf(DocNumber) + ")");

                        for (DocumentChange doc : value.getDocumentChanges()){
                            if (doc.getType() == DocumentChange.Type.ADDED){
                                String DocId = doc.getDocument().getId();
                                UploadDocData mDocData = doc.getDocument().toObject(UploadDocData.class).withId(DocId);
                                allDocData.add(mDocData);
                                allDocAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
    }


    private void loadDataForYouFav() {
        uploadFavDocData = new ArrayList<>();
        forYouFavDocAdapter = new ForYouFavDocAdapter(uploadFavDocData);
        recyclerView3.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        recyclerView3.getLayoutManager().smoothScrollToPosition(recyclerView3,new RecyclerView.State(), forYouFavDocAdapter.getItemCount());
        recyclerView3.setAdapter(forYouFavDocAdapter);

        firebaseFirestore.collection("Posts").orderBy("TimeStamp", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                        DocNumber = value.size();
//                        mDocCount.setText("प्रकाशित साहित्य (" + String.valueOf(DocNumber) + ")");
                        for (DocumentChange doc : value.getDocumentChanges()){
                            if (doc.getType() == DocumentChange.Type.ADDED){
                                String DocId = doc.getDocument().getId();
                                UploadDocData mDocData = doc.getDocument().toObject(UploadDocData.class).withId(DocId);
                                uploadFavDocData.add(mDocData);
                                forYouFavDocAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
    }

    private void loadDataForYou() {
        uploadDocData = new ArrayList<>();
        forYouDocAdapter = new ForYouDocAdapter(uploadDocData);
        recyclerView1.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        recyclerView1.getLayoutManager().smoothScrollToPosition(recyclerView1,new RecyclerView.State(), forYouDocAdapter.getItemCount());
        recyclerView1.setAdapter(forYouDocAdapter);

        firebaseFirestore.collection("Posts").orderBy("TimeStamp", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                        DocNumber = value.size();
//                        mDocCount.setText("प्रकाशित साहित्य (" + String.valueOf(DocNumber) + ")");
                        for (DocumentChange doc : value.getDocumentChanges()){
                            if (doc.getType() == DocumentChange.Type.ADDED){
                                String DocId = doc.getDocument().getId();
                                UploadDocData mDocData = doc.getDocument().toObject(UploadDocData.class).withId(DocId);
                                uploadDocData.add(mDocData);
                                forYouDocAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();

        AllCategory[] allCategories = new AllCategory[]{
                new AllCategory("थरारक गोष्टी",R.drawable.horrer),
                new AllCategory("भयकथा",R.drawable.horrer_1),
                new AllCategory("हास्य कथा",R.drawable.joker),
                new AllCategory("प्रेमकथा",R.drawable.love),
                new AllCategory("रहस्य कथा",R.drawable.horrer_2),
                new AllCategory("अन्य कथा",R.drawable.book),
                new AllCategory("लघु कथा",R.drawable.book_1),
                new AllCategory("विज्ञान कथा",R.drawable.science),
                new AllCategory("ऐतिहासिक कथा",R.drawable.historical),
                new AllCategory("पाककला",R.drawable.vegetables),
                new AllCategory("जीवन-चरित्र",R.drawable.ghandi),
                new AllCategory("बालकथा",R.drawable.boy),
//                new AllCategory("अनुभव",R.drawable.experince),
//                new AllCategory("कविता",R.drawable.poem),
                new AllCategory("बोथकथा",R.drawable.nature),
                new AllCategory("प्रवास वर्णन",R.drawable.road)
        };




        AllCategoryAdapter2 adapter = new AllCategoryAdapter2(allCategories);
        recyclerView2.setHasFixedSize(true);
        recyclerView2.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
//        recyclerView2.getLayoutManager().smoothScrollToPosition(recyclerView2,new RecyclerView.State(), adapter.getItemCount());
        recyclerView2.setAdapter(adapter);
    }

}