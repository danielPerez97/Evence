package daniel.perez.qrdialogview;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.TextView;

import java.util.Calendar;

import daniel.perez.core.Utils;

public class CalendarDialog {
    private static final String TAG = "CalendarDialog";
    private Context context;
    private int mMinute, mHour, mDay, mMonth, mYear;
    private TextView newTimeDateString;

    public CalendarDialog(Context context, TextView textView) {
        this.context = context;
        this.newTimeDateString = textView;
    }

    public void timeDialog() {
        final Calendar calendar = Calendar.getInstance();

        mHour = calendar.get(Calendar.HOUR_OF_DAY);
        mMinute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(context, (timePicker, hour, minute) -> {
            mHour = hour;
            mMinute = minute;
            newTimeDateString.setText(mHour + ":" + mMinute);
        }, mHour, mMinute, false);

        timePickerDialog.show();
    }

    public void dateDialog() {
        final Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(context, (datePicker, year, month, day) -> {
            mYear = year;
            mMonth = month + 1;
            mDay = day;
            newTimeDateString.setText(mMonth + "-" + mDay + "-" + mYear);
        }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }
}
