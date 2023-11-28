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

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;

import androidx.appcompat.widget.AppCompatTextView;

import com.google.blockly.model.Field;
import com.google.blockly.model.FieldDate;
import com.google.blockly.model.FieldDatee;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Renders a date and a date picker as part of a Block.
 */
public class BasicFieldDateeView extends AppCompatTextView implements FieldView {
    private static final DateFormat LOCALIZED_FORMAT = DateFormat.getDateInstance(DateFormat.SHORT);
    private DatePickerDialog.OnDateSetListener dateListener;

    private Calendar calendar = Calendar.getInstance();

    protected Field.Observer mFieldObserver = new Field.Observer() {
        @Override
        public void onValueChanged(Field field, String oldValue, String newValue) {
            String dateStr = LOCALIZED_FORMAT.format(mDateField.getDate());
            Log.e("datee test observer","in");
            if (!dateStr.contentEquals(getText())) {
//                setText(calendar.get(Calendar.YEAR)+"-"+calendar.get(Calendar.MONTH)+"-"+calendar.get(Calendar.DATE));
            }
        }
    };

    protected FieldDatee mDateField;

    public BasicFieldDateeView(Context context) {
        super(context);
//        setText(calendar.get(Calendar.YEAR)+"-"+calendar.get(Calendar.MONTH)+"-"+calendar.get(Calendar.DATE));
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    Log.e(mDateField.getLocalizedDateString(),"first no in????????????");
                    dialog = new DatePickerDialog(context, AlertDialog.THEME_HOLO_LIGHT,dateListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DATE));
                    dialog.getDatePicker().setCalendarViewShown(false);
                    dialog.getDatePicker().setSpinnersShown(true);

                    initListener();
                }
                dialog.show();
            }
        });

    }


    public BasicFieldDateeView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    initListener();
                    Log.e(mDateField.getLocalizedDateString(),"second no in????????????");
                    String [] date = getText().toString().split("-");
                    dialog = new DatePickerDialog(context, AlertDialog.THEME_HOLO_LIGHT,dateListener,Integer.parseInt(date[0]),Integer.parseInt(date[1])-1,Integer.parseInt(date[2]));
                    dialog.getDatePicker().setCalendarViewShown(false);
                    dialog.getDatePicker().setSpinnersShown(true);

                    //최소 날짜
                    Calendar minDate = Calendar.getInstance();
                    String [] minDay = get3DayAgoDate(getCurrentDate());
                    minDate.set(Integer.parseInt(minDay[0]),Integer.parseInt(minDay[1])-1,Integer.parseInt(minDay[2]));
                    dialog.getDatePicker().setMinDate(minDate.getTime().getTime());
                    //최대 날짜
                    Calendar maxDate = Calendar.getInstance();
                    String [] maxDay = getCurrentDate().split("-");
                    maxDate.set(Integer.parseInt(maxDay[0]),Integer.parseInt(maxDay[1])-1,Integer.parseInt(maxDay[2]));
                    dialog.getDatePicker().setMaxDate(maxDate.getTime().getTime());

                    Log.e("field date",mDateField.getDate().toString());

                }
                dialog.show();
            }
        });

        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.e("in after","text changed");
                if (mDateField != null) {
                    mDateField.setFromString(s.toString());
                }
            }
        });
    }

    public BasicFieldDateeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


//        setText(calendar.get(Calendar.YEAR)+"-"+calendar.get(Calendar.MONTH)+"-"+calendar.get(Calendar.DATE));
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    initListener();
                    Log.e(mDateField.getLocalizedDateString(),"third no in????????????");
                    String [] date = getText().toString().split("-");

                    dialog = new DatePickerDialog(context, AlertDialog.THEME_HOLO_LIGHT,dateListener,Integer.parseInt(date[0]),Integer.parseInt(date[1]),Integer.parseInt(date[2]));
                    dialog.getDatePicker().setCalendarViewShown(false);
                    dialog.getDatePicker().setSpinnersShown(true);
//                    Calendar minDate = Calendar.getInstance();
//                    String [] days3Ago = get3DayAgoDate();
//                    minDate.set();
//
//                    dialog.getDatePicker().setMinDate();


                }
                dialog.show();
            }
        });

        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.e("datee in after","text changed");
                if (mDateField != null) {
                    mDateField.setFromString(s.toString());
                }
            }
        });

    }

    private String[] get3DayAgoDate(String date) {
        String [] days = date.split("-");
        Calendar cal = Calendar.getInstance();
        cal.set(Integer.parseInt(days[0]), Integer.parseInt(days[1])-1, Integer.parseInt(days[2]));
        cal.add(Calendar.DATE, -2);
        Date weekago = cal.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd",	Locale.getDefault());
        return formatter.format(weekago).split("-");
    }

    private String getCurrentDate() {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        String getTime = sdf.format(date);
        return getTime;
    }


    @Override
    public void setField(Field field) {
        Log.e("datee","field in");
        FieldDatee dateField = (FieldDatee) field;
        if (mDateField == dateField) {
            return;
        }

        if (mDateField != null) {
            mDateField.unregisterObserver(mFieldObserver);
        }
        mDateField = dateField;
        if (mDateField != null) {

            setText(calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(Calendar.DATE));
//            setText(mDateField.getLocalizedDateString());
            mDateField.registerObserver(mFieldObserver);



        } else {
            setText("");
        }
    }

    @Override
    public Field getField() {
        return mDateField;
    }

    @Override
    public void unlinkField() {
        setField(null);
    }

    public void initListener(){
        dateListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                Log.e("datee date", year +","+monthOfYear+","+dayOfMonth);
//                mDateField.setFromString(mDateField.getLocalizedDateString());

                setText(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
            }
        };
    }
}
