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

import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.VisibleForTesting;

import com.google.blockly.utils.BlockLoadingException;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Adds a date picker to an Input. Dates must be in the format "YYYY-MM-DD"
 */
public final class FieldDatee extends Field {
    private static final String TAG = "FieldDatee";

    // Date format used for serialization.
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

    private final Date mDate = new Date();

    public FieldDatee(String name) {
        super(name, TYPE_DATEE);
    }

    public FieldDatee(String name, long milliseconds) {
        this(name);
        mDate.setTime(milliseconds);
    }

    @VisibleForTesting
    public FieldDatee(String name, String dateString) {
        this(name);
        if (!setFromString(dateString)) {
            throw new IllegalArgumentException("Invalid date: " + dateString);
        }
    }

    public static FieldDatee fromJson(JSONObject json) throws BlockLoadingException {
        Log.e("datee json",json.toString());
        String name = json.optString("name");
        if (TextUtils.isEmpty(name)) {
            throw new BlockLoadingException("field_datee \"name\" attribute must not be empty.");
        }
        FieldDatee field = new FieldDatee(name);
        String dateStr = json.optString("date");
        Log.e("dateStr",dateStr);
        if (!TextUtils.isEmpty(dateStr) && !field.setFromString(dateStr)) {
            throw new BlockLoadingException("Unable to parse date: " + dateStr);
        }
        return field;
    }

    @Override
    public FieldDatee clone() {
        return new FieldDatee(getName(), mDate.getTime());
    }

    @Override
    public boolean setFromString(String text) {
        Date date = null;
        try {
            date = DATE_FORMAT.parse(text);
            setDate(date);
            return true;
        } catch (ParseException e) {
            Log.e(TAG, "Unable to parse date " + text, e);
            return false;
        }
    }

    /**
     * @return The date in this field.
     */
    public Date getDate() {
        return mDate;
    }

    /**
     * Sets this field to the specified {@link Date}.
     */
    public void setDate(Date date) {
        if (date == null) {
            throw new IllegalArgumentException("Date may not be null.");
        }
        setTime(date.getTime());
    }

    /**
     * @return The string format for the date in this field.
     */
    public String getLocalizedDateString() {
        return getDate().toString();
    }

    /**
     * Sets this field to a specific time.
     *
     * @param millis The time in millis since UNIX Epoch.
     */
    public void setTime(long millis) {
        long oldTime = mDate.getTime();
        if (millis != oldTime) {
            String oldValue = getSerializedValue();
            mDate.setTime(millis);
            String newValue = getSerializedValue();
            fireValueChanged(oldValue, newValue);
        }
    }

    @Override
    public String getSerializedValue() {
        return DATE_FORMAT.format(mDate);
    }
}
