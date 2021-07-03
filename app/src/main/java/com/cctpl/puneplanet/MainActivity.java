package com.cctpl.puneplanet;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    TextView userName;
    CircleImageView userProfile;
    String UserName;
    Uri ProfileUrl;

    FirebaseAuth firebaseAuth;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();

        userName = findViewById(R.id.userName);
        userProfile = findViewById(R.id.userProfile);

        FirebaseUser user = firebaseAuth.getCurrentUser();
        UserName = user.getDisplayName();
        ProfileUrl = user.getPhotoUrl();

        Picasso.get().load(ProfileUrl).into(userProfile);
        userName.setText("काय चालंय " + UserName + " !");
    }
}