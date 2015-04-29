package edu.uw.aad.mzm.solution.homework4.ui;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import edu.uw.aad.mzm.solution.homework4.R;
import edu.uw.aad.mzm.solution.homework4.data.TaskDbHelper;
import edu.uw.aad.mzm.solution.homework4.model.Task;

/**
 * Created by martin on 4/28/2015.
 */
public class TaskListFragment extends ListFragment {

    private final static String TAG = TaskListFragment.class.getSimpleName();

    private TaskDbHelper mTaskDbHelper;
    private ListView mListViewTasks;
    private ArrayAdapter<Task> mTaskArrayAdapter;
    private List<Task> tasks;


    private void init(View rootView) {
        // Get task list data from DB
        mTaskDbHelper = new TaskDbHelper(getActivity());
        tasks = mTaskDbHelper.getTasks();

        // Set up UI ListView
        mListViewTasks = (ListView) rootView.findViewById(R.id.listViewTasks);
        mTaskArrayAdapter = new ArrayAdapter<Task>(getActivity(),  // context
                android.R.layout.simple_list_item_multiple_choice,                 // UI layout for the list item, multiple choices
//                android.R.layout.simple_list_item_single_choice,                     // UI layout for the list item, single choice
                tasks);                                                              // objects
        mListViewTasks.setAdapter(mTaskArrayAdapter);
//        mListViewTasks.setChoiceMode(ListView.CHOICE_MODE_SINGLE);                // Use this for single choice
        mListViewTasks.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    }


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
        refreshUI();
    }


    /**
     * A very ugly way to refresh the UI
     */
    private void refreshUI() {

        tasks = mTaskDbHelper.getTasks();           // Get all the tasks from db
        mTaskArrayAdapter.clear();                  // Clear the ArrayAdapter
        mTaskArrayAdapter.addAll(tasks);            // Add all tasks
        mListViewTasks.clearChoices();              // Clear all selections
        mTaskArrayAdapter.notifyDataSetChanged();
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
                // Delete single task
/*                int position = mListViewTasks.getCheckedItemPosition();
                Task task = mTaskArrayAdapter.getItem(position);
                deleteSingleTask(position, task.getId());*/

                // Delete multiple tasks
                ArrayList<Integer> ids = new ArrayList<Integer>();
                SparseBooleanArray checkedItems = mListViewTasks.getCheckedItemPositions();
                if (checkedItems.size() > 0) {
                    for (int i = 0; i < checkedItems.size(); i++) {
                        int position = checkedItems.keyAt(i);
                        Task task = mTaskArrayAdapter.getItem(position);
                        ids.add((int) task.getId());
                    }
                    mTaskDbHelper.deleteTasks(ids);
                }
                refreshUI();
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

        if (pos >= 0) {
            int rowsDeleted = mTaskDbHelper.deleteTask(id);
            if (rowsDeleted > 0) {
                refreshUI();
            }
        }
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
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /*The following interface must be defined in this list fragment so the hosting activity can
    implement the methods of this interface*/
    public interface Callbacks {
        void onTaskListSelected(String id);
    }
}
