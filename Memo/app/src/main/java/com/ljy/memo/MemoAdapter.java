package com.ljy.memo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MemoAdapter extends RecyclerView.Adapter<MemoAdapter.ViewHolder> {

    List<Memo> items = new ArrayList<>();

    public interface OnMemoClickListener {
        void onMemoClick(Memo memo, int position);
    }

    private OnMemoClickListener listener;

    public MemoAdapter(OnMemoClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Memo item = items.get(position);
        holder.setItem(item);
        holder.itemView.setOnClickListener(v -> {
            listener.onMemoClick(item, position);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public Memo getItem(int position) {
        return items.get(position);
    }

    public void addItems(List<Memo> items) {
        this.items = items;
    }

    public void removeItem(int position) {
        items.remove(position);
    }

    public void removeItems() {
        items = null;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView contents;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            contents = itemView.findViewById(R.id.contents);
        }

        public void setItem(Memo item) {
            title.setText(item.getTitle());
            contents.setText(item.getContents());
        }
    }
}
