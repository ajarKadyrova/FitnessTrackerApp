package com.example.fitnesstrackerapp.RunsHistory_Package;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnesstrackerapp.Item;
import com.example.fitnesstrackerapp.R;

import java.util.ArrayList;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    List<Item> mItems = new ArrayList<>();
    Context context;

    public ItemAdapter(Context context) {
        this.context = context;
    }

    public void setItems(List<Item> items) {
        if (items != null) {
            this.mItems.clear();
            this.mItems.addAll(items);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ItemAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_run, parent, false);
        return new ItemAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemAdapter.ItemViewHolder holder, int position) {
        holder.bind(mItems.get(position));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView date;
        TextView distance;
        TextView time;
        TextView speed;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date_rv);
            distance = itemView.findViewById(R.id.distance_rv);
            time = itemView.findViewById(R.id.time_rv);
            speed = itemView.findViewById(R.id.speed_rv);
        }

        public void bind(Item item) {
            date.setText(item.getDate());
            distance.setText(Double.valueOf(item.getDistance()) + " km");
            time.setText(item.getTime());
            speed.setText(Double.valueOf(item.getSpeed()) + " /km");
        }
    }
}