package com.cctpl.puneplanet;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.ColorSpace;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.text.Html;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DocReadingActivity extends AppCompatActivity {

    TextView mTitle,mDescription;
    String title,description,UserId;
    SeekBar seekBar;
    int i =16;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_reading);
        mTitle = findViewById(R.id.title);
        seekBar = findViewById(R.id.seekBar);
        mDescription = findViewById(R.id.description);

        Bundle bundle = getIntent().getExtras();

        seekBar.setMax(28);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            seekBar.setMin(16);
        }
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                mDescription.setTextSize(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        if (bundle!=null){
            title = bundle.getString("title");
            description = bundle.getString("description");
            UserId = bundle.getString("userId");

            //set text
            mTitle.setText(title);
            mDescription.setText(description);
        }

    }
}