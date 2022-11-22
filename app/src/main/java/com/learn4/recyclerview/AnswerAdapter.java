package com.learn4.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.learn4.R;
import com.learn4.dto.Answer;

import java.util.ArrayList;

public class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.CustomViewHolder> {

    private Context context;
    private ArrayList<Answer> answers;
    private LayoutInflater inflater;

    public interface OnItemClickEventListener {
        void onItemClick(String name);
    }

    private OnItemClickEventListener mItemClickListener;

    public void setOnItemClickListener(OnItemClickEventListener a_listener) {
        mItemClickListener = a_listener;
    }

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
        ViewGroup.LayoutParams layoutParams = (ViewGroup.LayoutParams)holder.itemView.getLayoutParams();
        layoutParams.width = 340;
        holder.itemView.requestLayout();

        holder.aImg.setBackgroundResource(answers.get(position).getImg());


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

            itemView.setOnClickListener(v->{
                int pos = getAdapterPosition() ;
                if (pos != RecyclerView.NO_POSITION) {
                    if (mItemClickListener != null) {
                        mItemClickListener.onItemClick(answers.get(pos).getAnswer()) ;
                    }
                }
            });
        }
    }
}