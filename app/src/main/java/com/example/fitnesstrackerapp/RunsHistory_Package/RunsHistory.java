package com.example.fitnesstrackerapp.RunsHistory_Package;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fitnesstrackerapp.R;

public class RunsHistory extends Fragment implements RunsHistoryContract.View {

    private static RunsHistory INSTANCE = null;
    RunsHistoryPresenter presenter;
    RecyclerView recyclerView;
    TextView emptyView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_runs_history, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        emptyView = view.findViewById(R.id.empty_view);
        setHasOptionsMenu(true);
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

    @Override
    public void onResume() {
        super.onResume();
        setViewItems();
    }

    @Override
    public void onStart() {
        super.onStart();
        setViewItems();
    }

    private void setViewItems() {
        presenter = new RunsHistoryPresenter(this, getContext());
        ItemAdapter adapter = new ItemAdapter(getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter.setItems(presenter.getAllItems());
        if(adapter == null) {
            emptyView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else{
            recyclerView.setAdapter(adapter);
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.runs_history_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all:
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Are you sure you want to delete all items?");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (presenter.getAllItems() != null) {
                            presenter.deleteAll();
                            setViewItems();
                        } else {
                            Toast.makeText(getContext(), "Database is empty", Toast.LENGTH_SHORT).show();
                        }
                        Toast.makeText(getContext(), "All items are deleted", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("Cancel", null);
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}