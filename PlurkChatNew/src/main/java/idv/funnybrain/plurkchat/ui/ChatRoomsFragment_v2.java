package idv.funnybrain.plurkchat.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.diegocarloslima.fgelv.lib.FloatingGroupExpandableListView;
import com.diegocarloslima.fgelv.lib.WrapperExpandableListAdapter;
import idv.funnybrain.plurkchat.FunnyActivity;
import idv.funnybrain.plurkchat.PlurkOAuth;
import idv.funnybrain.plurkchat.R;
import idv.funnybrain.plurkchat.RequestException;
import idv.funnybrain.plurkchat.data.Plurk_Users;
import idv.funnybrain.plurkchat.data.Plurks;
import idv.funnybrain.plurkchat.modules.Mod_Timeline;
import idv.funnybrain.plurkchat.utils.ImageCache;
import idv.funnybrain.plurkchat.utils.ImageFetcher;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

/**
 * Created by Freeman on 2014/4/16.
 */
public class ChatRoomsFragment_v2 extends SherlockFragment {
    // ---- constant variable START ----
    private static final boolean D = true;
    private static final String TAG = "ChatRoomsFragment_v2";

    private static final String IMAGE_CACHE_DIR = "thumbnails";

    protected boolean mPause = false;
    private final Object mPauseLock = new Object();

    private static final int LOADER_ID_GET_PLURKS = 0;
    private static final String HAS_PARAMS = "hasParams";

    private static final String OFFSET = "offset";
    private static final String LIMIT = "limit";
    private static final String FILTER = "filter";
    private static final String FAVORERS_DETAIL = "favorers_detail";
    private static final String LIMITED_DETAIL = "limited_detail";
    private static final String REPLURKERS_DETAIL = "replurkers_detail";
    // ---- constant variable END ----

    // ---- local variable START ----
    private static PlurkOAuth plurkOAuth;
    //private ExpandableListView list;
    private FloatingGroupExpandableListView list;
    private Button bt_more;
    //private ChatRoomExpandableListAdapter_v2 mAdapter;
    private BaseExpandableListAdapter mAdapter;

    private ImageFetcher mImageFetcher;

    private HashMap<String, Plurk_Users> plurk_users;

    private HashMap<String, List<Plurks>> plurks;
    private String oldest_posted_readable = "";
    private String oldest_posted = "null";

    private LoaderManager.LoaderCallbacks<JSONObject> getPlurks_Callback = new LoaderManager.LoaderCallbacks<JSONObject>() {
        @Override
        public Loader<JSONObject> onCreateLoader(int id, Bundle args) {
            HashMap<String, String> params = new HashMap<String, String>();
            if(args.getBoolean(HAS_PARAMS)) {
                if(args.containsKey(OFFSET)) {
                    params.put(OFFSET, args.getString(OFFSET));
                }
            } else {
            }
            return new Mod_Timeline_getPlurks_AsyncTaskLoader(getSherlockActivity(), params);
        }

        @Override
        public void onLoadFinished(Loader<JSONObject> loader, JSONObject data) {

            try {
                JSONObject obj_plurk_users = data.getJSONObject("plurk_users");
                Iterator<String> iterator = obj_plurk_users.keys();
                while (iterator.hasNext()) {
                    String idx = iterator.next();
                    plurk_users.put(idx, new Plurk_Users(obj_plurk_users.getJSONObject(idx)));
                    if(D) { Log.d(TAG, "plurk_users length: " + plurk_users.keySet()); }
                }

                JSONArray obj_plurks = data.getJSONArray("plurks");
                int obj_plurks_size = obj_plurks.length();
                for (int x = 0; x < obj_plurks_size; x++) {
                    Plurks post = new Plurks(obj_plurks.getJSONObject(x)); // get the post

                    String post_or_repost_id = post.getReplurker_id().equals("null") ? post.getOwner_id() : post.getReplurker_id();
                    if(plurks.containsKey(post_or_repost_id)) {
                        List<Plurks> tmp_list = plurks.get(post_or_repost_id);
                        boolean alreadyHas = false;
                        for(Plurks p: tmp_list) { // check if we already has this post.
                            if(p.getPlurk_id().equals(post.getPlurk_id())) {
                                alreadyHas = true;
                            }
                        }
                        if(!alreadyHas) {
                            plurks.get(post_or_repost_id).add(post);
                        }
                    } else {
                        List<Plurks> tmp_list = new ArrayList<Plurks>();
                        tmp_list.add(post);
                        plurks.put(post_or_repost_id, tmp_list);
                    }
                }

                Plurks oldest_plurk = new Plurks(obj_plurks.getJSONObject(obj_plurks_size-1));
                oldest_posted_readable = oldest_plurk.getReadablePostedDate();
                oldest_posted = oldest_plurk.getQueryFormatedPostedDate();

                if(D) { Log.d(TAG, "plurks: " + obj_plurks_size); }
            } catch(JSONException jsone) {
                Log.e(TAG, jsone.getMessage());
            }

            if(D) {
                Iterator<String> iter = plurks.keySet().iterator();
                while (iter.hasNext()) {
                    String key = iter.next();
                    List list = plurks.get(key);
                    Log.d(TAG, "who?" + key + " has list size: " + list.size());
                }
            }
            if(mAdapter == null) {
                mAdapter = new ChatRoomExpandableListAdapter_v2(getSherlockActivity().getLayoutInflater(), plurk_users, plurks, mImageFetcher);
                WrapperExpandableListAdapter wrapperAdapter = new WrapperExpandableListAdapter(mAdapter);
                list.setAdapter(wrapperAdapter);
                //list.setAdapter(mAdapter);
            } else {
                //mAdapter.addNewData(plurk_users, plurks);
                ((ChatRoomExpandableListAdapter_v2) mAdapter).addNewData();
            }
            bt_more.setVisibility(View.VISIBLE);
            bt_more.setEnabled(true);
            bt_more.setText(getString(R.string.oldest_post) + "\n" + oldest_posted_readable);
        }

        @Override
        public void onLoaderReset(Loader<JSONObject> loader) {

        }
    };
    // ---- local variable END ----

    public static ChatRoomsFragment_v2 newInstance() {
        if(D) { Log.d(TAG, "newInstance"); }
        ChatRoomsFragment_v2 chatRoomsFragment = new ChatRoomsFragment_v2();
        return chatRoomsFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(D) { Log.d(TAG, "onCreate"); }

        ImageCache.ImageCacheParams cacheParams =
                new ImageCache.ImageCacheParams(getSherlockActivity(), IMAGE_CACHE_DIR);

        mImageFetcher = new ImageFetcher(getSherlockActivity(), 100);
        mImageFetcher.setLoadingImage(R.drawable.default_plurk_avatar);
        mImageFetcher.addImageCache(getFragmentManager(), cacheParams);

        plurk_users = new HashMap<String, Plurk_Users>();
        plurks = new HashMap<String, List<Plurks>>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(D) { Log.d(TAG, "onCreateView"); }
        View v = inflater.inflate(R.layout.fragment_chatrooms, container, false);
        //list = (ExpandableListView) v.findViewById(R.id.elv_list);
        list = (FloatingGroupExpandableListView) v.findViewById(R.id.elv_list);
        //fixme
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
        bt_more = (Button) v.findViewById(R.id.bt_more);
        bt_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoaderManager manager = getLoaderManager();
                if(manager.getLoader(LOADER_ID_GET_PLURKS) == null) {
                    HashMap<String, String> params = new HashMap<String, String>();
                    params.put(OFFSET, oldest_posted);
                    getPlurks(params);
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putBoolean(HAS_PARAMS, true);
                    bundle.putString(OFFSET, oldest_posted);
                    manager.restartLoader(LOADER_ID_GET_PLURKS, bundle, getPlurks_Callback).forceLoad();
                }
                bt_more.setEnabled(false);
            }
        });
        list.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                if(D) { Log.d(TAG, "--->onChildClick"); }
                String idx = String.valueOf(mAdapter.getGroupId(groupPosition));

                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
//                ChattingRoomFragment chatting = ChattingRoomFragment.newInstance(String.valueOf(id));
                ChattingRoomFragment chatting = ChattingRoomFragment.newInstance(plurks.get(idx).get(childPosition), plurk_users.get(idx));
                ft.replace(R.id.fragment_content, chatting).addToBackStack("tag").commitAllowingStateLoss();
                getSherlockActivity().getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
                return true;
            }
        });

        if(mAdapter != null) {
            WrapperExpandableListAdapter wrapper = new WrapperExpandableListAdapter(mAdapter);
            list.setAdapter(wrapper);
            //list.setAdapter(mAdapter);
        }

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(D) { Log.d(TAG, "onActivityCreated"); }
        plurkOAuth = ((FunnyActivity) getActivity()).getPlurkOAuth();
        getPlurks(null);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(D) { Log.d(TAG, "onResume"); }
        mImageFetcher.setExitTasksEarly(false);
        if(mAdapter != null) {
//            list.setAdapter(mAdapter);
        } else {
//            getPlurks();
            getPlurks(null);
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
        super.onDestroy();
        if(D) { Log.d(TAG, "onDestroy"); }
        mImageFetcher.closeCache();
    }

    void getPlurks(HashMap<String, String> args) {
        HashMap<String, String> params = new HashMap<String, String>();
        params = args;
        if(this.plurks.size() == 0) {
            Bundle bundle = new Bundle();
            if(args != null) {
                bundle.putBoolean(HAS_PARAMS, true);
                bundle.putString(OFFSET, oldest_posted);
            } else {
                bundle.putBoolean(HAS_PARAMS, false);
            }
            getLoaderManager().initLoader(LOADER_ID_GET_PLURKS, bundle, getPlurks_Callback).forceLoad();
        } else {
//            setExpandableListAdapter();
        }
    }


    // ---- static inner class START ----
    static class Mod_Timeline_getPlurks_AsyncTaskLoader extends AsyncTaskLoader<JSONObject> {
        HashMap<String, String> params;

        public Mod_Timeline_getPlurks_AsyncTaskLoader(Context context, HashMap<String, String> params) {
            super(context);
            this.params = params;
        }

        @Override
        public JSONObject loadInBackground() {
            JSONObject result = null;

            String offset = null;
            int limit = 30;
            String filter = null;
            boolean favorers_detail = false;
            boolean limited_detail = false;
            boolean replurkers_detail = false;

            if(params.containsKey(OFFSET)) {
                offset = params.get(OFFSET);
            }
            if(params.containsKey(LIMIT)) {
                String tmp = params.get(LIMIT);
                limit = Integer.valueOf(tmp);
            }
            if(params.containsKey(FILTER)) {
                filter = params.get(FILTER);
            }
            if(params.containsKey(FAVORERS_DETAIL)) {
                String tmp = params.get(FAVORERS_DETAIL);
                if(tmp.equals("true")) { favorers_detail = true; }
            }
            if(params.containsKey(LIMITED_DETAIL)) {
                String tmp = params.get(LIMITED_DETAIL);
                if(tmp.equals("true")) { limited_detail = true; }
            }
            if(params.containsKey(REPLURKERS_DETAIL)) {
                String tmp = params.get(REPLURKERS_DETAIL);
                if(tmp.equals("true")) { replurkers_detail = true; }
            }

            try {
                result = plurkOAuth.getModule(Mod_Timeline.class).getPlurks(offset, limit, filter, favorers_detail, limited_detail, replurkers_detail);
                return result;
            } catch (RequestException e) {
                Log.e(TAG, e.getMessage());
                // e.printStackTrace();
            }
            return null;
        }

        @Override
        public void deliverResult(JSONObject data) {
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
        public void onCanceled(JSONObject data) {
            super.onCanceled(data);
        }

        @Override
        protected void onReset() {
            super.onReset();
            onStopLoading();
        }
    }
    // ---- static inner class END ----
}
