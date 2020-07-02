package com.ljy.todo_alarm;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface AlarmDao {

    @Insert
    void insert(Alarm alarm);

    @Query("select * from alarmTable ORDER BY date ASC, time ASC")
    List<Alarm> select();

    @Query("select * from alarmTable WHERE date = :date")
    List<Alarm> selectToday(String date);

    @Query("select id from alarmTable ORDER BY id DESC")
    int getId();

    @Delete
    void delete(Alarm alarm);
}
