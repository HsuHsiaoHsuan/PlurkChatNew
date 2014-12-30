package idv.funnybrain.plurkchat.asynctask;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import de.greenrobot.event.EventBus;
import idv.funnybrain.plurkchat.DataCentral;
import idv.funnybrain.plurkchat.RequestException;
import idv.funnybrain.plurkchat.data.Friend;
import idv.funnybrain.plurkchat.data.IHuman;
import idv.funnybrain.plurkchat.eventbus.Event_FriendsFans_GetFollowingByOffset;
import idv.funnybrain.plurkchat.modules.Mod_FriendsFans;
import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by freeman on 2014/7/28.
 */
public class Async_FriendsFans_getFollowingByOffset extends AsyncTaskLoader<Void> {
    private static final boolean D = false;
    private static final String TAG = "Async_FriendsFans_getFollowingByOffset";

    public Async_FriendsFans_getFollowingByOffset(Context context) {
        super(context);
    }

    @Override
    public Void loadInBackground() {
        JSONArray result = null;
        int result_size = 0;
        List<IHuman> following = new ArrayList<IHuman>();
        int round = 0;

        JsonFactory factory = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        try {
            do {
                if(D) { Log.d(TAG, "Mod_FriendsFans_getFollowingByOffset_AsyncTaskLoader, while: " + round); }
                result = DataCentral.getInstance(getContext()).getPlurkOAuth().getModule(Mod_FriendsFans.class).getFollowingByOffset(0 + 100 * round, 100);
                JsonParser parser = factory.createParser(result.toString());
                Friend[] tmp = mapper.readValue(parser, Friend[].class);
                following.addAll(Arrays.asList(tmp));
                round++;
            } while (result_size > 0);
            if (D) {
                Log.d(TAG, result.toString());
            }
        } catch (RequestException e) {
            Log.e(TAG, e.getMessage());
        } catch (JsonMappingException jme) {
            Log.e(TAG, jme.getMessage());
        } catch (JsonParseException jpe) {
            Log.e(TAG, jpe.getMessage());
        } catch (IOException ioe) {
            Log.e(TAG, ioe.getMessage());
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

        EventBus.getDefault().post(new Event_FriendsFans_GetFollowingByOffset(following));

        return null;
        //return following;
    }

    @Override
    public void deliverResult(Void data) {
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
    public void onCanceled(Void data) {
        super.onCanceled(data);
    }

    @Override
    protected void onReset() {
        super.onReset();
        onStopLoading();
    }
}
