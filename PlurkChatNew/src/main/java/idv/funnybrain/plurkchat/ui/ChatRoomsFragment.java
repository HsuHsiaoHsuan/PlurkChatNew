package idv.funnybrain.plurkchat.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ExpandableListView;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import idv.funnybrain.plurkchat.FunnyActivity;
import idv.funnybrain.plurkchat.PlurkOAuth;
import idv.funnybrain.plurkchat.R;
import idv.funnybrain.plurkchat.RequestException;
import idv.funnybrain.plurkchat.data.Me;
import idv.funnybrain.plurkchat.data.Plurk_Users;
import idv.funnybrain.plurkchat.data.Plurks;
import idv.funnybrain.plurkchat.modules.Mod_Timeline;
import idv.funnybrain.plurkchat.utils.ImageCache;
import idv.funnybrain.plurkchat.utils.ImageFetcher;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Freeman on 2014/4/3.
 */
public class ChatRoomsFragment extends SherlockFragment {
    // ---- constant variable START ----
    private static final boolean D = true;
    private static final String TAG = "FriendsFragment";

    private static final String IMAGE_CACHE_DIR = "thumbnails";

    protected boolean mPause = false;
    private final Object mPauseLock = new Object();

    private final String OFFSET = "offset";
    private final String LIMIT = "limit";
    private final String FILTER = "filter";
    private final String FAVORERS_DETAIL = "favorers_detail";
    private final String LIMITED_DETAIL = "limited_detail";
    private final String REPLURKERS_DETAIL = "replurkers_detail";
    // ---- constant variable END ----

    // ---- local variable START ----
    private PlurkOAuth plurkOAuth;
    private Me me;
    private ExpandableListView list;
    private Button bt_more;
    private ChatRoomExpandableListAdapter mAdapter;

    private ImageFetcher mImageFetcher;

    HashMap<String, Plurk_Users> plurk_users;
    HashMap<String, List<Plurks>> plurks;
    String oldest_posted_readable = "";
    String oldest_posted = "null";
    // ---- local variable END ----

    ChatRoomsFragment newInstance() {
        if(D) { Log.d(TAG, "newInstance"); }
        ChatRoomsFragment chatRoomsFragment = new ChatRoomsFragment();
        return chatRoomsFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        View v = inflater.inflate(R.layout.fragment_chatrooms, container, false);
        // View v = inflater.inflate(R.layout.fragment_friends, container, false);
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
        bt_more = (Button) v.findViewById(R.id.bt_more);
        bt_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put(OFFSET, oldest_posted);
                new Mod_Timeline_getPlurks_AsyncTask().execute(params);
                bt_more.setEnabled(false);
            }
        });
        list.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
//                ActionBar.Tab tab= getSherlockActivity().getSupportActionBar().newTab().setText(String.valueOf(id))
//                        .setTabListener(new TabListener<FriendsFragment>(
//                                getSherlockActivity(), "test"+id, FriendsFragment.class
//                        ));
//                getSherlockActivity().getSupportActionBar().addTab(tab);
                System.out.println(id);
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ChattingRoomFragment chatting = ChattingRoomFragment.newInstance(String.valueOf(id));
                ft.replace(R.id.fragment_content, chatting).addToBackStack("tag").commit();
//                FriendsFragment ff = new FriendsFragment();
//                ft.add(R.id.fragment_content, ff);
//                ft.commit();
                getSherlockActivity().getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
                return true;
            }
        });

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(D) { Log.d(TAG, "onActivityCreated"); }
        plurkOAuth = ((FunnyActivity) getActivity()).getPlurkOAuth();

        if(this.plurks.size() == 0) {
            HashMap<String, String> params = new HashMap<String, String>();
            new Mod_Timeline_getPlurks_AsyncTask().execute(params);
        } else {
            setExpandableListAdapter();
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

    private class Mod_Timeline_getPlurks_AsyncTask extends AsyncTask<HashMap<String, String>, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(HashMap<String, String>... params) {
            JSONObject result = null;

            String offset = null;
            int limit = 30;
            String filter = null;
            boolean favorers_detail = false;
            boolean limited_detail = false;
            boolean replurkers_detail = false;

            HashMap<String, String> param = params[0];
            if(param.containsKey(OFFSET)) {
                offset = param.get(OFFSET);
            }
            if(param.containsKey(LIMIT)) {
                String tmp = param.get(LIMIT);
                limit = Integer.valueOf(tmp);
            }
            if(param.containsKey(FILTER)) {
                filter = param.get(FILTER);
            }
            if(param.containsKey(FAVORERS_DETAIL)) {
                String tmp = param.get(FAVORERS_DETAIL);
                if(tmp.equals("true")) { favorers_detail = true; }
            }
            if(param.containsKey(LIMITED_DETAIL)) {
                String tmp = param.get(LIMITED_DETAIL);
                if(tmp.equals("true")) { limited_detail = true; }
            }
            if(param.containsKey(REPLURKERS_DETAIL)) {
                String tmp = param.get(REPLURKERS_DETAIL);
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
        protected void onPostExecute(JSONObject object) {
            super.onPostExecute(object);
            List<Plurks> plurks = new ArrayList<Plurks>();

            try {
                JSONObject obj_plurk_users = object.getJSONObject("plurk_users");
                Iterator<String> iterator = obj_plurk_users.keys();
                while (iterator.hasNext()) {
                    String idx = iterator.next();
                    ChatRoomsFragment.this.plurk_users.put(idx, new Plurk_Users(obj_plurk_users.getJSONObject(idx)));
                    if(D) { Log.d(TAG, "length: " + ChatRoomsFragment.this.plurk_users.keySet()); }
                }

                JSONArray obj_plurks = object.getJSONArray("plurks");
                for (int x = 0; x < obj_plurks.length(); x++) {
                    Plurks post = new Plurks(obj_plurks.getJSONObject(x)); // get the post
                    String own_id = post.getOwner_id(); // get the owner of the post

                    if(ChatRoomsFragment.this.plurks.containsKey(own_id)) {
                        List<Plurks> test = ChatRoomsFragment.this.plurks.get(own_id);
                        if(!test.contains(post)) {
                            ChatRoomsFragment.this.plurks.get(own_id).add(post);
                        }
                    } else {
                        List<Plurks> plurk_list = new ArrayList<Plurks>();
                        plurk_list.add(post);
                        ChatRoomsFragment.this.plurks.put(own_id, plurk_list);
                    }
                    //plurks.add(new Plurks(obj_plurks.getJSONObject(x)));
                }

                Plurks oldest_plurk = new Plurks(obj_plurks.getJSONObject(obj_plurks.length()-1));
                oldest_posted_readable = oldest_plurk.getReadablePostedDate();
                oldest_posted = oldest_plurk.getQueryFormatedPostedDate();

                if(D) { Log.d(TAG, "plurks: " + obj_plurks.length()); }
            } catch(JSONException jsone) {
                Log.e(TAG, jsone.getMessage());
            }
            setExpandableListAdapter();
        }
    }

    private void setExpandableListAdapter() {
        List<Plurk_Users> group_list = new ArrayList<Plurk_Users>(ChatRoomsFragment.this.plurk_users.values());
        List<List<Plurks>> child_list = new ArrayList<List<Plurks>>();
        for(int x=0; x<group_list.size(); x++) {
            String userId = group_list.get(x).getId();
            if(ChatRoomsFragment.this.plurks.containsKey(userId)) {
                child_list.add(ChatRoomsFragment.this.plurks.get(userId));
            } else
                child_list.add(new ArrayList<Plurks>());
        }

        for(int x=0; x<child_list.size(); x++) {
            if (child_list.get(x).size() == 0) {
                group_list.remove(x);
                child_list.remove(x);
            }
        }

        mAdapter = new ChatRoomExpandableListAdapter(
                getSherlockActivity(),
                group_list,
                child_list,
                mImageFetcher);
        list.setAdapter(mAdapter);

        bt_more.setVisibility(View.VISIBLE);
        bt_more.setText(getResources().getText(R.string.oldest_post) + ":\n" + oldest_posted_readable);
        bt_more.setEnabled(true);
    }

}