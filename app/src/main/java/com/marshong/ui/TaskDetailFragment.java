package com.marshong.ui;


import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.marshong.R;
import com.marshong.data.TaskDbHelper;
import com.marshong.model.Task;

import java.util.List;

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


    /*this method handles getting the arguments when the "extras are attached to the fragments"*/
    private int getTaskId() {

        if (getArguments().containsKey(TASK_ID)) {
            String stringTaskID = getArguments().getString(TASK_ID);
            return Integer.parseInt(stringTaskID);
        }

        return -1;
    }

    //use this method to get the bundle and convert that to a task
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate called, setting up dbHelper...");

        super.onCreate(savedInstanceState);

        //setup the DB Helper and get all the tasks stored in the DB
        //then get the ID of which task was selected from the extras
        mDbHelper = new TaskDbHelper(getActivity());
        //mTasks = mDbHelper.getTasks();

        //UUID uTaskID = (UUID)getActivity().getIntent().getSerializableExtra(TASK_ID);

        mTaskID = getActivity().getIntent().getStringExtra(TASK_ID);
        Bundle bundle = getArguments();

        //if (getArguments().containsKey(TASK_ID)) {
        //    mTaskID = getArguments().getString(TASK_ID);
        if (null != mTaskID) {
            int taskID = Integer.parseInt(mTaskID);
            mTask = mTasks.get(taskID);
        } else if (getArguments().containsKey(TASK_ID)) {
            mTaskID = getArguments().getString(TASK_ID);
            mTask = mTasks.get(Integer.parseInt(mTaskID));
        }
        Log.d(TAG, "found task: " + mTask);
        //}

        Toast.makeText(getActivity(),"Retrieved from Bundle or Args: " + mTask,Toast.LENGTH_SHORT).show();

    }

    //to make the code easier to read, do all the initial setup in here
    //i.e. setting up widgets
    private void init(View rootView) {
        Log.d(TAG, "init() called, setting up view widgets with task: " + mTask);
        mTextViewTaskDescr = (TextView) rootView.findViewById(R.id.fragment_detail_task_descr);
        mTextViewTaskName = (TextView) rootView.findViewById(R.id.fragment_detail_task_name);

        if (null != mTask) {
            mTextViewTaskName.setText(mTask.getName());
            mTextViewTaskDescr.setText(mTask.getDesc());
        }
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
