package com.portfolio.guardian.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.portfolio.guardian.R;

public class LandingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_landing);
    }

    public void nextTest(View view) {
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
    }
}
