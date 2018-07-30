package peter.kalata.com.eventapp.data.model;

import com.google.firebase.database.Exclude;

import org.parceler.Parcel;

import java.util.Date;

@Parcel
public class Event {

    public enum EventType {
        MEETINGS,
        HELP,
        TRIPS
    }

    @Exclude
    String key;
    String title;
    String description;
    String place;
    Date date;
    String userId;
    Date dateCreated;

    public Event() { }

    public Event(String title, String description, String place, Date date, String userId, Date dateCreated) {
        this.title = title;
        this.description = description;
        this.place = place;
        this.date = date;
        this.userId = userId;
        this.dateCreated = dateCreated;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }
}