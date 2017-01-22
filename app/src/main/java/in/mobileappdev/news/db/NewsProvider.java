package in.mobileappdev.news.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;

/**
 * Udacity
 * Created by satyanarayana.avv on 13-01-2017.
 */

public class NewsProvider extends ContentProvider {

    public static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(NewsContract.AUTHORITY, NewsContract.TABLE, NewsContract.ALL_NOTIFICATIONS);
    }

    private SQLiteDatabase db;

    @Override
    public boolean onCreate() {
        NewsDBHelper newsDBHelper = new NewsDBHelper(getContext());
        db = newsDBHelper.getWritableDatabase();

        if (db == null) {
            return false;
        }

        if (db.isReadOnly()) {
            db.close();
            db = null;
            return true;
        }

        return false;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(NewsContract.TABLE);

        switch (uriMatcher.match(uri)) {
            case NewsContract.ALL_NOTIFICATIONS:
                break;
            default:
                throw new IllegalArgumentException("Invalid URI: " + uri);
        }

        return qb.query(db, projection, selection, selectionArgs, null, null, null);
    }

    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)) {
            case NewsContract.ALL_NOTIFICATIONS:
                return NewsContract.CONTENT_TYPE;

            default:
                throw new IllegalArgumentException("Invalid URI: " + uri);
        }

    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues contentValues) {

        if (uriMatcher.match(uri) != NewsContract.ALL_NOTIFICATIONS) {
            throw new IllegalArgumentException("Invalid URI: " + uri);
        }

        long id = db.insert(NewsContract.TABLE, null, contentValues);

        if (id > 0) {
            return ContentUris.withAppendedId(uri, id);
        }
        throw new SQLException("Error inserting into table: " + NewsContract.TABLE);
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        int deleted = 0;

        switch (uriMatcher.match(uri)) {
            case NewsContract.ALL_NOTIFICATIONS:
                db.delete(NewsContract.TABLE, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Invalid URI: " + uri);
        }

        return deleted;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues contentValues, String s, String[] strings) {
        int updated = 0;

        switch (uriMatcher.match(uri)) {
            case NewsContract.ALL_NOTIFICATIONS:
                db.update(NewsContract.TABLE, contentValues, s, strings);
                break;

            default:
                throw new IllegalArgumentException("Invalid URI: " + uri);
        }

        return updated;
    }
}
