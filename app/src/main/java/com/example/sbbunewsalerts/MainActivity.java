package com.example.sbbunewsalerts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.example.sbbunewsalerts.Login_Registration.Login_Activity;

public class MainActivity extends AppCompatActivity {

        Handler handler;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);

            setContentView(R.layout.activity_main);
            if (getSupportActionBar() != null) {
                getSupportActionBar().hide();
            }

            handler = new Handler();
            handler.postDelayed(() -> {
                Intent intent = new Intent(MainActivity.this, Login_Activity.class);
                startActivity(intent);
                finish();
            }, 3000);


        }
}