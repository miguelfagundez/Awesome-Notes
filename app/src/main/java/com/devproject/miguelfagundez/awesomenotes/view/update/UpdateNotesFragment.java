package com.devproject.miguelfagundez.awesomenotes.view.update;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.devproject.miguelfagundez.awesomenotes.R;
import com.devproject.miguelfagundez.awesomenotes.utils.Constants;
import com.devproject.miguelfagundez.awesomenotes.viewmodel.NotesViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;

import java.util.Date;

/*******************************************
 * Fragment - UpdateNotesFragment
 * This fragment handle the update methods
 * A simple {@link Fragment} subclass.
 * @author: Miguel Fagundez
 * @date: April 24th, 2020
 * @version: 1.0
 ******************************************/
public class UpdateNotesFragment extends Fragment {

    // Members
    private static final String TAG = "UpdateNotesFragment";
    private String documentId;

    // Views
    private FloatingActionButton btnUpdateDone;
    private EditText etNoteDetailsTitle;
    private EditText etNoteDetailsBody;
    private TextView tvCreationDate;

    // ViewModel
    private NotesViewModel viewModel;

    public UpdateNotesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_update_notes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupViews();
        setupViewModel();
        setupListeners();
    }

    //*********************************************
    //             Setup components
    //*********************************************
    private void setupViews() {
        btnUpdateDone = getActivity().findViewById(R.id.fabUpdateDone);
        etNoteDetailsTitle = getActivity().findViewById(R.id.etNoteDetailsTitle);
        etNoteDetailsBody = getActivity().findViewById(R.id.etNoteDetailsBody);
        tvCreationDate = getActivity().findViewById(R.id.tvUpdateCreationCurrentDate);
    }

    private void setupViewModel() {
        viewModel = ViewModelProviders.of(this).get(NotesViewModel.class);
    }

    private void setupListeners() {
        btnUpdateDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkingNotesData()) {
                    //**********************************************
                    // ViewModel send the data to Firestore module
                    //**********************************************
                    viewModel.updateNote(etNoteDetailsTitle.getText().toString(), etNoteDetailsBody.getText().toString(), documentId);

                    // Returning to MainActivity (Recycler View: List of notes)
                    getActivity().onBackPressed();
                }else
                    Toast.makeText(getActivity(),R.string.title_no_empty,Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        // Getting document Id
        documentId = getArguments().getString(Constants.COLLECTION_NOTES_DOCUMENT_ID);

        // Note values
        String title = getArguments().getString(Constants.COLLECTION_NOTES_TITLE);
        etNoteDetailsTitle.setText(title);

        String body = getArguments().getString(Constants.COLLECTION_NOTES_BODY);
        etNoteDetailsBody.setText(body);

        String creation = getArguments().getString(Constants.COLLECTION_NOTES_CREATION);
        tvCreationDate.setText(creation);
    }

    //******************************
    // Menu bar options
    //******************************
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.findItem(R.id.menu_copy).setVisible(true);
        menu.findItem(R.id.menu_share).setVisible(true);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Item clicks from action bar menu
        int id = item.getItemId();

        switch (id){
            case R.id.menu_share:
                shareNote();
                break;
            case R.id.menu_copy:
                if (checkingNotesData()) {
                    //**********************************************
                    // ViewModel send the data to Firestore module
                    //**********************************************
                    viewModel.addNewNote(0,
                            "Copy " + etNoteDetailsTitle.getText().toString(),
                            etNoteDetailsBody.getText().toString(),
                            new Timestamp(new Date()),
                            "No photo",
                            false);
                    Toast.makeText(getActivity(), R.string.note_has_been_copied,Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(getActivity(), R.string.title_no_empty,Toast.LENGTH_SHORT).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    //*************************************
    // Sharing text plain with other apps
    //*************************************
    private void shareNote() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, etNoteDetailsTitle.getText().toString() + "\n\n" + etNoteDetailsBody.getText().toString());
        intent.setType("text/plain");

        startActivity(Intent.createChooser(intent, getString(R.string.select_app)));
    }

    //**********************************************
    // Validating title entry is not empty
    //**********************************************
    private boolean checkingNotesData(){
        String title = etNoteDetailsTitle.getText().toString();
        if(TextUtils.isEmpty(title))
            return false;
        return true;
    }
}
