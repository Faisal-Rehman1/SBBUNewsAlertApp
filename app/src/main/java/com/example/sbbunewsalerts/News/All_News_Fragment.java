package com.example.sbbunewsalerts.News;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.sbbunewsalerts.R;



public class All_News_Fragment extends Fragment {

    FrameLayout Framelayout;
    AppCompatButton btnVideoNews, btnAudioNews, btnImageNews;

    @SuppressLint("NonConstantResourceId")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        @SuppressLint("InflateParams") ViewGroup root = (ViewGroup) inflater.inflate(R.layout.all_news_fragment, null);


        btnAudioNews=root.findViewById(R.id.btn_ViewAudioNews);
        btnVideoNews=root.findViewById(R.id.btn_ViewVideoNews);
        btnImageNews=root.findViewById(R.id.btn_ViewImageNews);
        Framelayout=root.findViewById(R.id.AllNews_framelayout);


        if (savedInstanceState == null) {
            myMethod(0.5f,1f,0.5f, new View_ImageNews());
        }

        btnAudioNews.setOnClickListener(view -> {
            myMethod(0.5f,0.5f,1f,new View_AudioNews());
        });

        btnImageNews.setOnClickListener(view -> {
            myMethod(0.5f,1f,0.5f,new View_ImageNews());
        });
        btnVideoNews.setOnClickListener(view -> {
            myMethod(1f,0.5f,0.5f,new VIew_VideoNews());
        });


        return root;
    }

    private void myMethod(Float alpha1,Float alpha2,Float alpha3, Fragment fragment) {
        btnVideoNews.setAlpha(alpha1);
        btnImageNews.setAlpha(alpha2);
        btnAudioNews.setAlpha(alpha3);
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.AllNews_framelayout, fragment);
        ft.commit();
    }
}
