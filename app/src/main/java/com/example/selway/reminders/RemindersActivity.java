package com.example.selway.reminders;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.database.Cursor;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Sel on 2016/8/21.
 */
public class RemindersActivity extends AppCompatActivity {
    private static final String SHARE_APP_TAG = "ReminderShare";
    private ListView mListView;
    private ReminderDbAdapter mDbAdapter;
    private RemindersSimpleCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminders);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //设置图标
//       toolbar.setLogo(R.mipmap.ic_launcher);
        toolbar.setNavigationIcon(R.mipmap.ic_launcher);
        //标题
        toolbar.setTitle(R.string.app_name);
        //副标题
//         toolbar.setSubtitle("Sub title");
        setSupportActionBar(toolbar);
        mListView = (ListView) findViewById(R.id.reminders_list_view);
//        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.reminders_row, R.id.row_text, new String[]{"first record", "second record", "third record"});
//        mListView.setAdapter(arrayAdapter);
        mListView.setDivider(null);
        mDbAdapter = new ReminderDbAdapter(this);
        mDbAdapter.open();
        SharedPreferences setting = getSharedPreferences(SHARE_APP_TAG, 0);
        Boolean user_first = setting.getBoolean("IS_FIRST_RUN", true);
        if (user_first) {//第一次
            setting.edit().putBoolean("IS_FIRST_RUN", false).commit();
            mDbAdapter.deleteAllReminder();
            insertSomeReminders();
        }
//        if (savedInstanceState == null) {
//            mDbAdapter.deleteAllReminder();
//            insertSomeReminders();
//        }
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
            mListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

                @TargetApi(Build.VERSION_CODES.HONEYCOMB)
                @Override
                public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                    MenuInflater inflater = actionMode.getMenuInflater();
                    inflater.inflate(R.menu.cam_menu, menu);
                    return true;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                    return false;
                }

                @TargetApi(Build.VERSION_CODES.HONEYCOMB)
                @Override
                public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case R.id.menu_item_delete_reminder:
                            for (int nC = mCursorAdapter.getCount() - 1; nC >= 0; nC--) {
                                if (mListView.isItemChecked(nC)) {
                                    mDbAdapter.deleteReminder(getIdFromPosition(nC));
                                }
                            }
                            actionMode.finish();
                            mCursorAdapter.changeCursor(mDbAdapter.fetchAllReminders());
                            return true;
                    }
                    return false;
                }

                @Override
                public void onDestroyActionMode(ActionMode actionMode) {

                }

                @Override
                public void onItemCheckedStateChanged(ActionMode actionMode, int i, long l, boolean b) {

                }
            });
        }
    }

    private int getIdFromPosition(int nC) {
        return (int) mCursorAdapter.getItemId(nC);
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
                    int nId = getIdFromPosition(masterListPosition);
                    Reminder reminder = mDbAdapter.fetchReminderById(nId);
                    fireCustomDialog(reminder);
//delete reminder
                } else {
                    mDbAdapter.deleteReminder(getIdFromPosition(masterListPosition));
                    mCursorAdapter.changeCursor(mDbAdapter.fetchAllReminders());
                }
                dialog.dismiss();
            }
        });
    }

    private void fireCustomDialog(final Reminder reminder) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_custom);
        TextView titleView = (TextView) dialog.findViewById(R.id.custom_title);
        final EditText editCustom = (EditText) dialog.findViewById(R.id.custom_edit_reminder);
        Button commitButton = (Button) dialog.findViewById((R.id.custom_button_commit));
        final CheckBox checkBox = (CheckBox) dialog.findViewById(R.id.custom_check_box);
        LinearLayout rootLayout = (LinearLayout) dialog.findViewById(R.id.custom_root_layout);
        final boolean isEditOperation = (reminder != null);
        if (isEditOperation) {
            titleView.setText(R.string.reminder_edit);
            checkBox.setChecked(reminder.getiImportant() == 1);
            editCustom.setText(reminder.getContent());
            rootLayout.setBackgroundColor(ActivityCompat.getColor(this, R.color.blue));
        }
        commitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String reminderTest = editCustom.getText().toString();
                if (isEditOperation) {
                    Reminder reminderEdit = new Reminder(reminder.getId(), reminderTest, checkBox.isChecked() ? 1 : 0);
                    mDbAdapter.updateReminder(reminderEdit);
                } else {
                    mDbAdapter.createReminder(reminderTest, checkBox.isChecked());
                }
                mCursorAdapter.changeCursor(mDbAdapter.fetchAllReminders());
                dialog.dismiss();
            }
        });

        Button cancelButton = (Button) dialog.findViewById(R.id.custom_button_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
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
                fireCustomDialog(null);
                return true;
            case R.id.action_exit:
                finish();
                return true;
            default:
                return false;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private class OnItemClickListener implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//            onReminderItemClick(adapterView, view, position, l);
        }
    }
}
