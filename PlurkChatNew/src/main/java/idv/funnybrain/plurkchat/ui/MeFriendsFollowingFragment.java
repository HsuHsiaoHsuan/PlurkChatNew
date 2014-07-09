package idv.funnybrain.plurkchat.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
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

    static final int LOADER_ID_GET_FRIEND = 0;
    static final int LOADER_ID_GET_FOLLOWING = LOADER_ID_GET_FRIEND + 1;
    // ---- constant variable END ----

    // ---- local variable START ----
    private static PlurkOAuth plurkOAuth;
    static Me me;
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
        if(D) { Log.d(TAG, "onCreate"); }

        ImageCache.ImageCacheParams cacheParams =
                new ImageCache.ImageCacheParams(getSherlockActivity(), IMAGE_CACHE_DIR);

        mImageFetcher = new ImageFetcher(getSherlockActivity(), Integer.MAX_VALUE);
        mImageFetcher.setLoadingImage(R.drawable.default_plurk_avatar);
        mImageFetcher.addImageCache(getFragmentManager(), cacheParams);

        group_list = new ArrayList<String>();
        child_list = new ArrayList<List<IHuman>>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(D) { Log.d(TAG, "onCreateView"); }
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

        if(mAdapter != null) {
            list.setAdapter(mAdapter);
        }
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(D) { Log.d(TAG, "onCreateView"); }
        plurkOAuth = ((FunnyActivity) getActivity()).getPlurkOAuth();
        me = ((FunnyActivity) getActivity()).getMe();

        if(group_list.size()>0 && child_list.size()>0) {
        } else {
            group_list.add(0, getString(R.string.me));
            ArrayList<IHuman> me_list = new ArrayList<IHuman>();
            me_list.add(me);
            child_list.add(0, me_list);
            mAdapter = new MeFriendsFollowingExpandableListAdapter(getSherlockActivity().getLayoutInflater(), group_list, child_list, mImageFetcher);
            list.setAdapter(mAdapter);
        }
        getFriends();
    }

    @Override
    public void onResume() {
        if(D) { Log.d(TAG, "onResume"); }
        super.onResume();
        mImageFetcher.setExitTasksEarly(false);
        if(mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(D) { Log.d(TAG, "onPause"); }
        mImageFetcher.setPauseWork(false);
        mImageFetcher.setExitTasksEarly(true);
        mImageFetcher.flushCache();
    }

    @Override
    public void onDestroy() {
        if(D) { Log.d(TAG, "onDestroy"); }
        super.onDestroy();
        mImageFetcher.closeCache();
    }

    void getFriends() {
        getSherlockActivity().getSupportLoaderManager().initLoader(LOADER_ID_GET_FRIEND, null, new LoaderManager.LoaderCallbacks<List<IHuman>>() {
            @Override
            public Loader<List<IHuman>> onCreateLoader(int id, Bundle args) {
                return new Mod_FriendsFans_getFriendsByOffset_AsyncTaskLoader(getSherlockActivity());
            }

            @Override
            public void onLoadFinished(Loader<List<IHuman>> loader, List<IHuman> data) {
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
                    mAdapter = new MeFriendsFollowingExpandableListAdapter(getSherlockActivity().getLayoutInflater(), group_list, child_list, mImageFetcher);
                    list.setAdapter(mAdapter);
                } else {
                    mAdapter.addNewData(getString(R.string.friend), data);
                }
                getFollowing();
            }

            @Override
            public void onLoaderReset(Loader<List<IHuman>> loader) {

            }
        }).forceLoad();
    }

    void getFollowing() {
        getSherlockActivity().getSupportLoaderManager().initLoader(LOADER_ID_GET_FOLLOWING, null, new LoaderManager.LoaderCallbacks<List<IHuman>>() {
            @Override
            public Loader<List<IHuman>> onCreateLoader(int id, Bundle args) {
                return new Mod_FriendsFans_getFollowingByOffset_AsyncTaskLoader(getSherlockActivity());
            }

            @Override
            public void onLoadFinished(Loader<List<IHuman>> loader, List<IHuman> data) {
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
                    mAdapter = new MeFriendsFollowingExpandableListAdapter(getSherlockActivity().getLayoutInflater(), group_list, child_list, mImageFetcher);
                    list.setAdapter(mAdapter);
                } else {
                    mAdapter.addNewData(getString(R.string.following), data);
                }
            }

            @Override
            public void onLoaderReset(Loader<List<IHuman>> loader) {

            }
        }).forceLoad();
    }

    static class Mod_FriendsFans_getFriendsByOffset_AsyncTaskLoader extends AsyncTaskLoader<List<IHuman>> {
        public Mod_FriendsFans_getFriendsByOffset_AsyncTaskLoader(Context context) {
            super(context);
        }

        @Override
        public List<IHuman> loadInBackground() {
            JSONArray result = null;
            int result_size = 0;
            List<IHuman> friends = new ArrayList<IHuman>();
            int round = 0;

            try {
                do {
                    if(D) { Log.d(TAG, "Mod_FriendsFans_getFriendsByOffset_AsyncTask, while: " + round); }
                    result = plurkOAuth.getModule(Mod_FriendsFans.class).getFriendsByOffset(me.getHumanId(), 0 + 100 * round, 100);
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
        public void deliverResult(List<IHuman> data) {
            if(isStarted()) {
                super.deliverResult(data);
            }
        }

        @Override
        protected void onStopLoading() {
            super.onStopLoading();
            cancelLoad();
        }

        @Override
        public void onCanceled(List<IHuman> data) {
            super.onCanceled(data);
        }

        @Override
        protected void onReset() {
            super.onReset();
            onStopLoading();
        }
    }

    static class Mod_FriendsFans_getFollowingByOffset_AsyncTaskLoader extends AsyncTaskLoader<List<IHuman>> {
        public Mod_FriendsFans_getFollowingByOffset_AsyncTaskLoader(Context context) {
            super(context);
        }

        @Override
        public List<IHuman> loadInBackground() {
            JSONArray result = null;
            int result_size = 0;
            List<IHuman> following = new ArrayList<IHuman>();
            int round = 0;

            try {
                do {
                    if(D) { Log.d(TAG, "Mod_FriendsFans_getFriendsByOffset_AsyncTask, while: " + round); }
                    result = plurkOAuth.getModule(Mod_FriendsFans.class).getFollowingByOffset(0 + 100 * round, 100);

                    result_size = result.length();
                    for (int x = 0; x < result_size; x++) {
                        following.add(new Friend(result.getJSONObject(x)));
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
                for (int x = 0; x < following.size(); x++) {
                    String tmp = following.get(x).getHumanName();
                    if (tmp.equals("")) {
                        tmp = "!!!!!!!!!!!!";
                    }
                    System.out.println(x + " " + tmp + " " +
                            following.get(x).getHumanId() + " " +
                            ((Friend) following.get(x)).getNick_name() + " " +
                            ((Friend) following.get(x)).getFull_name());
                }
            }

            return following;
        }

        @Override
        public void deliverResult(List<IHuman> data) {
            if(isStarted()) {
                super.deliverResult(data);
            }
        }

        @Override
        protected void onStopLoading() {
            super.onStopLoading();
            cancelLoad();
        }

        @Override
        public void onCanceled(List<IHuman> data) {
            super.onCanceled(data);
        }

        @Override
        protected void onReset() {
            super.onReset();
            onStopLoading();
        }
    }

//    private class Mod_FriendsFans_getFriendsByOffset_AsyncTask extends AsyncTask<String, Void, List<IHuman>> {
//        @Override
//        protected List<IHuman> doInBackground(String... params) {
//            if(D) { Log.d(TAG, "Mod_FriendsFans_getFriendsByOffset_AsyncTask, doInBackground"); }
//            JSONArray result = null;
//            int result_size = 0;
//            List<IHuman> friends = new ArrayList<IHuman>();
//            int round = 0;
//
//            try {
//                do {
//                    if(D) { Log.d(TAG, "Mod_FriendsFans_getFriendsByOffset_AsyncTask, while: " + round); }
//                    result = plurkOAuth.getModule(Mod_FriendsFans.class).getFriendsByOffset(me.getHumanId(), 0 + 100 * round, 100);
//                    // CWT   7014485
//                    // kero  4373060
//                    // 6880391
//                    result_size = result.length();
//                    for (int x = 0; x < result_size; x++) {
//                        friends.add(new Friend(result.getJSONObject(x)));
//                    }
//                    round++;
//                } while (result_size > 0);
//                if (D) {
//                    Log.d(TAG, result.toString());
//                }
//            } catch (JSONException je) {
//                Log.e(TAG, je.getMessage());
//            } catch (RequestException e) {
//                Log.e(TAG, e.getMessage());
//            }
//
//            if(D) {
//                for (int x = 0; x < friends.size(); x++) {
//                    String tmp = friends.get(x).getHumanName();
//                    if (tmp.equals("")) {
//                        tmp = "!!!!!!!!!!!!";
//                    }
//                    System.out.println(x + " " + tmp + " " +
//                            friends.get(x).getHumanId() + " " +
//                            ((Friend) friends.get(x)).getNick_name() + " " +
//                            ((Friend) friends.get(x)).getFull_name());
//                }
//            }
//
//            return friends;
//        }
//
//        @Override
//        protected void onPostExecute(List<IHuman> friends) {
//            super.onPostExecute(friends);
//            if(child_list.size()==2) { child_list.remove(1); }
//            child_list.add(1, friends);
////            setExpandableListAdapter();
//            mAdapter = new MeFriendsFollowingExpandableListAdapter(getSherlockActivity().getLayoutInflater(), group_list, child_list, mImageFetcher);
//            mAdapter.notifyDataSetChanged();
//            list.setAdapter(mAdapter);
//        }
//    }

//    private void setExpandableListAdapter() {
//        mAdapter = new MeFriendsFollowingExpandableListAdapter(getSherlockActivity().getLayoutInflater(), group_list, child_list, mImageFetcher);
//        mAdapter.notifyDataSetChanged();
//        list.setAdapter(mAdapter);
//    }
}
