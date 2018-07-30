package peter.kalata.com.eventapp.ui.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import peter.kalata.com.eventapp.R;
import peter.kalata.com.eventapp.ui.login.LoginChangeFragmentListener;

public class EditTextDialogFragment extends DialogFragment {

    @BindView(R.id.et_dialog)
    EditText etDialog;

    public final static String ARG_TEXT= "arg_text";
    public final static String ARG_REQUEST_CODE = "arg_request_code";

    public EditTextDialogFragment() {
    }

    public static EditTextDialogFragment newInstance(String text, int requestCode) {
        EditTextDialogFragment frag = new EditTextDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TEXT, text);
        args.putInt(ARG_REQUEST_CODE, requestCode);
        frag.setArguments(args);
        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_edit, container, false);
        ButterKnife.bind(this, view);
        setCancelable(false);
        if (getArguments() != null) {

        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etDialog.setText(getArguments() != null ? getArguments().getString(ARG_TEXT) : "");
    }

    @OnClick(R.id.btn_cancel)
    void onCancelClick(View view) {
        dismiss();
    }

    @OnClick(R.id.btn_confirm)
    void onConfirmClick(View view) {
        if (getParentFragment() instanceof DialogListener) {
            ((DialogListener) getParentFragment()).onPositiveButtonClicked(etDialog.getText().toString(),
                    getArguments() != null ? getArguments().getInt(ARG_REQUEST_CODE) : 0);
        }
        dismiss();
    }

    public interface DialogListener {

        void onPositiveButtonClicked(String updatedText, int requestCode);

    }

}
