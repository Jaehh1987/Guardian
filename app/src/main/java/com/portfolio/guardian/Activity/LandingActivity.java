package com.portfolio.guardian.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.portfolio.guardian.R;

public class LandingActivity extends AppCompatActivity {
    private Button btnRegister;
    private EditText startaddress;
    private EditText destinationaddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        btnRegister = findViewById(R.id.btnRegister);
        startaddress = findViewById(R.id.etFrom);
        destinationaddress = findViewById(R.id.etTo);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextTest();
            }
        });
    }

    public void nextTest() {
        Intent intent = new Intent(this, MapActivity.class);
        String start = startaddress.getText().toString();
        String destination = destinationaddress.getText().toString();
        intent.putExtra("start",start);
        intent.putExtra("destination",destination);
        startActivity(intent);
    }
}
