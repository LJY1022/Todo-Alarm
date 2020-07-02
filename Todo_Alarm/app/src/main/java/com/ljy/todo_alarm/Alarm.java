package com.ljy.todo_alarm;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Calendar;

@Entity(tableName = "alarmTable")
public class Alarm {

    @PrimaryKey(autoGenerate = true)
    int id;
    String date;
    String time;
    String contents;

    public void Alarm(String date, String time, String contents) {
        this.date = date;
        this.time = time;
        this.contents = contents;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }
}
