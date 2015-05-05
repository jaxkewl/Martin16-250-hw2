package com.marshong.martin16_250_hw2.ui;

import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.SimpleCursorAdapter;

import com.marshong.martin16_250_hw2.R;
import com.marshong.martin16_250_hw2.data.TasksContract;

public class DeleteTaskActivity extends ActionBarActivity {

    private final static String TAG = DeleteTaskActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_task);
        Log.d(TAG, "onCreate...");

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new DeleteTaskFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_delete_task, menu);
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
     * A placeholder fragment containing a simple view.
     */
    public static class DeleteTaskFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

        private final static String TAG = DeleteTaskFragment.class.getSimpleName();
        private SimpleCursorAdapter mAdapter;

        public DeleteTaskFragment() {
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            Log.d(TAG, "onViewCreated...");
            super.onViewCreated(view, savedInstanceState);
            getListView().setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            Log.d(TAG, "onOptionsItemSelected...");

            int id = item.getItemId();

            //noinspection SimplifiableIfStatement
            switch (id) {

                case R.id.action_delete:

                    Intent delIntent = new Intent(getActivity(), DeleteTaskActivity.class);
                    startActivity(delIntent);
                    break;
            }


            return super.onOptionsItemSelected(item);
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


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_delete_task, container, false);
            Log.d(TAG, "onCreateView...");
            init(rootView);

            return rootView;
        }

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
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            mAdapter.swapCursor(data);
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            mAdapter.swapCursor(null);
        }
    }
}
