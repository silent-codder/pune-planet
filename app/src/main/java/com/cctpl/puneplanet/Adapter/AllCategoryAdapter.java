package com.cctpl.puneplanet.Adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.cctpl.puneplanet.Fragments.CategoryDocFragment;
import com.cctpl.puneplanet.R;
import com.cctpl.puneplanet.model.AllCategory;
import com.squareup.picasso.Picasso;

public class AllCategoryAdapter extends RecyclerView.Adapter<AllCategoryAdapter.ViewHolder> {

    private AllCategory[] allCategories;

    public AllCategoryAdapter(AllCategory[] allCategories) {
        this.allCategories = allCategories;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_view,parent,false);
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
    }

    @Override
    public int getItemCount() {
        return allCategories.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView categoryTxt;
        RelativeLayout relativeLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img);
            categoryTxt = itemView.findViewById(R.id.category);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);
        }
    }
}
