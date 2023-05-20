package com.example.sbbunewsalerts.Dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.sbbunewsalerts.Create_News_Fragments.Fragment_AudioNews;
import com.example.sbbunewsalerts.Create_News_Fragments.Fragment_ImageNews;
import com.example.sbbunewsalerts.Create_News_Fragments.Fragment_VideoNews;
import com.example.sbbunewsalerts.R;

public class Create_News extends AppCompatActivity {


   FrameLayout Framelayout;
    AppCompatButton btnVideoNews, btnAudioNews, btnImageNews;
    TextView tv_backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_news);

        Framelayout=findViewById(R.id.framelayout);

        btnAudioNews=findViewById(R.id.btnAudioNews);
        btnVideoNews=findViewById(R.id.btnVideoNews);
        btnImageNews=findViewById(R.id.btnImageNews);
        tv_backBtn = findViewById(R.id.VideoNews_backBtn);

        tv_backBtn.setOnClickListener(view -> gotoDashboard());

        if (savedInstanceState == null) {
            myMethod(0.5f,1f,0.5f, new Fragment_ImageNews());
        }

        btnAudioNews.setOnClickListener(view -> {
            myMethod(0.5f,0.5f,1f,new Fragment_AudioNews());
        });

        btnImageNews.setOnClickListener(view -> {
            myMethod(0.5f,1f,0.5f,new Fragment_ImageNews());
        });
        btnVideoNews.setOnClickListener(view -> {
            myMethod(1f,0.5f,0.5f,new Fragment_VideoNews());
        });
    }

    // method for OnClicks
    private void myMethod(Float alpha1,Float alpha2,Float alpha3, Fragment fragment) {
        btnVideoNews.setAlpha(alpha1);
        btnImageNews.setAlpha(alpha2);
        btnAudioNews.setAlpha(alpha3);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.framelayout, fragment);
        ft.commit();
    }

    private void gotoDashboard() {
        startActivity(new Intent(getApplicationContext(), Dashboard.class));
    }
}