package com.marshong.martin16_250_hw2.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by martin on 4/30/2015.
 */
public class TaskProvider extends ContentProvider {

    private static final String TAG = TaskProvider.class.getSimpleName();

    private static final int VERSION = 100;
    private static final int TASK_ID = 200;
    private static final int TASK_NAME = 300;

    private static final UriMatcher sUriMatcher = createUriMatcher();

    private static UriMatcher createUriMatcher() {

        Log.d(TAG, "creating URI Matcher using " + TasksContract.CONTENT_AUTHORITY + " " + TasksContract.PATH_TASK);
        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = TasksContract.CONTENT_AUTHORITY;


        //Note: when a URI comes in for a query, the method will determine if the query is for a
        //generic version or if its for a specific task_id. i.e. querying against the primary key.
        //In this case, the URI is setup in class MainActivity.onTaskListSelected
        //Also if there is another table you want to insert into, you'll need another URIMatcher, which
        //matches the TasksContract.PATH_ANOTHERTABLE URI.
        uriMatcher.addURI(authority, TasksContract.PATH_TASK, VERSION);
        uriMatcher.addURI(authority, TasksContract.PATH_TASK + "/#", TASK_ID);

        /*Note: to add another uriMatcher, i.e. task name uri matcher, than you need a special matcher string*/
        //FIXME
        uriMatcher.addURI(authority, TasksContract.PATH_TASK + "/task_name", TASK_NAME);


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

        //GOTCHA: Registering an Observer in content resolver through cursor
        //So after updating something in the DB, call getContext().getContentResolver().notifyChange(insertedId, null);
        //like in method "insert" below
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
        Log.d(TAG, "inserting into the DB..." + values);

        //get a the object to the writeable DB
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int uriType = sUriMatcher.match(uri);
        long rowId;

        switch (uriType) {
            case VERSION:
                rowId = db.insertOrThrow(TasksContract.Task.TABLE_NAME, null, values);
                getContext().getContentResolver().notifyChange(uri, null);
                return ContentUris.withAppendedId(TasksContract.Task.CONTENT_URI, rowId);
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.d(TAG, "Deleting from the DB...");

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int uriType = sUriMatcher.match(uri);
        int deletionCount = 0;

        switch (uriType) {
            case VERSION:
                deletionCount = db.delete(TasksContract.Task.TABLE_NAME, selection, selectionArgs);
                break;
            case TASK_ID:
                String id = uri.getLastPathSegment();
                deletionCount = db.delete(
                        TasksContract.Task.TABLE_NAME,
                        TasksContract.Task.ID + " = " + id +
                                (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), // append selection to query if selection is not empty
                        selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return deletionCount;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        Log.d(TAG, "updating the DB");
        return 0;
    }
}
