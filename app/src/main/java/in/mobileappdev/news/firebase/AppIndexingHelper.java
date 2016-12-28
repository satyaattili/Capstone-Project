package in.mobileappdev.news.firebase;

import android.app.Activity;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.appindexing.FirebaseAppIndex;
import com.google.firebase.appindexing.FirebaseUserActions;
import com.google.firebase.appindexing.Indexable;
import com.google.firebase.appindexing.builders.Actions;

import in.mobileappdev.news.utils.Constants;

/**
 * Created by satyanarayana.avv on 28-12-2016.
 */

public class AppIndexingHelper {

  private static String TAG = AppIndexingHelper.class.getSimpleName();


  public static void startAppIndexing(@NonNull final String title, @NonNull String newsUrl, Activity
      activity) {
    final String APP_INDEXING_URL = Uri.parse(Constants.BASE_SHARE_URL + newsUrl).toString();

    Indexable articleToIndex = new Indexable.Builder()
        .setName(title)
        .setUrl(APP_INDEXING_URL)
        .build();

    Task<Void> task = FirebaseAppIndex.getInstance().update(articleToIndex);

    task.addOnSuccessListener(activity, new OnSuccessListener<Void>() {
      @Override
      public void onSuccess(Void aVoid) {
        Log.d(TAG, "App Indexing -- Successful " + title);
      }
    });

    task.addOnFailureListener(activity, new OnFailureListener() {
      @Override
      public void onFailure(@NonNull Exception exception) {
        Log.e(TAG, "App Indexing Failed " + title + " -- Exception " + exception
            .getMessage());
      }
    });

    Task<Void> actionTask =
        FirebaseUserActions.getInstance().start(Actions.newView(title,
            APP_INDEXING_URL));

    actionTask.addOnSuccessListener(activity, new OnSuccessListener<Void>() {
      @Override
      public void onSuccess(Void aVoid) {
        Log.d(TAG, "App Indexing - Successfully started " + title);
      }
    });

    actionTask.addOnFailureListener(activity, new OnFailureListener() {
      @Override
      public void onFailure(@NonNull Exception exception) {
        Log.e(TAG, "App Indexing - Failed to start " + title + ". "
            + exception.getMessage());
      }
    });
  }

  public static void endAppIndexing(@NonNull final String title, @NonNull String newsUrl, Activity
      activity) {
    final String APP_INDEXING_URL = Uri.parse(Constants.BASE_SHARE_URL + newsUrl).toString();

    Task<Void> actionTask = FirebaseUserActions.getInstance().end(Actions.newView(title,
        APP_INDEXING_URL));

    actionTask.addOnSuccessListener(activity, new OnSuccessListener<Void>() {
      @Override
      public void onSuccess(Void aVoid) {
        Log.d(TAG, "App Indexing API: Successfully ended view action on " + title);
      }
    });

    actionTask.addOnFailureListener(activity, new OnFailureListener() {
      @Override
      public void onFailure(@NonNull Exception exception) {
        Log.e(TAG, "App Indexing API: Failed to end view action on " + title + ". "
            + exception.getMessage());
      }
    });
  }
}