package com.marshong.martin16_250_hw2.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 GOTCHA: Make sure the CONTENT_AUTHORITY name and the name defined in the manifest are the same.
 1. Add in CONTENT_AUTHORITY, PATH_TASK, BASE_CONTENT_URI,


 */
public class TasksContract {

    // Name of the Content Provider, use package name by convention so that it's unique on device
    // kinda like domain name.
    public static final String CONTENT_AUTHORITY = "com.marshong.martin16_250_hw2";

    // A path that points to the task table.
    //Note: for multiple tables, you'll need another PATH, i.e. PATH_ANOTHERTABLE
    public static final String PATH_TASK = "task";

    // Construct the Base Content Uri
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String DATABASE_NAME = "tasks";

    /**
     * Define the Task table
     */
    public static final class Task implements BaseColumns {

        // Content Uri = Content Authority + Path
        //kinda like URL
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_TASK).build();

        // Use MIME type prefix android.cursor.dir/ for returning multiple items
        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/com.marshong.martin16_250_hw2.tasks";

        // Use MIME type prefix android.cursor.item/ for returning a single item
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/com.marshong.martin16_250_hw2.task";

        // Define table name
        public static final String TABLE_NAME = "task";

        // Define table columns
        public static final String ID = BaseColumns._ID;
        public static final String TASK_NAME = "task_name";
        public static final String TASK_DESC = "task_desc";

        // Define projection for Task table
        public static final String[] PROJECTION = new String[] {
                /*0*/ TasksContract.Task.ID,
                /*1*/ TasksContract.Task.TASK_NAME,
                /*2*/ TasksContract.Task.TASK_DESC
        };
    }

}
