package com.learn4.view.drone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListAdapter;

import com.google.blockly.android.AbstractBlocklyActivity;
import com.google.blockly.android.BlocklySectionsActivity;
import com.google.blockly.android.codegen.CodeGenerationRequest;
import com.google.blockly.model.DefaultBlocks;
import com.learn4.R;

import java.util.Arrays;
import java.util.List;

public class DroneBlockActivity extends BlocklySectionsActivity {

    String TARGET_BASE_PATH;
    private FrameLayout mGeneratedFrameLayout;

    String [] turtle_files_kor = {"default/logic_blocks_kor.json","default/loop_blocks_kor.json","default/math_blocks_kor.json","default/variable_blocks_kor.json", "turtle/turtle_blocks_kor.json"};
    String [] turtle_files_eng = {"default/logic_blocks.json","default/loop_blocks.json","default/math_blocks.json","default/variable_blocks.json", "turtle/turtle_blocks.json"};


    static final List<String> TURTLE_BLOCK_DEFINITIONS = Arrays.asList(
            DefaultBlocks.COLOR_BLOCKS_PATH,
            DefaultBlocks.LOGIC_BLOCKS_PATH,
            DefaultBlocks.LOOP_BLOCKS_PATH,
            DefaultBlocks.MATH_BLOCKS_PATH,
            DefaultBlocks.TEXT_BLOCKS_PATH,
            DefaultBlocks.VARIABLE_BLOCKS_PATH,
            "turtle/turtle_blocks.json"
    );
    static final List<String> TURTLE_BLOCK_GENERATORS = Arrays.asList(
            "turtle/generators.js"
    );
    private static final int MAX_LEVELS = 2;
    private static final String[] LEVEL_TOOLBOX = new String[MAX_LEVELS];

    static {
        LEVEL_TOOLBOX[0] = "arduino_basic.xml";
        LEVEL_TOOLBOX[1] = "arduino_advanced.xml";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected View onCreateContentView(int containerId) {
        View root = getLayoutInflater().inflate(R.layout.activity_drone_block, null);
        mGeneratedFrameLayout = root.findViewById(R.id.generated_workspace);
        return root;
    }

    @NonNull
    @Override
    protected ListAdapter onCreateSectionsListAdapter() {
        String[] levelNames = new String[MAX_LEVELS];
        Log.e("path check","/data/data/"+this.getPackageName()+"/");
        TARGET_BASE_PATH = "/data/data/"+this.getPackageName()+"/";
        levelNames[0] = "ArduBasic";
        levelNames[1] = "ArduAdvanced";

        return new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_activated_1,
                android.R.id.text1,
                levelNames);
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
        return TURTLE_BLOCK_GENERATORS;
    }

    @NonNull
    @Override
    protected CodeGenerationRequest.CodeGeneratorCallback getCodeGenerationCallback() {
        return null;
    }
}