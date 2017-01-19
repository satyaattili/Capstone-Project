package in.mobileappdev.news.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Udacity
 * Created by satyanarayana.avv on 13-01-2017.
 */

public class NewsDBHelper extends SQLiteOpenHelper {

    public NewsDBHelper(Context context) {
        super(context, NewsContract.DB_NAME, null, NewsContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqlDB) {
        String sqlQuery = "CREATE TABLE " + NewsContract.TABLE + "("
                + NewsContract.Columns._ID + " INTEGER PRIMARY KEY," + NewsContract.Columns.KEY_NOTIFICATION_URL + " TEXT," + NewsContract.Columns.KEY_NOTIFICATION_MSG + " TEXT,"
                + NewsContract.Columns.KEY_NOTIFICATION_IMG + " TEXT," + NewsContract.Columns.KEY_NOTIFICATION_TIME + ")";

        sqlDB.execSQL(sqlQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqlDB, int i, int i2) {
        sqlDB.execSQL("DROP TABLE IF EXISTS " + NewsContract.TABLE);
        onCreate(sqlDB);
    }
}