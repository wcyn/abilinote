package com.learnmine.abilinote;

import android.content.ClipData;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.Map;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardViewNative;
import it.gmariotti.cardslib.library.view.listener.dismiss.DefaultDismissableManager;

/**
 * Created by baa on 9/28/2016.
 */


public class NotesTabFragment extends Fragment {

    public static final String LOG_TAG = "NoteTabFrag";
    public static final String S_BLOCKS_CHILD = MainActivity.S_BLOCKS_CHILD;
    public static final String NOTES_CHILD = MainActivity.NOTES_CHILD;

    // Firebase
    private DatabaseReference mFirebaseDatabaseReference;
    private FirebaseRecyclerAdapter<Note, MessageViewHolder>
            mFirebaseNoteAdapter;

//    private static ProgressBar progressBar;
    private ProgressBar noteListProgressBar;
    private RecyclerView noteRecyclerView;
    private LinearLayoutManager noteLinearLayoutManager;

    private String firstSentence;


    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public ClipData.Item currentItem;
//        public TextView messageTextView;
        public TextView card_main_inner_simple_title;
        public TextView card_main_inner_simple_sentence;
        public CardViewNative cardViewNative;


        public MessageViewHolder(View v) {
            super(v);
            cardViewNative = (CardViewNative) itemView.findViewById(R.id.noteCardView);
            card_main_inner_simple_title = (TextView) itemView.findViewById(R.id.card_main_inner_simple_title);
            card_main_inner_simple_sentence = (TextView) itemView.findViewById(R.id.card_main_inner_simple_sentence);

            view = v;
            view.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    Log.d("", "Clicked on card!");
                }
            });
//            messageTextView = (TextView) itemView.findViewById(R.id.noteTitleListItem);
        }

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        Log.d(MainActivity.LOG_TAG, "Inflating!");
        View rootView = inflater.inflate(R.layout.fragment_notes_tab, container, false);

        noteListProgressBar = (ProgressBar) rootView.findViewById(R.id.noteListProgressBar);
        noteRecyclerView = (RecyclerView) rootView.findViewById(R.id.noteRecyclerView);
        noteLinearLayoutManager = new LinearLayoutManager(getActivity());
        noteLinearLayoutManager.setStackFromEnd(false);
        noteRecyclerView.setLayoutManager(noteLinearLayoutManager);
        noteListProgressBar.setVisibility(ProgressBar.VISIBLE);


                //Firebase Reference
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();

        mFirebaseNoteAdapter = new FirebaseRecyclerAdapter<Note,
                MessageViewHolder>(
                Note.class,
                R.layout.note_list_item,
                MessageViewHolder.class,
                mFirebaseDatabaseReference.child(NOTES_CHILD)) {

            @Override
            protected void populateViewHolder(final MessageViewHolder viewHolder,
                                              final Note note, final int position) {
                noteListProgressBar.setVisibility(ProgressBar.INVISIBLE);

                viewHolder.card_main_inner_simple_title.setText(note.getTitle());
                final String noteKey = getRef(position).getKey();
                Log.d("", "NoteKey: " + noteKey);
                Log.d("", "Position: " + position);
                mFirebaseDatabaseReference.child(S_BLOCKS_CHILD + "/" + noteKey).limitToFirst(1)
                        .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Object value = dataSnapshot.getValue();
                        String sentenceKey = dataSnapshot.getKey();
//                        String sentenceBlock = dataSnapshot.child(sentenceKey);
                        Log.d("", "Top value: " + value);
                        if (value == null) {
                            Log.d("", "FirstSentence null!");
                            viewHolder.card_main_inner_simple_sentence.setTypeface(null, Typeface.ITALIC);
                        } else {
                            Log.d("", "FirstSentence not null! " + value);
                        }
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            firstSentence = child.child("text").getValue().toString();
                            Log.d("", "Child value: " + firstSentence );
                            viewHolder.card_main_inner_simple_sentence.setText(firstSentence);

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("", "loadPost:onCancelled", databaseError.toException());
                    }
                });

                Card card = new Card(getContext());
                card.setSwipeable(true);

                card.setId(noteKey);
                //Create a CardHeader
//                CardHeader header = new CardHeader(getContext());

                //Add Header to card
//                card.addCardHeader(header);
                card.setOnClickListener(new Card.OnCardClickListener() {
                    @Override
                    public void onClick(Card card, View view) {
                        Toast.makeText(getActivity(), "Clicked Card!",
                                Toast.LENGTH_SHORT).show();
                    }
                });

                card.setOnSwipeListener(new Card.OnSwipeListener() {
                    @Override
                    public void onSwipe(Card card) {
                        //Do something

                        Toast.makeText(getActivity(), "Swiped Card!",
                                Toast.LENGTH_SHORT).show();
                    }

                });

                card.setOnUndoSwipeListListener(new Card.OnUndoSwipeListListener() {
                    @Override
                    public void onUndoSwipe(Card card) {
                        //Do something
                        Toast.makeText(getActivity(), "Undo Swiped Card!",
                                Toast.LENGTH_SHORT).show();
                    }
                });

                card.setOnUndoHideSwipeListListener(new Card.OnUndoHideSwipeListListener() {
                    @Override
                    public void onUndoHideSwipe(Card card) {
                        //Do something
                        Toast.makeText(getActivity(), "Undo Swiped Hide!",
                                Toast.LENGTH_SHORT).show();
                    }
                });

                //Set the card inner text
//                card.setTitle(note.getTitle());
                viewHolder.cardViewNative.setCard(card);
//                card.setupInnerViewElements(container, );
            }
        };

        mFirebaseNoteAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int sentenceBlockCount = mFirebaseNoteAdapter.getItemCount();
                int lastVisiblePosition =
                        noteLinearLayoutManager.findFirstCompletelyVisibleItemPosition();
                // If the recycler view is initially being loaded or the
                // user is at the bottom of the list, scroll to the bottom
                // of the list to show the newly added message.
                if (lastVisiblePosition == 0 ||
                        (positionStart >= (sentenceBlockCount + 1) &&
                                lastVisiblePosition == (positionStart + 1))) {
                    noteRecyclerView.scrollToPosition(positionStart);
                }
            }
        });

        noteRecyclerView.setItemAnimator(new DefaultItemAnimator());
        noteRecyclerView.setLayoutManager(noteLinearLayoutManager);
        noteRecyclerView.setAdapter(mFirebaseNoteAdapter);

        // End Firebase Stuff



        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mFirebaseNoteAdapter.cleanup();
    }
}