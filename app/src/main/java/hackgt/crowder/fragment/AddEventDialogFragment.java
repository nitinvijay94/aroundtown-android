package hackgt.crowder.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.hackgt.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by hediwang on 15/9/26.
 */
public class AddEventDialogFragment extends DialogFragment {

    private AddEventInterface addEventInterface;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        addEventInterface = (AddEventInterface) activity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_add_event, null);
        final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        final DateFormat timeFormat = new SimpleDateFormat("hh:mm");

        final EditText startDate = ((EditText) view.findViewById(R.id.startDate));
        final EditText startTime = ((EditText) view.findViewById(R.id.startTime));
        final EditText endDate = ((EditText) view.findViewById(R.id.endDate));
        final EditText endTime = ((EditText) view.findViewById(R.id.endTime));

        Calendar current = new GregorianCalendar();
        final int year = current.get(Calendar.YEAR);
        final int month = current.get(Calendar.MONTH);
        final int day = current.get(Calendar.DAY_OF_MONTH);
        final int hour = current.get(Calendar.HOUR);

        Date later = new Date();
        startDate.setText(dateFormat.format(new Date()));
        startTime.setText(timeFormat.format(new Date(year, month, day, (hour + 1), 0)));
        endDate.setText(dateFormat.format(new Date()));
        endTime.setText(timeFormat.format(new Date(year, month, day, (hour + 2), 0)));

        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Date date = new Date(year - 1900, monthOfYear, dayOfMonth);
                        startDate.setText(dateFormat.format(date));
                    }
                }, year, month, day);
                dialog.show();
            }
        });
        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Date date = new Date(year - 1900, monthOfYear, dayOfMonth);
                        endDate.setText(dateFormat.format(date));
                    }
                }, year, month, day);
                dialog.show();
            }
        });
        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog dialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Date date = new Date();
                        date.setHours(hourOfDay);
                        date.setMinutes(minute);
                        date.setSeconds(0);
                        startTime.setText(timeFormat.format(date));
                    }
                }, hour + 1, 0, true);
                dialog.show();
            }
        });
        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog dialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Date date = new Date();
                        date.setHours(hourOfDay);
                        date.setMinutes(minute);
                        date.setSeconds(0);
                        endTime.setText(timeFormat.format(date));
                    }
                }, hour + 2, 0, true);
                dialog.show();
            }
        });
        builder.setView(view)
                .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String nameOfEvent = ((EditText) view.findViewById(R.id.title_event)).getText().toString().trim();
                        String description = ((EditText) view.findViewById(R.id.description)).getText().toString().trim();
                        String address = ((EditText) view.findViewById(R.id.location)).getText().toString().trim();
                        String startDate = ((EditText) view.findViewById(R.id.startDate)).getText().toString().trim() + " " + ((EditText) view.findViewById(R.id.startTime)).getText().toString().trim();
                        String endDate = ((EditText) view.findViewById(R.id.endDate)).getText().toString().trim() + " " + ((EditText) view.findViewById(R.id.endTime)).getText().toString().trim();
                        String tag0 = ((EditText) view.findViewById(R.id.tag_0)).getText().toString().trim();
                        String tag1 = ((EditText) view.findViewById(R.id.tag_1)).getText().toString().trim();
                        String tag2 = ((EditText) view.findViewById(R.id.tag_2)).getText().toString().trim();

                        if (nameOfEvent.length() == 0) {
                            Toast.makeText(getActivity(), "Name cannot be empty", Toast.LENGTH_SHORT).show();
                        } else if (description.length() == 0) {
                            Toast.makeText(getActivity(), "Description cannot be empty", Toast.LENGTH_SHORT).show();
                        } else if (address.length() == 0) {
                            Toast.makeText(getActivity(), "Address cannot be empty", Toast.LENGTH_SHORT).show();
                        } else {
                            addEventInterface.addEvent(nameOfEvent, description, address, startDate, endDate, tag0, tag1, tag2);
                        }

                        dismiss();
                    }
                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });
        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    public interface AddEventInterface {
        public void addEvent(String name, String description, String address, String start, String end, String tag0, String tag1, String tag2);
    }

}
