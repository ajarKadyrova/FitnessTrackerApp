package com.example.fitnesstrackerapp.RunsHistory_Package;

import com.example.fitnesstrackerapp.Item;

import java.util.List;

public interface RunsHistoryContract {

    interface View {

    }

    interface Presenter{
        List<Item> getAllItems();
    }
}
