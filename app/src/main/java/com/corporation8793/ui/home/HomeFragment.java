package com.corporation8793.ui.home;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.corporation8793.Application;
import com.corporation8793.R;
import com.corporation8793.activity.ContentsActivity;
import com.corporation8793.activity.MainActivity;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    Button free_mode,content_mode;
    Application myApplication;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        myApplication = (Application)getActivity().getApplication();
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        free_mode = root.findViewById(R.id.free_mode);
        content_mode = root.findViewById(R.id.content_mode);

        free_mode.setOnClickListener(v->{
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.putExtra("contents_name","none");
            intent.putExtra("id","0");
            myApplication.showLoadingScreen(getContext());
            startActivity(intent);
        });

        content_mode.setOnClickListener(v->{
            Intent intent = new Intent(getActivity(), ContentsActivity.class);
            startActivity(intent);
        });
//        final TextView textView = root.findViewById(R.id.text_home);
//        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        return root;
    }


    @Override
    public void onStop() {
        super.onStop();
        myApplication.hideLoadingScreen();
    }
}