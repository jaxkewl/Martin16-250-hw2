package edu.uw.aad.mzm.solution.homework4.ui;


import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import edu.uw.aad.mzm.solution.homework4.R;
import edu.uw.aad.mzm.solution.homework4.data.TaskDbHelper;
import edu.uw.aad.mzm.solution.homework4.model.Task;

/**
 * A simple {@link Fragment} subclass.
 */
public class TaskDetailFragment extends Fragment {

    private final static String TAG = TaskDetailFragment.class.getSimpleName();

    public static final String TASK_ID = "TASK_ID";

    private TextView mTextViewTaskName;
    private TextView mTextViewTaskDescr;

    //the following fields are from the DB and the DB object type
    private TaskDbHelper mDbHelper;
    private List<Task> mTasks;
    private String mTaskID;
    private Task mTask;

    public TaskDetailFragment() {
        // Required empty public constructor
    }


    //use this method to get the bundle and convert that to a task
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate called, setting up dbHelper...");

        super.onCreate(savedInstanceState);

        //setup the DB Helper and get all the tasks stored in the DB
        //then get the ID of which task was selected from the extras
        mDbHelper = new TaskDbHelper(getActivity());
        mTasks = mDbHelper.getTasks();

        if (getArguments().containsKey(TASK_ID)) {
            mTaskID = getArguments().getString(TASK_ID);

            int taskID = Integer.parseInt(mTaskID);
            mTask = mTasks.get(taskID);
            Log.d(TAG, "found task: " + mTask);
        }
    }

    //to make the code easier to read, do all the initial setup in here
    //i.e. setting up widgets
    private void init(View rootView) {
        mTextViewTaskDescr = (TextView) rootView.findViewById(R.id.fragment_detail_task_descr);
        mTextViewTaskName = (TextView) rootView.findViewById(R.id.fragment_detail_task_name);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_task_detail, container, false);
        init(rootView);

        return rootView;
    }


}
