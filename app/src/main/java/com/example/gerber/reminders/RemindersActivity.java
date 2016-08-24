package com.example.gerber.reminders;

import android.app.Dialog;
import android.database.Cursor;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

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
        if (savedInstanceState == null) {
            mDbAdapter.deleteAllReminder();
            insertSomeReminders();
        }
        Cursor cursor = mDbAdapter.fetchAllReminders();
        String[] from = new String[]{ReminderDbAdapter.COL_CONTENT};
        int[] to = new int[]{R.id.row_text};
        mCursorAdapter = new RemindersSimpleCursorAdapter(this, R.layout.reminders_row, cursor, from, to, 0);
        mListView.setAdapter(mCursorAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onReminderItemClick(parent, view, position, id);
            }
        });
    }

    private void insertSomeReminders() {
        mDbAdapter.createReminder("Buy Learn Android Studio", true);
        mDbAdapter.createReminder("Send Dad birthday gift", false);
        mDbAdapter.createReminder("Dinner at the Gage on Friday", false);
        mDbAdapter.createReminder("String squash racket", false);
        mDbAdapter.createReminder("Shovel and salt walkways", false);
        mDbAdapter.createReminder("Prepare Advanced Android syllabus", true);
        mDbAdapter.createReminder("Buy new office chair", false);
        mDbAdapter.createReminder("Call Auto-body shop for quote", false);
        mDbAdapter.createReminder("Renew membership to club", false);
        mDbAdapter.createReminder("Buy new Galaxy Android phone", true);
        mDbAdapter.createReminder("Sell old Android phone - auction", false);
        mDbAdapter.createReminder("Buy new paddles for kayaks", false);
        mDbAdapter.createReminder("Call accountant about tax returns", false);
        mDbAdapter.createReminder("Buy 300,000 shares of Google", false);
        mDbAdapter.createReminder("Call the Dalai Lama back", true);
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

    public void onReminderItemClick(AdapterView<?> parent, View view, final int masterListPosition, long id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(RemindersActivity.this);
        ListView modeListView = new ListView(RemindersActivity.this);
        String[] modes = new String[]{"Edit Reminder", "Delete Reminder"};
        ArrayAdapter<String> modeAdapter = new ArrayAdapter<>(RemindersActivity.this,
                android.R.layout.simple_list_item_1, android.R.id.text1, modes);
        modeListView.setAdapter(modeAdapter);
        builder.setView(modeListView);
        final Dialog dialog = builder.create();
        dialog.show();
        modeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//edit reminder
                if (position == 0) {
                    Toast.makeText(RemindersActivity.this, "edit "
                            + masterListPosition, Toast.LENGTH_SHORT).show();
//delete reminder
                } else {
                    Toast.makeText(RemindersActivity.this, "delete "
                            + masterListPosition, Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });
    }

    private class OnItemClickListener implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//            onReminderItemClick(adapterView, view, position, l);
        }
    }
}
