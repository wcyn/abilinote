package com.learnmine.abilinote;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by baa on 9/22/2016.
 */
public class AbilinoteDbAdapter {

    private static final String DATABASE_NAME = "abilinote.db";
    private static final int DATABASE_VERSION = 1;

    // Notes Database
    public static final String NOTE_TABLE = "notes";
    public static final String NOTE_COLUMN_ID = "_id";
    public static final String NOTE_COLUMN_TITLE = "title";
    public static final String NOTE_COLUMN_MESSAGE = "message";
    public static final String NOTE_COLUMN_CATEGORY = "category";
    public static final String NOTE_COLUMN_DATE = "date";

    // Sentence Blocks Database
    public static final String SENTENCE_BLOCK_TABLE = "sentence_blocks";
    public static final String SB_COLUMN_ID = "_id";
    public static final String SB_NOTE_ID = "noteId";
    public static final String SB_COLUMN_RELEVANT = "relevant";
    public static final String SB_COLUMN_SENTENCE = "sentence";
    public static final String SB_COLUMN_CATEGORY = "category";

    private String[] allNoteColumns = {NOTE_COLUMN_ID, NOTE_COLUMN_TITLE, NOTE_COLUMN_MESSAGE,
            NOTE_COLUMN_CATEGORY, NOTE_COLUMN_DATE};
    private String[] allSbColumns = {SB_COLUMN_ID, SB_NOTE_ID, SB_COLUMN_RELEVANT,
            SB_COLUMN_SENTENCE, SB_COLUMN_CATEGORY};

    public final static String CREATE_TABLE_NOTE = "create table " + NOTE_TABLE + " ( "
            + NOTE_COLUMN_ID + " integer primary key autoincrement, "
            + NOTE_COLUMN_TITLE + " text not null, "
            + NOTE_COLUMN_MESSAGE + " text not null, "
            + NOTE_COLUMN_CATEGORY + " integer not null, "
            + NOTE_COLUMN_DATE + ");";

    public final static String CREATE_TABLE_SENTENCE_BLOCK = "create table " + SENTENCE_BLOCK_TABLE
            + " ( "
            + SB_COLUMN_ID + " integer primary key autoincrement, "
            + SB_NOTE_ID + " long, "
            + "FOREIGN KEY (" + SB_NOTE_ID + ") REFERENCES " + NOTE_TABLE + "(" + NOTE_COLUMN_ID
            + "), "
            + SB_COLUMN_RELEVANT + " integer default 0, "
            + SB_COLUMN_SENTENCE + " text not null, "
            + SB_COLUMN_CATEGORY + " integer not null, "  + ");";

    private SQLiteDatabase sqlDB;
    private Context context;
    private AbilinoteDbHelper abilinoteDbHelper;

    public AbilinoteDbAdapter (Context ctx) {
        context = ctx;
    }

    public AbilinoteDbAdapter open() throws android.database.SQLException {
        abilinoteDbHelper = new AbilinoteDbHelper(context);
        sqlDB = abilinoteDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        abilinoteDbHelper.close();
    }

    public Note createNote(String title, String message, Note.Category category) {
        ContentValues values = new ContentValues();
        values.put(NOTE_COLUMN_TITLE, title);
        values.put(NOTE_COLUMN_MESSAGE, message);
        values.put(NOTE_COLUMN_CATEGORY, category.name());
        values.put(NOTE_COLUMN_DATE, Calendar.getInstance().getTimeInMillis() + "");

        long insertId = sqlDB.insert(NOTE_TABLE, null, values);

        Cursor cursor = sqlDB.query(NOTE_TABLE, allNoteColumns, NOTE_COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Note newNote = cursorToNote(cursor);
        cursor.close();
        return newNote;
    }

    public long updateNote(long idToUpdate, String newTitle, String newMessage,
                           Note.Category newCategory) {
        ContentValues values = new ContentValues();
        values.put(NOTE_COLUMN_TITLE, newTitle);
        values.put(NOTE_COLUMN_MESSAGE, newMessage);
        values.put(NOTE_COLUMN_CATEGORY, newCategory.name());
        values.put(NOTE_COLUMN_DATE, Calendar.getInstance().getTimeInMillis() + "");

        return sqlDB.update(NOTE_TABLE, values, NOTE_COLUMN_ID + " = " + idToUpdate, null);
    }

    public ArrayList<Note> getAllNotes() {
        ArrayList<Note> notes = new ArrayList<Note>();

        Cursor cursor = sqlDB.query(NOTE_TABLE, allNoteColumns, null, null, null, null, null);

        for(cursor.moveToLast(); !cursor.isBeforeFirst(); cursor.moveToPrevious()) {
            Note note = cursorToNote(cursor);
            notes.add(note);
            Log.d(MainActivity.LOG_TAG, "Note in get note: " + note);
        }
        cursor.close();
        return notes;
    }

    private Note cursorToNote(Cursor cursor) {
        Note newNote = new Note(cursor.getString(1), cursor.getString(2),
                Note.Category.valueOf(cursor.getString(3)), cursor.getLong(0), cursor.getLong(4));
        return newNote;
    }

    public SentenceBlock createSentenceBlock(String sentence, SentenceBlock.Category category,
                                             long noteId, boolean relevant) {
        ContentValues values = new ContentValues();
        values.put(SB_COLUMN_SENTENCE, sentence);
        values.put(SB_COLUMN_CATEGORY, category.name());
        values.put(SB_NOTE_ID, noteId);

        // Convert relevance to integer for data entry into SQL
        int rel = (relevant == true)? 1 : 0;
        values.put(SB_COLUMN_RELEVANT, rel);

        long insertId = sqlDB.insert(SENTENCE_BLOCK_TABLE, null, values);

        Cursor cursor = sqlDB.query(SENTENCE_BLOCK_TABLE, allSbColumns,
                SB_COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        SentenceBlock newSentenceBlock = cursorToSentenceBlock(cursor);
        cursor.close();
        return newSentenceBlock;
    }

    public long updateSentenceBlock(long idToUpdate, String newSentence) {
        ContentValues values = new ContentValues();
        values.put(NOTE_COLUMN_MESSAGE, newSentence);

        return sqlDB.update(SENTENCE_BLOCK_TABLE, values, SB_COLUMN_ID + " = " + idToUpdate, null);
    }

    public ArrayList<SentenceBlock> getAllSentenceBlocks() {
        ArrayList<SentenceBlock> sentenceBlocks = new ArrayList<>();

        Cursor cursor = sqlDB.query(SENTENCE_BLOCK_TABLE, allSbColumns,null, null, null, null,
                null);

        for(cursor.moveToLast(); !cursor.isBeforeFirst(); cursor.moveToPrevious()) {
            SentenceBlock sentenceBlock = cursorToSentenceBlock(cursor);
            sentenceBlocks.add(sentenceBlock);
            Log.d(MainActivity.LOG_TAG, "Sentence Block in get sentenceBlocks: " + sentenceBlock);
        }
        cursor.close();
        return sentenceBlocks;
    }

    private SentenceBlock cursorToSentenceBlock(Cursor cursor) {
        SentenceBlock newSentenceBlock = new SentenceBlock(cursor.getString(1)
                ,SentenceBlock.Category.valueOf(cursor.getString(2))
                ,cursor.getLong(3) ,cursor.getInt(0), cursor.getLong(4));
        return newSentenceBlock;
    }

    private static class AbilinoteDbHelper extends SQLiteOpenHelper {

        AbilinoteDbHelper (Context ctx) {
            super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // create NOte Table
            db.execSQL(CREATE_TABLE_NOTE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(AbilinoteDbHelper.class.getName(),
                    "Upgrading Database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + NOTE_TABLE);
            onCreate(db);
        }
    }
}
