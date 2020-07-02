package com.ljy.todo_alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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

    AlarmDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddAlarmBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        db = AlarmDatabase.getInstance(this);

        Intent intent = new Intent(this, AlarmReceiver.class);
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

                int month = binding.onetimeDatePicker.getMonth();
                int day = binding.onetimeDatePicker.getDayOfMonth();
                int hour = binding.onetimeTimePicker.getHour();
                int minute = binding.onetimeTimePicker.getMinute();

                Alarm alarm = new Alarm();

                String date = "" + binding.onetimeDatePicker.getYear() + "년 ";
                date += (month < 10) ? "0" + (month + 1) : (month + 1);
                date += "월 ";
                date += (day < 10) ? "0" + day : day;
                date += "일";
                String time = "" + hour + "시 ";
                time += (minute < 10) ? "0" + minute : minute;
                time += "분";

                alarm.setDate(date);
                alarm.setTime(time);
                alarm.setContents(binding.onetimeEditText.getText().toString());

                new insertTask().execute(alarm);
                new getIdTask().execute();
                pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_NO_CREATE);

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, binding.onetimeDatePicker.getYear());
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DATE, day);
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, 0);

                Log.i("asdf", "result : " + calendar.get(Calendar.YEAR) + ", " + calendar.get(Calendar.MONTH) + ", " + calendar.get(Calendar.DATE) + " /  " + calendar.get(Calendar.HOUR_OF_DAY) + ", " + calendar.get(Calendar.MINUTE));

                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

                finish();
            }
        });
    }

    class getIdTask extends AsyncTask<Void, Void, Integer> {

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            Log.i("asdf", " id : " + integer);
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            return db.getAlarmDao().getId();
        }
    }

    class insertTask extends AsyncTask<Alarm, Void, Void> {
        @Override
        protected Void doInBackground(Alarm... alarms) {
            db.getAlarmDao().insert(alarms[0]);
            return null;
        }
    }
}
