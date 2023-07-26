package com.learn4.view.custom.dialog.file;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.learn4.R;
import com.learn4.util.FileSharedPreferences;
import com.learn4.view.MainActivity;

import java.io.File;
import java.util.ArrayList;


public class FileListDialog extends Dialog {


    private View.OnClickListener confirm_listener;


    public RecyclerView file_list;

    public LinearLayout close_btn;

    public TextView main_title;


    public String name;
    public boolean check = false;

    FileLoadAdapter fileLoadAdapter;

    Context context;
    ArrayList<String> files;
    Activity activity;

    String type ="";

    public FileListDialog(@NonNull Activity activity, Context context, ArrayList<String> files, String type) {
        super(context);
        this.activity = activity;
        this.context = context;
        this.files = files;
        this.type = type;
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        setContentView(R.layout.dialog_file_list);

        file_list = findViewById(R.id.file_list);
        close_btn = findViewById(R.id.bottom_section);
        main_title = findViewById(R.id.main_title);


        TextView empty_save_code_text = findViewById(R.id.empty_save_code_text);

        if (type.equals("save_load") && files.size() ==0){
            empty_save_code_text.setVisibility(View.VISIBLE);
        }

        if (type.equals("example_load")){
            main_title.setText("학습예제");
        }

        fileLoadAdapter = new FileLoadAdapter(type,context,files);

        file_list.setAdapter(fileLoadAdapter);
        file_list.setLayoutManager(new LinearLayoutManager(getContext()));

        fileLoadAdapter.setOnItemClickListener(new FileLoadAdapter.OnItemClickEventListener() {
            @Override
            public void onItemClick(String name) {
                if (type.equals("example_load")){
                    ((MainActivity)activity).loadExample(name);
                }else{
                    ((MainActivity)activity).loadWorkspace(name);
                }


                Log.e("name",name);
                dismiss();
            }

            @Override
            public void onItemDelete(int position, String name) {
//                files.remove(position);
                fileLoadAdapter.deleteItem(position);
                FileSharedPreferences.setStringArrayList(context,"files", files);
                if (files.size() == 0){
                    empty_save_code_text.setVisibility(View.VISIBLE);
                }

            }
        });






        close_btn.setOnClickListener(v->{
            dismiss();
        });


    }
}
