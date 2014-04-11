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
    private List<Friend> friends;
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

        friends = new ArrayList<Friend>();
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
        if(friends.size()>0) {
            mAdapter = new FriendsListAdapter(getSherlockActivity(), friends, mImageFetcher);
            list.setAdapter(mAdapter);
        } else {
            new Mod_FriendsFans_getFriendsByOffset_AsyncTask().execute(me.getId());
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
                    result = plurkOAuth.getModule(Mod_FriendsFans.class).getFriendsByOffset(me.getId(), 0 + 100 * round, 100);
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
            FriendsFragment.this.friends = friends;
            mAdapter = new FriendsListAdapter(getSherlockActivity(), friends, mImageFetcher);
            // FIXME 在整個 layout 還沒出來的時候按下 back 會產生此錯誤。
            /*
            04-11 16:07:29.175: E/AndroidRuntime(31695): 	at idv.funnybrain.plurkchat.ui.FriendsFragment$Mod_FriendsFans_getFriendsByOffset_AsyncTask.onPostExecute(FriendsFragment.java:184)
            04-11 16:07:29.175: E/AndroidRuntime(31695): 	at idv.funnybrain.plurkchat.ui.FriendsFragment$Mod_FriendsFans_getFriendsByOffset_AsyncTask.onPostExecute(FriendsFragment.java:135)
            04-11 16:07:29.175: E/AndroidRuntime(31695): 	at android.os.AsyncTask.finish(AsyncTask.java:631)
            04-11 16:07:29.175: E/AndroidRuntime(31695): 	at android.os.AsyncTask.access$600(AsyncTask.java:177)
            04-11 16:07:29.175: E/AndroidRuntime(31695): 	at android.os.AsyncTask$InternalHandler.handleMessage(AsyncTask.java:644)
            04-11 16:07:29.175: E/AndroidRuntime(31695): 	at android.os.Handler.dispatchMessage(Handler.java:99)
            04-11 16:07:29.175: E/AndroidRuntime(31695): 	at android.os.Looper.loop(Looper.java:137)
            04-11 16:07:29.175: E/AndroidRuntime(31695): 	at android.app.ActivityThread.main(ActivityThread.java:4898)
            04-11 16:07:29.175: E/AndroidRuntime(31695): 	at java.lang.reflect.Method.invokeNative(Native Method)
            04-11 16:07:29.175: E/AndroidRuntime(31695): 	at java.lang.reflect.Method.invoke(Method.java:511)
            04-11 16:07:29.175: E/AndroidRuntime(31695): 	at com.android.internal.os.ZygoteInit$MethodAndArgsCaller.run(ZygoteInit.java:1006)
            04-11 16:07:29.175: E/AndroidRuntime(31695): 	at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:773)
            04-11 16:07:29.175: E/AndroidRuntime(31695): 	at dalvik.system.NativeStart.main(Native Method)
            */

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