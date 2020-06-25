package com.ljy.todo_alarm;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AlarmActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        Log.i("asdf","AlarmActivity is started");

        Toast.makeText(this, "AlarmActivity 실행됨", Toast.LENGTH_SHORT).show();
    }
}