package com.google.blockly.model;

import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;

import com.google.blockly.utils.BlockLoadingException;

import org.json.JSONObject;

/**
 * FieldJikco 참고
 */

public class FieldDotmatrix extends Field {
    public static final int DEFAULT_COLOR = 0xff0000;  // Red

    private int mColor;

    private String mCheck = "";

    public FieldDotmatrix(String name) {
        this(name, DEFAULT_COLOR);

        Log.e("???","when in");
    }

    public FieldDotmatrix(String name, int color) {
        super(name, TYPE_DOTMATRIX);
        setColor(color);
    }

    public static FieldDotmatrix fromJson(JSONObject json) throws BlockLoadingException {
        String name = json.optString("name");

        if (TextUtils.isEmpty(name)) {
            throw new BlockLoadingException("field_dotmatrix \"name\" attribute must not be empty.");
        }

        int color = DEFAULT_COLOR;

        String colourString = json.optString("dotmatrix");
        Log.e("colourString", colourString);

        if (!TextUtils.isEmpty(colourString)) {
            color = Color.parseColor(colourString);
        }

        return new FieldDotmatrix(name, color);
    }

    public FieldDotmatrix clone() {
        return new FieldDotmatrix(getName(), mColor);
    }

    @Override
    public boolean setFromString(String text) {
        Log.e("text", text);

        try {
            setColor(text);
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
