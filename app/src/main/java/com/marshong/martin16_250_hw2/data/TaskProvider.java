package com.marshong.martin16_250_hw2.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

/**
 * Created by martin on 4/30/2015.
 */
public class TaskProvider extends ContentProvider {

    private static final String TAG = TaskProvider.class.getSimpleName();

    private static final int VERSION = 100;
    private static final int TASK_ID = 200;

    private static final UriMatcher sUriMatcher = createUriMatcher();

    private static UriMatcher createUriMatcher() {

        Log.d(TAG, "creating URI Matcher using " + TasksContract.CONTENT_AUTHORITY + " " + TasksContract.PATH_TASK);
        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = TasksContract.CONTENT_AUTHORITY;


        //Note: when a URI comes in for a query, the method will determine if the query is for a
        //generic version or if its for a specific task_id. i.e. querying against the primary key.
        uriMatcher.addURI(authority, TasksContract.PATH_TASK, VERSION);
        uriMatcher.addURI(authority, TasksContract.PATH_TASK + "/#", TASK_ID);

        return uriMatcher;
    }

    private TaskDbHelper mDbHelper;


    @Override
    public boolean onCreate() {
        Log.d(TAG, "onCreate, new mDbHelper being created");
        mDbHelper = new TaskDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Log.d(TAG, "Querying the DB..." + uri.toString());

        // Use SQLiteQueryBuilder for querying db
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        // Set the table name
        queryBuilder.setTables(TasksContract.Task.TABLE_NAME);

        // Record id
        String id;

        // Match Uri pattern
        int uriType = sUriMatcher.match(uri);

        Log.d(TAG, "URI Type: " + uriType);

        switch (uriType) {
            case VERSION:
                //is this URI for a generic query?
                break;
            case TASK_ID:
                //does the URI match a specific pattern, and if so, we want to have a specific query
                //against a primary key.
                selection = TasksContract.Task.ID + " = ? ";
                id = uri.getLastPathSegment();
                selectionArgs = new String[]{id};
                Log.d(TAG, "looking for ID: " + id);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);

        }

        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = queryBuilder.query(
                db,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        Log.d(TAG, "getting Type...");
        switch ((sUriMatcher.match(uri))) {
            case VERSION:
                return TasksContract.Task.CONTENT_TYPE;
            case TASK_ID:
                return TasksContract.Task.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.d(TAG, "inserting into the DB...");

        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.d(TAG, "Deleting from the DB...");
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        Log.d(TAG, "updating the DB");
        return 0;
    }
}
