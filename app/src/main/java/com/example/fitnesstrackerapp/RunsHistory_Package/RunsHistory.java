package com.example.fitnesstrackerapp.RunsHistory_Package;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fitnesstrackerapp.CurrentRun_Package.CurrentRunContract;
import com.example.fitnesstrackerapp.R;

public class RunsHistory extends Fragment implements RunsHistoryContract.View {

    private static RunsHistory INSTANCE = null;
    RunsHistoryPresenter presenter;
    RecyclerView recyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_runs_history, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        return view;
    }

    public static RunsHistory getInstance(){
        if(INSTANCE == null){
            INSTANCE = new RunsHistory();
        }
        return INSTANCE;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setViewItems();
    }

    private void setViewItems() {
        presenter = new RunsHistoryPresenter((CurrentRunContract.View) this, getContext());
        ItemAdapter adapter = new ItemAdapter(getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        adapter.setItems(presenter.getAllItems());
        recyclerView.setAdapter(adapter);
    }
}