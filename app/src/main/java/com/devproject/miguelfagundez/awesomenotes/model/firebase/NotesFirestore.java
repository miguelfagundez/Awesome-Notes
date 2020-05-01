package com.devproject.miguelfagundez.awesomenotes.model.firebase;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import com.devproject.miguelfagundez.awesomenotes.model.pojo.Note;
import com.devproject.miguelfagundez.awesomenotes.utils.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Map;

import androidx.annotation.NonNull;

public class NotesFirestore {

    // Constants
    private static final String TAG = "NotesFirestore";
    private String response = null;

    public NotesFirestore(){}

    //**************************************************
    // Uploading images to Firebase Storage module
    //**************************************************
    //*********************
    // Profile pictures
    //*********************
    public void uploadProfileUserImageToFiresbase(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final StorageReference reference = FirebaseStorage.getInstance().getReference()
                .child(Constants.PROFILE_STORAGE_NAME)
                .child(userId + ".jpeg");
        reference.putBytes(baos.toByteArray())
                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()){
                            Log.d(TAG, "onComplete: Image name created!");
                            takeReferenceForDownload(reference);
                        }else{
                            Log.e(TAG, "onComplete: Error = ", task.getException());
                        }
                    }
                });
    }


    private void takeReferenceForDownload(StorageReference reference) {
        reference.getDownloadUrl()
                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()){
                            Log.d(TAG, "onComplete: Uri accepted " + task.getResult().toString());
                            setPhotoUritoFirebase(task.getResult());
                        }else{
                            Log.e(TAG, "onComplete: Error = ", task.getException());
                        }
                    }
                });
    }

    private void setPhotoUritoFirebase(Uri result) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                .setPhotoUri(result)
                .build();

        user.updateProfile(request)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Log.d(TAG, "onComplete: Successfully profile updated");
                        }else{
                            Log.e(TAG, "onComplete: Error = ", task.getException());
                        }
                    }
                });
    }

    //********************************
    // Firebase Authentication
    //********************************
    public FirebaseUser getCurrentUser(){ return FirebaseAuth.getInstance().getCurrentUser(); }

    public String getUserName() { return FirebaseAuth.getInstance().getCurrentUser().getDisplayName(); }

    public Uri getPhotoProfile() { return FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl(); }

    //********************************
    // Updating Profile User Data
    //********************************
    public String updateUserName(String newUserName){

        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                .setDisplayName(newUserName)
                .build();
        getCurrentUser().updateProfile(request)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Log.d(TAG, "onComplete: Profile Updated");
                            response = "onComplete: Profile Updated";
                        }else{
                            Log.e(TAG, "onComplete: Error = ", task.getException());
                            response = "Error trying to change user name";
                        }
                    }
                });
        return response;
    }

    //********************************
    // Adding or Updating notes
    //********************************
    public void addNewNote(int id, String title, String body, Timestamp creation, String photoPath, boolean priority) {

        // Instance of the database
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Taking the current sign in user
        // I do not need to check currentUser == null because I implement
        // the listener in the MainActivity
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Creating a note
        Note note = new Note(id, title, body, creation,photoPath,priority, userId);

        db.collection(Constants.COLLECTION_NOTES)
                .add(note)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "onSuccess: Note added");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: ", e);
                    }
                });
    }

    public void updateNote(Map<String, Object> map, String documentId) {
        // Instance of the database
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Taking the current sign in user
        // I do not need to check currentUser == null because I implement
        // the listener in the MainActivity
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        db.collection(Constants.COLLECTION_NOTES)
                .document(documentId)
                .update(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: Data updated..");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: ", e);
                    }
                });
    }
}
