package com.marshong.martin16_250_hw2.ui;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.marshong.martin16_250_hw2.R;
import com.marshong.martin16_250_hw2.data.TasksContract;


public class AddTaskActivity extends ActionBarActivity {

    private final static String TAG = AddTaskActivity.class.getSimpleName();

    private EditText mEditTextTaskName;
    private EditText mEditTextTaskDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        mEditTextTaskName = (EditText) findViewById(R.id.editTextTaskName);
        mEditTextTaskDesc = (EditText) findViewById(R.id.editTextTaskDesc);
        Button buttonOk = (Button) findViewById(R.id.buttonOk);

        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertTaskToDb();
            }
        });
    }

    private void insertTaskToDb() {
        //Note: inserting values has nothing to do with using the cursor loader. we need to use
        //the ContentResolver for CRUD, Create/Update/Delete

        String taskName = mEditTextTaskName.getText().toString();
        String taskDesc = mEditTextTaskDesc.getText().toString();
        boolean validTask = true;

        if (0 == taskName.trim().length()) {
            mEditTextTaskName.setError("Enter a valid Task Name");
            validTask = false;
        }

        if (0 == taskDesc.trim().length()) {
            mEditTextTaskDesc.setError("Enter a valid Task Description");
            validTask = false;
        }

        if (validTask) {
            Log.d(TAG, "Adding a task: " + taskName + " " + taskDesc);

            //Note: TaskProvider needs a URI and ContentValues as parameters.

            //First, create ContentValues to add data
            ContentValues contentValues = new ContentValues();
            contentValues.put(TasksContract.Task.TASK_NAME, taskName);
            contentValues.put(TasksContract.Task.TASK_DESC, taskDesc);

            //Second, get the URI to insert a task
            getContentResolver().insert(TasksContract.Task.CONTENT_URI, contentValues);

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_task, menu);
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

    /**
     * Get the user input in EditText
     *
     * @param editText
     * @return
     */
    private String getInput(EditText editText) {
        return editText.getText().toString().trim();
    }

}
