package peter.kalata.com.eventapp.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import java.util.concurrent.atomic.AtomicInteger;
import peter.kalata.com.eventapp.data.model.Event;
import peter.kalata.com.eventapp.ui.main.MainActivity;

import static peter.kalata.com.eventapp.ui.events.EventsFragment.ARGS_EVENT_TYPE;

public class NotificationUtils {

    private static final int PENDING_INTENT_ID = 101;
    private final static AtomicInteger c = new AtomicInteger(0);

    public static Notification createForegroundNotification(Context context, Event event, Event.EventType eventType) {
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        String channelId = TextsUtils.getGroupNodeString(eventType);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId,
                    TextsUtils.getEventCategoryDisplayString(eventType),
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(mChannel);
        }

        String content = TextsUtils.getNotificationContentString(event);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(TextsUtils.getEventCategoryDrawableId(eventType))
                .setContentTitle(TextsUtils.getEventCategoryDisplayString(eventType))
                .setContentText(content)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(content))
                .setContentIntent(createPendingIntent(context, eventType));

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        }

        return builder.build();
    }

    private static PendingIntent createPendingIntent(Context context, Event.EventType eventType) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setPackage(context.getPackageName());
        intent.putExtra(ARGS_EVENT_TYPE, eventType);
        return PendingIntent.getActivity(
                context,
                PENDING_INTENT_ID,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public static int getID() {
        return c.incrementAndGet();
    }


}