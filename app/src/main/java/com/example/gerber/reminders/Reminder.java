package com.example.gerber.reminders;

/**
 * Created by Sel on 2016/8/22.
 */
public class Reminder {
    private int mId;
    private String mContent;
    private int iImportant;


    public Reminder(int id, String content, int iImportant) {
        mId = id;
        mContent = content;
        this.iImportant = iImportant;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public int getiImportant() {
        return iImportant;
    }

    public void setiImportant(int iImportant) {
        this.iImportant = iImportant;
    }
}



