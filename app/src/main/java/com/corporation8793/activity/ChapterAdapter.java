package com.corporation8793.activity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.corporation8793.R;
import com.corporation8793.dto.Contents;

import java.util.ArrayList;

public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.ItemViewHolder> {

    // adapter에 들어갈 list 입니다.
    private ArrayList<Contents> listData = new ArrayList<>();
    private int width = 0;
    Context context;

    public ChapterAdapter(Context context, ArrayList<Contents> arrayList, int width){
        listData = arrayList;
        this.width = width;
        this.context = context;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // LayoutInflater를 이용하여 전 단계에서 만들었던 item.xml을 inflate 시킵니다.
        // return 인자는 ViewHolder 입니다.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chapter_content_layout, parent, false);
        view.getLayoutParams().width = width;
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        // Item을 하나, 하나 보여주는(bind 되는) 함수입니다.
        holder.onBind(listData.get(position));
    }

    @Override
    public int getItemCount() {
        // RecyclerView의 총 개수 입니다.
        return listData.size();
    }



    // RecyclerView의 핵심인 ViewHolder 입니다.
    // 여기서 subView를 setting 해줍니다.
    class ItemViewHolder extends RecyclerView.ViewHolder {

        private ImageView problem_group;
        private TextView chapter_content_title;
        private LinearLayout chapter_content_img;
        ProgressBar progressBar;
        private TextView percentage;
        ItemViewHolder(View itemView) {
            super(itemView);

            percentage = itemView.findViewById(R.id.percentage);
            chapter_content_title = itemView.findViewById(R.id.chapter_content_title);
            chapter_content_img = itemView.findViewById(R.id.chapter_content_img);
            problem_group = itemView.findViewById(R.id.problem_group);
            progressBar = itemView.findViewById(R.id.progress);
        }

        void onBind(Contents contents) {

            chapter_content_title.setText(contents.getTitle());
            percentage.setText(contents.getPercentage()*20+"%");
            chapter_content_img.setBackgroundResource(contents.getImage());
            problem_group.setBackgroundResource(contents.getGroup());
            progressBar.setProgress(contents.getPercentage()*20);

            itemView.setOnClickListener(v->{
                Intent intent = new Intent(context, ProblemActivity.class);
                intent.putExtra("step", 1);
                intent.putExtra("id", contents.getId());
                context.startActivity(intent);
            });

        }
    }
}