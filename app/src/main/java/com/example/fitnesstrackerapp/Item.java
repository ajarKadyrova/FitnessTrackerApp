package com.example.fitnesstrackerapp;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "items_table")
public class Item {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    private int mId;
    @ColumnInfo(name = "distance")
    private double mDistance;
    @ColumnInfo(name = "time")
    private String mTime;
    @ColumnInfo(name = "speed")
    private double mSpeed;
    @ColumnInfo(name = "calories")
    private double mCalories;
    @ColumnInfo(name = "date")
    private String mDate;

    public Item(double distance, String time, double speed, double calories, String date) {
        this.mDistance = distance;
        this.mTime = time;
        this.mSpeed = speed;
        this.mCalories = calories;
        this.mDate = date;
    }

    public int getId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    public double getDistance() {
        return mDistance;
    }

    public String getTime() {
        return mTime;
    }

    public double getSpeed() {
        return mSpeed;
    }

    public double getCalories() { return mCalories; }

    public String getDate() {
        return mDate;
    }
}
