package com.marshong.martin16_250_hw2.ui;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.marshong.martin16_250_hw2.R;
import com.marshong.martin16_250_hw2.data.TasksContract;


/**
 * Created by Margaret on 3/7/2015
 * Android 210 - Winter 2015 - Homework 4 Solution
 * <p/>
 * 1. Create a main screen to show list of tasks
 * 2. There are two menu items on ActionBar: Add & Delete
 * 3. Click on Add to go to a screen for adding a new task
 * - Create AddTaskActivity.java for taking user input
 * - Clicking on OK button in AddTaskActivity inserts a task to DB
 * - Back to the main screen to show list of tasks
 * 4. The ListView is set to select multiple choices or a single choice (code commented out)
 * 5. Click on Delete to delete the selected task(s)
 */
public class MainActivity extends ActionBarActivity implements TaskListFragment.Callbacks { //Callbacks is used for Master/Detail fucntionality, letting the hosting activity take care of list item clicks

    private final static String TAG = MainActivity.class.getSimpleName();

    private boolean mTwoPaneMode = false;   //class variable that gets set onCreate.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //determine if we are in Two Pane Mode by checking if an ID exists
        //Two Pane Mode = android devices with large real estate.

        //-sw600dp qualifier, i.e. Use this resource on any device whose smallest dimension
        //is 600dp or greater. sw= smallest width

        //check if the detail fragment container exists to determine if the user
        //is using a larger screen size
        if (null == findViewById(R.id.sw600_layout_task_detail)) {
            mTwoPaneMode = false;
        } else {
            mTwoPaneMode = true;
        }

        //all this method is doing is calling up the fragment manager
        //to start up the fragment to handle the task list
        FragmentManager fm = getFragmentManager();
        Fragment tlFragment = fm.findFragmentById(R.id.task_list_fragment_container);
        if (null == tlFragment) {
            tlFragment = new TaskListFragment();
            fm.beginTransaction().add(R.id.task_list_fragment_container, tlFragment).commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "MainActivity onCreateOptionsMenu");

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "MainActivity onOptionsItemSelected");
        return super.onOptionsItemSelected(item);
    }


    /*This is the callback method that this hosting activity needs to be responsible for.
    The fragment passes the responsibility to this Activity via the Callback interface this
    activity implemented. Used in master/detail list implementation*/
    @Override
    public void onTaskListSelected(String id) {
        Log.d(TAG, "onTaskListSelected: " + id);
        //GOTCHA: implement this method that was defined in the TaskListFragment interface.
        //when an item from the task list is selected, this method is how it is handled.


        //The way this method will handle master/detail list is first determine if we are in
        //single pane or dual pane mode


        if (mTwoPaneMode) {
            Log.d(TAG, "Dual Pane Mode...");

            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a fragment transaction.
            //Note: we are not starting a new activity, just simply replacing a fragment container
            //with a new fragment.

            //in dual pane mode, we are attaching a bundle as part of the fragment's argument,
            //which is different than the way we are passing extras for single pane. I am doing it this way
            //to show variety.
            Bundle bundle = new Bundle();
            bundle.putString(TaskDetailFragment.TASK_ID, id);

            Uri uri = Uri.parse(TasksContract.Task.CONTENT_URI + "/" + id);
            Log.d(TAG, "putting bundle: " + uri);
            bundle.putParcelable("uri", uri);

            TaskDetailFragment detailFragment = new TaskDetailFragment();
            detailFragment.setArguments(bundle);

            //Toast.makeText(this, "Selected Task: " + id, Toast.LENGTH_SHORT).show();

            //since the sw600_layout_task_detail exists, we can replace that with whatever the detailFragment says to replace it with
            getFragmentManager().beginTransaction().replace(R.id.sw600_layout_task_detail, detailFragment).commit();

        } else {
            //single pane mode
            //simply start a new activity, the way we always done, adding in the ID of what was selected

            Log.d(TAG, "Single Pane Mode...");

            //GOTCHA: we are attaching the task_id as part of the intent, which is different than
            //the way we are handling it for dual pane mode. It is part of the intent, not a part of the fragment.
            Intent intent = new Intent(MainActivity.this, TaskDetail.class);
            intent.putExtra(TaskDetailFragment.TASK_ID, id);


            //Note: create a URI with the ID of which task was selected and pass it to the TaskDetail activity.
            //      In the TaskDetail activity, we will retrieve the uri extra and create a bundle so it can be
            //      set in the fragments argument. Just a different way of doing it like the different way it's done in
            //      dual pane mode above.
            Uri uri = Uri.parse(TasksContract.Task.CONTENT_URI + "/" + id);
            Log.d(TAG, "putting extra: " + uri);
            intent.putExtra("uri", uri);


            startActivity(intent);
        }
    }
}




/*
Notes:

Fragments:
All Fragments need a hosting activity.

To create a fragment
1. In the hosting activity, find the fragment ID that's defined in the layout file
2. Instantiate the fragment java file, i.e. a file that extends Fragment or ListFragment
3. Use fragment manager to add or replace the fragment created in 1 and 2.
4. To pass information to fragments, create a bundle, and call setArguments.
5. To pass information to activities using intents, call putExtra
    a. Doing it this way, to access the Extra information from an intent you need to call up to the Activity and then the intent.
6. To call the options menu from the fragment, you need to set this "setHasOptionsMenu(true)"
    a. That way, the fragment can handle onCreateOptionsMenu and onOptionsItemSelected and not let the hosting activity handle it
7. onCreateView is the only method to implement if you want bare minimum for fragments
8. onViewCreated can be used after all the view components have been created


Master/Detail List

1. Create a fragment that extends ListFragment
2. Need to implement 2 methods and 1 interface, onAttach, onDetach, and a Callback interface
    a. In the Callbacks interface, create spec onTaskListSelected which is to be implemented by the hosting activity
    b. we are simply defining an interface defined within this fragment that was created in 1.
3. In the hosting activity, implement the interface defined in the ListFragment created in 1.
    a. Implement the method, onTaskListSelected, since this hosting activity will handle user clicks to determine which task was selected.
4. In the ListFragment class, implement onListItemClick, this is the method that calls Callback, passing in the ID (primary key) of what was clicked.



Split Pane
1. Create a new Layout with a qualifier, i.e. sw600dp or land
2. create a XML file in that directory with a root element of LinearLayout and create two Sub FrameLayouts.
    a. The first FrameLayout (List) should have ID similar to the fragment container of the single pane List FrameLayout.
    b. The second FrameLayout (Detail) should have ID similar to the fragment container fo the single pane Detail FrameLayout.
3. In the hosting activity, we need to determine if we are in two pane mode, by checking if either the IDs from 2 exist in R.id
    a. set a boolean if we are in two pane mode, so the entire class knows.
4. Regardless if we are in single or dual pane, the List Fragment container should be populated using Fragment Manager (same old way), since we used the same name from 2, it's abstracted for us.
5. In the callbacks method, from the master/detail list, we need to populate the correct layout depending if we are in two pane mode.
    a. if in single pane mode, start a new activity using the detail fragment container. Same old way
    b. if in dual pane, we don't start a new activity since we are already in the activity hosting two fragments, we just need to replace the detail fragment with new information.
       Use getFragmentManager().beginTransaction().replace to replace the detail fragment container with the new info.



Content Provider
1. The ContentProvider must be registered in the manifest. This name must match the name defined in the Contract.
2. All accessing of the database will be handled via a wrapper class called a Content Provider.
    a. Content Resolver and Cursor Loader allows you to access data from a ContentProvider asynchronously
    b. The ContentResolver does not directly invoke the ContentProviders query method directly.
       The CR's query method is invoked, which parses the URI, determines which CP to invoke, and
       then calls the CP's query method.
3. Remove insert/queries/deletes from the DB Helper and move them into the ContentProvider Class.
4. Create a class TaskProvider that extends ContentProvider. You will be forced to implement certain methods.
    a. onCreate, query, getType, insert, delete, update
5. TaskProvider created in 4, holds a DBHelper field to be used.
6. The CP will provide the uriMatcher which contains the URI needed to access the information.
7. The DB Helper class should only have onCreate and onUpgrade methods.
8. You must register an observer in the query method in the ContentProvider so when changes are made to the
   underlying DB using a ContentResolver, you can notify the ContentProvider that data has changed to requery.
    a. cursor.setNotificationUri
9. We marry the availability of the ContentProvider to the request of the CursorLoader using URIs. The CP advertises
   the URI when the CP gets instantiated, sUriMatcher. The CursorLoader uses AndroidContract.Version.CONTENT_URI,
   in the onCreateLoader method in TaskListFragment to call a specific URI to get a specific service.


Cursor Loader
1. CursorLoader is only used to query the ContentProvider using a ContentResolver. You can not use a
   CursorLoader to insert or delete. You must use a ContentResolver.
2. Implement, onCreateLoader, onLoadFinished, and onLoaderReset


ContentResolver.
1. This class is used to insert or delete from the DB.
2. call getContentResolver.insert, update, or delete.
3. when calling getContentResolver.insert(), it needs, as an argument, the Content URI to know where to insert or delete.
   a. Create ContentValues object to hold what you want to insert.

Loaders:
-There are 3 types of built in loaders. Loader, AsyncTaskLoader, CursorLoader.
-Loader is the base class and not very useful on its owns. It defines the API
that the LoaderManager uses to communicate with all loaders.
-All communication with loaders is handled by the LoaderManager, hence Loaders
retains their existing cursor data across the activity instance. (for example when
it is restarted due to a configuration change i.e. rotation), thus saving the cursor from re-queries
-A LoaderManager is responsible for managing one or more Loaders associated with an Activity or Fragment
-The LoaderManager manages the CursorLoader across the Activity lifecycle using LoaderCallbacks interface, which
need 3 methods implemented. onCreateLoader, onLoadFinished, onLoaderReset







*/
