package com.devproject.miguelfagundez.awesomenotes.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.devproject.miguelfagundez.awesomenotes.R;
import com.devproject.miguelfagundez.awesomenotes.view.onboarding.OnboardingData;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

/********************************************
 * Class- FirstTimePagerAdapter
 * This class implements the PagerAdapter
 * that is used in Onboarding Screens
 * @author: Miguel Fagundez
 * @date: April 24th, 2020
 * @version: 1.0
 * *******************************************/
public class FirstTimePagerAdapter extends PagerAdapter {

    private Context context;
    private ArrayList<OnboardingData> list;

    public FirstTimePagerAdapter(Context context, ArrayList<OnboardingData> list){
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_onboarding, null);

        // Connecting with views
        ImageView image = view.findViewById(R.id.ivOnboardingImage);
        TextView title = view.findViewById(R.id.tvOnboardingTitle);
        TextView description = view.findViewById(R.id.tvOnboardingDescription);

        // Adding data into the views
        image.setImageResource(list.get(position).getImage());
        title.setText(list.get(position).getTitle());
        description.setText(list.get(position).getDescription());

        // Adding the view into container
        container.addView(view);

        return view;
    }
}
