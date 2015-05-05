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
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.marshong.martin16_250_hw2.R;
import com.marshong.martin16_250_hw2.data.TaskDbHelper;
import com.marshong.martin16_250_hw2.data.TasksContract;
import com.marshong.martin16_250_hw2.model.Task;

import java.util.List;

/**
 * Created by martin on 4/28/2015.
 */
public class TaskListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private final static String TAG = TaskListFragment.class.getSimpleName();

    private TaskDbHelper mTaskDbHelper;
    private ListView mListViewTasks;
    private ArrayAdapter<Task> mTaskArrayAdapter;
    private List<Task> tasks;

    private Callbacks mCallbacks = sDummyCallbacks;

    private SimpleCursorAdapter mAdapter;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        //Toast.makeText(getActivity(), "onViewCreated...", Toast.LENGTH_LONG).show();
        Log.d(TAG, "onViewCreated...");
        super.onViewCreated(view, savedInstanceState);

        //GOTCHA: make sure to set choice mode multiple after the view is created
        getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);




        getListView().setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                Log.d(TAG,"onItemCheckedStateChanged...");

                // get the total # of checked items
                final int checkedCount = getListView().getCheckedItemCount();

                //set the title that will appear on the context bar
                mode.setTitle(checkedCount + " Selected");
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }
        });

    }


    private void init(View rootView) {
        Log.d(TAG, "init...");
        //Toast.makeText(getActivity(), "init...", Toast.LENGTH_LONG).show();
        String[] fromFields = new String[]{TasksContract.Task.TASK_NAME};
        int[] toFields = new int[]{android.R.id.text1};

        getLoaderManager().initLoader(0, null, this);

        mAdapter = new SimpleCursorAdapter(
                getActivity(),
                //android.R.layout.simple_expandable_list_item_1,
                android.R.layout.simple_list_item_multiple_choice,
                null,
                fromFields,
                toFields,
                0
        );

        setListAdapter(mAdapter);

    }

/*    private void initOld(View rootView) {
        // Get task list data from DB
        mTaskDbHelper = new TaskDbHelper(getActivity());
        tasks = mTaskDbHelper.getTasks();

        setListAdapter(new ArrayAdapter<Task>(
                getActivity(),
                android.R.layout.simple_list_item_activated_1,
                android.R.id.text1,
                tasks));
    }*/

/*    private void initOld(View rootView) {
        // Get task list data from DB
        mTaskDbHelper = new TaskDbHelper(getActivity());
        tasks = mTaskDbHelper.getTasks();

        // Set up UI ListView
        mListViewTasks = (ListView) rootView.findViewById(R.id.listView);
        mTaskArrayAdapter = new ArrayAdapter<Task>(getActivity(),  // context
                android.R.layout.simple_list_item_multiple_choice,                 // UI layout for the list item, multiple choices
//                android.R.layout.simple_list_item_single_choice,                     // UI layout for the list item, single choice
                tasks);                                                              // objects
        mListViewTasks.setAdapter(mTaskArrayAdapter);
//        mListViewTasks.setChoiceMode(ListView.CHOICE_MODE_SINGLE);                // Use this for single choice
        mListViewTasks.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    }*/


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_task_list, container, false);


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

                Toast.makeText(getActivity(),"Delete is implemented from the Details View",Toast.LENGTH_SHORT).show();

                /*Intent delIntent = new Intent(getActivity(), DeleteTaskActivity.class);
                startActivity(delIntent);*/


                //getListView().getCheckedItemPositions()


                // Delete single task
/*                int position = mListViewTasks.getCheckedItemPosition();
                Task task = mTaskArrayAdapter.getItem(position);
                deleteSingleTask(position, task.getId());*/

                // Delete multiple tasks
/*                ArrayList<Integer> ids = new ArrayList<Integer>();
                SparseBooleanArray checkedItems = mListViewTasks.getCheckedItemPositions();
                if (checkedItems.size() > 0) {
                    for (int i = 0; i < checkedItems.size(); i++) {
                        int position = checkedItems.keyAt(i);
                        Task task = mTaskArrayAdapter.getItem(position);
                        ids.add((int) task.getId());
                    }
                    mTaskDbHelper.deleteTasks(ids);
                }*/
                //refreshUI();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * Delete a single task from db
     *
     * @param pos
     * @param id
     */

    private void deleteSingleTask(int pos, long id) {

/*        if (pos >= 0) {
            int rowsDeleted = mTaskDbHelper.deleteTask(id);
            if (rowsDeleted > 0) {
                refreshUI();
            }
        }*/
        Toast.makeText(getActivity(), "Item Deleted @position #" + pos + " rowId is #" + id, Toast.LENGTH_LONG).show();
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



    /*The following 2 methods need to be overridden so the hosting activity can get a call back
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

    /*The following methods are required by implementing LoaderManager.LoaderCallbacks<Cursor>*/

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
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
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }


    /*The following interface must be defined in this list fragment so the hosting activity can
    implement the methods of this interface*/
    public interface Callbacks {
        void onTaskListSelected(String id);
    }

    private static Callbacks sDummyCallbacks = new Callbacks() {
        @Override
        public void onTaskListSelected(String id) {

        }
    };



    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Log.d(TAG, "onListItemClick: " + position + ", id: " + id);
        super.onListItemClick(l, v, position, id);

        // Notify the active callbacks interface (the activity, if the
        // fragment is attached to one) that an item has been selected.
        //Note: convert long to int is ok here, because more than like we will not have more than
        //~2 billion task IDs.
        mCallbacks.onTaskListSelected(Integer.toString((int) id));
    }
}
