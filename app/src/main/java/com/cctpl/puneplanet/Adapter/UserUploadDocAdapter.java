package com.cctpl.puneplanet.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.cctpl.puneplanet.DocReadingActivity;
import com.cctpl.puneplanet.R;
import com.cctpl.puneplanet.model.UploadDocData;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class UserUploadDocAdapter extends RecyclerView.Adapter<UserUploadDocAdapter.ViewHolder> {

    Context context;
    List<UploadDocData>uploadDocData;
    public UserUploadDocAdapter(List<UploadDocData> uploadDocData) {
        this.uploadDocData = uploadDocData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.upload_doc_view,parent,false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(uploadDocData.get(position).getTitle());
        Date d = new Date(uploadDocData.get(position).getTimeStamp());
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat1 = new SimpleDateFormat("dd MMM yyyy");
        String Date = dateFormat1.format(d.getTime());
        holder.date.setText(Date);
        Picasso.get().load(uploadDocData.get(position).getCoverImg()).into(holder.imageView);

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Intent intent = new Intent(context, DocReadingActivity.class);
                intent.putExtra("title",uploadDocData.get(position).getTitle());
                intent.putExtra("description",uploadDocData.get(position).getDescription());
                intent.putExtra("userId",uploadDocData.get(position).getUserId());
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return uploadDocData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title,date;
        ImageView imageView;
        RelativeLayout relativeLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            date = itemView.findViewById(R.id.date);
            imageView = itemView.findViewById(R.id.coverImg);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);
        }
    }
}
