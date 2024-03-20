package com.learn4.view.contents_mode.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.media.Image;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.blockly.android.control.BlocklyController;
import com.google.blockly.android.ui.BlockGroup;
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

    public LevelTestChapterAdapter(Context context,  ArrayList<Test> chapters, int width) {
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

        holder.itemView.setOnClickListener(v->{
            Log.e("hello","its me"+ position);
        });

//        if (holder.frameLayout != null)
//            holder.frameLayout.removeAllViews();


        View view = (View) chapters.get(position).getBlock();

        holder.frameLayout.addView(view);

        holder.block_img.post(new Runnable() {
            @Override
            public void run() {

                holder.block_img.setImageBitmap(getBitmapFromView(holder.frameLayout));
                holder.frameLayout.removeView(view);

            }
        });

    }

    public Bitmap getBitmapFromView(View v){
        Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight() ,
                Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.draw(c);
        return b;
    }

    @Override
    public int getItemCount() {
        return chapters.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        FrameLayout frameLayout;
        ImageView block_img;

        public CustomViewHolder(View itemView) {
            super(itemView);
            frameLayout = itemView.findViewById(R.id.blockview);
            block_img = itemView.findViewById(R.id.block_img);
        }
    }
}