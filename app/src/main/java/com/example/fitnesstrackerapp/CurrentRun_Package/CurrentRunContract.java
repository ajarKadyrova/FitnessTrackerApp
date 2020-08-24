package com.example.fitnesstrackerapp.CurrentRun_Package;

import com.example.fitnesstrackerapp.Item;

public interface CurrentRunContract {

    interface View {

    }

    interface Presenter{
        void insert(Item item);
    }
}
