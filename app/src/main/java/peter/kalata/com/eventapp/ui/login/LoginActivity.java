package peter.kalata.com.eventapp.ui.login;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import peter.kalata.com.eventapp.R;
import peter.kalata.com.eventapp.ui.main.MainActivity;

public class LoginActivity extends AppCompatActivity implements LoginChangeFragmentListener {

    public static void startLoginActivity(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    private FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        fm = getSupportFragmentManager();

        if (savedInstanceState == null) {
            fm.beginTransaction()
                    .replace(R.id.fl_container, LoginChooserFragment.newInstance())
                    .commit();
        }
    }

    @Override
    public void onChangeToCreateGroup() {
        fm.beginTransaction()
                .replace(R.id.fl_container, CreateGroupFragment.newInstance())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onChangeToUserDetails() {
        fm.beginTransaction()
                .replace(R.id.fl_container, UserDetailsFragment.newInstance())
                .addToBackStack(null)
                .commit();
    }
}
