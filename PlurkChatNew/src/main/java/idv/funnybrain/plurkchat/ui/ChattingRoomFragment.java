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
import idv.funnybrain.plurkchat.data.Me;
import idv.funnybrain.plurkchat.data.Plurks;
import idv.funnybrain.plurkchat.modules.Mod_Responses;
import idv.funnybrain.plurkchat.utils.ImageCache;
import idv.funnybrain.plurkchat.utils.ImageFetcher;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Freeman on 2014/4/11.
 */
public class ChattingRoomFragment extends SherlockFragment {
    // ---- constant variable START ----
    private static final boolean D = true;
    private static final String TAG = "ChattingRoomFragment";

    private static final String IMAGE_CACHE_DIR = "thumbnails";

    protected boolean mPause = false;
    private final Object mPauseLock = new Object();

    private final String PLURK_ID = "plurk_id";
    private final String FROM_RESPONSE = "from_response";
    // ---- constant variable END ----

    // ---- local variable START ----
    private PlurkOAuth plurkOAuth;
    private String chatting_plurk_id;
    private Me me;
    private ListView list;
    private ChattingRoomListAdapter mAdapter;

    private ImageFetcher mImageFetcher;

    private List<Friend> friends;
    private List<Plurks> responses;

    // ---- local variable END ----

    public static ChattingRoomFragment newInstance(String plurk_id) {
        if(D) { Log.d(TAG, "newInstance"); }
        ChattingRoomFragment chatting = new ChattingRoomFragment();
        Bundle bundle = chatting.getArguments();
        if(bundle == null) {
            bundle = new Bundle();
        }
        bundle.putString("id", plurk_id);
        chatting.setArguments(bundle);

        return chatting;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if(bundle != null) {
            chatting_plurk_id = bundle.getString("id");
        } else {
            chatting_plurk_id = "";
        }

        ImageCache.ImageCacheParams cacheParams =
                new ImageCache.ImageCacheParams(getSherlockActivity(), IMAGE_CACHE_DIR);

        mImageFetcher = new ImageFetcher(getSherlockActivity(), 100);
        mImageFetcher.setLoadingImage(R.drawable.default_plurk_avatar);
        mImageFetcher.addImageCache(getFragmentManager(), cacheParams);

        friends = new ArrayList<Friend>();
        responses = new ArrayList<Plurks>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chattingroom, container, false);
        list = (ListView) v.findViewById(R.id.chatting_list);
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
        if(D) { Log.d(TAG, "onActivityCreated"); }
        plurkOAuth = ((FunnyActivity) getActivity()).getPlurkOAuth();

        if(this.responses.size() == 0) {
            HashMap<String, String> params = new HashMap<String, String>();
            params.put(PLURK_ID, chatting_plurk_id);
            new Mod_Response_get_AsyncTask().execute(params);
        } else {
            //setListAdapter();
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

    private class Mod_Response_get_AsyncTask extends AsyncTask<HashMap<String, String>, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(HashMap<String, String>... params) {
            JSONObject result = null;

            String plurk_id = "";
            int from_response = 0;

            HashMap<String, String> param = params[0];
            if(param.containsKey(PLURK_ID)) {
                plurk_id = param.get(PLURK_ID);
            } else {
                return null;
            }
            if(param.containsKey(FROM_RESPONSE)) {
                from_response = Integer.valueOf(param.get(FROM_RESPONSE));
            }

            try {
                result = plurkOAuth.getModule(Mod_Responses.class).get(plurk_id, from_response);
            } catch (RequestException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(JSONObject object) {
            super.onPostExecute(object);

            System.out.println(object);
        }
    }
}
