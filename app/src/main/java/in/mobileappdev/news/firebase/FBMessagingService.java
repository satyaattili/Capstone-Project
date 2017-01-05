package in.mobileappdev.news.firebase;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import in.mobileappdev.news.R;
import in.mobileappdev.news.db.DatabaseHandler;
import in.mobileappdev.news.ui.MainActivity;
import in.mobileappdev.news.ui.NewsDetailWebActivity;
import in.mobileappdev.news.utils.Constants;

/**
 * Udacity
 * Created by satyanarayana.avv on 03-01-2017.
 */


public class FBMessagingService extends FirebaseMessagingService {

  private static final String TAG = "FBMessagingService";
  public static final String KEY_BODY = "body";
  public static final String KEY_IMG_URL = "imageUrl";

  @Override
  public void onMessageReceived(RemoteMessage remoteMessage) {
    Log.d(TAG, "onMessageReceived " + remoteMessage);

   /* if (remoteMessage.getData().size() > 0) {
      Log.d(TAG, "Message data payload: " + remoteMessage.getData());
      sendNotification(remoteMessage.getData().get(KEY_MESSAGE));
    }*/

    String body = remoteMessage.getData().get(KEY_BODY);
    if (body != null) {
      sendNotification(remoteMessage.getData().get(KEY_BODY),
          remoteMessage.getData().get(KEY_IMG_URL));
      return;
    }

    if (remoteMessage.getNotification() != null) {
      sendNotification(remoteMessage.getNotification().getBody(), "");
    }


  }

  //This method is only generating push notification
  //It is same as we did in earlier posts
  private void sendNotification(String messageBody, String url) {

    Intent intent = new Intent(this, NewsDetailWebActivity.class);
    intent.putExtra(Constants.DEEPLINK, messageBody);
    intent.setAction(Intent.ACTION_VIEW);

    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
        PendingIntent.FLAG_UPDATE_CURRENT);


    Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
        .setSmallIcon(R.mipmap.ic_launcher)
        .setContentTitle(getResources().getString(R.string.app_name))
        .setContentText(messageBody)
        .setAutoCancel(true)
        .setSound(defaultSoundUri)
        .setContentIntent(pendingIntent);

    NotificationManager notificationManager =
        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

    notificationManager.notify(0, notificationBuilder.build());
  }
}