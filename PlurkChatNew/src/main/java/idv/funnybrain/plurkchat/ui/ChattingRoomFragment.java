package idv.funnybrain.plurkchat.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.AsyncTaskLoader;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.android.volley.Network;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
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
public class ChattingRoomFragment extends Fragment {
    // ---- constant variable START ----
    private static final boolean D = true;
    private static final String TAG = ChattingRoomFragment.class.getSimpleName();

//    protected boolean mPause = false;
//    private final Object mPauseLock = new Object();

//    private final String PLURK_ID = "plurk_id";
//    private final String FROM_RESPONSE = "from_response";
    // ---- constant variable END ----

    // ---- local variable START ----
    private static PlurkOAuth plurkOAuth;
    private String chatting_plurk_id;
    private ListView list;
    private Plurks original_post;
    private Plurk_Users original_poster;

    private DataCentral mData;
    private ImageLoader mImageLoader;

    private ChattingRoomListAdapter mAdapter = null;
    private HashMap<String, Friend> friends;
    private List<Responses> responses;

    private List<AsyncTaskLoader> runningTask;
    // ---- local variable END --

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

        mData = DataCentral.getInstance(getActivity());
        mImageLoader = mData.getImageLoader();

        friends = new HashMap<String, Friend>();
        responses = new ArrayList<Responses>();

        runningTask = new ArrayList<AsyncTaskLoader>();

        Bundle bundle = getArguments();
        if(bundle != null) {
            original_post = bundle.getParcelable("post");
            original_poster = bundle.getParcelable("poster");
            chatting_plurk_id = ((Plurks)bundle.getParcelable("post")).getPlurk_id();
            if(D) { Log.d(TAG, "-->onCreate, " + ((Plurks)bundle.getParcelable("post")).getContent()); }
        } else {
            chatting_plurk_id = "";
        }

//        ImageCache.ImageCacheParams cacheParams =
//                new ImageCache.ImageCacheParams(getSherlockActivity(), DataCentral.IMAGE_CACHE_DIR);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chattingroom, container, false);

        NetworkImageView iv_original_poseter = (NetworkImageView) v.findViewById(R.id.iv_original_poseter);
        TextView tv_original_content = (TextView) v.findViewById(R.id.tv_original_content);

        iv_original_poseter.setImageUrl(original_poster.getHumanImage(), mImageLoader);
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

        if(mAdapter != null) {
            list.setAdapter(mAdapter);
        }
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(D) { Log.d(TAG, "onActivityCreated"); }
        plurkOAuth = DataCentral.getInstance(getActivity()).getPlurkOAuth();
        getResponses();
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        if(D) { Log.d(TAG, "onPause"); }
        for (AsyncTaskLoader loader : runningTask) {
            loader.cancelLoad();
        }
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(D) { Log.d(TAG, "onDestroy"); }
        getSherlockActivity().getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
    }

    void getResponses() {
        AsyncTaskLoader tmp = new Async_Responses_Get(getActivity(), chatting_plurk_id);
        tmp.forceLoad();
        runningTask.add(tmp);
    }

    void sendResponse(String response, Qualifier qualifier) {
        AsyncTaskLoader tmp = new Async_Responses_ResponseAdd(getActivity(), chatting_plurk_id, response, null);
        tmp.forceLoad();
        runningTask.add(tmp);
    }

    public void onEventMainThread(Event_Responses_Get event) {
        friends.clear();
        responses.clear();
        friends.putAll(event.getFriendsData());
        responses.addAll(event.getResponsesData());
        if (mAdapter == null) {
            mAdapter = new ChattingRoomListAdapter(getActivity().getLayoutInflater(), friends, responses);
            list.setAdapter(mAdapter);
        } else {
            //mAdapter.notifyDataSetChanged();
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
