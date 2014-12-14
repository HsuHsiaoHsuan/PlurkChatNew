package idv.funnybrain.plurkchat.ui;

import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import de.greenrobot.event.EventBus;
import idv.funnybrain.plurkchat.*;
import idv.funnybrain.plurkchat.asynctask.Async_Responses_Get;
import idv.funnybrain.plurkchat.asynctask.Async_Responses_ResponseAdd;
import idv.funnybrain.plurkchat.data.*;
import idv.funnybrain.plurkchat.eventbus.Event_Error;
import idv.funnybrain.plurkchat.eventbus.Event_Responses_Get;
import idv.funnybrain.plurkchat.eventbus.Event_Responses_ResponseAdd;
import idv.funnybrain.plurkchat.utils.ImageCache;
import idv.funnybrain.plurkchat.utils.ImageFetcher;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

/**
 * Created by Freeman on 2014/4/11.
 */
public class ChattingRoomFragment extends SherlockFragment {
    // ---- constant variable START ----
    private static final boolean D = true;
    private static final String TAG = "ChattingRoomFragment";

    protected boolean mPause = false;
    private final Object mPauseLock = new Object();

    private final String PLURK_ID = "plurk_id";
    private final String FROM_RESPONSE = "from_response";
    // ---- constant variable END ----

    // ---- local variable START ----
    private static PlurkOAuth plurkOAuth;
    private String chatting_plurk_id;
    // private Me me;
    private ListView list;
    //private ChattingRoomListAdapter mAdapter;ChattingRoomFragment
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
                new ImageCache.ImageCacheParams(getSherlockActivity(), DataCentral.IMAGE_CACHE_DIR);

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
        tv_original_content.setMovementMethod(LinkMovementMethod.getInstance());

        EditText et_newMsg = (EditText) v.findViewById(R.id.chatting_input);
        final String newMsg = et_newMsg.getText().toString();

        Button bt_send = (Button) v.findViewById(R.id.chatting_send);
        bt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendResponse(newMsg, Qualifier.SHARES);
            }
        });

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
        plurkOAuth = DataCentral.getInstance(getSherlockActivity()).getPlurkOAuth();
        getResponses();
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
        mImageFetcher.setExitTasksEarly(false);
//        if(mAdapter == null) {
//        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(D) { Log.d(TAG, "onPause"); }
        EventBus.getDefault().unregister(this);
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
        new Async_Responses_Get(getSherlockActivity(), chatting_plurk_id).forceLoad();
    }

    void sendResponse(String response, Qualifier qualifier) {
        new Async_Responses_ResponseAdd(getSherlockActivity(), chatting_plurk_id, response, null).forceLoad();
    }

//    private void setListAdapter() {
//        mAdapter = new ChattingRoomListAdapter(getSherlockActivity().getLayoutInflater(), friends, responses, mImageFetcher);
//        list.setAdapter(mAdapter);
//    }

    public void onEventMainThread(Event_Responses_Get event) {
        ChattingRoomListAdapter mAdapter = null;

        JSONObject data = event.getData();

        JsonFactory factory = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        try {
            JSONObject obj_friends = data.getJSONObject("friends");
            Iterator<String> iterator = obj_friends.keys();
            while (iterator.hasNext()) {
                String idx = iterator.next();
                // FIXME
                //friends.put(idx, new Friend( obj_friends.getJSONObject(idx)));
                JsonParser parser = factory.createParser(obj_friends.getJSONObject(idx).toString());
                Friend tmp = mapper.readValue(parser, Friend.class);
                friends.put(idx, tmp);

                if (D) { Log.d(TAG, "friends length: " + friends.size()); }
            }

            JSONArray array_responses = data.getJSONArray("responses");
            int size = array_responses.length();
            for (int x = 0; x < size; x++) {
                Responses response = new Responses(array_responses.getJSONObject(x));
                responses.add(response);
                if(D) { Log.d(TAG, "responses: " + response.getContent()); }
            }

            // TODO handle error
            JSONObject obj_error = data.getJSONObject("error_text");
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (mAdapter == null) {
            mAdapter = new ChattingRoomListAdapter(getSherlockActivity().getLayoutInflater(), friends, responses, mImageFetcher);
            list.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    public void onEventMainThread(Event_Responses_ResponseAdd event) {
        JSONObject data = event.getData();

        try {
            JSONObject obj_error = data.getJSONObject("error_text");
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            getResponses();
        }
    }

    public void onEventMainThread(Event_Error event) {
        Log.d("freeman", "ChattingRoom, " + event.getData());
    }
}
