package com.learn4.view.contents_mode.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.learn4.R;
import com.learn4.data.dto.Level;
import com.learn4.data.dto.contents.LevelTest;
import com.learn4.view.contents.LevelChapterAdapter;
import com.learn4.view.recyclerview.HorizonViewHolder;
import com.learn4.view.recyclerview.ItemDecoration;

import java.util.ArrayList;

public class LevelTestAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    public ArrayList<LevelTest> levels;
    private LayoutInflater inflater;
    int width = 0;
    public boolean deco_check = true;

    public LevelTestChapterAdapter levelTestChapterAdapter;

    public LevelTestAdapter(Context context, ArrayList<LevelTest> levels, int width) {
        this.context = context;
        this.levels = levels;
        this.inflater = LayoutInflater.from(context);
        this.width = width;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = inflater.inflate(R.layout.level_item_layout, parent, false);

        return new HorizonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        levelTestChapterAdapter = new LevelTestChapterAdapter(context, levels.get(position).chapters, width);
        ((HorizonViewHolder) holder).recyclerView.setAdapter(levelTestChapterAdapter);
        ((HorizonViewHolder) holder).recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
//        ((HorizonViewHolder) holder).recyclerView.setLayoutManager(new GridLayoutManager(context,4));
        ((HorizonViewHolder) holder).recyclerView.setHasFixedSize(true);
//        ((HorizonViewHolder) holder).recyclerView.addItemDecoration(new ItemDecoration(context));
        String [] level_split = levels.get(position).level.split(" ");
        SpannableStringBuilder ssb = new SpannableStringBuilder(levels.get(position).level);
        ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#ff953a")), 5, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.setSpan(new RelativeSizeSpan(1.2f), 5, 7, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        ((HorizonViewHolder) holder).level.setText(ssb);

    }

    @Override
    public int getItemCount() {
        return levels.size();
    }

    @Override
    public int getItemViewType(int position) {
        return levels.get(position).id;
    }
}