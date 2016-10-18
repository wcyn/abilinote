package com.learnmine.abilinote;

import android.app.Activity;
import android.content.Intent;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class VoiceActivity extends Activity implements
        RecognitionListener {
    public static class MessageViewHolder extends RecyclerView.ViewHolder {

        public TextView messageTextView;
        public MessageViewHolder(View v) {
            super(v);
            messageTextView = (TextView) itemView.findViewById(R.id.sentenceBlockTextView);
        }
    }

    public static final String LOG_TAG = "VoiceActivity";
    public static final String S_BLOCKS_CHILD = MainActivity.S_BLOCKS_CHILD;
    public static final String NOTES_CHILD = MainActivity.NOTES_CHILD;

    private String sentences;
    private String newNoteId = "hi";
    ListView lvVoiceResults;
    ListView lvRelevantNotes;
    private EditText noteTitle;

    private static ToggleButton toggleButton;
    private static ProgressBar progressBar;
    private ProgressBar roundProgressBar;
    private RecyclerView sentenceBlockRecyclerView;
    private LinearLayoutManager sBlockLinearLayoutManager;

    private static SpeechRecognizer speech = null;
    private static Intent recognizerIntent;
    static final int check = 1111;
    static final String[] texts = {
            "Recording Audio"
    };
    TextToSpeech tts;

    // Firebase
    private DatabaseReference mFirebaseDatabaseReference;
    private FirebaseRecyclerAdapter<SentenceBlock, MessageViewHolder>
            mFirebaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "Voice Created");
        setContentView(R.layout.activity_voice);

//        lvVoiceResults = (ListView) findViewById(R.id.lvVoiceResults);
//        lvRelevantNotes = (ListView) findViewById(R.id.lvRelevantNotes);
        progressBar = (ProgressBar) findViewById(R.id.progressBarVoice);
        roundProgressBar = (ProgressBar) findViewById(R.id.roundProgressBar);


        sentenceBlockRecyclerView = (RecyclerView) findViewById(R.id.sBlockRecyclerView);
        sBlockLinearLayoutManager = new LinearLayoutManager(this);
        sBlockLinearLayoutManager.setStackFromEnd(false);
        sentenceBlockRecyclerView.setLayoutManager(sBlockLinearLayoutManager);

        toggleButton = (ToggleButton) findViewById(R.id.toggleBtnVoice);
        noteTitle = (EditText) findViewById(R.id.voiceEditNoteTitle);
        toggleButton.setChecked(true);



        //Firebase Reference
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();

        // Create a new Note
        newNoteId = mFirebaseDatabaseReference.child(NOTES_CHILD)
                .push().getKey();
        mFirebaseDatabaseReference.child(NOTES_CHILD).child(newNoteId).child("title")
                .setValue(noteTitle.getText().toString());

        Log.d(MainActivity.LOG_TAG, "newNoteId: " + newNoteId);

        noteTitle.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                mFirebaseDatabaseReference.child(NOTES_CHILD).child(newNoteId).child("title")
                        .setValue(noteTitle.getText().toString());
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        mFirebaseAdapter = new FirebaseRecyclerAdapter<SentenceBlock,
                MessageViewHolder>(
                SentenceBlock.class,
                R.layout.sentence_block,
                MessageViewHolder.class,
                mFirebaseDatabaseReference.child(S_BLOCKS_CHILD + "/" + newNoteId)) {

            @Override
            protected void populateViewHolder(MessageViewHolder viewHolder,
                                              SentenceBlock sentenceBlock, int position) {
                roundProgressBar.setVisibility(ProgressBar.INVISIBLE);
                viewHolder.messageTextView.setText(sentenceBlock.getText());
            }
        };

        mFirebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int sentenceBlockCount = mFirebaseAdapter.getItemCount();
                int lastVisiblePosition =
                        sBlockLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                // If the recycler view is initially being loaded or the
                // user is at the bottom of the list, scroll to the bottom
                // of the list to show the newly added message.
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (sentenceBlockCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    sentenceBlockRecyclerView.scrollToPosition(positionStart);
                }
            }
        });

        sentenceBlockRecyclerView.setLayoutManager(sBlockLinearLayoutManager);
        sentenceBlockRecyclerView.setAdapter(mFirebaseAdapter);

        // End Firebase Stuff

//        noteCat = Note.Category.PERSONAL;
//        sentenceBlockCat = SentenceBlock.Category.NOTHING;

        // set up db adapter
//        dbAdapter = new AbilinoteDbAdapter(VoiceActivity.this);
//        dbAdapter.open();
//        newNote = dbAdapter.createNote("Edit Title", "", noteCat);
//        sentences = newNote.getMessage();

        setUpSpeech();

        //generate list
//        ArrayList<String> list = new ArrayList<String>();
//        list.add("item1");
//        list.add("item2");

        //instantiate custom adapter
//        CustomArrayAdapter adapter = new CustomArrayAdapter(list, this);

        //handle listview and assign adapter
//        ListView lView = (ListView)findViewById(R.id.lvVoiceReturn);
//        lView.setAdapter(adapter);

        // start recording immediately activity is created
        listenToSpeech();
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    Log.d(LOG_TAG, "Button is checked");
                    setUpSpeech();
                    listenToSpeech();
                } else {
                    Log.d(LOG_TAG, "Button Not Checked");
                    stopListenToSpeech();
                }
            }
        });

    }

    public void saveSentenceBlock(String sentence) {
        Log.d(LOG_TAG, "Creating Note");
        SentenceBlock sentenceBlock = new
                SentenceBlock(sentence);
        mFirebaseDatabaseReference.child(S_BLOCKS_CHILD + "/" + newNoteId)
                .push().setValue(sentenceBlock);
//        Log.d(LOG_TAG, "Values: sentence, noteid: " + sentence + newNote.getId());
        // add sentence to the database
//        dbAdapter.createSentenceBlock(sentence, newNote.getId(), true);
//        ArrayList<SentenceBlock> sentenceBlocks = dbAdapter.getAllSentenceBlocks();
//        lvVoiceResults.setAdapter(new ArrayAdapter<>
//                (this, android.R.layout.simple_list_item_1,
//                sentenceBlocks));

    }

    public void saveEntireNoteHandler(View view) {
//        dbAdapter.updateNote(newNote.getId(), title.getText() + "", sentences + "", noteCat);
//        dbAdapter.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "Voice onResume");
        // recreate Speech Recognizer
//        toggleButton.setChecked(false);
//        setUpSpeech();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(LOG_TAG, "Voice onStop");
        if (speech != null) {
            speech.destroy();
//            dbAdapter.close();
            Log.i(LOG_TAG, "destroy");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(LOG_TAG, "Voice onPause");
        if (speech != null) {
            speech.destroy();
//            dbAdapter.close();
            Log.i(LOG_TAG, "Voice destroy");
        }
    }

    public void setUpSpeech() {
        Log.d(LOG_TAG, "Voice Set up Speech");
        speech = SpeechRecognizer.createSpeechRecognizer(this);
        speech.setRecognitionListener(this);
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,
//                "en");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
                this.getPackageName());
//        recognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS,true);
//        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CONFIDENCE_SCORES,true);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH) ;
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS,
                120000);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS,
                120000);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS,
                120000);

//        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);
    }

    public static void listenToSpeech() {
        Log.d(LOG_TAG, "Voice listenToSpeech");
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setIndeterminate(true);
        speech.startListening(recognizerIntent);
    }

    public static void stopListenToSpeech() {
        Log.d(LOG_TAG, "Voice stopListenToSpeech");
        progressBar.setIndeterminate(false);
        progressBar.setVisibility(View.INVISIBLE);
        speech.stopListening();
        speech.destroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(LOG_TAG, "Voice onActivityResult");
        if (requestCode == check) {
            Log.d(LOG_TAG, "Request Code == check");
            if (resultCode == RESULT_OK) {
                Log.d(LOG_TAG, "Voice Result Okay");
                ArrayList<String> results = data.getStringArrayListExtra(
                        RecognizerIntent.EXTRA_RESULTS);
//                lvVoiceResults.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
//                        results));
            }
        }
    }

    @Override
    public void onReadyForSpeech(Bundle bundle) {
        Log.d(LOG_TAG, "Voice onReadyForSpeech");
    }

    @Override
    public void onBeginningOfSpeech() {
        Log.i(LOG_TAG, "Voice onBeginningOfSpeech");
        progressBar.setIndeterminate(false);
        progressBar.setMax(10);
    }

    @Override
    public void onRmsChanged(float rmsdB) {
        Log.i(LOG_TAG, "onRmsChanged: " + rmsdB);
        progressBar.setProgress((int) rmsdB);
    }

    @Override
    public void onBufferReceived(byte[] bytes) {
        Log.i(LOG_TAG, "onBufferReceived: " + bytes);
    }

    @Override
    public void onEndOfSpeech() {
        Log.i(LOG_TAG, "onEndOfSpeech");
        progressBar.setIndeterminate(true);
//        toggleButton.setChecked(false);

    }

    @Override
    public void onError(int errorCode) {
        String errorMessage = getErrorText(errorCode);
        //display in short period of time
        Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
        Log.d(LOG_TAG, "FAILED " + errorMessage);
//        returnedText.setText(errorMessage);
//        toggleButton.setChecked(false);
        speech.destroy();
        setUpSpeech();
        listenToSpeech();
    }

    @Override
    public void onResults(Bundle results) {
        Log.i(LOG_TAG, "onResults");
        ArrayList<String> matches = results
                .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
//        lvVoiceResults.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
//                matches));
        String text;
//        for (String result : matches)
//            text += result + "\n";
        text = matches.get(0);
        Log.d(LOG_TAG, "Call save sentence block");
        saveSentenceBlock(text);
        listenToSpeech();
    }

    @Override
    public void onPartialResults(Bundle bundle) {
        Log.d(LOG_TAG, "Voice onPartial Results");
//        ArrayList<String> matches = bundle
//                .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
//        lvVoiceResults.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
//                matches));
//        String text;
//        text = matches.get(0);
//        saveSentenceBlock(text);
//        listenToSpeech();
    }

    @Override
    public void onEvent(int i, Bundle bundle) {
        Log.d(LOG_TAG, "Voice onEvent");
    }

    public static String getErrorText(int errorCode) {
        String message;
        switch (errorCode) {
            case SpeechRecognizer.ERROR_AUDIO:
                message = "Audio recording error";
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                message = "Client side error";
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                message = "Insufficient permissions";
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                message = "Network error";
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                message = "Network timeout";
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                message = "No match";
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                message = "RecognitionService busy";
                break;
            case SpeechRecognizer.ERROR_SERVER:
                message = "error from server";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                message = "No speech input";
                break;
            default:
                message = "Didn't understand, please try again.";
                break;
        }
        return message;
    }
}
