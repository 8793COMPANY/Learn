/*
 * Copyright 2016 Google Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.blockly.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.Log;

import com.google.blockly.utils.BlockLoadingException;

import org.json.JSONObject;

/**
 * Adds a toggleable checkbox to an Input.
 */
public final class FieldJikco extends Field {
    public static final int DEFAULT_COLOR = 0xff0000;  // Red

    private int mColor;
    private String mCheck ="";

    public FieldJikco(String name) {
        this(name, DEFAULT_COLOR);
        Log.e("???","when in");
    }

    public FieldJikco(String name, int color) {
        super(name, TYPE_JIKCO);
        setColor(color);
    }

    public static FieldJikco fromJson(JSONObject json) throws BlockLoadingException {
        String name = json.optString("name");
        if (TextUtils.isEmpty(name)) {
            throw new BlockLoadingException("field_jikco \"name\" attribute must not be empty.");
        }
        int color = DEFAULT_COLOR;

        String colourString = json.optString("jikco");
        Log.e("colourString",colourString);
        if (!TextUtils.isEmpty(colourString)) {
            color = Color.parseColor(colourString);
        }
        return new FieldJikco(name, color);
    }

    @Override
    public FieldJikco clone() {
        return new FieldJikco(getName(), mColor);
    }

    @Override
    public boolean setFromString(String text) {
        Log.e("text",text);
        try {
            setColor(text);
//            setColor(Color.parseColor("#ff6565"));
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }

    /**
     * @return The current color in this field.
     */
    public int getColor() {
        return mColor;
    }

    /**
     * Sets the color stored in this field.
     *
     * @param color A color in the form 0xRRGGBB
     */
    public void setColor(int color) {
        final int newColor = 0xFFFFFF & color;
        if (mColor != newColor) {
            String oldValue = getSerializedValue();
            mColor = newColor;
            String newValue = getSerializedValue();
            fireValueChanged(oldValue, newValue);
        }
    }

    public void setColor(String check) {
        if (!mCheck.equals(check)) {
            String oldValue = getSerializedValue();
//            mColor = newColor;
            mCheck = check;
            String newValue = getSerializedValue();
            fireValueChanged(oldValue, newValue);
        }
    }



    @Override
    public String getSerializedValue() {
        return mCheck;
    }
}
