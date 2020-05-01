package com.devproject.miguelfagundez.awesomenotes.model.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.Timestamp;


/********************************************
 * Pojo Class - Note
 * This class handles the note variables
 * that is needed for Firebase Firestore.
 * Parcelable is implemented.
 * @author: Miguel Fagundez
 * @date: April 24th, 2020
 * @version: 1.0
 * *******************************************/
public class Note implements Parcelable {

    // Members
    private int id;
    private String title;
    private String body;
    private Timestamp creation;
    private String photo;
    private boolean priority;
    private String userId;

    // Public empty constructor for Firebase
    public Note(){}

    // Default constructor
    public Note(int id, String title, String body, Timestamp creation, String photo, boolean priority, String userId) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.creation = creation;
        this.photo = photo;
        this.priority = priority;
        this.userId = userId;
    }

    protected Note(Parcel in) {
        id = in.readInt();
        title = in.readString();
        body = in.readString();
        creation = in.readParcelable(Timestamp.class.getClassLoader());
        photo = in.readString();
        priority = in.readByte() != 0;
        userId = in.readString();
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    //********************************
    // Getters and Setters
    //********************************
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Timestamp getCreation() {
        return creation;
    }

    public void setCreation(Timestamp creation) {
        this.creation = creation;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public boolean getIsPriority() {
        return priority;
    }

    public void setPriority(boolean priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", creation=" + creation +
                ", photo='" + photo + '\'' +
                ", priority=" + priority +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeString(body);
        parcel.writeParcelable(creation, i);
        parcel.writeString(photo);
        parcel.writeByte((byte) (priority ? 1 : 0));
        parcel.writeString(userId);
    }
}
