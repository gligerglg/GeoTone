package apps.gligerglg.geotone;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by Gayan Lakshitha on 3/4/2018.
 */

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener{

    String timeObj;
    public interface TimeDialogListener{
        void onFinishDialog(String time);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));

    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        TimeDialogListener timeDialogListener = (TimeDialogListener)getActivity();
        if(i>11)
        {
            if(i!=12) {
                i -= 12;
            }

            timeObj = i + ":" + i1 + " PM";
        }
        else
            timeObj = i + ":" + i1 + " AM";
        timeDialogListener.onFinishDialog(timeObj);
    }
}
