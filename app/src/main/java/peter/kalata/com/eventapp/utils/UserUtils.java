package peter.kalata.com.eventapp.utils;

import java.util.List;

import peter.kalata.com.eventapp.data.model.User;

public final class UserUtils {

    public static boolean isCurrentUserInList(List<User> users) {
        String currentUserKey = SharedPrefsUtils.getUserKey();
        for (User user: users) {
            if (user.getUserKey().equals(currentUserKey)) {
                return true;
            }
        }
        return false;
    }



}
