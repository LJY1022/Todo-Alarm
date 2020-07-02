package com.ljy.todo_alarm;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ljy.todo_alarm.databinding.ActivityAlarmBinding;

public class AlarmActivity extends AppCompatActivity {

    ActivityAlarmBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAlarmBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.finishButton.setOnClickListener(v -> {
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Toast.makeText(this, "종료 버튼을 눌러주세요", Toast.LENGTH_SHORT).show();
    }
}