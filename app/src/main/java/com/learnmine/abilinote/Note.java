package com.learnmine.abilinote;

/**
 * Created by baa on 10/11/2016.
 */

public class Note {
    private String title;
    private String firstSentence;


    public Note() {
    }

    public Note(String title, String firstSentence) {
        this.title = title;
        this.firstSentence = firstSentence;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFirstSentence() {
        return firstSentence;
    }

    public void setFirstSentence(String firstSentence) {
        this.firstSentence = firstSentence;
    }

}
