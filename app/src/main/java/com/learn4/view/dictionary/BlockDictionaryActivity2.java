package com.learn4.view.dictionary;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;

import com.google.blockly.android.BlocklySectionsActivity;
import com.google.blockly.android.codegen.CodeGenerationRequest;
import com.google.blockly.android.control.BlocklyController;
import com.google.blockly.android.ui.CategoryView;
import com.google.blockly.model.Block;
import com.google.blockly.model.BlocklyCategory;
import com.google.blockly.model.DefaultBlocks;
import com.learn4.R;
import com.learn4.data.dto.CodeBlock;
import com.learn4.data.room.AppDatabase2;
import com.learn4.data.room.dao.BlockDictionaryDao;
import com.learn4.data.room.entity.BlockDictionary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BlockDictionaryActivity2 extends BlocklySectionsActivity {

    Button back_btn, dictionary_setup_btn, dictionary_loop_btn, dictionary_method_btn, dictionary_etc_btn;

    AppDatabase2 db2 = null;
    BlockDictionaryDao blockDictionaryDao;

    List<BlockDictionary> blockDictionaryList = new ArrayList<>();
    ArrayList<CodeBlock> dictionary_block_list = new ArrayList<>();

    RecyclerView block_list;
    CodeDictionaryAdapter dictionaryAdapter;

    CategoryView mCategoryView;
    BlocklyController controller;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block_dictionary2);

        back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(v -> finish());

        dictionary_setup_btn = findViewById(R.id.dictionary_setup_btn);
        dictionary_loop_btn = findViewById(R.id.dictionary_loop_btn);
        dictionary_method_btn = findViewById(R.id.dictionary_method_btn);
        dictionary_etc_btn = findViewById(R.id.dictionary_etc_btn);

        db2 = AppDatabase2.getInstance(getBaseContext());

        blockDictionaryDao = db2.blockDictionaryDao();
        blockDictionaryList = blockDictionaryDao.findAll();

        block_list = findViewById(R.id.block_list);
        block_list.setLayoutManager(new LinearLayoutManager(this));

        mCategoryView = mBlocklyActivityHelper.getmCategoryView();
        controller = getController();

        @SuppressLint("NonConstantResourceId") View.OnClickListener onClickListener = v -> {
            switch (v.getId()) {
                case R.id.dictionary_setup_btn:
                    clickEvent(0);
                    break;
                case R.id.dictionary_loop_btn:
                    clickEvent(1);
                    break;
                case R.id.dictionary_method_btn:
                    clickEvent(2);
                    break;
                case R.id.dictionary_etc_btn:
                    clickEvent(3);
                    break;
            }
        };

        dictionary_setup_btn.setOnClickListener(onClickListener);
        dictionary_loop_btn.setOnClickListener(onClickListener);
        dictionary_method_btn.setOnClickListener(onClickListener);
        dictionary_etc_btn.setOnClickListener(onClickListener);

        clickEvent(0);
    }

    private void clickEvent(int num) {
        Button [] button = {dictionary_setup_btn, dictionary_loop_btn, dictionary_method_btn, dictionary_etc_btn};

        for (int i = 0; i <button.length; i++) {
            button[i].setSelected(num == i);
        }

        dictionary_block_list.clear();

        List<BlocklyCategory.CategoryItem> blocks = mCategoryView.mRootCategory.getSubcategories().get(num).getItems();
        Log.e("test", "size : " + blocks.size()+"");

        for (BlocklyCategory.CategoryItem item : blocks) {
            String title = "";
            String info = "";
            boolean check = false;

            if (item.getType() == BlocklyCategory.CategoryItem.TYPE_BLOCK) {
                BlocklyCategory.BlockItem blockItem = (BlocklyCategory.BlockItem) item;
                Block block = blockItem.getBlock();

                String [] splitBlock = blockItem.getBlock().toString().split("\"");
                Log.e("block", splitBlock[1]);

                for (int i = 0; i < blockDictionaryList.size(); i++) {
                    if (blockDictionaryList.get(i).getBlock_type().equals(splitBlock[1])) {
                        title = blockDictionaryList.get(i).getBlock_name();
                        info = blockDictionaryList.get(i).getBlock_explanation();
                        check = true;
                    }
                }

                if (check) {
                    dictionary_block_list.add(new CodeBlock("0", title, info, R.drawable.problem_block2, block));
                }
            }
        }

        dictionaryAdapter = new CodeDictionaryAdapter(getApplicationContext(), controller, dictionary_block_list);
        block_list.setAdapter(dictionaryAdapter);
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
}