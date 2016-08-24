package com.example.gerber.reminders;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Sel on 2016/8/24.
 */
public class RemindersSimpleCursorAdapter extends SimpleCursorAdapter {

    public RemindersSimpleCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return super.newView(context, cursor, parent);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        super.bindView(view, context, cursor);
        ViewHolder holder = (ViewHolder) view.getTag();
        if (holder == null) {
            holder = new ViewHolder();
            holder.collmp = cursor.getColumnIndexOrThrow(ReminderDbAdapter.COL_IMPORTANT);
            holder.listTab = view.findViewById(R.id.row_tab);
            view.setTag(holder);
        }

        if (cursor.getInt(holder.collmp) > 0)
            holder.listTab.setBackgroundColor(ContextCompat.getColor(context,R.color.orange));
         else
            holder.listTab.setBackgroundColor(ContextCompat.getColor(context,R.color.green));
    }

    private static class ViewHolder {
        int collmp;
        View listTab;
    }
}
