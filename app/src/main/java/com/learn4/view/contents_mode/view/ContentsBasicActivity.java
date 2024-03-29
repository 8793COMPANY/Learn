package com.learn4.view.contents_mode.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.google.blockly.android.BlocklySectionsActivity;
import com.google.blockly.android.codegen.CodeGenerationRequest;
import com.google.blockly.android.control.BlocklyController;
import com.google.blockly.android.ui.CategoryView;
import com.google.blockly.model.DefaultBlocks;
import com.learn4.R;
import com.learn4.util.Application;
import com.learn4.util.DisplaySize;
import com.learn4.view.problem.basic.Step1;
import com.learn4.view.problem.basic.Step2;
import com.learn4.view.problem.basic.Step5;

import java.util.Arrays;
import java.util.List;

public class ContentsBasicActivity extends BlocklySectionsActivity {

    TextView title;
    Button back_btn, next_btn, upload_btn, end_btn, next_level_btn;
    LinearLayout button_group;

    int pos = 0;
    String [] titles = { "목표 영상", "학습 목표", "한글 알고리즘 코딩", "매칭 설명", "영문 코딩", "실행 영상", "회로도" };

    CategoryView mCategoryView;
    BlocklyController controller;

    int mode = 1;

    static final List<String> TURTLE_BLOCK_DEFINITIONS = Arrays.asList(
            DefaultBlocks.COLOR_BLOCKS_PATH,
            DefaultBlocks.LOGIC_BLOCKS_PATH,
            DefaultBlocks.LOOP_BLOCKS_PATH,
            DefaultBlocks.MATH_BLOCKS_PATH,
            DefaultBlocks.TEXT_BLOCKS_PATH,
            DefaultBlocks.VARIABLE_BLOCKS_PATH,
            "turtle/turtle_blocks.json"
    );

    private static final int MAX_LEVELS = 2;
    private static final String[] LEVEL_TOOLBOX = new String[MAX_LEVELS];

    static {
        LEVEL_TOOLBOX[0] = "arduino_basic.xml";
        LEVEL_TOOLBOX[1] = "arduino_advanced.xml";
    }

    String [] answer = {"pinMode","inout_digital_write","base_delay","inout_digital_write","base_delay"};
    int current_pos =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contents_basic);

        title = findViewById(R.id.title);
        back_btn = findViewById(R.id.back_btn);
        next_btn = findViewById(R.id.next_btn);
        upload_btn = findViewById(R.id.upload_btn);
        end_btn = findViewById(R.id.end_btn);
        next_level_btn = findViewById(R.id.next_level_btn);
        button_group = findViewById(R.id.button_group);

        // 글씨 크기 조정을 위한 기준 조정
        Application.getStandardSize(this);

        title.setTextSize(DisplaySize.font_size_y_36);
        upload_btn.setTextSize(DisplaySize.font_size_y_28);
        end_btn.setTextSize(DisplaySize.font_size_y_28);
        next_level_btn.setTextSize(DisplaySize.font_size_y_28);

        hide_soft_key();
        replaceFragment(pos);
        button_group.setVisibility(View.GONE);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (pos == 0) {
                    //finish();
                    Log.e("testtest", "start");
                } else {
                    pos--;

                    title.setText(titles[pos]);
                    replaceFragment(pos);

                    if (pos != 6) {
                        next_btn.setVisibility(View.VISIBLE);
                        button_group.setVisibility(View.GONE);
                    }
                }
            }
        });

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (pos < 6) {
                    pos++;

                    title.setText(titles[pos]);
                    replaceFragment(pos);

                    if (pos == 6) {
                        next_btn.setVisibility(View.GONE);
                        button_group.setVisibility(View.VISIBLE);
                    }
                } else {
                    Log.e("testtest", "end");
                }
            }
        });

    }

    @NonNull
    @Override
    protected ListAdapter onCreateSectionsListAdapter() {
        return null;
    }

    @Override
    protected boolean onSectionChanged(int oldSection, int newSection) {
        return false;
    }

    @NonNull
    @Override
    protected String getToolboxContentsXmlPath() {
        return "turtle/" + LEVEL_TOOLBOX[getCurrentSectionIndex()];
    }

    @NonNull
    @Override
    protected List<String> getBlockDefinitionsJsonPaths() {
        return TURTLE_BLOCK_DEFINITIONS;
    }


    @NonNull
    @Override
    protected List<String> getGeneratorsJsPaths() {
        return null;
    }

    @NonNull
    @Override
    protected CodeGenerationRequest.CodeGeneratorCallback getCodeGenerationCallback() {
        return null;
    }

    // 프래그먼트 전환
    public void replaceFragment(int pos){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (pos == 0){
            transaction.replace(R.id.fragment, new Contents_Step1());
        } else if (pos == 1){
            transaction.replace(R.id.fragment, new Contents_Step2());
        } else if (pos == 2){
            transaction.replace(R.id.fragment, new Contents_Step3());
        } else if (pos == 3){
            transaction.replace(R.id.fragment, new Contents_Step4());
        } else if (pos == 4){
            transaction.replace(R.id.fragment, new Contents_Step5());
        } else if (pos == 5){
            transaction.replace(R.id.fragment, new Contents_Step6());
        } else if (pos == 6){
            transaction.replace(R.id.fragment, new Contents_Step7());
        }

        transaction.commit();
    }

    // 하단 소프트키 숨기기
    public void hide_soft_key() {
        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;
        boolean isImmersiveModeEnabled = ((uiOptions | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY) == uiOptions);
        if (isImmersiveModeEnabled) {
            Log.i("Is on?", "Turning immersive mode mode off. ");
        } else {
            Log.i("Is on?", "Turning immersive mode mode on.");
        }
        newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);
    }
}