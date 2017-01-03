package in.mobileappdev.news.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import in.mobileappdev.news.models.Notification;

/**
 * Created by satyanarayana.avv on 03-01-2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

  private static final int DATABASE_VERSION = 1;
  private static final String DATABASE_NAME = "in.mobileappde.news";
  private static final String TABLE_NOTIFICATIONS = "notifications";
  private static final String TABLE_SAVED_ARTICLES = "favorite";
  private static final String KEY_ID = "id";
  private static final String KEY_NOTIFICATION_MSG = "message";
  private static final String KEY_NOTIFICATION_IMG = "message_img";
  private static final String KEY_NOTIFICATION_TIME = "created_at";

  public DatabaseHandler(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  // Creating Tables
  @Override
  public void onCreate(SQLiteDatabase db) {
    String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_NOTIFICATIONS + "("
        + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NOTIFICATION_MSG + " TEXT,"
        + KEY_NOTIFICATION_IMG + " TEXT," +  KEY_NOTIFICATION_TIME+")";
    db.execSQL(CREATE_CONTACTS_TABLE);
  }

  // Upgrading database
  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    // Drop older table if existed
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTIFICATIONS);

    // Create tables again
    onCreate(db);
  }

  /**
   * All CRUD(Create, Read, Update, Delete) Operations
   */

  // Adding new contact
  public void saveNotification(String notification, String imageUrl) {
    SQLiteDatabase db = this.getWritableDatabase();

    ContentValues values = new ContentValues();
    values.put(KEY_NOTIFICATION_MSG, notification); // Contact Name
    values.put(KEY_NOTIFICATION_IMG, imageUrl); // Contact Phone

    String desiredFormat = new SimpleDateFormat(
        "yyyy-M-dd hh:mm:ss", Locale.ENGLISH).format(new Date());;

    values.put(KEY_NOTIFICATION_TIME, desiredFormat);

    // Inserting Row
    db.insert(TABLE_NOTIFICATIONS, null, values);
    db.close(); // Closing database connection
  }


  // Getting All Contacts
  public List<Notification> getAllNotifications() {
    List<Notification> notificatioList = new ArrayList<Notification>();
    // Select All Query
    String selectQuery = "SELECT  * FROM " + TABLE_NOTIFICATIONS;

    SQLiteDatabase db = this.getWritableDatabase();
    Cursor cursor = db.rawQuery(selectQuery, null);

    // looping through all rows and adding to list
    if (cursor.moveToFirst()) {
      do {
        Notification contact = new Notification(cursor.getString(1),cursor.getString(2),cursor
            .getString(3));
        notificatioList.add(contact);
      } while (cursor.moveToNext());
    }

    // return contact list
    return notificatioList;
  }

}