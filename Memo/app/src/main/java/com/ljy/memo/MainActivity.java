package com.ljy.memo;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ljy.memo.databinding.ActivityMainBinding;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MemoAdapter.OnMemoClickListener {

    private ActivityMainBinding binding;
    private MemoDatabase db;
    private MemoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = MemoDatabase.getInstance(this);

        adapter = new MemoAdapter(this);
        binding.recyclerview.setAdapter(adapter);
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(this));

        binding.floatingActionButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, EditActivity.class);
            startActivity(intent);
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(binding.recyclerview);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new SelectTask().execute();
    }

    @Override
    public void onMemoClick(Memo memo, int position) {
        Intent intent = new Intent(this, EditActivity.class);

        intent.putExtra("id", memo.getId());
        intent.putExtra("title", memo.getTitle());
        intent.putExtra("contents", memo.getContents());
        intent.putExtra("date", memo.getDate());
        intent.putExtra("imagePath", memo.getImagePath());

        startActivity(intent);
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            final int position = viewHolder.getAdapterPosition();

            Memo item = adapter.getItem(position);
            new SelectTask().execute(item);

            adapter.removeItem(position);
            adapter.notifyItemRemoved(position);
        }
    };

    class SelectTask extends AsyncTask<Memo, Void, List<Memo>> {
        @Override
        protected List<Memo> doInBackground(Memo... memos) {
            if (memos.length != 0)
                db.getMemoDao().delete(memos[0]);
            return db.getMemoDao().select();
        }

        @Override
        protected void onPostExecute(List<Memo> memos) {
            super.onPostExecute(memos);
            adapter.removeItems();
            adapter.addItems(memos);
            adapter.notifyDataSetChanged();
        }
    }

}