package in.mobileappdev.news.firebase;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import in.mobileappdev.news.R;
import in.mobileappdev.news.db.NewsContract;
import in.mobileappdev.news.ui.NewsDetailWebActivity;
import in.mobileappdev.news.utils.Constants;

public class NewsFirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    NotificationCompat.Builder notificationBuilder;

    Bitmap image;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO(developer): Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        //Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
        Log.d(TAG, "FCM Data: " + remoteMessage.getData());

        //String tag = remoteMessage.getNotification().getTag();
        String title = remoteMessage.getData().get("title");
        String msg = remoteMessage.getData().get("message");
        String img = remoteMessage.getData().get("image");
        String type = remoteMessage.getData().get("type");

        ContentValues values = new ContentValues();
        values.put(NewsContract.Columns.KEY_NOTIFICATION_MSG, title);
        values.put(NewsContract.Columns.KEY_NOTIFICATION_IMG, img);
        values.put(NewsContract.Columns.KEY_NOTIFICATION_URL, msg);

        String desiredFormat = new SimpleDateFormat(
                "yyyy-M-dd hh:mm:ss", Locale.ENGLISH).format(new Date());;

        values.put(NewsContract.Columns.KEY_NOTIFICATION_TIME, desiredFormat);

        image = getBitmapFromURL(img);

        Uri uri = NewsContract.CONTENT_URI;
        getApplicationContext().getContentResolver().insert(uri,values);
        sendNotification(type,title, msg, image);
    }

    private void sendNotification(String type, String title,String messageBody, Bitmap img) {
        Intent intent = new Intent(this, NewsDetailWebActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(Constants.DEEPLINK, messageBody);
        intent.setAction(Intent.ACTION_VIEW);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 , intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        if (type.equalsIgnoreCase("image")) {
            notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(title)
                    .setContentText(messageBody)
                    .setStyle(new NotificationCompat.BigPictureStyle()
                            .bigPicture(img))
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);
        } else {
            notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(title)
                    .setContentText(messageBody)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);
        }

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
