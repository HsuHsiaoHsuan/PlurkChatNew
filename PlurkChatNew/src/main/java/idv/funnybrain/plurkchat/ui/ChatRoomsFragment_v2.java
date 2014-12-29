package idv.funnybrain.plurkchat.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockFragment;
// import com.diegocarloslima.fgelv.lib.FloatingGroupExpandableListView;
// import com.diegocarloslima.fgelv.lib.WrapperExpandableListAdapter;
import de.greenrobot.event.EventBus;
import idv.funnybrain.plurkchat.DataCentral;
import idv.funnybrain.plurkchat.PlurkOAuth;
import idv.funnybrain.plurkchat.R;
import idv.funnybrain.plurkchat.asynctask.Async_Timeline_GetPlurks;
import idv.funnybrain.plurkchat.data.Plurk_Users;
import idv.funnybrain.plurkchat.data.Plurks;
import idv.funnybrain.plurkchat.eventbus.Event_Timeline_GetPlurks;
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

    public static final String OFFSET = "offset";
    public static final String LIMIT = "limit";
    public static final String FILTER = "filter";
    public static final String FAVORERS_DETAIL = "favorers_detail";
    public static final String LIMITED_DETAIL = "limited_detail";
    public static final String REPLURKERS_DETAIL = "replurkers_detail";
    // ---- constant variable END ----

    // ---- local variable START ----
    private static PlurkOAuth plurkOAuth;
    private ExpandableListView list;
    private Button bt_more;
    private BaseExpandableListAdapter mAdapter;
    private HashMap<String, Plurk_Users> plurk_users;

    private HashMap<String, List<Plurks>> plurks;
    private String oldest_posted_readable = "";
    private String oldest_posted = "null";
    // ---- local variable END ----

    public static ChatRoomsFragment_v2 newInstance() {
        if (D) { Log.d(TAG, "newInstance"); }
        ChatRoomsFragment_v2 chatRoomsFragment = new ChatRoomsFragment_v2();
        return chatRoomsFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (D) { Log.d(TAG, "onCreate"); }
        plurk_users = new HashMap<String, Plurk_Users>();
        plurks = new HashMap<String, List<Plurks>>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (D) { Log.d(TAG, "onCreateView"); }
        View v = inflater.inflate(R.layout.fragment_chatrooms, container, false);
        list = (ExpandableListView) v.findViewById(R.id.elv_list);
        bt_more = (Button) v.findViewById(R.id.bt_more);
        bt_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put(HAS_PARAMS, "true");
                params.put(OFFSET, oldest_posted);
                new Async_Timeline_GetPlurks(getSherlockActivity(), params).forceLoad();
            }
        });
        list.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                if (D) { Log.d(TAG, "--->onChildClick"); }
                String idx = String.valueOf(mAdapter.getGroupId(groupPosition));

                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ChattingRoomFragment chatting = ChattingRoomFragment.newInstance(plurks.get(idx).get(childPosition), plurk_users.get(idx));
                ft.replace(R.id.fragment_content, chatting).addToBackStack("tag").commitAllowingStateLoss();
                getSherlockActivity().getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
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
        if (D) { Log.d(TAG, "onActivityCreated"); }
        plurkOAuth = DataCentral.getInstance(getSherlockActivity()).getPlurkOAuth();
        getPlurks(null);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (D) { Log.d(TAG, "onResume"); }
        EventBus.getDefault().register(this);

        SharedPreferences settings = getSherlockActivity().getPreferences(SherlockActivity.MODE_PRIVATE);
        oldest_posted_readable = settings.getString("oldest_posted_readable", "");
        oldest_posted = settings.getString("oldest_posted", "null");

        if(mAdapter != null) {
            bt_more.setText(getString(R.string.oldest_post) + "\n" + oldest_posted_readable);
        } else {
            getPlurks(null);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (D) { Log.d(TAG, "onPause"); }
        EventBus.getDefault().unregister(this);

        SharedPreferences settings = getSherlockActivity().getPreferences(SherlockActivity.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("oldest_posted_readable", oldest_posted_readable);
        editor.putString("oldest_posted", oldest_posted);
        editor.commit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (D) { Log.d(TAG, "onDestroy"); }
    }

    void getPlurks(HashMap<String, String> args) {
        if(this.plurks.size() == 0) {
            HashMap<String, String> params = new HashMap<String, String>();
            if(args != null) {
                params = args;
            } else {
                params.put(HAS_PARAMS, "false");
            }
            new Async_Timeline_GetPlurks(getSherlockActivity(), params).forceLoad();
        } else {
        }
    }


    public void onEventMainThread(Event_Timeline_GetPlurks event) {
        JSONObject data = event.getData();

        Date post_date = new Date();

        try {
            JSONObject obj_plurk_users = data.getJSONObject("plurk_users");
            Iterator<String> iterator = obj_plurk_users.keys();
            while (iterator.hasNext()) {
                String idx = iterator.next();
                plurk_users.put(idx, new Plurk_Users(obj_plurk_users.getJSONObject(idx)));
                if (D) { Log.d(TAG, "plurk_users length: " + plurk_users.keySet()); }
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

                if(post.getDate().before(post_date)) {
                    oldest_posted_readable = post.getReadablePostedDate();
                    oldest_posted = post.getQueryFormatedPostedDate();
                    post_date = post.getDate();
                }
            }

            // FIXME
            // Plurks oldest_plurk = new Plurks(obj_plurks.getJSONObject(obj_plurks_size-1));
            // oldest_posted_readable = oldest_plurk.getReadablePostedDate();
            // oldest_posted = oldest_plurk.getQueryFormatedPostedDate();

            if (D) { Log.d(TAG, "plurks: " + obj_plurks_size); }
        } catch(JSONException jsone) {
            Log.e(TAG, jsone.getMessage());
        }

        if (D) {
            Iterator<String> iter = plurks.keySet().iterator();
            while (iter.hasNext()) {
                String key = iter.next();
                List list = plurks.get(key);
                if (D) Log.d(TAG, "who?" + key + " has list size: " + list.size());
            }
        }
        if(mAdapter == null) {
            mAdapter = new ChatRoomExpandableListAdapter_v2(getSherlockActivity().getLayoutInflater(), plurk_users, plurks);
            list.setAdapter(mAdapter);
        } else {
            ((ChatRoomExpandableListAdapter_v2) mAdapter).addNewData();
        }
        bt_more.setVisibility(View.VISIBLE);
        bt_more.setEnabled(true);
        bt_more.setText(getString(R.string.oldest_post) + "\n" + oldest_posted_readable);
    }
}