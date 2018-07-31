package peter.kalata.com.eventapp.data.repository;

import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import peter.kalata.com.eventapp.App;
import peter.kalata.com.eventapp.R;
import peter.kalata.com.eventapp.data.model.Event;
import peter.kalata.com.eventapp.data.model.Event.EventType;
import peter.kalata.com.eventapp.data.model.Group;
import peter.kalata.com.eventapp.data.model.User;
import peter.kalata.com.eventapp.utils.DateTimeUtils;
import peter.kalata.com.eventapp.utils.SharedPrefsUtils;
import peter.kalata.com.eventapp.utils.SingleLiveEvent;
import peter.kalata.com.eventapp.utils.TextsUtils;

public class EventRepository {

    private DatabaseReference groupDatabaseReference;
    private final SingleLiveEvent<Throwable> error = new SingleLiveEvent<>();
    private final SingleLiveEvent<Boolean> createGroupSuccess = new SingleLiveEvent<>();
    private final SingleLiveEvent<Boolean> createUserSuccess = new SingleLiveEvent<>();
    private final SingleLiveEvent<Boolean> leaveGroupSuccess = new SingleLiveEvent<>();
    private final SingleLiveEvent<Boolean> createEventSuccess = new SingleLiveEvent<>();
    private final SingleLiveEvent<Boolean> loginSuccess = new SingleLiveEvent<>();
    private final SingleLiveEvent<List<User>> eventUsers = new SingleLiveEvent<>();
    private MutableLiveData<List<User>> users = new MutableLiveData<>();
    private MutableLiveData<User> currentUser = new MutableLiveData<>();
    private MutableLiveData<List<Event>> meetings = new MutableLiveData<>();
    private MutableLiveData<List<Event>> help = new MutableLiveData<>();
    private MutableLiveData<List<Event>> trips = new MutableLiveData<>();

    private static final String GROUP_NODE = "groups";
    private static final String USERS_NODE = "users";
    public static final String MEETINGS_NODE = "meetings";
    public static final String HELP_NODE = "help";
    public static final String TRIPS_NODE = "trips";
    private static final String PASSWORD_NODE = "password";
    private static final String ORDER_BY_DATE_FIELD = "date/time";
    private static final String ORDER_BY_CREATED_DATE_FIELD = "dateCreated/time";

    public EventRepository() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.setPersistenceEnabled(true);
        groupDatabaseReference = firebaseDatabase.getReference().child(GROUP_NODE);
    }

    public void loadUsers() {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<User> users = new ArrayList<>();
                for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);
                    users.add(user);
                }
                EventRepository.this.users.setValue(users);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                error.setValue(new Throwable(databaseError.getMessage()));

            }
        };

        groupDatabaseReference.child(SharedPrefsUtils.getGroupName())
                .child(USERS_NODE)
                .addValueEventListener(valueEventListener);
    }

    public void loadUsersForEvent(String eventKey, EventType eventType, EventUsersRepositoryCallback<User> firebaseCallback) {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<User> users = new ArrayList<>();
                for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);
                    users.add(user);
                }
                firebaseCallback.onSuccess(users);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                firebaseCallback.onError(new Throwable(databaseError.getMessage()));
            }
        };

        groupDatabaseReference.child(SharedPrefsUtils.getGroupName())
                .child(TextsUtils.getGroupNodeString(eventType))
                .child(eventKey)
                .child(USERS_NODE)
                .addValueEventListener(valueEventListener);
    }

    public void loadCurrentUser() {
        String userKey = SharedPrefsUtils.getUserKey();

        groupDatabaseReference.child(SharedPrefsUtils.getGroupName())
                .child(USERS_NODE)
                .child(userKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User user = dataSnapshot.getValue(User.class);
                    currentUser.setValue(user);
                } else {
                    error.setValue(new Throwable(App.getAppContext().getString(R.string.error_no_group)));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                error.setValue(new Throwable(databaseError.getMessage()));
            }
        });
    }

    public void loadEvents(EventType eventType) {

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Event> events = new ArrayList<>();
                for (DataSnapshot eventSnapshot: dataSnapshot.getChildren()) {
                    Event event = eventSnapshot.getValue(Event.class);
                    event.setKey(eventSnapshot.getKey());
                    events.add(event);
                }
                switch (eventType) {
                    case MEETINGS:
                        EventRepository.this.meetings.setValue(events);
                        break;
                    case HELP:
                        EventRepository.this.help.setValue(events);
                        break;
                    case TRIPS:
                        EventRepository.this.trips.setValue(events);
                        break;
                    default:
                        throw new IllegalArgumentException();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                error.setValue(new Throwable(databaseError.getMessage()));
            }
        };

        Query query = groupDatabaseReference.child(SharedPrefsUtils.getGroupName())
                .child(TextsUtils.getGroupNodeString(eventType))
                .orderByChild(ORDER_BY_DATE_FIELD)
                .startAt(DateTimeUtils.getCurrentTimestamp());
        query.addValueEventListener(valueEventListener);
    }

    public void writeNewGroup(Group group) {

        groupDatabaseReference.child(group.getName()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    error.setValue(new Throwable(App.getAppContext().getString(R.string.error_group_exist)));
                } else {
                    groupDatabaseReference.child(group.getName()).setValue(group)
                            .addOnSuccessListener(aVoid -> {
                                SharedPrefsUtils.setGroupName(group.getName());
                                createGroupSuccess.setValue(true);
                            })
                            .addOnFailureListener(e -> error.setValue(new Throwable(e.getMessage())));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                error.setValue(new Throwable(databaseError.getMessage()));
            }
        });
    }

    public void updateUser(User user) {

        groupDatabaseReference.child(SharedPrefsUtils.getGroupName())
                .child(USERS_NODE)
                .child(SharedPrefsUtils.getUserKey())
                .setValue(user)
                .addOnSuccessListener(aVoid -> {
                    currentUser.setValue(user);
                })
                .addOnFailureListener(e -> error.setValue(new Throwable(e.getMessage())));
    }

    public void leaveGroup() {

        groupDatabaseReference.child(SharedPrefsUtils.getGroupName())
                .child(USERS_NODE)
                .child(SharedPrefsUtils.getUserKey())
                .removeValue((databaseError, databaseReference) -> {
                    if (databaseError == null) {
                        SharedPrefsUtils.setFirstRun(true);
                        SharedPrefsUtils.setUserKey(null);
                        SharedPrefsUtils.setGroupName(null);
                        leaveGroupSuccess.setValue(true);
                    } else {
                        error.setValue(new Throwable(databaseError.getMessage()));
                    }
                });
    }

    public void writeEvent(Event event, EventType eventType) {
        String groupName = SharedPrefsUtils.getGroupName();
        String key = groupDatabaseReference.child(groupName).child(TextsUtils.getGroupNodeString(eventType)).push().getKey();

        groupDatabaseReference.child(groupName)
                .child(TextsUtils.getGroupNodeString(eventType))
                .child(key)
                .setValue(event)
                .addOnSuccessListener(aVoid -> {
                    createEventSuccess.setValue(true);
                })
                .addOnFailureListener(e -> error.setValue(new Throwable(e.getMessage())));
    }

    public void writeEventUser(String eventKey, EventType eventType, User currentUser) {
        String groupName = SharedPrefsUtils.getGroupName();

        groupDatabaseReference.child(groupName)
                .child(TextsUtils.getGroupNodeString(eventType))
                .child(eventKey)
                .child(USERS_NODE)
                .child(currentUser.getUserKey())
                .setValue(currentUser)
                .addOnFailureListener(e -> error.setValue(new Throwable(e.getMessage())));
    }

    public void removeEventUser(String eventKey, EventType eventType, User currentUser) {

        groupDatabaseReference.child(SharedPrefsUtils.getGroupName())
                .child(TextsUtils.getGroupNodeString(eventType))
                .child(eventKey)
                .child(USERS_NODE)
                .child(currentUser.getUserKey())
                .removeValue((databaseError, databaseReference) -> {
                    if (databaseError == null) {

                    } else {
                        error.setValue(new Throwable(databaseError.getMessage()));
                    }
                });
    }

    public void writeNewUser(User user) {
        String groupName = SharedPrefsUtils.getGroupName();
        String key = groupDatabaseReference.child(groupName).child(USERS_NODE).push().getKey();
        user.setUserKey(key);

        groupDatabaseReference.child(groupName).child(USERS_NODE).child(key).setValue(user)
                .addOnSuccessListener(aVoid -> {
                    SharedPrefsUtils.setUserKey(key);
                    SharedPrefsUtils.setFirstRun(false);
                    SharedPrefsUtils.setCheckNewEventTimestamp(DateTimeUtils.getCurrentTimestamp());
                    createUserSuccess.setValue(true);
                })
                .addOnFailureListener(e -> error.setValue(new Throwable(e.getMessage())));
    }

    public void login(String groupName, String password) {

        groupDatabaseReference.child(groupName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if (String.valueOf(dataSnapshot.child(PASSWORD_NODE).getValue()).equals(password)) {
                        SharedPrefsUtils.setGroupName(groupName);
                        loginSuccess.setValue(true);
                    } else {
                        loginSuccess.setValue(false);
                    }
                } else {
                    error.setValue(new Throwable(App.getAppContext().getString(R.string.error_no_group)));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                error.setValue(new Throwable(databaseError.getMessage()));
            }
        });
    }

    public Single<List<Event>> loadEventsForWidget(EventType eventType) {

        return Single.create(subscriber -> {
            ValueEventListener valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<Event> events = new ArrayList<>();
                    for (DataSnapshot eventSnapshot: dataSnapshot.getChildren()) {
                        Event event = eventSnapshot.getValue(Event.class);
                        event.setKey(eventSnapshot.getKey());
                        events.add(event);
                    }
                    subscriber.onSuccess(events);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    subscriber.onError(new Throwable(databaseError.getMessage()));
                }

            };

            Query query = groupDatabaseReference.child(SharedPrefsUtils.getGroupName())
                    .child(TextsUtils.getGroupNodeString(eventType))
                    .orderByChild(ORDER_BY_DATE_FIELD)
                    .startAt(DateTimeUtils.getCurrentTimestamp());
            query.addValueEventListener(valueEventListener);
        });

    }

    public Single<List<Event>> loadNewEvents(EventType eventType) {

        return Single.create(subscriber -> {
            ValueEventListener valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<Event> events = new ArrayList<>();
                    for (DataSnapshot eventSnapshot: dataSnapshot.getChildren()) {
                        Event event = eventSnapshot.getValue(Event.class);
                        event.setKey(eventSnapshot.getKey());
                        events.add(event);
                    }
                    subscriber.onSuccess(events);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    subscriber.onError(new Throwable(databaseError.getMessage()));
                }

            };
            Query query = groupDatabaseReference.child(SharedPrefsUtils.getGroupName())
                    .child(TextsUtils.getGroupNodeString(eventType))
                    .orderByChild(ORDER_BY_CREATED_DATE_FIELD)
                    .startAt(SharedPrefsUtils.getCheckNewEventTimestamp());
            query.addValueEventListener(valueEventListener);
        });

    }

    public SingleLiveEvent<List<User>> getEventUsers() {
        return eventUsers;
    }

    public MutableLiveData<List<User>> getUsers() {
        return users;
    }

    public MutableLiveData<User> getCurrentUser() {
        return currentUser;
    }

    public MutableLiveData<List<Event>> getMeetings() {
        return meetings;
    }

    public MutableLiveData<List<Event>> getHelp() {
        return help;
    }

    public MutableLiveData<List<Event>> getTrips() {
        return trips;
    }

    public SingleLiveEvent<Throwable> getError() {
        return error;
    }

    public SingleLiveEvent<Boolean> getCreateGroupSuccess() {
        return createGroupSuccess;
    }

    public SingleLiveEvent<Boolean> getCreateUserSuccess() {
        return createUserSuccess;
    }

    public SingleLiveEvent<Boolean> getLeaveGroupSuccess() {
        return leaveGroupSuccess;
    }

    public SingleLiveEvent<Boolean> getCreateEventSuccess() {
        return createEventSuccess;
    }

    public SingleLiveEvent<Boolean> getLoginSuccess() {
        return loginSuccess;
    }

    public interface EventUsersRepositoryCallback<T> {
        void onSuccess(List<T> result);

        void onError(Throwable throwable);
    }

}
