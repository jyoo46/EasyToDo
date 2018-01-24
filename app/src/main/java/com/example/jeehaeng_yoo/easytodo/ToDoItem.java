package com.example.jeehaeng_yoo.easytodo;

/**
 * Created by JeeHaeng_Yoo on 12/29/2017.
 */

public class ToDoItem {
    private int mId;
    private String mName;
    private String mDate;
    private String mTime;
    private String mDetail;

    public ToDoItem() {}

    public int getId() {return this.mId;}

    public void setId(int id) {this.mId = id;}

    public String getName() {return this.mName;}

    public void setName(String name) {this.mName = name;}

    public String getDate() {return mDate;}

    public void setDate(String date) {this.mDate = date;}

    public String getTime() {return mTime;}

    public void setTime(String time) {this.mTime = time;}

    public String getDetail() {
        return mDetail;
    }

    public void setDetail(String mDetail) {
        this.mDetail = mDetail;
    }
}

