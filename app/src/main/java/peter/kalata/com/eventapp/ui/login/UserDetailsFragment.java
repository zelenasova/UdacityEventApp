package peter.kalata.com.eventapp.ui.login;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.jakewharton.rxbinding2.view.RxView;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import peter.kalata.com.eventapp.BaseFragment;
import peter.kalata.com.eventapp.R;
import peter.kalata.com.eventapp.data.model.Group;
import peter.kalata.com.eventapp.data.model.User;
import peter.kalata.com.eventapp.ui.main.MainActivity;
import peter.kalata.com.eventapp.utils.TextsUtils;

public class UserDetailsFragment extends BaseFragment {

    @BindView(R.id.et_fullname)
    EditText etFullName;

    @BindView(R.id.et_email)
    EditText etEmail;

    @BindView(R.id.et_phone_number)
    EditText etPhoneNumber;

    @BindView(R.id.btn_continue)
    Button btnContinue;

    private UserDetailsViewModel userDetailsViewModel;
    private final CompositeDisposable composite = new CompositeDisposable();

    public UserDetailsFragment() {}

    public static UserDetailsFragment newInstance() {
        return new UserDetailsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_details, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setContinueButtonListener();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupViewModel();
    }

    private void setupViewModel() {
        userDetailsViewModel = ViewModelProviders.of(this).get(UserDetailsViewModel.class);
        userDetailsViewModel.getError().observe(this, throwable -> showErrorDialog(throwable.getMessage(),
                UserDetailsFragment.class.getName()));
        userDetailsViewModel.getCreateUserSuccess().observe(this, success -> {
            MainActivity.startMainActivity(getActivity());
            requireActivity().finish();
        });
    }

    private boolean isFormValid(String fullName, String email, String phoneNumber) {
        boolean isValid = true;

        if (TextUtils.isEmpty(fullName)) {
            isValid = false;
            etFullName.setError(getResources().getString(R.string.error_empty_field));
        }

        if (!TextsUtils.isEmailValid(email)) {
            isValid = false;
            etEmail.setError(getResources().getString(R.string.error_email));
        }

        if (TextUtils.isEmpty(phoneNumber) || phoneNumber.length() < 10) {
            isValid = false;
            etPhoneNumber.setError(getResources().getString(R.string.error_phone));
        }

        return isValid;
    }

    private void setContinueButtonListener() {
        composite.add(RxView.clicks(btnContinue)
                .debounce(300, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aVoid -> {
                    String fullName = etFullName.getText().toString().trim();
                    String email = etEmail.getText().toString().trim();
                    String phoneNumber = etPhoneNumber.getText().toString().trim();
                    if (isFormValid(fullName, email, phoneNumber)) {
                        User user = new User(fullName, email, phoneNumber);
                        userDetailsViewModel.createUser(user);
                    }
                })
        );
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        composite.clear();
    }

}
