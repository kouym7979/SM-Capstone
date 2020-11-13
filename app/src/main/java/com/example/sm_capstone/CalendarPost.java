package com.example.sm_capstone;

import java.sql.Time;
import java.util.Date;

public class CalendarPost {

    private String scheduleId;
    private String time;
    private String date;
    private String writer_name;
    private String comments;

    //빈 생성자
    public CalendarPost(){    }

    public String getScheduleId() { return scheduleId; }

    public void setScheduleId(String scheduleId) { this.scheduleId = scheduleId; }

    public String getTime() { return time; }

    public void setTime(String time) { this.time = time; }

    public String getDate() { return date; }

    public void setDate(String date) { this.date = date; }

    public String getWriter_name() { return writer_name; }

    public void setWriter_name(String writer_name) { this.writer_name = writer_name; }

    public String getComments() { return comments; }

    public void setComments(String comments) { this.comments = comments; }

    public CalendarPost(String scheduleId, String writer_name, String date, String time, String commnets){
        this.scheduleId = scheduleId;
        this.writer_name = writer_name;
        this.date = date;
        this.time = time;
        this.comments = comments;
    }

    public String toString(){
        return "Schedule{" +
                "scheduleId='"+scheduleId+'\'' +
                ", writer_name='" + writer_name + '\''+
                ", date='" + date + '\''+
                ", time='" + time + '\''+
                ", comments='" + comments +
                '}';


    }
}
