package idv.funnybrain.plurkchat.ui;

import android.content.AsyncTaskLoader;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import idv.funnybrain.plurkchat.DataCentral;
import idv.funnybrain.plurkchat.R;
import idv.funnybrain.plurkchat.asynctask.Async_FriendFans_getFriendsByOffset;
import idv.funnybrain.plurkchat.asynctask.Async_FriendsFans_getFollowingByOffset;
import idv.funnybrain.plurkchat.asynctask.Async_Users_Me;
import idv.funnybrain.plurkchat.data.IHuman;
import idv.funnybrain.plurkchat.eventbus.Event_FriendsFans_GetFollowingByOffset;
import idv.funnybrain.plurkchat.eventbus.Event_FriendsFans_GetFriendsByOffset;

public class MeFriendsFollowingFragment extends Fragment {
    private static final String TAG = "MeFriendsFollowing";
    private static final boolean D = false;
    protected boolean mPause = false;
    private final Object mPauseLock = new Object();

    static final int LOADER_ID_GET_FRIEND = 0;
    static final int LOADER_ID_GET_FOLLOWING = LOADER_ID_GET_FRIEND + 1;

    private ExpandableListView list;
    private List<String> group_list;
    private List<List<IHuman>> child_list;
    private MeFriendsFollowingExpandableListAdapter adapter;
    private List<AsyncTaskLoader> runningTask;

    public static MeFriendsFollowingFragment newInstance() {
        MeFriendsFollowingFragment f = new MeFriendsFollowingFragment();
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        group_list = new ArrayList<String>();
        child_list = new ArrayList<List<IHuman>>();
        runningTask = new ArrayList<AsyncTaskLoader>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me_friend_following, container, false);
        list = (ExpandableListView) view.findViewById(R.id.elv_list);

        list.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View view, int groupPosition, int childPosition, long id) {
                IHuman child = (IHuman) adapter.getChild(groupPosition, childPosition);

                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment prev = getFragmentManager().findFragmentByTag("user_detail");
                if (prev != null) { ft.remove(prev); }
                ft.addToBackStack(null);

                UserDetailDialogFragment detail = UserDetailDialogFragment.newInstance(child.getHumanId());
                detail.show(ft, "user_detail");

                return true;
            }
        });

        if(adapter != null) {
            list.setAdapter(adapter);
        }
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(group_list.size()>0 && child_list.size()>0) {
        } else {
            group_list.add(0, getString(R.string.me));
            ArrayList<IHuman> me_list = new ArrayList<IHuman>();
            me_list.add(DataCentral.getInstance(getActivity()).getMe());
            child_list.add(0, me_list);
            adapter = new MeFriendsFollowingExpandableListAdapter(getActivity().getLayoutInflater(), group_list, child_list);
            list.setAdapter(adapter);
        }
        new Async_Users_Me().execute("");
        AsyncTaskLoader tmp = new Async_FriendFans_getFriendsByOffset(getActivity());
        tmp.forceLoad();
        runningTask.add(tmp);
    }

    @Override
    public void onResume() {
        if(D) { Log.d(TAG, "onResume"); }
        super.onResume();
        if(adapter != null) {
            adapter.notifyDataSetChanged();
        }
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        if(D) { Log.d(TAG, "onPause"); }
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Event_FriendsFans_GetFriendsByOffset event) {
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

        if (adapter == null) {
            adapter = new MeFriendsFollowingExpandableListAdapter(getActivity().getLayoutInflater(), group_list, child_list);
            list.setAdapter(adapter);
        } else {
            //adapter.addNewData(getString(R.string.friend), data);
            adapter.notifyDataSetChanged();
        }

        for (int x = 0; x < adapter.getGroupCount(); x++) {
            list.expandGroup(x);
        }

        AsyncTaskLoader tmp = new Async_FriendsFans_getFollowingByOffset(getActivity());
        tmp.forceLoad();
        runningTask.add(tmp);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Event_FriendsFans_GetFollowingByOffset event) {
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

        if(adapter == null) {
            adapter = new MeFriendsFollowingExpandableListAdapter(getActivity().getLayoutInflater(), group_list, child_list);
            list.setAdapter(adapter);
        } else {
            //adapter.addNewData(getString(R.string.following), data);
            adapter.notifyDataSetChanged();
        }

        for (int x = 0; x < adapter.getGroupCount(); x++) {
            list.expandGroup(x);
        }
    }
}