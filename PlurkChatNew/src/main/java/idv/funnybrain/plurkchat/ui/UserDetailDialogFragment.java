package idv.funnybrain.plurkchat.ui;

import android.os.Bundle;
import android.util.Log;
import com.actionbarsherlock.app.SherlockDialogFragment;

/**
 * Created by freeman on 2014/12/15.
 */
public class UserDetailDialogFragment extends SherlockDialogFragment {
    private static final boolean D = true;
    private static final String TAG = "UserDetailDialogFragment";

    private String userID = "";

    public static UserDetailDialogFragment newInstance(String user_id) {
        if(D) { Log.d(TAG, " --> newInstance " + user_id); }
        UserDetailDialogFragment detail = new UserDetailDialogFragment();
        Bundle bundle = detail.getArguments();
        if(bundle == null) {
            bundle = new Bundle();
        }
        bundle.putString("user_id", user_id);
        detail.setArguments(bundle);
        return detail;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if(bundle != null) {
            userID = bundle.getString("user_id");
        }
    }
}