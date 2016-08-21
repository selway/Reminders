package com.example.gerber.reminders;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ListViewCompat;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class RemindersActivity extends AppCompatActivity {
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminders);
        mListView = (ListView) findViewById(R.id.reminders_list_view);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.reminders_row, R.id.row_text, new String[]{"first record", "second record", "third record"});
        mListView.setAdapter(arrayAdapter);
    }
}
