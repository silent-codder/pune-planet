package com.cctpl.puneplanet.Adapter;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.cctpl.puneplanet.DocReadingActivity;
import com.cctpl.puneplanet.R;
import com.cctpl.puneplanet.model.UploadDocData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class AllDocAdapter extends RecyclerView.Adapter<AllDocAdapter.ViewHolder> {

    Context context;
    int count;
    FirebaseFirestore firebaseFirestore;
    ProgressDialog progressDialog;
    List<UploadDocData>uploadDocData;
    public AllDocAdapter(List<UploadDocData> uploadDocData) {
        this.uploadDocData = uploadDocData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_doc_view,parent,false);
        context = parent.getContext();
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading..");
        firebaseFirestore = FirebaseFirestore.getInstance();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(uploadDocData.get(position).getTitle());
        holder.description.setText(uploadDocData.get(position).getDescription());
        Date d = new Date(uploadDocData.get(position).getTimeStamp());

//        @SuppressLint("SimpleDateFormat") DateFormat dateFormat1 = new SimpleDateFormat("dd MMM yyyy");
//        String Date = dateFormat1.format(d.getTime());
//        holder.date.setText(Date);

        String CoverImgUrl = uploadDocData.get(position).getCoverImg();
        if (!TextUtils.isEmpty(CoverImgUrl)){
            Picasso.get().load(CoverImgUrl).into(holder.imageView);
        }

        firebaseFirestore.collection("Posts").document(uploadDocData.get(position).UploadDocId)
                .collection("ReadingCount").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (!value.isEmpty()){
                    count = value.size();

                    if (count > 99){
                        holder.count.setText("100+ वेळा वाचले");
                    }else if (count > 9){
                        holder.count.setText("10+ वेळा वाचले");
                    }else if (count > 999){
                        holder.count.setText("1K+ वेळा वाचले");
                    }else {
                        holder.count.setText(String.valueOf(count) + " वेळा वाचले");
                    }
                }
            }
        });
        holder.imageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                progressDialog.setCanceledOnTouchOutside(false);
                HashMap<String,Object>map = new HashMap<>();
                map.put("TimeStamp",System.currentTimeMillis());
                firebaseFirestore.collection("Posts").document(uploadDocData.get(position).UploadDocId)
                        .collection("ReadingCount").add(map).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        AppCompatActivity activity = (AppCompatActivity) v.getContext();
                        Intent intent = new Intent(context, DocReadingActivity.class);
                        intent.putExtra("title",uploadDocData.get(position).getTitle());
                        intent.putExtra("description",uploadDocData.get(position).getDescription());
                        intent.putExtra("userId",uploadDocData.get(position).getUserId());
                        activity.startActivity(intent);
                        progressDialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Failed to open !! Try again", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });

            }
        });

        //Get UserName here

        firebaseFirestore.collection("Users").document(uploadDocData.get(position).getUserId()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            holder.userName.setText(task.getResult().getString("UserName"));
                        }
                    }
                });
    }

    @Override
    public int getItemCount() {
        return uploadDocData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title,count,description,userName;
        ImageView imageView;
        RelativeLayout imageLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            userName = itemView.findViewById(R.id.userName);
            description = itemView.findViewById(R.id.description);
            count = itemView.findViewById(R.id.readingCount);
            imageView = itemView.findViewById(R.id.coverImg);
            imageLayout = itemView.findViewById(R.id.relativeLayout);
        }
    }
}
