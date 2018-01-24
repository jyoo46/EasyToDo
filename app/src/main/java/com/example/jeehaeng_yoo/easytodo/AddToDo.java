package com.example.jeehaeng_yoo.easytodo;

import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

public class AddToDo extends AppCompatActivity implements View.OnFocusChangeListener, View.OnClickListener {

    Button addButton;
    EditText txtDate, txtTime, txtName, txtDetail;
    private int mYear, mMonth, mDay, mDayW, mHour, mMinute;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy / MM / dd EE");
    SimpleDateFormat timeFormat = new SimpleDateFormat("hh : mm aa");

    private ItemListOpenHelper mDB;
    private ItemListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_do);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        toolbar.setTitle("Add New To-Do");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        txtName = (EditText) findViewById(R.id.nameField);
        txtDate = (EditText) findViewById(R.id.dateField);
        txtTime = (EditText) findViewById(R.id.timeField);
        txtDetail = (EditText) findViewById(R.id.detailField);

        addButton = (Button) findViewById(R.id.addButton);

        txtDate.setOnFocusChangeListener(this);
        txtDate.setShowSoftInputOnFocus(false);
        txtDate.setInputType(InputType.TYPE_NULL);
        txtDate.setCursorVisible(false);
        txtTime.setOnFocusChangeListener(this);
        txtTime.setShowSoftInputOnFocus(false);
        txtTime.setInputType(InputType.TYPE_NULL);
        txtTime.setCursorVisible(false);

        addButton.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFocusChange(View view, boolean focus) {
        InputMethodManager imm =  (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        if (view == txtDate && focus) {
            txtName.clearFocus();
            txtDate.requestFocus();
            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);
            mDayW = c.get(Calendar.DAY_OF_WEEK);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            Date date = new Date(year - 1900, monthOfYear, dayOfMonth);

                            txtDate.setText(dateFormat.format(date).toString());
                            txtDate.clearFocus();
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }

        if (view == txtTime && focus) {
            txtName.clearFocus();
            txtTime.requestFocus();
            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            Date time = new Date();
                            time.setMinutes(minute);
                            time.setHours(hourOfDay);
                            txtTime.setText(timeFormat.format(time).toString());
                            txtTime.clearFocus();
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
    }

    @Override
    public void onClick(View view) {
        String[] data = {txtName.getText().toString(), txtDate.getText().toString(), txtTime.getText().toString(), txtDetail.getText().toString()};
        mDB = new ItemListOpenHelper(this);
        long id = mDB.insert(data);
        //mDB.closeDB();
        finish();
    }

//    @Override
//    public void onStop() {
//        super.onStop();
//        mDB.closeDB();
//    }
}
