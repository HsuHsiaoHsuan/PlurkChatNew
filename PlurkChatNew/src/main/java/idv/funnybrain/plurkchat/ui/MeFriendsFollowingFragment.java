package idv.funnybrain.plurkchat.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import com.actionbarsherlock.app.SherlockFragment;
import de.greenrobot.event.EventBus;
import idv.funnybrain.plurkchat.DataCentral;
import idv.funnybrain.plurkchat.R;
import idv.funnybrain.plurkchat.asynctask.Async_FriendFans_getFriendsByOffset;
import idv.funnybrain.plurkchat.asynctask.Async_FriendsFans_getFollowingByOffset;
import idv.funnybrain.plurkchat.data.IHuman;
import idv.funnybrain.plurkchat.eventbus.Event_GetFollowingByOffset;
import idv.funnybrain.plurkchat.eventbus.Event_GetFriendsByOffset;
import idv.funnybrain.plurkchat.utils.ImageCache;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Freeman on 2014/4/12.
 */
public class MeFriendsFollowingFragment extends SherlockFragment {
    // ---- constant variable START ----
    private static final boolean D = true;
    private static final String TAG = "MeFriendsFollowingFragment";
    protected boolean mPause = false;
    private final Object mPauseLock = new Object();

    static final int LOADER_ID_GET_FRIEND = 0;
    static final int LOADER_ID_GET_FOLLOWING = LOADER_ID_GET_FRIEND + 1;
    // ---- constant variable END ----

    // ---- local variable START ----
    private ExpandableListView list;
    // ---- local variable END ----

    // ---- static variable START ----
    static List<String> group_list;
    static List<List<IHuman>> child_list;
    static MeFriendsFollowingExpandableListAdapter mAdapter;
//    static ImageFetcher mImageFetcher;
    // ---- static variable END ----

    public static MeFriendsFollowingFragment newInstance() {
        if(D) { Log.d(TAG, "newInstance"); }
        MeFriendsFollowingFragment f = new MeFriendsFollowingFragment();
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(D) { Log.d(TAG, "onCreate"); }

        ImageCache.ImageCacheParams cacheParams =
                new ImageCache.ImageCacheParams(getSherlockActivity(), DataCentral.IMAGE_CACHE_DIR);

//        mImageFetcher = new ImageFetcher(getSherlockActivity(), Integer.MAX_VALUE);
//        mImageFetcher.setLoadingImage(R.drawable.default_plurk_avatar);
//        mImageFetcher.addImageCache(getFragmentManager(), cacheParams);

        group_list = new ArrayList<String>();
        child_list = new ArrayList<List<IHuman>>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(D) { Log.d(TAG, "onCreateView"); }
        View v = inflater.inflate(R.layout.fragment_me_friend_following, container, false);
        list = (ExpandableListView) v.findViewById(R.id.elv_list);

//        list.setOnScrollListener(new AbsListView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//                if(scrollState == SCROLL_STATE_FLING) {
//                    if(!idv.funnybrain.plurkchat.utils.Utils.hasHoneycomb()) {
//                        mImageFetcher.setPauseWork(true);
//                    }
//                } else {
//                    mImageFetcher.setPauseWork(false);
//                }
//            }
//
//            @Override
//            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//            }
//        });

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
//            mAdapter = new MeFriendsFollowingExpandableListAdapter(getSherlockActivity().getLayoutInflater(), group_list, child_list, mImageFetcher);
            mAdapter = new MeFriendsFollowingExpandableListAdapter(getSherlockActivity().getLayoutInflater(), group_list, child_list);
            list.setAdapter(mAdapter);
        }
        // getFriends();
        new Async_FriendFans_getFriendsByOffset(getSherlockActivity()).forceLoad();
    }

    @Override
    public void onResume() {
        if(D) { Log.d(TAG, "onResume"); }
        super.onResume();
//        mImageFetcher.setExitTasksEarly(false);
        if(mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        if(D) { Log.d(TAG, "onPause"); }
//        mImageFetcher.setPauseWork(false);
//        mImageFetcher.setExitTasksEarly(true);
//        mImageFetcher.flushCache();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroy() {
        if(D) { Log.d(TAG, "onDestroy"); }
        super.onDestroy();
//        mImageFetcher.closeCache();
    }

    public void onEventMainThread(Event_GetFriendsByOffset event) {
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
//            mAdapter = new MeFriendsFollowingExpandableListAdapter(getSherlockActivity().getLayoutInflater(), group_list, child_list, mImageFetcher);
            mAdapter = new MeFriendsFollowingExpandableListAdapter(getSherlockActivity().getLayoutInflater(), group_list, child_list);
            list.setAdapter(mAdapter);
        } else {
            mAdapter.addNewData(getString(R.string.friend), data);
        }

        new Async_FriendsFans_getFollowingByOffset(getSherlockActivity()).forceLoad();
    }

    public void onEventMainThread(Event_GetFollowingByOffset event) {
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
//            mAdapter = new MeFriendsFollowingExpandableListAdapter(getSherlockActivity().getLayoutInflater(), group_list, child_list, mImageFetcher);
            mAdapter = new MeFriendsFollowingExpandableListAdapter(getSherlockActivity().getLayoutInflater(), group_list, child_list);
            list.setAdapter(mAdapter);
        } else {
            mAdapter.addNewData(getString(R.string.following), data);
        }
    }
}
