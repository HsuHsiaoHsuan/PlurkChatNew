package idv.funnybrain.plurkchat.ui;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import idv.funnybrain.plurkchat.FunnyActivity;
import idv.funnybrain.plurkchat.PlurkOAuth;
import idv.funnybrain.plurkchat.R;
import idv.funnybrain.plurkchat.RequestException;
import idv.funnybrain.plurkchat.data.*;
import idv.funnybrain.plurkchat.modules.Mod_Responses;
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
    private static PlurkOAuth plurkOAuth;
    private String chatting_plurk_id;
    private Me me;
    private ListView list;
    private ChattingRoomListAdapter mAdapter;
    private Plurks original_post;
    private Plurk_Users original_poster;

    private ImageFetcher mImageFetcher;

    static HashMap<String, Friend> friends;
    static List<Responses> responses;

    // ---- local variable END ----

//    public static ChattingRoomFragment newInstance(String plurk_id) {
//        if(D) { Log.d(TAG, "newInstance"); }
//        ChattingRoomFragment chatting = new ChattingRoomFragment();
//        Bundle bundle = chatting.getArguments();
//        if(bundle == null) {
//            bundle = new Bundle();
//        }
//        bundle.putString("id", plurk_id);
//        chatting.setArguments(bundle);
//
//        return chatting;
//    }

    public static ChattingRoomFragment newInstance(Plurks plurk, Plurk_Users users) {
        if(D) { Log.d(TAG, "-->newInstance_v2, " + plurk.getContent()); }
        ChattingRoomFragment chatting = new ChattingRoomFragment();
        Bundle bundle = chatting.getArguments();
        if(bundle == null) {
            bundle = new Bundle();
        }
        bundle.putParcelable("post", plurk);
        bundle.putParcelable("poster", users);
        chatting.setArguments(bundle);

        return chatting;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if(bundle != null) {
//            chatting_plurk_id = bundle.getString("id");
            original_post = (Plurks) bundle.getParcelable("post");
            original_poster = (Plurk_Users) bundle.getParcelable("poster");
            chatting_plurk_id = ((Plurks)bundle.getParcelable("post")).getPlurk_id();
            if(D) { Log.d(TAG, "-->onCreate, " + ((Plurks)bundle.getParcelable("post")).getContent()); }
        } else {
            chatting_plurk_id = "";
        }

        ImageCache.ImageCacheParams cacheParams =
                new ImageCache.ImageCacheParams(getSherlockActivity(), IMAGE_CACHE_DIR);

        mImageFetcher = new ImageFetcher(getSherlockActivity(), 100);
        mImageFetcher.setLoadingImage(R.drawable.default_plurk_avatar);
        mImageFetcher.addImageCache(getFragmentManager(), cacheParams);

        friends = new HashMap<String, Friend>();
        responses = new ArrayList<Responses>();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chattingroom, container, false);

        ImageView iv_original_poseter = (ImageView) v.findViewById(R.id.iv_original_poseter);
        TextView tv_original_content = (TextView) v.findViewById(R.id.tv_original_content);

        mImageFetcher.loadImage(original_poster.getHumanImage(), iv_original_poseter);
        tv_original_content.setText(Html.fromHtml(original_post.getContent()));

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
        me = ((FunnyActivity) getActivity()).getMe();

        getResponses();
//        if(this.responses.size() == 0) {
//            HashMap<String, String> params = new HashMap<String, String>();
//            params.put(PLURK_ID, chatting_plurk_id);
//            new Mod_Response_get_AsyncTask().execute(params);
//        } else {
//            setListAdapter();
//        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mImageFetcher.setExitTasksEarly(false);
        if(mAdapter == null) {
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
        getSherlockActivity().getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
    }

    void getResponses() {
        getLoaderManager().initLoader(0, null, new LoaderManager.LoaderCallbacks<JSONObject>() {
            @Override
            public Loader<JSONObject> onCreateLoader(int id, Bundle args) {
                return new Mod_Response_get_AsyncTaskLoader(getSherlockActivity(), chatting_plurk_id);
            }

            @Override
            public void onLoadFinished(Loader<JSONObject> loader, JSONObject data) {
                try {
                    JSONObject obj_friends = data.getJSONObject("friends");
                    Iterator<String> iterator = obj_friends.keys();
                    while (iterator.hasNext()) {
                        String idx = iterator.next();
                        friends.put(idx, new Friend(obj_friends.getJSONObject(idx)));
                        if (D) { Log.d(TAG, "friends length: " + friends.size()); }
                    }

                    JSONArray array_responses = data.getJSONArray("responses");
                    int size = array_responses.length();
                    for (int x = 0; x < size; x++) {
                        Responses response = new Responses(array_responses.getJSONObject(x));
                        responses.add(response);
                        if(D) { Log.d(TAG, "responses: " + response.getContent()); }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (mAdapter == null) {
                    mAdapter = new ChattingRoomListAdapter(getSherlockActivity().getLayoutInflater(), friends, responses, mImageFetcher, me.getHumanId());
                } else {
                    // mAdapter.addNewData();
                }
            }

            @Override
            public void onLoaderReset(Loader<JSONObject> loader) {

            }
        }).forceLoad();
    }

    static class Mod_Response_get_AsyncTaskLoader extends AsyncTaskLoader<JSONObject> {
        String original_post_id;

        public Mod_Response_get_AsyncTaskLoader(Context context, String input) {
            super(context);
            original_post_id = input;
        }

        @Override
        public JSONObject loadInBackground() {
            JSONObject result = null;
            try {
                result = plurkOAuth.getModule(Mod_Responses.class).get(original_post_id, 0);
            } catch (RequestException e) {
                e.printStackTrace();
            }
            return result;
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

            try {
                JSONObject obj_friends = object.getJSONObject("friends");
                Iterator<String> iterator = obj_friends.keys();
                while(iterator.hasNext()) {
                    String idx = iterator.next();
                    ChattingRoomFragment.this.friends.put(idx, new Friend(obj_friends.getJSONObject(idx)));
                    if(D) { Log.d(TAG, "length: " + ChattingRoomFragment.this.friends.size()); }
                }

                JSONArray array_responses = object.getJSONArray("responses");
                int size = array_responses.length();
                for(int x=0; x<size; x++) {
                    Responses response = new Responses(array_responses.getJSONObject(x));
                    ChattingRoomFragment.this.responses.add(response);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            setListAdapter();
            //System.out.println(object);
        }
    }

    private void setListAdapter() {
        mAdapter = new ChattingRoomListAdapter(getSherlockActivity().getLayoutInflater(), friends, responses, mImageFetcher, me.getHumanId());
        list.setAdapter(mAdapter);
    }
}
