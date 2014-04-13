package idv.funnybrain.plurkchat.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ExpandableListView;
import com.actionbarsherlock.app.SherlockFragment;
import idv.funnybrain.plurkchat.FunnyActivity;
import idv.funnybrain.plurkchat.PlurkOAuth;
import idv.funnybrain.plurkchat.R;
import idv.funnybrain.plurkchat.RequestException;
import idv.funnybrain.plurkchat.data.Friend;
import idv.funnybrain.plurkchat.data.IHuman;
import idv.funnybrain.plurkchat.data.Me;
import idv.funnybrain.plurkchat.modules.Mod_FriendsFans;
import idv.funnybrain.plurkchat.utils.ImageCache;
import idv.funnybrain.plurkchat.utils.ImageFetcher;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Freeman on 2014/4/12.
 */
public class MeFriendsFollowingFragment extends SherlockFragment {
    // ---- constant variable START ----
    private static final boolean D = true;
    private static final String TAG = "MeFriendsFollowingFragment";
    private static final String IMAGE_CACHE_DIR = "thumbnails";
    protected boolean mPause = false;
    private final Object mPauseLock = new Object();
    // ---- constant variable END ----

    // ---- local variable START ----
    private static PlurkOAuth plurkOAuth;
    private Me me;
    private ExpandableListView list;
//    private List<Friend> friends;
//    private FriendsListAdapter mAdapter;
    // ---- local variable END ----

    // ---- static variable START ----
    static List<String> group_list;
    static List<List<IHuman>> child_list;
    static MeFriendsFollowingExpandableListAdapter mAdapter;
    static ImageFetcher mImageFetcher;
    // ---- static variable END ----

    public static MeFriendsFollowingFragment newInstance() {
        if(D) { Log.d(TAG, "newInstance"); }
        MeFriendsFollowingFragment f = new MeFriendsFollowingFragment();
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ImageCache.ImageCacheParams cacheParams =
                new ImageCache.ImageCacheParams(getSherlockActivity(), IMAGE_CACHE_DIR);

        mImageFetcher = new ImageFetcher(getSherlockActivity(), Integer.MAX_VALUE);
        mImageFetcher.setLoadingImage(R.drawable.default_plurk_avatar);
        mImageFetcher.addImageCache(getFragmentManager(), cacheParams);

        group_list = new ArrayList<String>();
        group_list.add(0, getString(R.string.me));
        group_list.add(1, getString(R.string.friend));
        group_list.add(2, getString(R.string.following));
        group_list.add(3, getString(R.string.fans));
        child_list = new ArrayList<List<IHuman>>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_me_friend_following, container, false);
        list = (ExpandableListView) v.findViewById(R.id.elv_list);

        list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(scrollState == SCROLL_STATE_FLING) {
                    if(!idv.funnybrain.plurkchat.utils.Utils.hasHoneycomb()) {
                        mImageFetcher.setPauseWork(true);
                    }
                } else {
                    mImageFetcher.setPauseWork(false);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        plurkOAuth = ((FunnyActivity) getActivity()).getPlurkOAuth();
        me = ((FunnyActivity) getActivity()).getMe();

        if(group_list.size()>0 && child_list.size()>0) {
//            mAdapter = new MeFriendsFollowingExpandableListAdapter(getSherlockActivity().getLayoutInflater(), group_list, child_list, mImageFetcher);
//            mAdapter = new FriendsListAdapter(getSherlockActivity().getLayoutInflater(), friends, mImageFetcher);
//            list.setAdapter(mAdapter);
//            setExpandableListAdapter();
            mAdapter = new MeFriendsFollowingExpandableListAdapter(getSherlockActivity().getLayoutInflater(), group_list, child_list, mImageFetcher);
            mAdapter.notifyDataSetChanged();
            list.setAdapter(mAdapter);
            new Mod_FriendsFans_getFriendsByOffset_AsyncTask().execute(me.getHumanId());
        } else {
            ArrayList<IHuman> me_list = new ArrayList<IHuman>();
            me_list.add(me);
            child_list.add(0, me_list);
//            setExpandableListAdapter();
            mAdapter = new MeFriendsFollowingExpandableListAdapter(getSherlockActivity().getLayoutInflater(), group_list, child_list, mImageFetcher);
            mAdapter.notifyDataSetChanged();
            list.setAdapter(mAdapter);
            new Mod_FriendsFans_getFriendsByOffset_AsyncTask().execute(me.getHumanId());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mImageFetcher.setExitTasksEarly(false);
        if(mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mImageFetcher.setPauseWork(false);
        mImageFetcher.setExitTasksEarly(true);
        mImageFetcher.flushCache();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mImageFetcher.closeCache();
    }

    private class Mod_FriendsFans_getFriendsByOffset_AsyncTask extends AsyncTask<String, Void, List<IHuman>> {
        @Override
        protected List<IHuman> doInBackground(String... params) {
            if(D) { Log.d(TAG, "Mod_FriendsFans_getFriendsByOffset_AsyncTask, doInBackground"); }
            JSONArray result = null;
            int result_size = 0;
            List<IHuman> friends = new ArrayList<IHuman>();
            int round = 0;

            try {
                do {
                    if(D) { Log.d(TAG, "Mod_FriendsFans_getFriendsByOffset_AsyncTask, while: " + round); }
                    result = plurkOAuth.getModule(Mod_FriendsFans.class).getFriendsByOffset("4373060", 0 + 100 * round, 100);
                    // CWT   7014485
                    // kero  4373060
                    // 6880391
                    result_size = result.length();
                    for (int x = 0; x < result_size; x++) {
                        friends.add(new Friend(result.getJSONObject(x)));
                    }
                    round++;
                } while (result_size > 0);
                if (D) {
                    Log.d(TAG, result.toString());
                }
            } catch (JSONException je) {
                Log.e(TAG, je.getMessage());
            } catch (RequestException e) {
                Log.e(TAG, e.getMessage());
            }

            if(D) {
                for (int x = 0; x < friends.size(); x++) {
                    String tmp = friends.get(x).getHumanName();
                    if (tmp.equals("")) {
                        tmp = "!!!!!!!!!!!!";
                    }
                    System.out.println(x + " " + tmp + " " +
                            friends.get(x).getHumanId() + " " +
                            ((Friend) friends.get(x)).getNick_name() + " " +
                            ((Friend) friends.get(x)).getFull_name());
                }
            }

            return friends;
        }

        @Override
        protected void onPostExecute(List<IHuman> friends) {
            super.onPostExecute(friends);
            if(child_list.size()==2) { child_list.remove(1); }
            child_list.add(1, friends);
//            setExpandableListAdapter();
            mAdapter = new MeFriendsFollowingExpandableListAdapter(getSherlockActivity().getLayoutInflater(), group_list, child_list, mImageFetcher);
            mAdapter.notifyDataSetChanged();
            list.setAdapter(mAdapter);
        }
    }

//    private void setExpandableListAdapter() {
//        mAdapter = new MeFriendsFollowingExpandableListAdapter(getSherlockActivity().getLayoutInflater(), group_list, child_list, mImageFetcher);
//        mAdapter.notifyDataSetChanged();
//        list.setAdapter(mAdapter);
//    }
}
