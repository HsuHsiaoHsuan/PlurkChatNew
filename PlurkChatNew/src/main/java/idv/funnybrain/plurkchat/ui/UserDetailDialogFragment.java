package idv.funnybrain.plurkchat.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.actionbarsherlock.app.SherlockDialogFragment;
import idv.funnybrain.plurkchat.asynctask.Async_Profile_getPublicProfile;

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

        if (D) { Log.d(TAG, "yes I get user_id: " + userID); }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        new Async_Profile_getPublicProfile(getSherlockActivity(), userID).forceLoad();
    }
}