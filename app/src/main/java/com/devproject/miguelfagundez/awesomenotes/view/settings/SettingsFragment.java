package com.devproject.miguelfagundez.awesomenotes.view.settings;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import de.hdodenhof.circleimageview.CircleImageView;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.devproject.miguelfagundez.awesomenotes.R;
import com.devproject.miguelfagundez.awesomenotes.utils.Constants;
import com.devproject.miguelfagundez.awesomenotes.view.authentication.AuthActivity;
import com.devproject.miguelfagundez.awesomenotes.viewmodel.NotesViewModel;
import com.firebase.ui.auth.AuthUI;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment implements FirebaseAuth.AuthStateListener {

    // Constants
    private static final String TAG = "SettingsFragment";

    //Views declarations
    private CircleImageView circleImageView;
    private TextInputEditText textInputEditText;
    private Button buttonUpdate;
    private Button buttonLogout;
    private ProgressBar progressBar;

    // ViewModel
    private NotesViewModel viewModel;

    public SettingsFragment() {
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
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupViews();
        setupViewModel();
        setupListeners();
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        // Filling the text for username and photo for user profile
        if(viewModel.isCurrentUserIsNotNull()){
            String userName = viewModel.getUserName();
            Uri uri = viewModel.getUserProfilePhoto();
            if(userName != null){
                textInputEditText.setText(userName);
                textInputEditText.setSelection(userName.length());
            }

            if(uri != null){
                Glide.with(getActivity()).load(uri).into(circleImageView);
            }
        }
    }

    //*****************************************************
    // Setup components (Views, ViewModel, and Listeners)
    //*****************************************************
    private void setupViews() {
        circleImageView = getActivity().findViewById(R.id.civUserPhoto);
        textInputEditText = getActivity().findViewById(R.id.tietUserName);
        buttonUpdate = getActivity().findViewById(R.id.btnUpdateSettings);
        buttonLogout = getActivity().findViewById(R.id.btnLogout);
        progressBar = getActivity().findViewById(R.id.pbSettings);
        progressBar.setVisibility(View.GONE);
    }

    private void setupViewModel() {
        viewModel = ViewModelProviders.of(this).get(NotesViewModel.class);
    }

    private void setupListeners() {
        //*************************
        // Update user profile
        //*************************
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean profileChanged = false;
                buttonUpdate.setEnabled(false);
                progressBar.setVisibility(View.VISIBLE);

                //*******************************************
                // Changing the image if bitmap is not null
                //*******************************************
                if (!viewModel.isBitmapNull()){
                    profileChanged = true;
                    viewModel.uploadProfilePhoto();
                }else{
                    Log.d(TAG, "onClick: Profile photo is null..");
                }

                //***********************************************
                // Changing the user name only if it has changed
                //***********************************************
                if (!viewModel.getUserName().equals(textInputEditText.getText().toString())){
                    String result = viewModel.updateProfileUserName(textInputEditText.getText().toString());
                    profileChanged = true;
                }else{
                    Log.d(TAG, "onClick: Username has not changed..");
                }
                buttonUpdate.setEnabled(true);
                progressBar.setVisibility(View.GONE);

                if (profileChanged){
                    Toast.makeText(getActivity(), R.string.profile_update_successfully,Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getActivity(), R.string.profile_has_not_changed,Toast.LENGTH_SHORT).show();
                }
            }
        });

        //*************************
        // Close current session
        //*************************
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), R.string.logout,Toast.LENGTH_SHORT).show();
                AuthUI.getInstance().signOut(getActivity());
            }
        });

        //*************************
        // Take user photo
        //*************************
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(getActivity().getPackageManager())!=null){
                    startActivityForResult(intent, Constants.IMAGE_CAPTURE_CODE);
                }
            }
        });

    }

    //******************************
    // Menu bar options
    //******************************
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.findItem(R.id.menu_settings).setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    //******************************
    // Photo profile result
    //******************************
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        final int RESULT_OK = getActivity().RESULT_OK;

        if (requestCode == Constants.IMAGE_CAPTURE_CODE){
            if (resultCode == RESULT_OK){
                viewModel.setBitmap((Bitmap) data.getExtras().get("data"));
                circleImageView.setImageBitmap(viewModel.getBitmap());
            }else{
                Toast.makeText(getActivity(), R.string.error_camera, Toast.LENGTH_SHORT).show();
            }
        }
    }

    //***********************
    // Calling AuthActivity
    //***********************
    private void callAuthActivity() {
        Intent intent = new Intent(getActivity(), AuthActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    //****************************************
    // Register and remove Auth listener
    //****************************************
    @Override
    public void onStart() {
        super.onStart();
        // Adding this listener before app goes foreground
        FirebaseAuth.getInstance().addAuthStateListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        // Removing this listener before app goes background
        FirebaseAuth.getInstance().removeAuthStateListener(this);
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
    }
}
