package com.example.sbbunewsalerts.News;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sbbunewsalerts.Create_News_Fragments.Fragment_VideoNews;
import com.example.sbbunewsalerts.Create_News_Fragments.Video_NewsModel;
import com.example.sbbunewsalerts.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;


public class VIew_VideoNews extends Fragment {


    RecyclerView recyclerView;
    Video_NewsAdapter mainAdapter;

    @SuppressLint("NonConstantResourceId")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        @SuppressLint("InflateParams") ViewGroup root = (ViewGroup) inflater.inflate(R.layout.view_video_news_fragment, null);

        recyclerView = root.findViewById(R.id.View_VideoNews_RV);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseRecyclerOptions<Video_NewsModel> options =
                new FirebaseRecyclerOptions.Builder<Video_NewsModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child(new Fragment_VideoNews().VideoDb_Refernce), Video_NewsModel.class)
                        .build();

        mainAdapter = new Video_NewsAdapter(options);
        recyclerView.setAdapter(mainAdapter);

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        recyclerView.getRecycledViewPool().clear();
        mainAdapter.startListening();
        mainAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStop() {
        super.onStop();
        mainAdapter.stopListening();
    }
}
