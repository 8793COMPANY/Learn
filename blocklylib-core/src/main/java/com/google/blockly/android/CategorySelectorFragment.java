/*
 *  Copyright 2017 Google Inc. All Rights Reserved.
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.google.blockly.android;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.OrientationHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.blockly.Setting;
import com.google.blockly.android.codegen.CodeGenerationRequest;
import com.google.blockly.android.control.BlocklyController;
import com.google.blockly.android.ui.CategorySelectorUI;
import com.google.blockly.android.ui.CategoryView;
import com.google.blockly.android.ui.Rotation;
import com.google.blockly.android.ui.WorkspaceHelper;
import com.google.blockly.model.BlocklyCategory;
import com.google.blockly.model.DefaultBlocks;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.List;

/**
 * Creates a set of tabs of Blockly categories. The set of categories should be provided by
 * {@link #setContents(BlocklyCategory)}. Interaction with other fragments is not handled by this
 * class and should be done by registering a callback with
 * {@link #setCategoryCallback(CategorySelectorUI.Callback)}.
 *
 */
public class CategorySelectorFragment extends Fragment implements CategorySelectorUI {
    private static final String TAG = "CategorySelectorFragment";

    protected CategoryView mCategoryView;
    protected WorkspaceHelper mHelper;
    protected BlocklyController mController;

    protected int mScrollOrientation = OrientationHelper.HORIZONTAL;
    protected int mWorkspaceType = 0;
    protected @Rotation.Enum int mLabelRotation = Rotation.NONE;



    static final List<String> TURTLE_BLOCK_GENERATORS = Arrays.asList(
            "turtle/generators.js"
    );



  /*  public void get_ports() {
        PeripheralManager manager = PeripheralManager.getInstance();
        List<String> deviceList = manager.getUartDeviceList();
        if (deviceList.isEmpty()) {
            Log.i(TAG, "No UART port available on this device.");
        } else {
            Log.i(TAG, "List of available devices: " + deviceList);
            System.out.println(deviceList);
        }
    }*/


    @Override
    public void onInflate(Context context, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(context, attrs, savedInstanceState);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.BlocklyCategory,
                0, 0);

        try {
            mScrollOrientation = a.getInt(R.styleable.BlocklyCategory_scrollOrientation,
                    mScrollOrientation);
            //noinspection ResourceType
            mLabelRotation = a.getInt(R.styleable.BlocklyCategory_labelRotation, mLabelRotation);
            mWorkspaceType = a.getInt(R.styleable.BlocklyCategory_workspaceType, mWorkspaceType);
            Log.e("hello workspacetype", mWorkspaceType+"");
            Setting.workspace_type = mWorkspaceType;
        } finally {
            a.recycle();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        Log.e("category test", "categoryselector oncreateview 0 ");
        int layout = 0;
        if (mWorkspaceType == 0){
            layout = mScrollOrientation == OrientationHelper.VERTICAL
                    ? R.layout.default_category_start : R.layout.default_category_horizontal;
        }else {
            layout = R.layout.drone_category;
        }

        mCategoryView = (CategoryView) inflater.inflate(layout, null);
        mCategoryView.setLabelRotation(Rotation.CLOCKWISE);

//        BlocklyActivityHelper blocklyActivityHelper =  new BlocklyActivityHelper(
//                (AppCompatActivity) getActivity(), getChildFragmentManager());;
//        blocklyActivityHelper.requestCodeGeneration(
//                DefaultBlocks.LANGUAGE_DEFINITION,
//                TURTLE_BLOCK_DEFINITIONS,
//                TURTLE_BLOCK_GENERATORS,
//                getCodeGenerationCallback());

        Log.e("category test", "categoryselector oncreateview");
        return mCategoryView;
    }



    public void setContents(BlocklyCategory rootCategory) {
        mCategoryView.setContents(rootCategory);
    }

    public void setCurrentCategory(BlocklyCategory category) {
        mCategoryView.setCurrentCategory(category);
    }

    public BlocklyCategory getCurrentCategory() {
        return mCategoryView.getCurrentCategory();
    }

    public void setCategoryCallback(CategorySelectorUI.Callback categoryCallback) {
        mCategoryView.setCallback(categoryCallback);
    }


}
