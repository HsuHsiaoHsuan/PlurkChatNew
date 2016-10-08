package idv.funnybrain.plurkchat.ui;

import android.app.DialogFragment;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import de.greenrobot.event.EventBus;
import idv.funnybrain.plurkchat.DataCentral;
import idv.funnybrain.plurkchat.R;
import idv.funnybrain.plurkchat.asynctask.Async_Profile_getPublicProfile;
import idv.funnybrain.plurkchat.data.PublicProfile;
import idv.funnybrain.plurkchat.data.User_Info;
import idv.funnybrain.plurkchat.eventbus.Event_Profile_getPublicProfile;
import org.w3c.dom.Text;

/**
 * Created by freeman on 2014/12/15.
 */
public class UserDetailDialogFragment extends DialogFragment {
    private static final boolean D = true;
    private static final String TAG = "UserDetailDialogFragment";

    private String userID = "";

    private DataCentral mData;
    private ImageLoader mImageLoader;
    private View view;

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

        mData = DataCentral.getInstance(getActivity());
        mImageLoader = mData.getImageLoader();
        if (D) { Log.d(TAG, "yes I get user_id: " + userID); }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();

        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        new Async_Profile_getPublicProfile(getActivity(), userID).forceLoad();
    }

    public void onEventMainThread(Event_Profile_getPublicProfile event) {
        PublicProfile profile = event.getData();
        User_Info info = profile.getUser_info();

        // title
        this.getDialog().setTitle(info.getPage_title());

        // image
        ((NetworkImageView) view.findViewById(R.id.avatar))
                .setImageUrl(info.getAvatar_big(), mImageLoader);

        // huname name
        ((TextView) view.findViewById(R.id.human_name))
                .setText(info.getHumanName());

        // nick name
        ((TextView) view.findViewById(R.id.nick_name))
                .setText(info.getNick_name());

        // about
        ((TextView) view.findViewById(R.id.about))
                .setText(info.getAbout());

        // location
        TextView tv_location = (TextView) view.findViewById(R.id.location);
        tv_location.setText(tv_location.getText() + "\n" + info.getLocation());
//        ((TextView) view.findViewById(R.id.location))
//                .setText(info.getLocation());

        // relationship
        TextView tv_relationship = (TextView) view.findViewById(R.id.relationship);
        tv_relationship.setText(tv_relationship.getText() + "\n" + getString(info.getRelationship().getReadableStringResources()));
//        ((TextView) view.findViewById(R.id.relationship))
//                .setText(info.getRelationship().toString());

        // birthday
        TextView tv_birthday = (TextView) view.findViewById(R.id.birthday);
        tv_birthday.setText(tv_birthday.getText() + "\n" + info.getDate_of_birth());
//        ((TextView) view.findViewById(R.id.birthday))
//                .setText(info.getDate_of_birth());

        // gender
        int gender = info.getGender();
        if (gender == 1) {
            ((ImageView) view.findViewById(R.id.gender))
                    .setImageResource(R.drawable.gender_male);
        } else if (gender == 0) {
            ((ImageView) view.findViewById(R.id.gender))
                    .setImageResource(R.drawable.gender_female);
        }

        // karma
        TextView tv_karma = (TextView) view.findViewById(R.id.karma);
        tv_karma.setText(tv_karma.getText() + "\n" + String.valueOf(info.getKarma()));
//        ((TextView) view.findViewById(R.id.karma))
//                .setText(Double.toString(info.getKarma()));

        // plurks_total
        TextView tv_plurks = (TextView) view.findViewById(R.id.plurks_total);
        tv_plurks.setText(tv_plurks.getText() + "\n" + String.valueOf(info.getPlurks_count()));
//        ((TextView) view.findViewById(R.id.plurks_total))
//                .setText(Integer.toString(info.getPlurks_count()));

        // replurks
        TextView tv_replurks = (TextView) view.findViewById(R.id.replurks);
        tv_replurks.setText(tv_replurks.getText() + "\n" + String.valueOf(info.getResponse_count()));
//        ((TextView) view.findViewById(R.id.replurks))
//                .setText(Integer.toString(info.getResponse_count()));

        // are friends
        TextView tv_areFriends = (TextView) view.findViewById(R.id.are_friends);
        tv_areFriends.setText(tv_areFriends.getText() + "\n" + (profile.isAre_friends() ? getString(R.string._true) : getString(R.string._false)));
//        ((TextView) view.findViewById(R.id.are_friends))
//                .setText(Boolean.toString(profile.isAre_friends()));

        // is following
        TextView tv_following = (TextView) view.findViewById(R.id.is_following);
        tv_following.setText(tv_following.getText() + "\n" + (profile.isIs_following() ? getString(R.string._true) : getString(R.string._false)));
//        ((TextView) view.findViewById(R.id.is_following))
//                .setText(Boolean.toString(profile.isIs_following()));
    }
}