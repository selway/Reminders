package com.example.gerber.reminders;

import android.database.Cursor;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by Sel on 2016/8/21.
 */
public class RemindersActivity extends AppCompatActivity {
    private ListView mListView;
    private ReminderDbAdapter mDbAdapter;
    private RemindersSimpleCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminders);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //设置图标
//       toolbar.setLogo(R.drawable.ic_launcher);
        //标题
//        toolbar.setTitle(R.string.app_name);
        //副标题
//         toolbar.setSubtitle("Sub title");
        setSupportActionBar(toolbar);
        mListView = (ListView) findViewById(R.id.reminders_list_view);
//        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.reminders_row, R.id.row_text, new String[]{"first record", "second record", "third record"});
//        mListView.setAdapter(arrayAdapter);
        mListView.setDivider(null);
        mDbAdapter = new ReminderDbAdapter(this);
        mDbAdapter.open();
        Cursor cursor = mDbAdapter.fetchAllReminders();
        String[] from = new String[]{ReminderDbAdapter.COL_CONTENT};
        int[] to = new int[]{R.id.row_text};
        mCursorAdapter = new RemindersSimpleCursorAdapter(this, R.layout.reminders_row, cursor, from, to, 0);
        mListView.setAdapter(mCursorAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_reminders, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_new:
                //create new Reminder
                Log.d(getLocalClassName(), "create new Reminder");
                return true;
            case R.id.action_exit:
                finish();
                return true;
            default:
                return false;
        }
    }
}
