package com.cctpl.puneplanet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.cctpl.puneplanet.Fragments.HomeFragment;
import com.cctpl.puneplanet.Fragments.LibraryFragment;
import com.cctpl.puneplanet.Fragments.MoreFragment;
import com.cctpl.puneplanet.Fragments.UpdateFragment;
import com.cctpl.puneplanet.Fragments.WriteFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    TextView userName;
    CircleImageView userProfile;
    String UserName;
    Uri ProfileUrl;
    Fragment selectFragment;

    FirebaseAuth firebaseAuth;
    BottomNavigationView bottomNavigationView;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        bottomNavigationView = findViewById(R.id.nav);

        FirebaseUser user = firebaseAuth.getCurrentUser();
        UserName = user.getDisplayName();
        ProfileUrl = user.getPhotoUrl();


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.home :
                        selectFragment = new HomeFragment();
                        break;
                    case R.id.library :
                        selectFragment = new LibraryFragment();
                        break;
                    case R.id.write :
                        selectFragment = new WriteFragment();
                        break;
                    case R.id.update :
                        selectFragment = new UpdateFragment();
                        break;
                    case R.id.more :
                        selectFragment = new MoreFragment();
                        break;
                }

                if (selectFragment != null){
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectFragment).addToBackStack(null).commit();
                }

                return true;
            }
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();
    }
}