package com.corporation8793.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.corporation8793.R;
import com.corporation8793.activity.ChapterActivity;
import com.corporation8793.dto.Chapter;

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
        if (!chapters.get(position).chapterName.equals("none")){
            int resID = context.getResources().getIdentifier("chapter"+String.valueOf(chapters.get(position).id) , "drawable" ,
                    context.getPackageName());
            holder.ivChapter.setBackgroundResource(resID);
//            holder.ivChapter.setBackgroundColor(chapters.get(position).image);
        }else{
            if (Build.VERSION.SDK_INT >= 21) {
                holder.ivChapter.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffdca2")));
            }
        }
//        Picasso.get().load(chapter.imageUrl).into(holder.ivChapter);

        Log.e("id",String.valueOf(chapters.get(position).id));

        ViewGroup.LayoutParams layoutParams = (ViewGroup.LayoutParams)holder.itemView.getLayoutParams();
        layoutParams.width = 400;
        layoutParams.height = 400;
        holder.itemView.requestLayout();

        holder.itemView.setOnClickListener(v->{
            if (chapters.get(position).chapterName.equals("LED 깜박이기")){
                Intent intent = new Intent(context.getApplicationContext(), ChapterActivity.class);
                intent.putExtra("id",String.valueOf(chapters.get(position).id));
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