package com.ljy.todo_alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.tabs.TabLayout;
import com.ljy.todo_alarm.databinding.ActivityAddAlarmBinding;

import java.util.Calendar;

public class AddAlarmActivity extends AppCompatActivity {

    ActivityAddAlarmBinding binding;
    int position;
    PendingIntent pendingIntent;
    AlarmManager alarmManager;


    BroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddAlarmBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        receiver = new AlarmReceiver();

        IntentFilter filter = new IntentFilter("android.intent.action.Alarm");
        this.registerReceiver(receiver, filter);

        Intent intent = new Intent(this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);


        binding.tabLayout.getTabAt(0).select();
        position = 0;

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    binding.repeat.setVisibility(View.VISIBLE);
                    binding.oneTime.setVisibility(View.GONE);
                    position = 0;
                } else {
                    binding.repeat.setVisibility(View.GONE);
                    binding.oneTime.setVisibility(View.VISIBLE);
                    position = 1;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        binding.addButton.setOnClickListener(v -> {
            if (position == 0) {
                Toast.makeText(getApplicationContext(), "repeatAlarm", Toast.LENGTH_SHORT).show();
            } else if (position == 1) {
                Toast.makeText(getApplicationContext(), "onetimeAlarm", Toast.LENGTH_SHORT).show();

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, binding.onetimeDatePicker.getYear());
                calendar.set(Calendar.MONTH, binding.onetimeDatePicker.getMonth());
                calendar.set(Calendar.DAY_OF_MONTH, binding.onetimeDatePicker.getDayOfMonth());
                calendar.set(Calendar.HOUR_OF_DAY, binding.onetimeTimePicker.getHour());
                calendar.set(Calendar.MINUTE, binding.onetimeTimePicker.getMinute());

                Log.i("asdf", "result : " + calendar.get(Calendar.YEAR) + ", " + calendar.get(Calendar.MONTH) + ", " + calendar.get(Calendar.DAY_OF_MONTH) + " /  " + calendar.get(Calendar.HOUR_OF_DAY) + ", " + calendar.get(Calendar.MINUTE));

                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

                finish();
            }
        });
    }
}
