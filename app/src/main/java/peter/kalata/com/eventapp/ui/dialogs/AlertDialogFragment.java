package peter.kalata.com.eventapp.ui.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import peter.kalata.com.eventapp.R;

public class AlertDialogFragment extends DialogFragment {

    public final static String ARG_MSG= "arg_message";

    public AlertDialogFragment() {
    }

    public static AlertDialogFragment newInstance(String message) {
        AlertDialogFragment frag = new AlertDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_MSG, message);
        frag.setArguments(args);
        return frag;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String message = getArguments().getString(ARG_MSG);
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setMessage(message)
                .setPositiveButton(R.string.ok, (dialog, id) -> dismiss());
        return builder.create();
    }
}
