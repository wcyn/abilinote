package com.learnmine.abilinote;

/**
 * Created by baa on 9/24/2016.
 */
public class SentenceBlock {
    private String  sentence;
    private long sentenceId, noteId;
    private int relevant;


    public SentenceBlock (String sentence, long noteId, int relevant) {
        this.sentence = sentence;
        this.noteId = noteId;
        this.relevant = relevant;
        this.sentenceId = 0;
    }

    public SentenceBlock (String sentence, long noteId, int relevant
            ,long sentenceId) {
        this.sentence = sentence;
        this.noteId = noteId;
        this.relevant = relevant;
        this.sentenceId = sentenceId;
    }

    public String getSentence() { return sentence; }

    public int getRelevance() { return relevant; }

    public long getId() { return sentenceId; }

    public long getNoteId() { return noteId; }

    public String toString() {
        String s = "ID: " + sentenceId + "noteID: " + noteId + "relevant: " + relevant +
                " sentence: " + sentence;
        return s;
    }

//    public int getAssociateDrawable() { return categoryToDrawable(category); }

//    public static int categoryToDrawable(Category category) {
//        switch (category) {
//            case IMPORTANT:
//                return R.mipmap.ic_p;
//            case NOTHING:
//                return R.mipmap.ic_q;
//            case ASSIGNMENT:
//                return R.mipmap.ic_f;
//            case IDEA:
//                return R.mipmap.ic_q;
//            case RESEARCH:
//                return R.mipmap.ic_t;
//        }
//
//        return R.mipmap.ic_p; //default
//    }

}
