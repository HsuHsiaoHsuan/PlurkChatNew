package idv.funnybrain.plurkchat.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import com.actionbarsherlock.app.SherlockFragment;
import idv.funnybrain.plurkchat.FunnyActivity;
import idv.funnybrain.plurkchat.PlurkOAuth;
import idv.funnybrain.plurkchat.R;
import idv.funnybrain.plurkchat.RequestException;
import idv.funnybrain.plurkchat.data.Friend;
import idv.funnybrain.plurkchat.data.Language;
import idv.funnybrain.plurkchat.data.Me;
import idv.funnybrain.plurkchat.data.Qualifier;
import idv.funnybrain.plurkchat.modules.Mod_FriendsFans;
import idv.funnybrain.plurkchat.modules.Mod_Timeline;
import idv.funnybrain.plurkchat.utils.ImageCache;
import idv.funnybrain.plurkchat.utils.ImageFetcher;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Freeman on 2014/4/2.
 */
public class FriendsFragment extends SherlockFragment {
    // ---- constant variable START ----
    private static final boolean D = true;
    private static final String TAG = "FriendsFragment";
    private static final String IMAGE_CACHE_DIR = "thumbnails";
    protected boolean mPause = false;
    private final Object mPauseLock = new Object();
    // ---- constant variable END ----

    // ---- local variable START ----
    private PlurkOAuth plurkOAuth;
    private Me me;
    private ListView list;
    private FriendsListAdapter mAdapter;

    private ImageFetcher mImageFetcher;
    // ---- local variable END ----

    FriendsFragment newInstance() {
        if(D) { Log.d(TAG, "newInstance"); }
        FriendsFragment friendsFragment = new FriendsFragment();
        return friendsFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ImageCache.ImageCacheParams cacheParams =
                new ImageCache.ImageCacheParams(getSherlockActivity(), IMAGE_CACHE_DIR);

        mImageFetcher = new ImageFetcher(getSherlockActivity(), Integer.MAX_VALUE);
        mImageFetcher.setLoadingImage(R.drawable.default_plurk_avatar);
        mImageFetcher.addImageCache(getFragmentManager(), cacheParams);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_friends, container, false);
        list = (ListView) v.findViewById(R.id.list);
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
        //((FunnyActivity) getActivity()).doTest();
        plurkOAuth = ((FunnyActivity) getActivity()).getPlurkOAuth();
        me = ((FunnyActivity) getActivity()).getMe();
        //new PlurkTmpAsyncTask().execute("");
        new Mod_FriendsFans_getFriendsByOffset_AsyncTask().execute(me.getId());
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

    private class Mod_FriendsFans_getFriendsByOffset_AsyncTask extends AsyncTask<String, Void, List<Friend>> {
        @Override
        protected List<Friend> doInBackground(String... params) {
            if(D) { Log.d(TAG, "Mod_FriendsFans_getFriendsByOffset_AsyncTask, doInBackground"); }
            JSONArray result = null;
            List<Friend> friends = new ArrayList<Friend>();
            int round = 0;

            try {
                do {
                    if(D) { Log.d(TAG, "Mod_FriendsFans_getFriendsByOffset_AsyncTask, while: " + round); }
                    result = plurkOAuth.getModule(Mod_FriendsFans.class).getFriendsByOffset("4373060", 0 + 100 * round, 100);
                    // CWT   7014485
                    // kero  4373060
                    // 6880391
                    for (int x = 0; x < result.length(); x++) {
                        friends.add(new Friend(result.getJSONObject(x)));
                    }
                    round++;
                } while (result.length() > 0);
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
                    String tmp = friends.get(x).getDisplay_name();
                    if (tmp.equals("")) {
                        tmp = "!!!!!!!!!!!!";
                    }
                    System.out.println(x + " " + tmp + " " +
                            friends.get(x).getId() + " " +
                            friends.get(x).getNick_name() + " " +
                            friends.get(x).getFull_name());
                }
            }

            return friends;
        }

        @Override
        protected void onPostExecute(List<Friend> friends) {
            super.onPostExecute(friends);
            mAdapter = new FriendsListAdapter(getSherlockActivity(), friends, mImageFetcher);
            list.setAdapter(mAdapter);
        }
    }

    private class PlurkTmpAsyncTask extends AsyncTask<String, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... params) {
            JSONObject result = null;
            try {
                result = plurkOAuth.getModule(Mod_Timeline.class).plurkAdd("(wave)(wave)(wave)"+ Math.random(), Qualifier.SAYS, null, 0, Language.TR_CH);
            } catch (RequestException e) {
                e.printStackTrace();
            }
            return result;
        }
    }
}