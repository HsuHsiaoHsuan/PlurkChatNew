package idv.funnybrain.plurkchat.ui;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.AsyncTaskLoader;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import idv.funnybrain.plurkchat.DataCentral;
import idv.funnybrain.plurkchat.R;
import idv.funnybrain.plurkchat.asynctask.Async_FriendFans_getFriendsByOffset;
import idv.funnybrain.plurkchat.asynctask.Async_FriendsFans_getFollowingByOffset;
import idv.funnybrain.plurkchat.data.IHuman;
import idv.funnybrain.plurkchat.eventbus.Event_FriendsFans_GetFollowingByOffset;
import idv.funnybrain.plurkchat.eventbus.Event_FriendsFans_GetFriendsByOffset;

/**
 * Created by Freeman on 2014/4/12.
 */
public class MeFriendsFollowingFragment extends Fragment {
    // ---- constant variable START ----
    private static final boolean D = false;
    private static final String TAG = MeFriendsFollowingFragment.class.getSimpleName();
    protected boolean mPause = false;
    private final Object mPauseLock = new Object();

    static final int LOADER_ID_GET_FRIEND = 0;
    static final int LOADER_ID_GET_FOLLOWING = LOADER_ID_GET_FRIEND + 1;
    // ---- constant variable END ----

    // ---- local variable START ----
    private ExpandableListView list;
    private List<String> group_list;
    private List<List<IHuman>> child_list;
    private MeFriendsFollowingExpandableListAdapter mAdapter;

    private List<AsyncTaskLoader> runningTask;
    // ---- local variable END ----

    public static MeFriendsFollowingFragment newInstance() {
        if(D) { Log.d(TAG, "newInstance"); }
        MeFriendsFollowingFragment f = new MeFriendsFollowingFragment();
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(D) { Log.d(TAG, "onCreate"); }

        group_list = new ArrayList<String>();
        child_list = new ArrayList<List<IHuman>>();

        runningTask = new ArrayList<AsyncTaskLoader>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(D) { Log.d(TAG, "onCreateView"); }
        View v = inflater.inflate(R.layout.fragment_me_friend_following, container, false);
        list = (ExpandableListView) v.findViewById(R.id.elv_list);

        list.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View view, int groupPosition, int childPosition, long id) {
                IHuman child = (IHuman)mAdapter.getChild(groupPosition, childPosition);

                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment prev = getFragmentManager().findFragmentByTag("user_detail");
                if (prev != null) { ft.remove(prev); }
                ft.addToBackStack(null);

                UserDetailDialogFragment detail = UserDetailDialogFragment.newInstance(child.getHumanId());
                detail.show(ft, "user_detail");

                return true;
            }
        });

        if(mAdapter != null) {
            list.setAdapter(mAdapter);
        }
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(D) { Log.d(TAG, "onCreateView"); }

        if(group_list.size()>0 && child_list.size()>0) {
        } else {
            group_list.add(0, getString(R.string.me));
            ArrayList<IHuman> me_list = new ArrayList<IHuman>();
            me_list.add(DataCentral.getInstance(getActivity()).getMe());
            child_list.add(0, me_list);
            mAdapter = new MeFriendsFollowingExpandableListAdapter(getActivity().getLayoutInflater(), group_list, child_list);
            list.setAdapter(mAdapter);
        }
        AsyncTaskLoader tmp = new Async_FriendFans_getFriendsByOffset(getActivity());
        tmp.forceLoad();
        runningTask.add(tmp);
    }

    @Override
    public void onResume() {
        if(D) { Log.d(TAG, "onResume"); }
        super.onResume();
        if(mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        if(D) { Log.d(TAG, "onPause"); }
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroy() {
        if(D) { Log.d(TAG, "onDestroy"); }
        super.onDestroy();
    }

    public void onEventMainThread(Event_FriendsFans_GetFriendsByOffset event) {
        List<IHuman> data = event.getData();
        String tag = getString(R.string.friend);
        if (group_list.contains(tag)) {
            int idx = group_list.indexOf(tag);
            child_list.remove(idx);
            child_list.add(idx, data);
        } else {
            group_list.add(getString(R.string.friend));
            child_list.add(data);
        }

        if (mAdapter == null) {
            mAdapter = new MeFriendsFollowingExpandableListAdapter(getActivity().getLayoutInflater(), group_list, child_list);
            list.setAdapter(mAdapter);
        } else {
            //mAdapter.addNewData(getString(R.string.friend), data);
            mAdapter.notifyDataSetChanged();
        }

        for (int x = 0; x < mAdapter.getGroupCount(); x++) {
            list.expandGroup(x);
        }

        AsyncTaskLoader tmp = new Async_FriendsFans_getFollowingByOffset(getActivity());
        tmp.forceLoad();
        runningTask.add(tmp);
    }

    public void onEventMainThread(Event_FriendsFans_GetFollowingByOffset event) {
        List<IHuman> data = event.getData();

        String tag = getString(R.string.following);
        if (group_list.contains(tag)) {
            int idx = group_list.indexOf(tag);
            child_list.remove(idx);
            child_list.add(idx, data);
        } else {
            group_list.add(getString(R.string.following));
            child_list.add(data);
        }

        if(mAdapter == null) {
            mAdapter = new MeFriendsFollowingExpandableListAdapter(getActivity().getLayoutInflater(), group_list, child_list);
            list.setAdapter(mAdapter);
        } else {
            //mAdapter.addNewData(getString(R.string.following), data);
            mAdapter.notifyDataSetChanged();
        }

        for (int x = 0; x < mAdapter.getGroupCount(); x++) {
            list.expandGroup(x);
        }
    }
}