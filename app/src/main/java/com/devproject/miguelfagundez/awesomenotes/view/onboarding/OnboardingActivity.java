package com.devproject.miguelfagundez.awesomenotes.view.onboarding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.devproject.miguelfagundez.awesomenotes.R;
import com.devproject.miguelfagundez.awesomenotes.adapters.FirstTimePagerAdapter;
import com.devproject.miguelfagundez.awesomenotes.utils.Constants;
import com.devproject.miguelfagundez.awesomenotes.view.authentication.AuthActivity;
import com.devproject.miguelfagundez.awesomenotes.viewmodel.NotesViewModel;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

/********************************************
 * OnboardingActivity
 *
 * @author: Miguel Fagundez
 * @date: April 25th, 2020
 * @version: 1.0
 * *******************************************/
public class OnboardingActivity extends AppCompatActivity {

    // Members
    private int position;
    private FirstTimePagerAdapter adapter;

    // Views
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private Button btnBack;
    private Button btnNext;

    // ViewModel
    private NotesViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        viewModel = ViewModelProviders.of(this).get(NotesViewModel.class);

        if(viewModel.isOnboardingDone()){
            callAuthActivity();
        }

        setupMembers();
        // Set up the view pager
        setupViewPager();
        // Set up view visibility
        setupViewVisibility();
        // setup buttons listeners
        setupListeners();
    }

    private void setupMembers() {
        position = 0;
        adapter = new FirstTimePagerAdapter(this, setupOnboardingData());
        viewPager = findViewById(R.id.vpIntro);
        tabLayout = findViewById(R.id.tabPagerIndicator);
        btnBack = findViewById(R.id.btnBack);
        btnNext = findViewById(R.id.btnNext);
    }

    // Initialization of Data
    private ArrayList<OnboardingData> setupOnboardingData() {
        ArrayList<OnboardingData> list = new ArrayList<OnboardingData>();

        list.add(new OnboardingData(
                getString(R.string.title_onboarding),
                getString(R.string.one_time_register),
                R.drawable.data_protection_mobile_min));

        list.add(new OnboardingData(
                        getString(R.string.title_picture_onboarding),
                        getString(R.string.notes_pictures),
                        R.drawable.share_data_min));

        list.add(new OnboardingData(
                        getString(R.string.title_memory_onboarding),
                        getString(R.string.notes_external_card),
                        R.drawable.delete_min));

        return list;
    }

    // Set up TabLayout with this viewpager
    private void setupViewPager() {
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewVisibility() {
        // Button Back visibility
        btnBack.setVisibility(View.GONE);
        if(position!=0) btnBack.setVisibility(View.VISIBLE);

        // Button next and Go to MainActivity
        btnNext.setVisibility(View.VISIBLE);
    }

    private void setupListeners() {
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                position = viewPager.getCurrentItem();
                if(position < Constants.NUM_ONBOARDING_SCREENS){
                    position += 1;
                    viewPager.setCurrentItem(position);
                    setupViewVisibility();
                }
                if(position == Constants.NUM_ONBOARDING_SCREENS){
                    viewModel.writeOnboardingDone();
                    callAuthActivity();
                }

            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                position = viewPager.getCurrentItem();
                if (position > 0){
                    position -= 1;
                    viewPager.setCurrentItem(position);
                    setupViewVisibility();
                }
            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                position = tab.getPosition();
                if (position >= 0) setupViewVisibility();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void callAuthActivity() {
        Intent intent = new Intent(this, AuthActivity.class);
        startActivity(intent);
        finish();
    }

}
