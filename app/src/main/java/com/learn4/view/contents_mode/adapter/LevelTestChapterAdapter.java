package com.learn4.view.contents_mode.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.learn4.R;
import com.learn4.data.dto.Chapter;
import com.learn4.data.dto.contents.ChapterTest;
import com.learn4.data.dto.contents.Test;
import com.learn4.view.chapter.ChapterActivity;

import java.util.ArrayList;

public class LevelTestChapterAdapter extends RecyclerView.Adapter<LevelTestChapterAdapter.CustomViewHolder> {

    private Context context;
    private ArrayList<Test> chapters;
    private LayoutInflater inflater;
    private int width =0;

    public LevelTestChapterAdapter(Context context, ArrayList<Test> chapters, int width) {
        this.context = context;
        this.chapters = chapters;
        this.inflater = LayoutInflater.from(context);
        this.width = width;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = inflater.inflate(R.layout.level_test_chapter_item_layout, parent, false);

        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {


//        ViewGroup.LayoutParams layoutParams = (ViewGroup.LayoutParams)holder.itemView.getLayoutParams();
//        layoutParams.width = width;
//        layoutParams.height = width;
//        holder.itemView.requestLayout();

        if (chapters.get(position).getAnswer().equals("hello")){
            holder.itemView.setBackgroundColor(Color.BLACK);
        }

        holder.itemView.setOnClickListener(v->{
            Log.e("hello","its me");
        });

        chapters.get(position).getBlock()

        holder.frameLayout.addView();

    }

    @Override
    public int getItemCount() {
        return chapters.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        FrameLayout frameLayout;

        public CustomViewHolder(View itemView) {
            super(itemView);
            frameLayout = itemView.findViewById(R.id.blockview);
        }
    }
}