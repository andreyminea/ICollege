package com.example.icollege.Main;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.icollege.R;

public class TimeTableActivity extends AppCompatActivity {

    ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table);
        img = (ImageView) findViewById(R.id.scheduleImage);

        String url = getString(R.string.linkSchedule);

        Glide.with(this)
                .load(url)
                .fitCenter()
                .into(img);
    }
}