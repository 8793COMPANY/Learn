package com.corporation8793.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.corporation8793.MySharedPreferences;
import com.corporation8793.R;
import com.corporation8793.dto.Contents;
import com.corporation8793.problem.SolvingProblem;

import java.util.ArrayList;
import java.util.List;

public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.ItemViewHolder> {

    // adapter에 들어갈 list 입니다.
    private ArrayList<Contents> listData = new ArrayList<>();
    private int width = 0;
    private String chapter_id = "0";
    Context context;
    public static int current_chapter =0;

    public ChapterAdapter(Context context, ArrayList<Contents> arrayList, int width, String chapter_id){
        listData = arrayList;
        this.width = width;
        this.context = context;
        this.chapter_id = chapter_id;
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
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position, @NonNull List<Object> payloads) {
        // Item을 하나, 하나 보여주는(bind 되는) 함수입니다.
        if (payloads.isEmpty()){
            super.onBindViewHolder(holder,position,payloads);
        } else{
            for(Object payload : payloads){
                String type = (String) payload;
                if (TextUtils.equals(type, "lock_check")){
                    Log.e("hi","payloads in~");
                    listData.get(position).open = true;
                    holder.lock_check.setBackgroundResource(0);

                }else{
                    int progress = 0;
                    if (position != 3) {
                        progress = MySharedPreferences.getInt(context, listData.get(position).title + " MAX");
                    }else{

                        progress = listData.get(position).percentage = MySharedPreferences.getInt(context, "문제풀이"+chapter_id);
                    }

                    listData.get(position).percentage = progress;
                    holder.progressBar.setProgress(progress*20);
                    holder.percentage.setText(progress*20+"%");
                }
            }
        }

//        holder.onBind(listData.get(position));
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
        private ImageView lock_check;
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
            lock_check = itemView.findViewById(R.id.lock_check);
        }

        void onBind(Contents contents) {

            chapter_content_title.setText(contents.getTitle());
            percentage.setText(contents.getPercentage()*20+"%");
            chapter_content_img.setBackgroundResource(contents.getImage());
            problem_group.setBackgroundResource(contents.getGroup());
            progressBar.setProgress(contents.getPercentage()*20);
            if (contents.open)
                lock_check.setBackgroundResource(0);

            itemView.setOnClickListener(v->{
                current_chapter = getAdapterPosition();
                if (contents.open) {
                    if (getAdapterPosition() == 3) {
                        Intent intent = new Intent(context, SolvingProblem.class);
                        intent.putExtra("step", 1);
                        intent.putExtra("id", contents.getId());
                        intent.putExtra("chapter_id", chapter_id);
                        context.startActivity(intent);
                    } else {
                        Intent intent = new Intent(context, ProblemActivity.class);
                        intent.putExtra("step", getAdapterPosition());
                        intent.putExtra("id", contents.getId());
                        context.startActivity(intent);
                    }
                }
            });

        }
    }
}