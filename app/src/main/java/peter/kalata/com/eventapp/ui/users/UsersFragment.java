package peter.kalata.com.eventapp.ui.users;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import peter.kalata.com.eventapp.BaseFragment;
import peter.kalata.com.eventapp.R;
import peter.kalata.com.eventapp.data.model.User;
import peter.kalata.com.eventapp.ui.login.UserDetailsViewModel;
import peter.kalata.com.eventapp.ui.main.MainActivity;

public class UsersFragment extends BaseFragment {

    @BindView(R.id.rvUsers)
    RecyclerView rvUsers;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private UsersViewModel usersViewModel;
    private UserAdapter adapter;

    public UsersFragment() {}

    public static UsersFragment newInstance() {
        return new UsersFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users, container, false);
        ButterKnife.bind(this, view);
        setupToolBar();
        setupRecyclerView();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupViewModel();
    }

    private void setupToolBar() {
        toolbar.setTitle(getString(R.string.users));
        ((AppCompatActivity)requireActivity()).setSupportActionBar(toolbar);
    }

    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false);
        rvUsers.setLayoutManager(layoutManager);
        adapter = new UserAdapter();
        rvUsers.setAdapter(adapter);
        rvUsers.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
    }

    private void setupViewModel() {
        usersViewModel = ViewModelProviders.of(this).get(UsersViewModel.class);
        usersViewModel.getError().observe(this, throwable -> showErrorDialog(throwable.getMessage(),
                UsersFragment.class.getName()));
        usersViewModel.getUsers().observe(this, users -> {
            adapter.setUsers(users);
        });
    }

}
