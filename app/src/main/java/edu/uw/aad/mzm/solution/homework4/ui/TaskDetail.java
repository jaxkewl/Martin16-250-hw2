package edu.uw.aad.mzm.solution.homework4.ui;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import edu.uw.aad.mzm.solution.homework4.R;

public class TaskDetail extends ActionBarActivity {

    private final static String TAG = TaskDetail.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        Log.d(TAG, "onCreate...");


        Bundle bundle = getIntent().getExtras();
        String chosenTaskID = bundle.getString(TaskDetailFragment.TASK_ID);

        Toast.makeText(this, "Selected Task: " + chosenTaskID, Toast.LENGTH_SHORT).show();

        FragmentManager fm = getFragmentManager();
        Fragment taskDetailFragment = fm.findFragmentById(R.id.task_detail_fragment_container);
        if (null == taskDetailFragment) {
            taskDetailFragment = new TaskDetailFragment();
            fm.beginTransaction().add(R.id.task_detail_fragment_container, taskDetailFragment).commit();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_task_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
