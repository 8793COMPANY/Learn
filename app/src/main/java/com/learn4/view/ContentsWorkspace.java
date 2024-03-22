package com.learn4.view;

import androidx.annotation.NonNull;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.google.blockly.android.BlocklySectionsActivity;
import com.google.blockly.android.codegen.CodeGenerationRequest;
import com.google.blockly.android.control.BlocklyController;
import com.google.blockly.model.DefaultBlocks;
import com.google.blockly.utils.TestApplication;
import com.learn4.R;
import com.learn4.util.Application;

import java.util.Arrays;
import java.util.List;

public class ContentsWorkspace extends BlocklySectionsActivity {

    private static final int MAX_LEVELS = 2;
    private static final String[] LEVEL_TOOLBOX = new String[MAX_LEVELS];

    String TARGET_BASE_PATH;

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

    static {
        LEVEL_TOOLBOX[0] = "arduino_basic.xml";
        LEVEL_TOOLBOX[1] = "arduino_advanced.xml";
    }

    BlocklyController controller;

    int standardSize_X, standardSize_Y;
    float density;
    TextView testText, testText2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_contents_workspace);

        Log.e("testtesttt", "ContentsWorkspace onCreate");
        Log.e("testtesttt", TestApplication.getWorkspace_name());

        // 하단 소프트키 숨기기
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

        controller = getController();

        controller.recenterWorkspace();
    }

    @Override
    protected View onCreateContentView(int containerId) {
        getLayoutInflater();
        View root = getLayoutInflater().inflate(R.layout.activity_contents_workspace, null);

        Log.e("testtesttt", "ContentsWorkspace onCreateContentView");

        View blockly_contents_workspace = root.findViewById(R.id.blockly_contents_workspace);
        testText = blockly_contents_workspace.findViewById(R.id.testText);
        testText2 = blockly_contents_workspace.findViewById(R.id.testText2);


        getStandardSize();

        testText.setTextSize((float) (standardSize_X / 3));
        testText.setTextSize((float) (standardSize_Y / 5));

        testText2.setTextSize((float) (standardSize_Y / 8));
        testText2.setTextSize((float) (Application.standardSize_Y / 5));

        //return super.onCreateContentView(containerId);
        return root;
    }

    private final CodeGenerationRequest.CodeGeneratorCallback mCodeGeneratorCallback =
            new CodeGenerationRequest.CodeGeneratorCallback() {
                @Override
                public void onFinishCodeGeneration(final String generatedCode, String xml) {
                    Log.e("testtesttt", "ContentsWorkspace onFinishCodeGeneration");
                }
            };

    @NonNull
    @Override
    protected ListAdapter onCreateSectionsListAdapter() {
        Log.e("testtesttt", "ContentsWorkspace onCreateSectionsListAdapter");

        // Create the game levels with the labels "Level 1", "Level 2", etc., displaying
        // them as simple text items in the sections drawer.
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
        Log.e("testtesttt", "ContentsWorkspace onSectionChanged");

        Log.e("+oldSection",oldSection+"");
        Log.e("+newSection",newSection+"");
        reloadToolbox();
        return true;
    }

    @NonNull
    @Override
    protected String getToolboxContentsXmlPath() {
        Log.e("testtesttt", "ContentsWorkspace getToolboxContentsXmlPath");

        // Expose a different set of blocks to the user at each level.
        return "turtle/" + LEVEL_TOOLBOX[getCurrentSectionIndex()];
    }

    @NonNull
    @Override
    protected List<String> getBlockDefinitionsJsonPaths() {
        Log.e("testtesttt", "ContentsWorkspace getBlockDefinitionsJsonPaths");

        // Use the same blocks for all the levels. This lets the user's block code carry over from
        // level to level. The set of blocks shown in the toolbox for each level is defined by the
        // toolbox path below.
        return TURTLE_BLOCK_DEFINITIONS;
    }

    @NonNull
    @Override
    protected List<String> getGeneratorsJsPaths() {
        Log.e("testtesttt", "ContentsWorkspace getGeneratorsJsPaths");

        return TURTLE_BLOCK_GENERATORS;
    }

    @NonNull
    @Override
    protected CodeGenerationRequest.CodeGeneratorCallback getCodeGenerationCallback() {
        Log.e("testtesttt", "ContentsWorkspace getCodeGenerationCallback");

        Log.e("in!","getCodeGenerationCallback");
        return mCodeGeneratorCallback;
    }

    public Point getScreenSize(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        return size;
    }

    public void getStandardSize() {
        Point ScreenSize = getScreenSize(this);
        density  = getResources().getDisplayMetrics().density;

        standardSize_X = (int) (ScreenSize.x / density);
        standardSize_Y = (int) (ScreenSize.y / density);
    }

}