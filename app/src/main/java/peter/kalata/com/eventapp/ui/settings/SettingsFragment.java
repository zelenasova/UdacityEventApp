package peter.kalata.com.eventapp.ui.settings;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import peter.kalata.com.eventapp.BaseFragment;
import peter.kalata.com.eventapp.R;
import peter.kalata.com.eventapp.data.model.Event;
import peter.kalata.com.eventapp.data.model.Event.EventType;
import peter.kalata.com.eventapp.data.model.User;
import peter.kalata.com.eventapp.ui.create_event.CreateEventViewModel;
import peter.kalata.com.eventapp.ui.dialogs.EditTextDialogFragment;
import peter.kalata.com.eventapp.ui.login.LoginActivity;
import peter.kalata.com.eventapp.ui.login.UserDetailsViewModel;
import peter.kalata.com.eventapp.utils.DateTimeUtils;
import peter.kalata.com.eventapp.utils.SharedPrefsUtils;

public class SettingsFragment extends BaseFragment implements EditTextDialogFragment.DialogListener {

    @BindView(R.id.tv_name)
    TextView tvName;

    @BindView(R.id.tv_mail)
    TextView tvMail;

    @BindView(R.id.tv_phone_number)
    TextView tvPhoneNumber;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private static final String TAG = SettingsFragment.class.getName();

    private static final int  NAME_REQUEST_CODE = 100;
    private static final int  MAIL_REQUEST_CODE = 101;
    private static final int  PHONE_REQUEST_CODE = 102;

    private SettingsViewModel settingsViewModel;
    private User currentUser;

    public SettingsFragment() {}

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupToolBar();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupViewModel();
    }

    private void setupToolBar() {
        toolbar.setTitle(getString(R.string.settings));
        ((AppCompatActivity)requireActivity()).setSupportActionBar(toolbar);
    }

    private void setupViewModel() {
        settingsViewModel = ViewModelProviders.of(this).get(SettingsViewModel.class);
        settingsViewModel.getError().observe(this, throwable -> showErrorDialog(throwable.getMessage(),
                SettingsFragment.class.getName()));
        settingsViewModel.getLeaveGroupSuccess().observe(this, success -> {
            LoginActivity.startLoginActivity(requireContext());
            requireActivity().finish();
        });
        settingsViewModel.getCurrentUser().observe(this, user -> {
            currentUser = user;
            tvName.setText(user.getFullname());
            tvMail.setText(user.getEmail());
            tvPhoneNumber.setText(user.getPhoneNumber());
        });
    }

    private void showEditTextDialog(String defaultText, int requestCode) {
        EditTextDialogFragment.newInstance(defaultText, requestCode).show(getChildFragmentManager(), TAG);
    }

    @OnClick(R.id.iv_edit_name)
    void onEditNameClick(View view) {
        showEditTextDialog(tvName.getText().toString(), NAME_REQUEST_CODE);
    }

    @OnClick(R.id.iv_edit_mail)
    void onEditMailClick(View view) {
        showEditTextDialog(tvMail.getText().toString(), MAIL_REQUEST_CODE);
    }

    @OnClick(R.id.iv_edit_phone)
    void onEditPhoneClick(View view) {
        showEditTextDialog(tvPhoneNumber.getText().toString(), PHONE_REQUEST_CODE);
    }

    @Override
    public void onPositiveButtonClicked(String updatedText, int requestCode) {
        switch (requestCode) {
            case NAME_REQUEST_CODE: {
                currentUser.setFullname(updatedText);
                settingsViewModel.updateUser(currentUser);
                break;
            }
            case MAIL_REQUEST_CODE: {
                currentUser.setEmail(updatedText);
                settingsViewModel.updateUser(currentUser);
                break;
            }
            case PHONE_REQUEST_CODE: {
                currentUser.setPhoneNumber(updatedText);
                settingsViewModel.updateUser(currentUser);
                break;
            }
        }
    }

    @OnClick(R.id.btn_leave_group)
    void onLeaveGroupClick(View view) {
        settingsViewModel.leaveGroup();
    }

}
