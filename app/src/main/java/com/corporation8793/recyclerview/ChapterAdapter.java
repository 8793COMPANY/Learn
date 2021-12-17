package com.corporation8793.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.corporation8793.R;
import com.corporation8793.activity.ProblemActivity;
import com.corporation8793.data.Chapter;

import java.util.ArrayList;

public class ChapterAdapter  extends RecyclerView.Adapter<ChapterAdapter.CustomViewHolder> {

    private Context context;
    private ArrayList<Chapter> chapters;
    private LayoutInflater inflater;

    public ChapterAdapter(Context context, ArrayList<Chapter> chapters) {
        this.context = context;
        this.chapters = chapters;
        this.inflater = LayoutInflater.from(context);
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
//        Chapter chapter = chapters.get(position);
//        holder.tvChapterName.setText(chapter.chapterName);
        if (chapters.get(position).chapterName.equals("none")){
            holder.ivChapter.setBackgroundColor(chapters.get(position).image);
        }else{
            holder.ivChapter.setBackgroundResource(chapters.get(position).image);
        }
//        Picasso.get().load(chapter.imageUrl).into(holder.ivChapter);

        ViewGroup.LayoutParams layoutParams = (ViewGroup.LayoutParams)holder.itemView.getLayoutParams();
        layoutParams.width = 400;
        layoutParams.height = 400;
        holder.itemView.requestLayout();

        holder.itemView.setOnClickListener(v->{
            if (chapters.get(position).chapterName.equals("LED 깜박이기")){
                Intent intent = new Intent(context.getApplicationContext(), ProblemActivity.class);
                context.startActivity(intent);
            }else{
                Toast.makeText(context,"아직 열람할 수 없습니다.",Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return chapters.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivChapter;

        public CustomViewHolder(View itemView) {
            super(itemView);
            ivChapter = itemView.findViewById(R.id.chapter);
        }
    }
}