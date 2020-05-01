package com.devproject.miguelfagundez.awesomenotes.listeners;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;

/********************************************
 * Interface - NotesListeners
 * This interface connects the NotesAdapter with
 * MainActivity in order to detect the click
 * in the recycler view
 * @author: Miguel Fagundez
 * @date: April 24th, 2020
 * @version: 1.0
 * *******************************************/
public interface NotesListeners {

    void updateNote(String title, String photo, Timestamp creation, String body, String documentId);
    void deleteNote(DocumentSnapshot snapshot);

}
