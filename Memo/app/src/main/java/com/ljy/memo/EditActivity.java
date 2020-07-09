package com.ljy.memo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ljy.memo.databinding.ActivityEditBinding;

import java.io.File;
import java.time.LocalDateTime;

import yuku.ambilwarna.AmbilWarnaDialog;

public class EditActivity extends AppCompatActivity {

    ActivityEditBinding binding;
    MemoDatabase db;
    int id;
    int tColor = 0;
    int width = 3;

    private String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = MemoDatabase.getInstance(this);

        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        String title = intent.getStringExtra("title");
        String content = intent.getStringExtra("contents");
        imagePath = intent.getStringExtra("imagePath");

        if (title != null) binding.title.setText(title);
        if (content != null) binding.contents.setText(content);
        if (imagePath != null) {
            File imgFile = new File(imagePath);
            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                binding.memoImage.setImageBitmap(myBitmap);

                binding.memoImage.setVisibility(View.VISIBLE);
                binding.drawingLayout.setVisibility(View.GONE);
            }
        }

        binding.colorBtn.setOnClickListener(v -> {
            AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(this, tColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
                @Override
                public void onCancel(AmbilWarnaDialog dialog) {
                }

                @Override
                public void onOk(AmbilWarnaDialog dialog, int color) {
                    tColor = color;
                    binding.drawingView.setColor(color);
                    binding.colorBtn.setBackgroundColor(color);
                }
            });
            colorPicker.show();
        });

        binding.upWidthBtn.setOnClickListener(v -> {
            if (width < 20) {
                binding.drawingView.setLineWith(width++);
                binding.width.setText("" + width);
            }
        });

        binding.downWidthBtn.setOnClickListener(v -> {
            if (width > 1) {
                binding.drawingView.setLineWith(width--);
                binding.width.setText("" + width);
            }
        });

        binding.resetBtn.setOnClickListener(v -> {
            binding.drawingView.reset();
        });
    }


    @Override
    public void onBackPressed() {
        if (binding.title.getText().toString().isEmpty() || binding.contents.getText().toString().isEmpty()) {
            Toast.makeText(this, "제목 또는 내용이 없어 저장하지 못했습니다", Toast.LENGTH_SHORT).show();
        } else {
            Memo item = new Memo();
            item.title = binding.title.getText().toString();
            item.contents = binding.contents.getText().toString();
            item.date = LocalDateTime.now().toString();
            if (imagePath == null) {
                item.imagePath = binding.drawingView.save(this);
            } else {
                item.imagePath = imagePath;
            }
            new SaveTask().execute(item);
        }
        super.onBackPressed();
    }

    class SaveTask extends AsyncTask<Memo, Void, Void> {

        @Override
        protected Void doInBackground(Memo... memos) {
            if (id == 0) {
                db.getMemoDao().insert(memos[0]);
            } else {
                memos[0].id = id;
                db.getMemoDao().update(memos[0]);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(getApplicationContext(), "저장되었습니다", Toast.LENGTH_SHORT).show();
        }

    }
}