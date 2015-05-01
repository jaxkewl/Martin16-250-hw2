package com.marshong.ui;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.marshong.R;
import com.marshong.data.TaskDbHelper;
import com.marshong.model.Task;

import java.util.List;


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
public class MainActivity extends ActionBarActivity implements TaskListFragment.Callbacks {

    private final static String TAG = MainActivity.class.getSimpleName();

    private TaskDbHelper mTaskDbHelper;
    private ListView mListViewTasks;
    private ArrayAdapter<Task> mTaskArrayAdapter;
    private List<Task> tasks;

    private boolean mTwoPaneMode = false;

    /*private void oldWay() {
        // Get task list data from DB
        mTaskDbHelper = new TaskDbHelper(this);
        tasks = mTaskDbHelper.getTasks();

        // Set up UI ListView
        mListViewTasks = (ListView) findViewById(R.id.listViewTasks);
        mTaskArrayAdapter = new ArrayAdapter<Task>(this,  // context
                android.R.layout.simple_list_item_multiple_choice,                 // UI layout for the list item, multiple choices
//                android.R.layout.simple_list_item_single_choice,                     // UI layout for the list item, single choice
                tasks);                                                              // objects
        mListViewTasks.setAdapter(mTaskArrayAdapter);
//        mListViewTasks.setChoiceMode(ListView.CHOICE_MODE_SINGLE);                // Use this for single choice
        mListViewTasks.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    }
*/
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

        //all this MainActivity is doing is calling up the fragment manager
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
        //refreshUI();
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


    /*This is the callback method that this hostig activity needs to be responsible for.
    The fragment passes the responsibility to this Activity via the Callback interface this
    activity implmented.*/
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

            //in dual pane mode, we are attaching the bundle as part of the fragment's argument,
            //which is different than the way we are passing extras for single pane.
            Bundle bundle = new Bundle();
            bundle.putString(TaskDetailFragment.TASK_ID, id);
            TaskDetailFragment detailFragment = new TaskDetailFragment();
            detailFragment.setArguments(bundle);

            Toast.makeText(this, "Selected Task: " + id, Toast.LENGTH_SHORT).show();

            getFragmentManager().beginTransaction().replace(R.id.sw600_layout_task_detail, detailFragment).commit();


        } else {
            //single pane mode
            //simply start a new activity, the way we always done, bundling in the ID of what was selected

            Log.d(TAG, "Single Pane Mode...");

            //GOTCHA: we are attaching the task_id as part of the intent, which is different than
            //the way we are handling it for dual pane mode
            Intent intent = new Intent(MainActivity.this, TaskDetail.class);
            intent.putExtra(TaskDetailFragment.TASK_ID, id);
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
5. To pass information to activities, call putExtra
    a. Doing it this way, to access the Extra information from a fragment you need to call up to the Activity and then the intent.
6. To call the options menu from the fragment, you need to set this setHasOptionsMenu(true)
    a. That way, the fragment can handle onCreateOptionsMenu and onOptionsItemSelected
7. onCreateView is the only method to implement if you want bare minimum for fragments


Master/Detail List

1. Create a fragment that extends ListFragment
2. Need to implement minimum of 2 methods and 2 interface, onAttach, onDetach, and a Callback interface
    a. In the Callbacks interface, create spec onTaskListSelected which is to be implemented by the hosting activity
3. In the hosting activity, implement the interface defined in the ListFragment created in 1.
    a. Implement the method, onTaskListSelected, since this hosting activity will handle user clicks to determine which task was selected.
4. In the ListFragment class, implement onListItemClick, this is the method that calls Callback, passing in the ID of what was clicked.



Split Pane
1. Create a new Layout with a qualifier, i.e. sw600dp or land
2. create a XML file in that directory with a root element of LinearLayout and create two Sub FrameLayouts.
    a. The first FrameLayout (List) should have ID similar to the fragment container of the single pane List FrameLayout.
    b. The second FrameLayout (Detail) should have ID similar to the fragment container fo the single pane Detail FrameLayout.
3. In the hosting activity, we need to determine if we are in two pane mode, by checking if either the IDs from 2 exist in R.id
    a. set a boolean if we are in two pane mode, so the entire class knows.
4. Regardless if we are in single or dual pane, the List Fragment container should be populated using Fragment Manager (same old way), since we used the same name from 2, its abstracted for us.
5. In the callbacks method, from the master/detail list, we need to populate the correct layout depending if we are in two pane mode.
    a. if in single pane mode, start a new activity using the detail fragment container.
    b. if in dual pane, we don't start a new activity since we are already in the activity hosting two fragments, we just need to replace the detail fragment with new information.
       Use getFragmentManager().beginTransaction().replace to replace the detail fragment container with the new info.



Content Provider
1. The ContentProvider must be registered in the manifest.
1. All accessing of the database will be handled via a wrapper class called a Content Provider.
    a. Content Resolver or Cursor Loader allows you to access data from a ContentProvider
    b. The ContentResolver does not directly invoke the ContentProviders query method directly.
       The CR's query method is invoked, which parses the URI, determines which CP to invoke, and
       then calls the CP's query method.
2. Remove insert/queries/deletes from the DB Helper and move them into the ContentProvider Class.
3. Create a class TaskProvider that extends ContentProvider. You will be forced to implement certain methods.
    a. onCreate, query, getType, insert, delete, update
4. ContentProvider created in 3, holds a DBHelper field to be used.
5. The CP will provide the uriMatcher which contains the URI needed to access the information.
6. The DB Helper class should only have onCreate and onUpgrade methods.


Cursor Loader
1.


*/
