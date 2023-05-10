/*
 *  Copyright 2016 Google Inc. All Rights Reserved.
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

package com.google.blockly.android.ui.fieldview;

import android.app.Application;
import android.content.Context;
import android.content.res.TypedArray;
import androidx.appcompat.widget.AppCompatSpinner;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.google.blockly.android.R;
import com.google.blockly.android.BlockDropdownClick;
import com.google.blockly.model.Field;
import com.google.blockly.model.FieldDropdown;

import java.util.List;

/**
 * Renders a dropdown field as part of a Block.
 */
public class BasicFieldDropdownView extends AppCompatSpinner implements FieldView {
    private static final String TAG = "BasicFieldDropdownView";

    public Field.Observer mFieldObserver = new Field.Observer() {
        @Override
        public void onValueChanged(Field field, String oldValue, String newValue) {
            setSelection(mDropdownField.getSelectedIndex());

            Log.e("test observer",context_name);

            Log.e("test", "observer");


        }
    };



//    protected FieldDropdown mDropdownField;

    public FieldDropdown mDropdownField;

    protected int mItemLayout;
    protected int mItemDropdownLayout;
    public boolean main_check;
    String context_name="";



    public BlockDropdownClick mListener;

    /*private BlockDropdownClick mListener;
>>>>>>> 1cb9472eb191ca1cb270664424def900f8231cfe
    public void setOnBlockDropdownClickListener(BlockDropdownClick listener) {
        Log.e("test in","setonblockdropdown");
        this.mListener = listener;
<<<<<<< HEAD
        main_check = true;
        Log.e("test main_check", main_check+"");

    }
=======
    }*/





    /**
     * Constructs a new {@link BasicFieldDropdownView}.
     *
     * @param context The application's context.
     */
    public BasicFieldDropdownView(Context context) {
        super(context);

        loadAttributes(null, 0);
    }

    public BasicFieldDropdownView(Context context, Boolean check) {
        super(context);
        loadAttributes(null, 0);
        main_check = check;
    }
    public void setMainCheck(Boolean check){
        main_check = check;
    }

    public Boolean getMainCheck(){
        return main_check;
    }

    public BasicFieldDropdownView(Context context, AttributeSet attrs) {
        super(context, attrs);
        loadAttributes(attrs, 0);
    }

    public BasicFieldDropdownView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        loadAttributes(attrs, defStyleAttr);
    }

    private void loadAttributes(AttributeSet attrs, int defStyleAttr) {
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs, R.styleable.BasicFieldDropdownView, defStyleAttr, 0);

        try {
            mItemLayout = a.getResourceId(R.styleable.BasicFieldDropdownView_itemLayout,
                    android.R.layout.simple_spinner_item);
            mItemDropdownLayout = a.getResourceId(
                    R.styleable.BasicFieldDropdownView_dropdownItemLayout,
                    android.R.layout.simple_spinner_dropdown_item);
        } finally {
            a.recycle();
        }
    }

    @Override

    public void setField(Field field) {
        FieldDropdown dropdownField = (FieldDropdown) field;
        if (mDropdownField == dropdownField) {
            return;
        }

        if (mDropdownField != null) {
            mDropdownField.unregisterObserver(mFieldObserver);
        }
        mDropdownField = dropdownField;
        if (mDropdownField != null) {
            List<String> items = mDropdownField.getDisplayNames();
            //여기 체크
            Log.e("items name check",items.get(0));
            ArrayAdapter<String> adapter =
                    new ArrayAdapter<>(getContext(), mItemLayout, items);
            adapter.setDropDownViewResource(mItemDropdownLayout);
            setAdapter(adapter);
            if (items.size() > 0) {
                setSelection(mDropdownField.getSelectedIndex());
            }
            Log.e("hello in ",mDropdownField.getSelectedIndex()+"");
            mDropdownField.registerObserver(mFieldObserver);
        } else {
            setSelection(0);
            setAdapter(null);
        }
    }

    @Override
    public Field getField() {
        return mDropdownField;
    }

    @Override
    public void setSelection(int position) {

        Log.e("test", "onnnn");

        Log.e("test", mListener+"");
        Log.e("test main_check", main_check+"");

        //Log.e("test", mListener+"");


        if (position == getSelectedItemPosition()) {
            return;
        }
        super.setSelection(position);
        if (mDropdownField != null) {
            Log.e("test", "on??");
            mDropdownField.setSelectedIndex(position);

        }


//        if (getMainCheck()){
//=======
//        /*if(mListener != null) {
//>>>>>>> 1cb9472eb191ca1cb270664424def900f8231cfe
//            mListener.onBlockDropdownClick(position);
//            Log.e("test", "on2");
//        }*/



    }

    @Override
    public void unlinkField() {
        setField(null);
    }
}
