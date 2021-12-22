package com.corporation8793.recyclerview;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.corporation8793.R;
import com.corporation8793.dto.Answer;

import java.util.ArrayList;

public class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.CustomViewHolder> {

    private Context context;
    private ArrayList<Answer> answers;
    private LayoutInflater inflater;

    public AnswerAdapter(Context context, ArrayList<Answer> answers) {
        this.context = context;
        this.answers = answers;
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
        Log.e("position",position+"");
        ViewGroup.LayoutParams layoutParams = (ViewGroup.LayoutParams)holder.itemView.getLayoutParams();
        layoutParams.width = 340;
        holder.itemView.requestLayout();

        holder.aImg.setBackgroundResource(answers.get(position).getImg());

        holder.itemView.setOnClickListener(v->{
           Toast.makeText(context,answers.get(position).getAnswer(),Toast.LENGTH_SHORT).show();
        });

    }

    @Override
    public int getItemCount() {
        return answers.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        public ImageView aImg;

        public CustomViewHolder(View itemView) {
            super(itemView);
            aImg = itemView.findViewById(R.id.chapter);
        }
    }
}