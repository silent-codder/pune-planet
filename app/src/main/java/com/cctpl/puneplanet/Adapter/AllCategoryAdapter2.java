package com.cctpl.puneplanet.Adapter;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.cctpl.puneplanet.Fragments.CategoryDocFragment;
import com.cctpl.puneplanet.R;
import com.cctpl.puneplanet.model.AllCategory;
import com.cctpl.puneplanet.model.UploadDocData;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

public class AllCategoryAdapter2 extends RecyclerView.Adapter<AllCategoryAdapter2.ViewHolder> {

    private AllCategory[] allCategories;
    FirebaseFirestore firebaseFirestore;

    public AllCategoryAdapter2(AllCategory[] allCategories) {
        this.allCategories = allCategories;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_doc_for_you_view,parent,false);
        firebaseFirestore = FirebaseFirestore.getInstance();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Picasso.get().load(allCategories[position].getImgId()).into(holder.imageView);
        holder.categoryTxt.setText(allCategories[position].getCategory());
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Fragment fragment = new CategoryDocFragment();
                Bundle bundle = new Bundle();
                bundle.putString("Category",allCategories[position].getCategory());
                fragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack(null).commit();
            }
        });

        firebaseFirestore.collection("Posts").whereEqualTo("SubCategory",allCategories[position].getCategory())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        int DocNumber = value.size();
                        holder.docCount.setText("" + String.valueOf(DocNumber) + " कथा");
                    }
                });
    }

    @Override
    public int getItemCount() {
        return allCategories.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView categoryTxt,docCount;
        RelativeLayout relativeLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img);
            categoryTxt = itemView.findViewById(R.id.category);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);
            docCount = itemView.findViewById(R.id.docCount);
        }
    }
}
