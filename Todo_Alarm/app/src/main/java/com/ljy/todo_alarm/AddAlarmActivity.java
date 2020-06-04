package com.ljy.todo_alarm;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.ljy.todo_alarm.databinding.ActivityAddAlarmBinding;

public class AddAlarmActivity extends AppCompatActivity {

    ActivityAddAlarmBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddAlarmBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    binding.repeat.setVisibility(View.VISIBLE);
                    binding.oneTime.setVisibility(View.GONE);
                } else {
                    binding.repeat.setVisibility(View.GONE);
                    binding.oneTime.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
