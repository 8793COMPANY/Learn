package com.learn4.view.custom.dialog.file;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.learn4.R;
import com.learn4.data.dto.Subject;
import com.learn4.util.MySharedPreferences;
import com.learn4.view.chapter.ChapterActivity;
import com.learn4.view.contents.LevelChapterAdapter;
import com.learn4.view.problem.basic.AnswerAdapter;
import com.learn4.view.problem.basic.ProblemActivity;
import com.learn4.view.problem.solve.SolvingProblem;

import java.util.ArrayList;
import java.util.List;

public class FileLoadAdapter extends RecyclerView.Adapter<FileLoadAdapter.ItemViewHolder> {

    // adapter에 들어갈 list 입니다.
    private ArrayList<String> listData = new ArrayList<>();
    private int width = 0;
    Context context;
    String type ="";


    public interface OnItemClickEventListener {
        void onItemClick(String name);
        void onItemDelete(int pos, String name);
    }

    private OnItemClickEventListener mItemClickListener;

    public void setOnItemClickListener(OnItemClickEventListener a_listener) {
        mItemClickListener = a_listener;
    }


    public FileLoadAdapter(String type, Context context, ArrayList<String> arrayList){
        listData = arrayList;
        this.width = width;
        this.context = context;
        this.type = type;

        Log.e("file", listData.size()+"");
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.file_list_itemview, parent, false);
        view.getLayoutParams().height = 80;
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        // Item을 하나, 하나 보여주는(bind 되는) 함수입니다.
        Log.e("file list check",listData.get(position));
        holder.file_name_area.setText(listData.get(position));

        if (type.equals("save_load")){
            holder.delete_btn.setVisibility(View.VISIBLE);
        }

        holder.select_btn.setOnClickListener(v->{
            if (position != RecyclerView.NO_POSITION) {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClick(listData.get(position));
                }
            }
        });

        holder.delete_btn.setOnClickListener(v->{
            Log.e("delete","in");
            if (position != RecyclerView.NO_POSITION) {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemDelete(position,listData.get(position));
                }
            }
        });

//        holder.itemView.setOnClickListener(v->{
//            if (position != RecyclerView.NO_POSITION) {
//                if (mItemClickListener != null) {
//                    mItemClickListener.onItemDelete(position,listData.get(position));
//                }
//            }
//        });
    }

    public void deleteItem(int position) {
        listData.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, listData.size());
    }



    @Override
    public int getItemCount() {
        // RecyclerView의 총 개수 입니다.
        return listData.size();
    }


    class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView file_name_area;
        private Button select_btn;
        private Button delete_btn;
        ItemViewHolder(View itemView) {
            super(itemView);

            file_name_area = itemView.findViewById(R.id.file_name);
            select_btn = itemView.findViewById(R.id.select_btn);
            delete_btn = itemView.findViewById(R.id.delete_btn);
        }

    }


}