package com.devproject.miguelfagundez.awesomenotes.view.add;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.devproject.miguelfagundez.awesomenotes.R;
import com.devproject.miguelfagundez.awesomenotes.utils.Constants;
import com.devproject.miguelfagundez.awesomenotes.viewmodel.NotesViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.Date;

/*******************************************
 * Fragment - AddNotesFragment
 * This fragment handle adding notes to DB
 * A simple {@link Fragment} subclass.
 * @author: Miguel Fagundez
 * @date: April 24th, 2020
 * @version: 1.0
 ******************************************/
public class AddNotesFragment extends Fragment {

    // Constants
    private static final String TAG = "NotesDetailsFragment";

    // Views declarations
    private FloatingActionButton btnDone;
    private EditText editTextTitle;
    private EditText editTextBody;
    private ImageView imageViewNotePhoto;
    private TextView tvCreationDate;

    // Members
    private String timeStamp;

    // NotesViewModel
    private NotesViewModel viewModel;

    public AddNotesFragment() {
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
        return inflater.inflate(R.layout.fragment_notes_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupViews();
        setupViewModel();
        cleanViews();
        setupListeners();
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        cleanViews();
    }

    //*********************************************
    //             Setup components
    //*********************************************
    private void setupViews() {
        btnDone = getActivity().findViewById(R.id.fabDone);
        editTextTitle = getActivity().findViewById(R.id.etNoteDetailsTitle);
        editTextBody = getActivity().findViewById(R.id.etNoteDetailsBody);
        imageViewNotePhoto = getActivity().findViewById(R.id.ivNotePhoto);
        tvCreationDate = getActivity().findViewById(R.id.tvCreationCurrentDate);
    }


    private void setupViewModel() {
        viewModel = ViewModelProviders.of(this).get(NotesViewModel.class);
    }

    //*****************************************************
    //  I need to clean or init some views
    //*****************************************************
    private void cleanViews() {
        editTextTitle.setText("");
        editTextBody.setText("");
        imageViewNotePhoto.setImageResource(R.drawable.awesomenote);

        SimpleDateFormat simpleDateString = new SimpleDateFormat(Constants.DATE_FORMAT);
        timeStamp = simpleDateString.format(new Date());
        tvCreationDate.setText(timeStamp);

    }

    private void setupListeners() {
        //*****************************************************
        // Adding a new note in Firebase Firestore module
        //*****************************************************
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkingNotesData()) {
                    //**********************************************
                    // ViewModel send the data to Firestore module
                    //**********************************************
                    viewModel.addNewNote(0,
                            editTextTitle.getText().toString(),
                            editTextBody.getText().toString(),
                            new Timestamp(new Date()),
                            "No photo",
                            false);

                    // Returning to MainActivity (Recycler View: List of notes)
                    getActivity().onBackPressed();
                }else
                    Toast.makeText(getActivity(),R.string.title_no_empty,Toast.LENGTH_SHORT).show();
            }
        });

    }

    //******************************
    // Menu bar options
    //******************************
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.findItem(R.id.menu_add).setVisible(false);
        menu.findItem(R.id.menu_copy).setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    //**********************************************
    // Validating title entry is not empty
    //**********************************************
    private boolean checkingNotesData(){
        String title = editTextTitle.getText().toString();
        if(TextUtils.isEmpty(title))
            return false;
        return true;
    }
}
