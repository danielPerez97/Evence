package projects.csce.evence.view.ui;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

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

        TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                mHour = hour;
                mMinute = minute;
                newTimeDateString.setText( mHour + ":" + mMinute);
            }
        }, mHour, mMinute, false);


        timePickerDialog.show();

    }

    public void dateDialog() {
        final Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                mYear = year;
                mMonth = month + 1;
                mDay = day;

                newTimeDateString.setText(mMonth + "/" + mDay + "/" + mYear);
            }
        }, mYear, mMonth, mDay);

        datePickerDialog.show();
    }
}
