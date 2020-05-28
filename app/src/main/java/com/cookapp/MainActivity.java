package com.cookapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button btnReg,btnLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnReg = findViewById(R.id.btnReg);
        btnLog = findViewById(R.id.btnLog);


        btnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent logIn = new Intent(MainActivity.this, Login.class);
                startActivity(logIn);
            }
        });

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent logUp = new Intent(MainActivity.this, Registration.class);
                startActivity(logUp);
            }
        });
    }

    @Override
    protected void onStop(){
        super.onStop();
        //Banner.stopAutoCycle();
    }
}


