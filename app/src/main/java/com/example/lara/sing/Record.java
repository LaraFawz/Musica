package com.example.lara.sing;

/**
 * Created by Lara on 5/10/2018.
 */

public class Record {
    private int recId;
    private String userName;
    private int userId;
    private String date;
    private String data;
    private String title;


    public Record() {
    }

    public Record(int recId, String userName, int userId, String date, String data,  String title) {
        this.recId = recId;
        this.userName = userName;
        this.userId = userId;
        this.date = date;
        this.data = data;
        this.title = title;
    }
}


