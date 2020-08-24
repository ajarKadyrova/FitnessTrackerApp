package com.example.fitnesstrackerapp.CurrentRun_Package;

import android.app.Application;
import android.content.Context;

import com.example.fitnesstrackerapp.Item;
import com.example.fitnesstrackerapp.data.AppDatabase;

public class CurrentRunPresenter implements CurrentRunContract.Presenter {

    CurrentRunContract.View view;
    AppDatabase appDatabase;

    public CurrentRunPresenter(CurrentRunContract.View view, Context context) {
        this.view = view;
        appDatabase = AppDatabase.getInstance(context);
    }

    @Override
    public void insert(Item item) { appDatabase.itemDao().insert(item); }
}
