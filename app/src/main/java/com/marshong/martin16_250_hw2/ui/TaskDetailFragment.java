package com.marshong.martin16_250_hw2.ui;


import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.marshong.martin16_250_hw2.R;
import com.marshong.martin16_250_hw2.data.TasksContract;

/**
 * A simple {@link Fragment} subclass.
 */
public class TaskDetailFragment extends Fragment {

    private final static String TAG = TaskDetailFragment.class.getSimpleName();

    //this holds the primary key of the Task we have selected.
    public static final String TASK_ID = "TASK_ID";

    //holds oure view objects so we can output the task and task description
    private TextView mTextViewTaskName;
    private TextView mTextViewTaskDescr;

    private Uri mUri;
    private String mTaskName;
    private String mTaskDescr;
    private String mId;

    public TaskDetailFragment() {
        // Required empty public constructor
    }


    //use this method to get the bundle and convert that to a task
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate called, setting up dbHelper...");
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);    //set this to true so the onOptionsItemSelected can be called for this fragment

        //get the content URI and the primary key from the bundle
        Bundle bundle = getArguments();
        if (null != bundle) {
            mUri = bundle.getParcelable("uri"); // get URI
        }
        mId = bundle.getString(TaskDetailFragment.TASK_ID);
        Log.d(TAG, "Retrieved ID: " + mId);
        Log.d(TAG, "Retrieved URI: " + mUri);


        //use a content receiver to query the DB, we could have used a cursor loader here
        //but since we are only getting 1 item, and for sake of showing variety, we used a CR instead of CL.
        Cursor c = getActivity().getContentResolver().query(mUri,
                TasksContract.Task.PROJECTION,
                null,
                null,
                null);

        if (c.moveToFirst()) {
            mTaskName = c.getString(c.getColumnIndexOrThrow(TasksContract.Task.TASK_NAME));
            mTaskDescr = c.getString(c.getColumnIndexOrThrow(TasksContract.Task.TASK_DESC));
        }
        //close the cursor
        c.close();

        //just some logging and outputting to the screen
        Log.d(TAG, "found task: " + mTaskName + " " + mTaskDescr);
        Toast.makeText(getActivity(), "Retrieved from Bundle or Args: " + mTaskName, Toast.LENGTH_SHORT).show();
    }

    //to make the code easier to read, do all the initial setup in here
    //i.e. setting up widgets
    private void init(View rootView) {
        Log.d(TAG, "init() called, setting up view widgets");

        //setting up the views
        mTextViewTaskDescr = (TextView) rootView.findViewById(R.id.fragment_detail_task_descr);
        mTextViewTaskName = (TextView) rootView.findViewById(R.id.fragment_detail_task_name);

        if (null != mTaskName && null != mTaskDescr) {
            mTextViewTaskName.setText(mTaskName);
            mTextViewTaskDescr.setText(mTaskDescr);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_task_detail, container, false);

        init(rootView); //separate method for readability

        return rootView;
    }

    //use the table's primary key for reference
    private void deleteTask(String id) {
        Log.d(TAG, "Deleting a task: " + id);

        //Note: TaskProvider needs a URI and delete arguments. Similar to the DB Helper class we've used before.

        //Second, get the URI to insert a task
        String whereClause = TasksContract.Task._ID + "=" + id;
        getActivity().getContentResolver().delete(TasksContract.Task.CONTENT_URI, whereClause, null);

        //after deleting, go back to the Master List Page.
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected..." + item.getItemId());

        switch (item.getItemId()) {

            case R.id.action_delete:
                //delete this task using the primary key
                deleteTask(mId);

                Toast.makeText(getActivity(), "Task " + mTaskName + " deleted, ID:" + mId, Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
