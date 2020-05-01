package com.devproject.miguelfagundez.awesomenotes.utils;


/********************************************
 * Class - Constants
 * This class handles some constants that are
 * being used in this project
 * @author: Miguel Fagundez
 * @date: April 23th, 2020
 * @version: 1.0
 * *******************************************/
public class Constants {

    //*********************************************************
    // Activities for results
    //*********************************************************
    public static final int AUTH_RESULT_CODE = 1001;   // AuthActivity
    public static final int IMAGE_CAPTURE_CODE = 1002;

    //**********************************************************
    // Firestore Database
    //**********************************************************
    public static final String COLLECTION_NOTES = "notes";
    public static final String COLLECTION_NOTE = "note";
    public static final String COLLECTION_NOTES_DOCUMENT_ID = "documentId";
    public static final String COLLECTION_NOTES_USER_ID = "userId";
    public static final String COLLECTION_NOTES_TITLE = "title";
    public static final String COLLECTION_NOTES_BODY = "body";
    public static final String COLLECTION_NOTES_PHOTO = "photo";
    public static final String COLLECTION_NOTES_CREATION = "creation";
    public static final String PROFILE_STORAGE_NAME = "profilePhotos";
    public static final String NOTES_IMAGES_STORAGE_NAME = "notesPhotos";

    //***********************************************************
    // SharedPreferences
    //***********************************************************
    public static final String SHARED_PREFERENCES_NAME = "awesome_notes_pref";
    public static final String IS_FIRST_TIME = "is_first_time";

    //***********************************************************
    // Format, permissions, and other general uses
    //***********************************************************
    public static final String DATE_FORMAT = "EEE, d MMM yyyy HH:mm";
    //***********************************************************
    // Onboarding navegation
    //***********************************************************
    public static final int NUM_ONBOARDING_SCREENS = 3;
}
