package hackgt.crowder.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hackgt.R;

/**
 * Created by hediwang on 15/9/26.
 */
public class AddEventDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_add_event, null);
        builder.setView(view)
                .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String nameOfEvent = ((EditText) view.findViewById(R.id.title_event)).getText().toString();
                        String description = ((EditText) view.findViewById(R.id.description)).getText().toString();
                        String address = ((EditText) view.findViewById(R.id.location)).getText().toString();
                        Toast.makeText(getActivity(), "Event Added Successfully", Toast.LENGTH_SHORT).show();
                        dismiss();
                    }
                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });
        return builder.create();
    }

}
