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
import io.reactivex.disposables.CompositeDisposable;
import peter.kalata.com.eventapp.BaseFragment;
import peter.kalata.com.eventapp.R;
import peter.kalata.com.eventapp.data.model.Group;

public class CreateGroupFragment extends BaseFragment {

    @BindView(R.id.et_group_name)
    EditText etGroupName;

    @BindView(R.id.et_password)
    EditText etPassword;

    @BindView(R.id.et_leader_name)
    EditText etLeaderName;

    @BindView(R.id.et_leader_mail)
    EditText etLeaderMail;

    @BindView(R.id.et_leader_phone)
    EditText etLeaderPhone;

    @BindView(R.id.et_group_description)
    EditText etDescription;

    @BindView(R.id.btn_create)
    Button btnCreate;

    private CreateGroupViewModel createGroupViewModel;
    private LoginChangeFragmentListener listener;
    private final CompositeDisposable composite = new CompositeDisposable();

    public CreateGroupFragment() {}

    public static CreateGroupFragment newInstance() {
        return new CreateGroupFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_group, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setCreateGroupButtonListener();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupViewModel();
    }

    private void setupViewModel() {
        createGroupViewModel = ViewModelProviders.of(this).get(CreateGroupViewModel.class);
        createGroupViewModel.getError().observe(this, throwable -> showErrorDialog(throwable.getMessage(),
                CreateGroupFragment.class.getName()));
        createGroupViewModel.getCreateGroupSuccess().observe(this, success -> {
            listener.onChangeToUserDetails();
        });
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

    private boolean isFormValid(String groupName, String password) {
        boolean isValid = true;

        if (TextUtils.isEmpty(groupName) || groupName.length() < 5) {
            isValid = false;
            etGroupName.setError(getResources().getString(R.string.error_group_name));
        }

        if (TextUtils.isEmpty(password) || password.length() < 5) {
            isValid = false;
            etPassword.setError(getResources().getString(R.string.error_password));
        }
        return isValid;
    }

    private void setCreateGroupButtonListener() {
        composite.add(RxView.clicks(btnCreate)
                .debounce(300, TimeUnit.MILLISECONDS)
                .subscribe(aVoid -> {
                    String groupName = etGroupName.getText().toString().trim();
                    String password = etPassword.getText().toString().trim();
                    String leaderName = etLeaderName.getText().toString().trim();
                    String leaderMail = etLeaderMail.getText().toString().trim();
                    String leaderPhone = etLeaderPhone.getText().toString().trim();
                    String description = etDescription.getText().toString().trim();
                    if (isFormValid(groupName, password)) {
                        Group group = new Group(groupName, password, leaderName, leaderMail,
                                leaderPhone, description);
                        createGroupViewModel.createGroup(group);
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
