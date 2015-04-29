package edu.uw.aad.mzm.solution.homework4.ui;

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

import java.util.List;

import edu.uw.aad.mzm.solution.homework4.R;
import edu.uw.aad.mzm.solution.homework4.data.TaskDbHelper;
import edu.uw.aad.mzm.solution.homework4.model.Task;


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

    private void oldWay() {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //determine if we are in Two Pane Mode by checking if an ID exists
        //Two Pane Mode = android devices with large real estate.

        //-sw600dp qualifier, i.e. Use this resource on any device whose smallest dimension
        //is 600dp or greater. sw= smallest width


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


    @Override
    public void onTaskListSelected(String id) {
        //GOTCHA: implement this method that was defined in the TaskListFragment interface.
        //when an item from the task list is selected, this method is how it is handled.


        //The way this method will handle master/detail list is first determine if we are in
        //single pane or dual pane mode


        if (mTwoPaneMode) {
            Log.d(TAG, "Dual Pane Mode...");
        } else {
            //single pane mode
            //simply start a new activity, the way we always done, bundling in the ID of what was selected

            Log.d(TAG, "Single Pane Mode...");

            Intent intent = new Intent(MainActivity.this, TaskDetailFragment.class);
            intent.putExtra(TaskDetailFragment.TASK_ID, id);
            startActivity(intent);
        }
    }
}
