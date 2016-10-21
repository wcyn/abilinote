package com.learnmine.abilinote;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.view.CardViewNative;

import static com.learnmine.abilinote.VoiceActivity.NOTES_CHILD;

/**
 * A simple {@link Fragment} subclass.
 */
public class NoteListFragment extends Fragment {
//    private ArrayList<Note> notes;
//    private NoteAdapter noteAdapter;
    public static final String MESSAGES_CHILD = "messages";
    private static final int REQUEST_INVITE = 1;
    public static final int DEFAULT_MSG_LENGTH_LIMIT = 10;
    public static final String ANONYMOUS = "anonymous";
    private static final String MESSAGE_SENT_EVENT = "message_sent";
    private SharedPreferences mSharedPreferences;

    // fragment RecyclerView things
    private RecyclerView mMessageRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private ProgressBar mProgressBar;
    private EditText mMessageEditText;

    private static final String TAG = "RecyclerViewFragment";
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private static final int SPAN_COUNT = 2;
    private static final int DATASET_COUNT = 60;

//    private ProgressBar roundNoteProgressBar;

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

//    protected LayoutManagerType mCurrentLayoutManagerType;
//
//    protected RadioButton mLinearLayoutRadioButton;
//    protected RadioButton mGridLayoutRadioButton;

    protected RecyclerView mRecyclerView;
    protected CustomRecyclerAdapter mAdapter;
    private RecyclerView noteRecyclerView;
    private LinearLayoutManager noteLinearLayoutManager;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected String[] mDataset;

    protected LayoutManagerType mCurrentLayoutManagerType;

    protected RadioButton mLinearLayoutRadioButton;
    protected RadioButton mGridLayoutRadioButton;


    // Firebase instance variables
    private DatabaseReference mFirebaseDatabaseReference;
    private FirebaseRecyclerAdapter<Note, MessageViewHolder>
            mFirebaseAdapter;

    public static class MessageViewHolder extends RecyclerView.ViewHolder {

        public  CardViewNative cardViewNative;
//        public TextView noteTitleView;
        //        public TextView messengerTextView;
//        public CircleImageView messengerImageView;
        public MessageViewHolder(View v) {
            super(v);
            cardViewNative = (CardViewNative) itemView.findViewById(R.id.noteCardView);
//            noteTitleView = (TextView) itemView.findViewById(R.id.noteTitleListItem);
//            messengerTextView = (TextView) itemView.findViewById(R.id.messengerTextView);
//            messengerImageView = (CircleImageView) itemView.findViewById(R.id.messengerImageView);
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        roundNoteProgressBar = (ProgressBar) findViewById(R.id.roundNoteProgressBar);

        //Firebase Reference
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mFirebaseAdapter = new FirebaseRecyclerAdapter<Note,
                MessageViewHolder>(
                Note.class,
                R.layout.note_list_item,
                MessageViewHolder.class,
                mFirebaseDatabaseReference.child(NOTES_CHILD)) {

            @Override
            protected void populateViewHolder(MessageViewHolder viewHolder,
                                              Note note, int position) {
//                roundProgressBar.setVisibility(ProgressBar.INVISIBLE);
                Card card = new Card(getContext(), R.layout.note_card_inner_content);

                card.setId(String.valueOf(position));

                //Create a CardHeader
                CardHeader header = new CardHeader(getContext());

                //Add Header to card
                card.addCardHeader(header);

                //Set the card inner text
                card.setTitle(note.getTitle());

                viewHolder.cardViewNative.setCard(card);
//                viewHolder.noteTitleView.setText(note.getTitle());
            }
        };

        mFirebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int sentenceBlockCount = mFirebaseAdapter.getItemCount();
                int lastVisiblePosition =
                        noteLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                // If the recycler view is initially being loaded or the
                // user is at the bottom of the list, scroll to the bottom
                // of the list to show the newly added message.
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (sentenceBlockCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    noteRecyclerView.scrollToPosition(positionStart);
                }
            }
        });

        noteRecyclerView.setLayoutManager(noteLinearLayoutManager);
        noteRecyclerView.setAdapter(mFirebaseAdapter);
        // Initialize dataset, this data would usually come from a local content provider or
        // remote server.
//        initDataset();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recycler_view_frag, container, false);
        rootView.setTag(TAG);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.noteRecyclerView);

        // LinearLayoutManager is used here, this will layout the elements in a similar fashion
        // to the way ListView would layout elements. The RecyclerView.LayoutManager defines how
        // elements are laid out.
        mLayoutManager = new LinearLayoutManager(getActivity());

        mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;

        if (savedInstanceState != null) {
            // Restore saved layout manager type.
            mCurrentLayoutManagerType = (LayoutManagerType) savedInstanceState
                    .getSerializable(KEY_LAYOUT_MANAGER);
        }
        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);

        mAdapter = new CustomRecyclerAdapter(mDataset);
        // Set CustomAdapter as the adapter for RecyclerView.
        mRecyclerView.setAdapter(mAdapter);

        mLinearLayoutRadioButton = (RadioButton) rootView.findViewById(R.id.linear_layout_rb);
        mLinearLayoutRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRecyclerViewLayoutManager(LayoutManagerType.LINEAR_LAYOUT_MANAGER);
            }
        });

        mGridLayoutRadioButton = (RadioButton) rootView.findViewById(R.id.grid_layout_rb);
        mGridLayoutRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRecyclerViewLayoutManager(LayoutManagerType.GRID_LAYOUT_MANAGER);
            }
        });

        return rootView;
    }

    /**
     * Set RecyclerView's LayoutManager to the one given.
     *
     * @param layoutManagerType Type of layout manager to switch to.
     */
    public void setRecyclerViewLayoutManager(LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        if (mRecyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        switch (layoutManagerType) {
            case GRID_LAYOUT_MANAGER:
                mLayoutManager = new GridLayoutManager(getActivity(), SPAN_COUNT);
                mCurrentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER;
                break;
            case LINEAR_LAYOUT_MANAGER:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
                break;
            default:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        }

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(scrollPosition);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save currently selected layout manager.
        savedInstanceState.putSerializable(KEY_LAYOUT_MANAGER, mCurrentLayoutManagerType);
        super.onSaveInstanceState(savedInstanceState);
    }

    /**
     * Generates Strings for RecyclerView's adapter. This data would usually come
     * from a local content provider or remote server.
     */
    private void initDataset() {

        mDataset = new String[DATASET_COUNT];
        for (int i = 0; i < DATASET_COUNT; i++) {
            mDataset[i] = "This is element #" + i;
        }
    }

//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
////        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//        String[] values = new String[]{"Blue", "Red", "Yellow", "Green", "Purple", "Orange"};
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
//                android.R.layout.simple_list_item_1, values);
////        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
//        setListAdapter(adapter);
////        AbilinoteDbAdapter abilinoteDbAdapter = new AbilinoteDbAdapter(
////                getActivity().getBaseContext());
////        abilinoteDbAdapter.open();
////        notes = abilinoteDbAdapter.getAllNotes();
////        Log.d(MainActivity.LOG_TAG, "Notes here: " + notes);
////        abilinoteDbAdapter.close();
////        notes = new ArrayList<Note>();
////        notes.add(new Note("New Note Title", "Body of our Note", Note.Category.FINANCE));
////        notes.add(new Note("Personal Note", "Remember to remember stuff, okay?", Note.Category.PERSONAL));
////        notes.add(new Note("Another Quote Note", "Yes, that rhymes, just like extraordinary quotes should", Note.Category.QUOTE));
////        notes.add(new Note("Finance Note", "The Body of a note on Finance", Note.Category.FINANCE));
////        notes.add(new Note("Technical Note", "An example of something pretty technical", Note.Category.TECHNICAL));
////        notes.add(new Note("My Favorite Quote", "My Favorite Quote has to be the Quote by Abraham Lincoln", Note.Category.QUOTE));notes.add(new Note("Another Quote Note", "Yes, that rhymes, just like extraordinary quotes should", Note.Category.QUOTE));
////        notes.add(new Note("Finance Note", "The Body of a note on Finance", Note.Category.FINANCE));
////        notes.add(new Note("Technical Note", "An example of something pretty technical", Note.Category.TECHNICAL));
////        notes.add(new Note("My Favorite Quote", "My Favorite Quote has to be the Quote by Abraham Lincoln", Note.Category.QUOTE));
//
////        noteAdapter = new NoteAdapter(getActivity(), notes);
////        setListAdapter(noteAdapter);
//
//        getListView().setDivider(ContextCompat.getDrawable(getActivity(),
//                android.R.color.darker_gray));
//        getListView().setDividerHeight(1);
//    }
//
//    @Override
//    public void onListItemClick(ListView l, View v, int position, long id) {
//        super.onListItemClick(l, v, position, id);
//        launchNoDetailActivity(NoteListActivity.FragmentToLaunch.VIEW,position);
//    }

//    public static class MessageViewHolder extends RecyclerView.ViewHolder {
//
//        public TextView noteTitleView;
//        public TextView messengerTextView;
//        public CircleImageView messengerImageView;
//        public MessageViewHolder(View v) {
//            super(v);
//            noteTitleView = (TextView) itemView.findViewById(R.id.noteTitleView);
//            messengerTextView = (TextView) itemView.findViewById(R.id.messengerTextView);
//            messengerImageView = (CircleImageView) itemView.findViewById(R.id.messengerImageView);
//        }
//
//    }

    private void launchNoDetailActivity(NoteListActivity.FragmentToLaunch ftl, int position) {

        // Grab the note info associated which the note we clicked on
//        Note note = (Note) getListAdapter().getItem(position);

        //Create a new intent that launches our NoteDetailActivity
        Intent intent = new Intent(getActivity(), NoteDetailActivity.class);

        // Pass along the Information of the NOte we Clicked on to our NoteDetailActivity
//        intent.putExtra(NoteListActivity.NOTE_TITLE_EXTRA, note.getTitle());
//        intent.putExtra(NoteListActivity.NOTE_MESSAGE_EXTRA, note.getMessage());
//        intent.putExtra(NoteListActivity.NOTE_CATEGORY_EXTRA, note.getCategory());
//        intent.putExtra(NoteListActivity.NOTE_ID_EXTRA, note.getId());

        switch (ftl) {
            case VIEW:
                intent.putExtra(NoteListActivity.NOTE_FRAGMENT_TO_LOAD_EXTRA,
                        NoteListActivity.FragmentToLaunch.VIEW);
                break;
            case EDIT:
                intent.putExtra(NoteListActivity.NOTE_FRAGMENT_TO_LOAD_EXTRA,
                        NoteListActivity.FragmentToLaunch.EDIT);
                break;
        }
        startActivity(intent);

    }
}
