package com.devproject.miguelfagundez.awesomenotes.view.onboarding;

/********************************************
 * Class - OnboardingData
 * This class has a data skeleton for my
 * Onboarding screens
 * @author: Miguel Fagundez
 * @date: April 25th, 2020
 * @version: 1.0
 * *******************************************/
public class OnboardingData {

    private String title;
    private String description;
    private int image;

    public OnboardingData(String title, String description, int image) {
        this.title = title;
        this.description = description;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public int getImage() {
        return image;
    }

    @Override
    public String toString() {
        return "OnboardingData{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", image=" + image +
                '}';
    }
}
