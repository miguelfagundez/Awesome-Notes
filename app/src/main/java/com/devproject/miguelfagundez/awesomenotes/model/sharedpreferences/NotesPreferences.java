package com.devproject.miguelfagundez.awesomenotes.model.sharedpreferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.devproject.miguelfagundez.awesomenotes.utils.Constants;

/********************************************
 * Class- NotesPreferences
 * This class handle the basic shared preferences.
 * Only it used for Onboarding screens.
 * @author: Miguel Fagundez
 * @date: April 25th, 2020
 * @version: 1.0
 * *******************************************/
public class NotesPreferences {

    private SharedPreferences preferences = null;

    public NotesPreferences(Context context){
        if (preferences == null){
            preferences = context.getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, context.MODE_PRIVATE);
        }
    }

    //****************************************
    // Write options available:
    // String, Boolean
    //****************************************
     public void write(String key, boolean value){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    //****************************************
    // Read options available:
    // String, Boolean
    //****************************************
    public boolean readBoolean(String key) {
        return preferences.getBoolean(key,false);
    }

}
