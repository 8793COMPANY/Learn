package com.learn4.view.contents_mode.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
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
import com.learn4.data.dto.CodeBlock;
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
        holder.onBind(chapters.get(position));

        if (position == 0){
            holder.first_check_view.setVisibility(View.VISIBLE);
        }

        if (chapters.get(position).getAnswer().equals("question")){
            holder.block_img.setBackgroundResource(R.drawable.question_block_image);
            holder.block_img.setImageResource(R.drawable.block_question_mark);
        }else{

            if (holder.frameLayout != null)
                holder.frameLayout.removeAllViews();

            Log.e("leveltestchapteradapter position",position+"");

            holder.itemView.setOnClickListener(v->{
                Log.e("hello","its me"+ position);
            });

//        if (holder.frameLayout != null)
//            holder.frameLayout.removeAllViews();


            View view = (View) chapters.get(position).getBlock();


//            view.setScaleX(0.7f);
//            view.setScaleY(0.7f);
            view.setPivotX(0);

            holder.frameLayout.addView(view);

            holder.block_img.post(new Runnable() {
                @Override
                public void run() {

                    holder.block_img.setImageBitmap(getBitmapFromView(holder.frameLayout));
                    holder.frameLayout.removeView(view);
                    Log.e("holder block img", holder.block_img.getWidth()+"");

                }
            });

        }



    }

    public Bitmap getBitmapFromView(View v){
        Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight() ,
                Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.draw(c);
        return b;
    }

    public static int targetWidth = 500;

    public Bitmap aspectRatioBitamp(Bitmap source) {
        double aspectRatio = (double) source.getHeight() / (double) source.getWidth(); // 종횡비 계산
        int targetHeight = (int) (targetWidth * aspectRatio);
        Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);
        if (result != source) {
            source.recycle();
        }
        return result;
    }

    @Override
    public int getItemCount() {
        return chapters.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        FrameLayout frameLayout;
        ImageView block_img;

        View first_check_view;

        public CustomViewHolder(View itemView) {
            super(itemView);
            frameLayout = itemView.findViewById(R.id.blockview);
            block_img = itemView.findViewById(R.id.block_img);
            first_check_view = itemView.findViewById(R.id.first_check_view);
        }

        void onBind(Test block) {
            Log.e("block hi","hey");


        }
    }
}