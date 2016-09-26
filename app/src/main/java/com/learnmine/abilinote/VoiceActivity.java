package com.learnmine.abilinote;

import android.app.Activity;
import android.content.Intent;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;

public class VoiceActivity extends Activity implements
        RecognitionListener {

    private AbilinoteDbAdapter dbAdapter;
    public static final String LOG_TAG = "VoiceActivity";

    Note.Category noteCat;
    SentenceBlock.Category sentenceBlockCat;
    Note newNote;

    private String sentences;
    ListView lvVoiceResults;
    ListView lvRelevantNotes;
    private EditText title;
    private static ToggleButton toggleButton;
    private static ProgressBar progressBar;
    private static SpeechRecognizer speech = null;
    private static Intent recognizerIntent;
    static final int check = 1111;
    static final String[] texts = {
            "Recording Audio"
    };
    TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "Voice Created");
        setContentView(R.layout.activity_voice);

        lvVoiceResults = (ListView) findViewById(R.id.lvVoiceResults);
        lvRelevantNotes = (ListView) findViewById(R.id.lvRelevantNotes);
        progressBar = (ProgressBar) findViewById(R.id.progressBarVoice);
        toggleButton = (ToggleButton) findViewById(R.id.toggleBtnVoice);
        title = (EditText) findViewById(R.id.voiceEditNoteTitle);
        toggleButton.setChecked(true);
        noteCat = Note.Category.PERSONAL;
        sentenceBlockCat = SentenceBlock.Category.NOTHING;

        // set up db adapter
        dbAdapter = new AbilinoteDbAdapter(VoiceActivity.this);
        dbAdapter.open();
        newNote = dbAdapter.createNote("Edit Title", "", noteCat);
        sentences = newNote.getMessage();

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
        // add sentence to the database
        dbAdapter.createSentenceBlock(sentence, sentenceBlockCat, newNote.getId(), true);


        ArrayList<SentenceBlock> sentenceBlocks = dbAdapter.getAllSentenceBlocks();
        lvVoiceResults.setAdapter(new ArrayAdapter<>
                (this, android.R.layout.simple_list_item_1,
                sentenceBlocks));

    }

    public void saveEntireNoteHandler(View view) {
        dbAdapter.updateNote(newNote.getId(), title.getText() + "", sentences + "", noteCat);
        dbAdapter.close();
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
            dbAdapter.close();
            Log.i(LOG_TAG, "destroy");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(LOG_TAG, "Voice onPause");
        if (speech != null) {
            speech.destroy();
            dbAdapter.close();
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
                lvVoiceResults.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                        results));
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
        lvVoiceResults.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                matches));
        String text;
//        for (String result : matches)
//            text += result + "\n";
        text = matches.get(0);
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
