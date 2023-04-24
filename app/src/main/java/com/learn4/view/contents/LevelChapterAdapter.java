package com.learn4.view.contents;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.learn4.R;
import com.learn4.view.chapter.ChapterActivity;
import com.learn4.data.dto.Chapter;

import java.util.ArrayList;

public class LevelChapterAdapter extends RecyclerView.Adapter<LevelChapterAdapter.CustomViewHolder> {

    private Context context;
    private ArrayList<Chapter> chapters;
    private LayoutInflater inflater;
    private int width =0;

    public LevelChapterAdapter(Context context, ArrayList<Chapter> chapters, int width) {
        this.context = context;
        this.chapters = chapters;
        this.inflater = LayoutInflater.from(context);
        this.width = width;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = inflater.inflate(R.layout.chapter_item_layout, parent, false);

        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {

        if (!chapters.get(position).chapterName.equals("none")){
            int resID = context.getResources().getIdentifier("contents"+String.valueOf(chapters.get(position).id) , "drawable" ,
                    context.getPackageName());
            holder.ivChapter.setBackgroundResource(resID);

            // 변경 필요 13 >> 10
            if (chapters.get(position).id > 13) {
                holder.chapter_background.setBackgroundResource(R.drawable.chapter_lock_image);
            } else {
                holder.chapter_background.setBackgroundResource(0);
            }
        }else{
            if (Build.VERSION.SDK_INT >= 21) {
                holder.ivChapter.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffdca2")));
            }
        }

        ViewGroup.LayoutParams layoutParams = (ViewGroup.LayoutParams)holder.itemView.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = width;
        holder.itemView.requestLayout();

        holder.itemView.setOnClickListener(v->{
            Log.e("idddd", chapters.get(position).id+"");

            /*if (chapters.get(position).id > 10) {
                Toast.makeText(context,"아직 열람할 수 없습니다.",Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(context.getApplicationContext(), ChapterActivity.class);
                intent.putExtra("id",String.valueOf(chapters.get(position).id));
                context.startActivity(intent);
            }*/
            Intent intent = new Intent(context.getApplicationContext(), ChapterActivity.class);
            intent.putExtra("id",String.valueOf(chapters.get(position).id));
            context.startActivity(intent);
            /*if (chapters.get(position).chapterName.equals("LED 깜빡이기")){
                Intent intent = new Intent(context.getApplicationContext(), ChapterActivity.class);
                intent.putExtra("id",String.valueOf(chapters.get(position).id));
                context.startActivity(intent);
            }else{
                Toast.makeText(context,"아직 열람할 수 없습니다.",Toast.LENGTH_SHORT).show();
            }*/
        });

    }

    @Override
    public int getItemCount() {
        return chapters.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivChapter;
        public ImageView chapter_background;

        public CustomViewHolder(View itemView) {
            super(itemView);
            ivChapter = itemView.findViewById(R.id.chapter);
            chapter_background = itemView.findViewById(R.id.chapter_background);
        }
    }
}