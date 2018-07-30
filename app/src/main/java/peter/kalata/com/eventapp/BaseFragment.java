package peter.kalata.com.eventapp;

import android.support.v4.app.Fragment;
import android.widget.Toast;

import peter.kalata.com.eventapp.ui.dialogs.AlertDialogFragment;

public abstract class BaseFragment extends Fragment {

    public void showToast(String message) {
        Toast.makeText(App.getAppContext(), message, Toast.LENGTH_LONG).show();
    }

    public void showErrorDialog(String message, String tag) {
        AlertDialogFragment.newInstance(message).show(getChildFragmentManager(), tag);
    }

}
