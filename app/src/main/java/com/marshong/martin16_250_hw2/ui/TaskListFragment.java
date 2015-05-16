package com.marshong.martin16_250_hw2.ui;

import android.app.Activity;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.marshong.martin16_250_hw2.R;
import com.marshong.martin16_250_hw2.data.TasksContract;

/**
 * Created by martin on 4/28/2015.
 */
public class TaskListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> { //LoaderCallbacks inteface is a simple contract the the LoaderManager uses to report data back to the client, implement the 3 required methods below.

    private final static String TAG = TaskListFragment.class.getSimpleName();

    // The loader's unique id. Loader ids are specific to the Activity or
    // Fragment in which they reside.
    private final static int LOADER_ID = 0;

    //the fragment's current callback object, which is notified of list item clicks. used in Master/Detail implementation.
    //different than the callback this class implements.
    private Callbacks mCallbacks = sDummyCallbacks;

    // --------------------------
    //tablet only field variables
    // --------------------------
    //The serialization (saved instance state) Bundle key representing the
    //activated item position. Only used on tablets.
    private static final String STATE_ACTIVATED_POSITION = "activated_position";

    //The current activated item position. Only used on tablets.
    private int mActivatedPosition = ListView.INVALID_POSITION;


    private SimpleCursorAdapter mAdapter;

    //for readability, put all initial actions in here, which is called from onCreateView.
    private void init(View rootView) {
        Log.d(TAG, "init...");
        String[] fromFields = new String[]{TasksContract.Task.TASK_NAME};   //name of the table column

        //Note: to use your own custom layout, the toFields, must be pointing to the name of the
        //ID in of your text view in the custom layout.
        //int[] toFields = new int[]{R.id.textViewPrettyList};
        int[] toFields = new int[]{R.id.textViewTaskNameFancyList}; //points to a view, inside of a layout.xml

        //the loader manager is an abstract class that is associated with an activity or fragment for
        //managing loader instances.

        // "this" (the last argument used in initLoader) is the Activity
        // (which implements the LoaderCallbacks<Cursor>
        // interface) is the callbacks object through which we will interact
        // with the LoaderManager. The LoaderManager uses this object to
        // instantiate the Loader and to notify the client when data is made
        // available/unavailable.
        getLoaderManager().initLoader(LOADER_ID, null, this);

        mAdapter = new SimpleCursorAdapter(
                getActivity(),
                //android.R.layout.simple_expandable_list_item_1,
                //android.R.layout.simple_list_item_multiple_choice,
                R.layout.fancy_list_item,  //Note: use your own custom layout here.
                null,
                fromFields,
                toFields,
                0
        );
        setListAdapter(mAdapter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_task_list, container, false);

        //for readability call this method.
        init(rootView);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        // Start a new or restarts an existing loader
        getLoaderManager().restartLoader(0, null, this);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "TaskListFragment onOptionsItemSelected");

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_add:
                Intent intent = new Intent(getActivity(), AddTaskActivity.class);
                startActivity(intent);
                break;
            case R.id.action_delete:
                Toast.makeText(getActivity(), "Delete is implemented from the Details View", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.d(TAG, "TaskListFragment onCreateOptionsMenu");
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_add_task, menu);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);    //GOTCHA: this needs to be set to true so the fragment's onCreateOptionsMenu will be called

    }




    /*The following 3 methods are required by implementing LoaderManager.LoaderCallbacks<Cursor>*/

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        //Note: this is how you associate a cursor loader with the LoaderManager.
        //Since this method is required, define your CursorLoader here.
        CursorLoader cursorLoader = new CursorLoader(getActivity(),
                TasksContract.Task.CONTENT_URI,
                TasksContract.Task.PROJECTION,
                null,
                null,
                null
        );
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor data) {
        // The asynchronous load is complete and the data
        // is now available for use. Only now can we associate
        // the queried Cursor with the SimpleCursorAdapter.
        mAdapter.swapCursor(data);


        /*// A switch-case is useful when dealing with multiple Loaders/IDs
        switch (loader.getId()) {
            case LOADER_ID:
                // The asynchronous load is complete and the data
                // is now available for use. Only now can we associate
                // the queried Cursor with the SimpleCursorAdapter.
                mAdapter.swapCursor(cursor);
                break;
        }
        // The listview now displays the queried data.*/
    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {
        // For whatever reason, the Loader's data is now unavailable.
        // Remove any references to the old data by replacing it with
        // a null Cursor.
        mAdapter.swapCursor(null);
    }



    /*Master/Detail: The following 2 methods (onAttach, onDetach) need to be overridden so the hosting activity can get a call back
            to handle clicks and call the TaskDetailFragment*/

    @Override
    public void onAttach(Activity activity) {

        super.onAttach(activity);

        // Activities containing this fragment must implement its callbacks.
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }


        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        // Reset the active callbacks interface to the dummy implementation.
        mCallbacks = sDummyCallbacks;
    }

    /*The following interface must be defined in this list fragment so the hosting activity can
        implement the methods of this interface*/
    public interface Callbacks {
        void onTaskListSelected(String id);
    }

    /**
     * A dummy implementation of the {@link Callbacks} interface that does
     * nothing. Used only when this fragment is not attached to an activity.
     * Note: from the Sample Master/Detail and not from cursor loader sample.
     */
    private static Callbacks sDummyCallbacks = new Callbacks() {
        @Override
        public void onTaskListSelected(String id) {

        }
    };


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Log.d(TAG, "onListItemClick: " + position + ", id: " + id); //position is position in the list. id is the primary key
        super.onListItemClick(l, v, position, id);

        // Notify the active callbacks interface (the hosting activity, if the
        // fragment is attached to one) that an item has been selected.
        //Note: convert long to int is ok here, because more than likely we will not have more than
        //~2 billion task IDs.
        mCallbacks.onTaskListSelected(Integer.toString((int) id));  //in our case MainActivity, the hosting activity will be responsible for handling this
    }
}
