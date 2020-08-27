package com.example.fitnesstrackerapp.RunsHistory_Package;

import android.content.Context;

import com.example.fitnesstrackerapp.Item;
import com.example.fitnesstrackerapp.data.AppDatabase;

import java.util.List;

public class RunsHistoryPresenter implements RunsHistoryContract.Presenter  {

    RunsHistoryContract.View view;
    AppDatabase appDatabase;

    public RunsHistoryPresenter(RunsHistoryContract.View view, Context context) {
        this.view = view;
        appDatabase = AppDatabase.getInstance(context);
    }

    @Override
    public List<Item> getAllItems() { return appDatabase.itemDao().getAllItems(); }

    @Override
    public void deleteAll() { appDatabase.itemDao().deleteAll(); }

}
