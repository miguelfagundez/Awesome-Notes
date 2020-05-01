package com.devproject.miguelfagundez.awesomenotes.view.authentication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.devproject.miguelfagundez.awesomenotes.R;
import com.devproject.miguelfagundez.awesomenotes.utils.Constants;
import com.devproject.miguelfagundez.awesomenotes.view.home.MainActivity;
import com.devproject.miguelfagundez.awesomenotes.viewmodel.NotesViewModel;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

/********************************************
 * Activity - AuthActivity
 * This activity show the login screen
 * @author: Miguel Fagundez
 * @date: April 23th, 2020
 * @version: 1.0
 * *******************************************/
public class AuthActivity extends AppCompatActivity {

    // Members
    private static final String TAG = "AuthActivity";
    private NotesViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        setupViewModel();
        //*****************************
        // if a session is open, then
        // goes to MainActivity
        //*****************************
        if (viewModel.isCurrentUserIsNotNull()){
            callMainActivity();
        }
    }

    private void setupViewModel() {
        viewModel = ViewModelProviders.of(this).get(NotesViewModel.class);
    }

    //***********************
    // Calling MainActivity
    //***********************
    public void callMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    //*********************************************
    //       Verify current User status
    //*********************************************
    public void openSessionOrRegister(View view) {

        // List of different ways to registers:
        // Email and Google authentication
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                // Sign in (Email)
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build()
        );
        // Creating an intent using AuthUI for open or register users
        Intent intent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setTosAndPrivacyPolicyUrls("https://miguelfagundez.weebly.com/","https://miguelfagundez.weebly.com/privacy-policy.html")
                .setLogo(R.drawable.awesomenote)
                .setAlwaysShowSignInMethodScreen(true)
                .build();

        startActivityForResult(intent, Constants.AUTH_RESULT_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case Constants.AUTH_RESULT_CODE:
                if(resultCode == RESULT_OK){
                    // Sign in for new or existent user
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    if (currentUser.getMetadata().getCreationTimestamp() == currentUser.getMetadata().getLastSignInTimestamp()){
                        Toast.makeText(this, R.string.new_user,Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(this, R.string.welcome_back,Toast.LENGTH_SHORT).show();
                    }
                    callMainActivity();
                }else{
                    // Sign in failed
                    // 1: User has cancelled
                    // 2: Error message
                    IdpResponse response = IdpResponse.fromResultIntent(data);
                    if(response == null){
                        Toast.makeText(this, R.string.canceled_sign_in,Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(this, "Error: " + response.getError(),Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }
}
