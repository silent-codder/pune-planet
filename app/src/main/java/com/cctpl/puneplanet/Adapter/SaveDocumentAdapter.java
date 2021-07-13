package com.cctpl.puneplanet.Adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.cctpl.puneplanet.MainActivity;
import com.cctpl.puneplanet.R;
import com.cctpl.puneplanet.TextActivity;
import com.cctpl.puneplanet.model.DocData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SaveDocumentAdapter extends RecyclerView.Adapter<SaveDocumentAdapter.ViewHolder>{

    List<DocData> docData;
    Context context;
    String UserId,DocId;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;

    public SaveDocumentAdapter(List<DocData> docData) {
        this.docData = docData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.draft_view,parent,false);
        context = parent.getContext();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserId = docData.get(position).getUserId();
        DocId = docData.get(position).DocId;
        holder.mTitle.setText(docData.get(position).getTitle());
        holder.mDescription.setText(docData.get(position).getDescription());
        Date d = new Date(docData.get(position).getTimeStamp());
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat1 = new SimpleDateFormat("hh : mm a");
        String Date = dateFormat1.format(d.getTime());
        holder.mDate.setText(Date + " ला अखेरचे संपादन केले");

        holder.mBtnStartWriting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Intent intent = new Intent(activity, TextActivity.class);
                intent.putExtra("DocId",DocId);
                activity.startActivity(intent);
            }
        });

        holder.mBtnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(context,holder.mBtnMore);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.draft_popup_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.delete:
                                deleteDocument(position);
                                break;

                        }
                        return true;
                    }
                });

                popup.show();//showing popup menu
        }
        });
    }

    void deleteDocument(int position) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.delete_alart_dialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        TextView btnDelete = dialog.findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseFirestore.collection("Users").document(UserId).collection("SaveDocument")
                        .document(DocId).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            docData.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position,docData.size());
                            dialog.dismiss();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
            }
        });

        TextView btnClose = dialog.findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


    }

    @Override
    public int getItemCount() {
        return docData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTitle,mDescription,mDate;
        Button mBtnStartWriting;
        ImageView mBtnMore;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.title);
            mDescription = itemView.findViewById(R.id.description);
            mBtnStartWriting = itemView.findViewById(R.id.btnStartWriting);
            mBtnMore = itemView.findViewById(R.id.btnMore);
            mDate = itemView.findViewById(R.id.date);
        }
    }
}
