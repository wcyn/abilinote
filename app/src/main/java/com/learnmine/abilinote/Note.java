package com.learnmine.abilinote;

/**
 * Created by baa on 9/21/2016.
 */
public class Note {
    private String title, message;
    private long noteId, dateCreatedMilli;
    private Category category;

    public enum Category {PERSONAL, TECHNICAL, QUOTE, FINANCE}

    public Note (String title, String message, Category category) {
        this.title = title;
        this.message = message;
        this.category = category;
        this.noteId = 0;
        this.dateCreatedMilli = 0;

    }

    public Note (String title, String message, Category category, long noteId,
                 long dateCreatedMilli) {
        this.title = title;
        this.message = message;
        this.category = category;
        this.noteId = noteId;
        this.dateCreatedMilli = dateCreatedMilli;
    }

    public String getTitle() { return title; }

    public String getMessage() { return message; }

    public Category getCategory() { return category; }

    public long getDate() { return dateCreatedMilli; }

    public long getId() { return noteId; }

    public String toString() {
        String s = "ID: " + noteId + " title: " + title + " Message: " + message + " itemId: " +
                category.name() + " Date: ";
        return s;
    }

    public int getAssociateDrawable() { return categoryToDrawable(category); }

    public static int categoryToDrawable(Category category) {
        switch (category) {
            case PERSONAL:
                return R.mipmap.ic_p;
            case FINANCE:
                return R.mipmap.ic_f;
            case QUOTE:
                return R.mipmap.ic_q;
            case TECHNICAL:
                return R.mipmap.ic_t;
        }

        return R.mipmap.ic_p; //default
    }

}
