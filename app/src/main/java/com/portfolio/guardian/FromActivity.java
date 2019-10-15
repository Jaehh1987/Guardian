package com.portfolio.guardian;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class FromActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_from);
    }

    public void moveTo(View view) {
        Intent intent = new Intent(this, ToActivity.class);
        startActivity(intent);
    }
}
