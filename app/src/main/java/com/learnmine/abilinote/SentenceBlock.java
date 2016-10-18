package com.learnmine.abilinote;

/**
 * Created by baa on 10/11/2016.
 */

public class SentenceBlock {
    private String text;
    private String note;

    public SentenceBlock() {
    }

    public SentenceBlock(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

}

