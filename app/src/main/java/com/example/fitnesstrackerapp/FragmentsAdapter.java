package com.example.fitnesstrackerapp;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.fitnesstrackerapp.CurrentRun_Package.CurrentRunFragment;
import com.example.fitnesstrackerapp.RunsHistory_Package.RunsHistoryFragment;

public class FragmentsAdapter extends FragmentPagerAdapter {

    Context context;

    public FragmentsAdapter(FragmentManager fm, Context context){
        super(fm);
        this.context = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0){
            return RunsHistoryFragment.getInstance();
        } else if(position == 1){
            return CurrentRunFragment.getInstance();
        }
        else return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
