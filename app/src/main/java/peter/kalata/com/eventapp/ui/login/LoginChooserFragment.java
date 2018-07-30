package peter.kalata.com.eventapp.ui.login;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
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


public class LoginChooserFragment extends BaseFragment {

    @BindView(R.id.et_group_name)
    EditText etGroupName;

    @BindView(R.id.et_password)
    EditText etPassword;

    @BindView(R.id.btn_login)
    Button btnLogin;

    private LoginChangeFragmentListener listener;
    private LoginChooserViewModel loginChooserViewModel;
    private final CompositeDisposable composite = new CompositeDisposable();

    public LoginChooserFragment() {}

    public static LoginChooserFragment newInstance() {
        return new LoginChooserFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_chooser, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setLoginButtonListener();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupViewModel();
    }

    private void setupViewModel() {
        loginChooserViewModel = ViewModelProviders.of(this).get(LoginChooserViewModel.class);
        loginChooserViewModel.getError().observe(this, throwable -> showErrorDialog(throwable.getMessage(),
                CreateGroupFragment.class.getName()));
        loginChooserViewModel.getLoginSuccess().observe(this, success -> {
            if (success) {
                listener.onChangeToUserDetails();
            } else {
                showErrorDialog(getString(R.string.error_wrong_password), CreateGroupFragment.class.getName());
            }
        });
    }

    private boolean isFormValid(String name, String phone) {
        boolean isValid = true;

        if (TextUtils.isEmpty(name) || name.length() < 5) {
            isValid = false;
            etGroupName.setError(getResources().getString(R.string.error_group_name));
        }

        if (TextUtils.isEmpty(phone) || phone.length() < 5) {
            isValid = false;
            etPassword.setError(getResources().getString(R.string.error_password));
        }
        return isValid;
    }

    private void setLoginButtonListener() {
        composite.add(RxView.clicks(btnLogin)
                .debounce(300, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aVoid -> {
                    String groupName = etGroupName.getText().toString().trim();
                    String password = etPassword.getText().toString().trim();
                    if (isFormValid(groupName, password)) {
                        loginChooserViewModel.login(groupName, password);
                    }

                })
        );
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof LoginChangeFragmentListener) {
            listener = (LoginChangeFragmentListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @OnClick(R.id.btn_create_group)
    void onCreateGroupClick(View view) {
        listener.onChangeToCreateGroup();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        composite.clear();
    }

}
