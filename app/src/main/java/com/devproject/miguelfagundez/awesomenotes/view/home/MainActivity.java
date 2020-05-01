package com.devproject.miguelfagundez.awesomenotes.view.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.devproject.miguelfagundez.awesomenotes.R;
import com.devproject.miguelfagundez.awesomenotes.adapters.NotesAdapter;
import com.devproject.miguelfagundez.awesomenotes.listeners.NotesListeners;
import com.devproject.miguelfagundez.awesomenotes.model.pojo.Note;
import com.devproject.miguelfagundez.awesomenotes.utils.Constants;
import com.devproject.miguelfagundez.awesomenotes.view.authentication.AuthActivity;
import com.devproject.miguelfagundez.awesomenotes.view.add.AddNotesFragment;
import com.devproject.miguelfagundez.awesomenotes.view.settings.SettingsFragment;
import com.devproject.miguelfagundez.awesomenotes.view.update.UpdateNotesFragment;
import com.devproject.miguelfagundez.awesomenotes.viewmodel.NotesViewModel;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

/********************************************
 * Activity - MainActivity
 * This activity is the Home screen that
 * implements RecyclerView with Firesbase
 * @author: Miguel Fagundez
 * @date: April 22th, 2020
 * @version: 1.0
 * *******************************************/
public class MainActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener, NotesListeners {

    // TAG
    private static final String TAG = "MainActivity";

    //Views declarations
    private RecyclerView recyclerView;
    private NotesAdapter adapter;
    private FloatingActionButton floatActionButton;

    //Delete direction (LEFT)
    private ItemTouchHelper.SimpleCallback simpleCallback;

    //ViewModel declaration
    private NotesViewModel viewModel;

    //Activities or Fragments declarations
    private SettingsFragment settingsFragment;
    private AddNotesFragment addNewNoteFragment;
    private UpdateNotesFragment updateNoteFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupViews();
        setupViewModel();
        setupActivitiesOrFragments();
        setupFloatingActionButtions();

    }

    //*********************************************
    //             Setup components
    //*********************************************

    private void setupViewModel() {
         viewModel = ViewModelProviders.of(this).get(NotesViewModel.class);
    }
    private void setupViews() {
        recyclerView = findViewById(R.id.rvMainActivity);
        floatActionButton = findViewById(R.id.fabAddNewNote);
    }

    private void setupActivitiesOrFragments() {

        settingsFragment = new SettingsFragment();
        addNewNoteFragment = new AddNotesFragment();
        updateNoteFragment = new UpdateNotesFragment();
    }

    private void setupFloatingActionButtions() {
        floatActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callAddNotesFragment();
            }
        });
    }

    //**********************************
    // Calling Activities & Fragments
    //**********************************
    private void callAuthActivity() {
        Intent intent = new Intent(this, AuthActivity.class);
        startActivity(intent);
        finish();
    }

    private void callAddNotesFragment(){
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(
                        R.anim.slide_in,
                        R.anim.slide_out,
                        R.anim.slide_in,
                        R.anim.slide_out
                )
                .replace(R.id.fragmentContainer, addNewNoteFragment)
                .addToBackStack(addNewNoteFragment.getTag())
                .commit();
    }

    private void callUpdateNotesFragment(){
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(
                        R.anim.slide_in,
                        R.anim.slide_out,
                        R.anim.slide_in,
                        R.anim.slide_out
                )
                .replace(R.id.fragmentContainer, updateNoteFragment)
                .addToBackStack(updateNoteFragment.getTag())
                .commit();
    }

    //*************************************************
    // Implementing the left direction for deleting
    //*************************************************
    private void setupSimpleCallback() {
        simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                if (direction == ItemTouchHelper.LEFT){
                    NotesAdapter.NotesViewHolder notesViewHolder = (NotesAdapter.NotesViewHolder) viewHolder;
                    notesViewHolder.deleteNoteFromRecyclerView();
                }
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                // From the library
                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.colorDelete))
                        .addActionIcon(R.drawable.ic_delete)
                        .create()
                        .decorate();

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
    }

    //*********************************************
    //              Menu options
    //*********************************************
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu addign item in the action bar
        getMenuInflater().inflate(R.menu.menu, menu);
        menu.findItem(R.id.menu_copy).setVisible(false);
        menu.findItem(R.id.menu_share).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        // Item clicks from action bar menu
        int id = item.getItemId();

        switch (id){
            case R.id.menu_settings:
                //*********************************
                // Calling Settings Fragment
                //*********************************
                getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(
                            R.anim.slide_in,
                            R.anim.slide_out,
                            R.anim.slide_in,
                            R.anim.slide_out
                    )
                        .replace(R.id.fragmentContainer, settingsFragment)
                        .addToBackStack(settingsFragment.getTag())
                        .commit();
                break;
            case R.id.menu_add:
                //***************************************
                // Calling add note fragment
                //***************************************
                callAddNotesFragment();
                break;
            case R.id.menu_logout:
                //***************************************
                // Closing user session in firebase
                //***************************************
                Toast.makeText(this, R.string.logout,Toast.LENGTH_SHORT).show();

                AuthUI.getInstance().signOut(this);
                break;
        }

        return super.onOptionsItemSelected(item);
    }



    //****************************
    // if Firebase state change
    // I can take some actions
    //****************************
    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        //*******************************
        // Checking any open session
        //*******************************
        if (FirebaseAuth.getInstance().getCurrentUser() == null){
            callAuthActivity();
            return;
        }
        // I can handle other options here
        setupRecyclerView(firebaseAuth.getCurrentUser());
    }

    private void setupRecyclerView(FirebaseUser user){
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Preparing the Firebase Query
        // We only take the notes of the current user
        Query query = FirebaseFirestore.getInstance()
                .collection(Constants.COLLECTION_NOTES)
                .whereEqualTo(Constants.COLLECTION_NOTES_USER_ID, user.getUid())
                .orderBy(Constants.COLLECTION_NOTES_CREATION, Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Note> options = new FirestoreRecyclerOptions.Builder<Note>()
                .setQuery(query, Note.class)
                .build();

        // Now, I create the note adapter with the parameters above as an argument
        adapter = new NotesAdapter(options, this);
        recyclerView.setAdapter(adapter);

        // Listening for real-time update
        adapter.startListening();

        // Deleting left direction
        setupSimpleCallback();
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }

    //*******************************************************************
    //                  Listeners implementations
    //*******************************************************************
    //********************************
    // Connecting with NotesAdapter
    // Detecting clicks on each note
    //********************************
    @Override
    public void updateNote(String title, String photo, Timestamp creation, String body, String documentId) {
        Bundle bundle = new Bundle();

        bundle.putString(Constants.COLLECTION_NOTES_TITLE, title);
        bundle.putString(Constants.COLLECTION_NOTES_PHOTO, photo);

        CharSequence date = DateFormat.format(Constants.DATE_FORMAT, creation.toDate());
        bundle.putString(Constants.COLLECTION_NOTES_CREATION, date.toString());

        bundle.putString(Constants.COLLECTION_NOTES_BODY, body);
        bundle.putString(Constants.COLLECTION_NOTES_DOCUMENT_ID, documentId);

        updateNoteFragment.setArguments(bundle);

        callUpdateNotesFragment();

    }

    //**************************************************
    // Implementing: Delete & Undo behaviors
    //**************************************************
    @Override
    public void deleteNote(DocumentSnapshot snapshot) {

        final DocumentReference reference = snapshot.getReference();
        final Note note = snapshot.toObject(Note.class);

        reference.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Log.d(TAG, "onComplete: Deleted successfully");
                            Snackbar.make(recyclerView, R.string.note_deleted, Snackbar.LENGTH_LONG)
                                    .setAction(R.string.undo, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            reference.set(note);
                                        }
                                    }).show();
                        }else{
                            Log.e(TAG, "onComplete: ", task.getException());
                        }
                    }
                });
    }

    //****************************************
    // Register and remove Auth listener
    //****************************************
    @Override
    protected void onStart() {
        super.onStart();
        // Adding this listener before app goes foreground
        FirebaseAuth.getInstance().addAuthStateListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Removing this listener before app goes background
        FirebaseAuth.getInstance().removeAuthStateListener(this);
        if (adapter != null){
            adapter.stopListening();
        }
    }
}
