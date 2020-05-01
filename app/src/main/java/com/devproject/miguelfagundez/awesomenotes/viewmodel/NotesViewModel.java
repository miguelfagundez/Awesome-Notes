package com.devproject.miguelfagundez.awesomenotes.viewmodel;

import android.app.Application;
import android.graphics.Bitmap;
import android.net.Uri;

import com.devproject.miguelfagundez.awesomenotes.model.firebase.NotesFirestore;
import com.devproject.miguelfagundez.awesomenotes.model.sharedpreferences.NotesPreferences;
import com.devproject.miguelfagundez.awesomenotes.utils.Constants;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

/********************************************
 * Class- NotesViewModel
 * This class handle the ViewModel component
 * @author: Miguel Fagundez
 * @date: April 22th, 2020
 * @version: 1.0
 * *******************************************/
public class NotesViewModel extends AndroidViewModel {

    // Members
    private Bitmap profilePhoto = null;
    // Connecting with DB Cloud Firestore
    private NotesFirestore firestore;
    // Connecting with Shared Preferences (only used for checking first time launch)
    private NotesPreferences preferences;

    public NotesViewModel(@NonNull Application application) {
        super(application);
        firestore = new NotesFirestore();
        preferences = new NotesPreferences(application.getApplicationContext());
    }

    //**********************************
    // Bitmap image for photo profile
    //**********************************
    public void setBitmap(Bitmap bitmap){ profilePhoto = bitmap; }
    public Bitmap getBitmap(){ return profilePhoto; }
    public boolean isBitmapNull() { return profilePhoto == null; }

    //*******************************
    // Accessing firestore object
    //*******************************
    public void uploadProfilePhoto(){
        firestore.uploadProfileUserImageToFiresbase(profilePhoto);
        // Avoid to save many times tha same image
        profilePhoto = null;
    }

    public void addNewNote(int id, String title, String body, Timestamp creation, String photoPath, boolean priority) {
        firestore.addNewNote(id, title, body, creation, photoPath, priority);
    }

    //*********************************
    // FirebaseAuth & FirebaseUser
    //*********************************
    public boolean isCurrentUserIsNotNull(){ return firestore.getCurrentUser()!=null; }
    public FirebaseUser getCurrentUser(){ return firestore.getCurrentUser(); }
    public String getUserName(){ return firestore.getUserName(); }
    public Uri getUserProfilePhoto(){ return firestore.getPhotoProfile(); }

    public String updateProfileUserName(String newUserName){ return firestore.updateUserName(newUserName); }

    public void updateNote(String title, String body, String documentId) {
        Map<String, Object> map = new HashMap<>();
        map.put(Constants.COLLECTION_NOTES_TITLE, title);
        map.put(Constants.COLLECTION_NOTES_BODY, body);

        firestore.updateNote(map, documentId);
    }

    public void writeOnboardingDone() {
        preferences.write(Constants.IS_FIRST_TIME, true);
    }

    public boolean isOnboardingDone(){
        return preferences.readBoolean(Constants.IS_FIRST_TIME);
    }

}
