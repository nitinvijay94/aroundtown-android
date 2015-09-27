package hackgt.crowder.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hackgt.R;

/**
 * Created by hediwang on 15/9/27.
 */
public class AddCommentDiaglogFragment extends DialogFragment {

    AddCommentInterface addCommentInterface;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_comment_dialog, null, false);
        final EditText titleView = (EditText) view.findViewById(R.id.title);
        final EditText commentView = (EditText) view.findViewById(R.id.comment);
        builder.setView(view);
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        }).setPositiveButton(getString(R.string.add_comment), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String title = titleView.getText().toString().trim();
                String comment = commentView.getText().toString().trim();
                if (title.length() == 0) {
                    Toast.makeText(getActivity(), "Title should not be empty", Toast.LENGTH_SHORT).show();
                } else if (comment.length() == 0) {
                    Toast.makeText(getActivity(), "Comment should not be empty", Toast.LENGTH_SHORT).show();
                } else {
                    addCommentInterface.addComment(title, comment);
                }
            }
        });
        return builder.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        addCommentInterface = (AddCommentInterface) activity;

    }

    public interface AddCommentInterface {
        public void addComment(String title, String comment);
    }
}
