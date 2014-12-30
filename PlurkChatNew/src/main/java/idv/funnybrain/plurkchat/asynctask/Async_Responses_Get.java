package idv.funnybrain.plurkchat.asynctask;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import de.greenrobot.event.EventBus;
import idv.funnybrain.plurkchat.DataCentral;
import idv.funnybrain.plurkchat.RequestException;
import idv.funnybrain.plurkchat.data.Friend;
import idv.funnybrain.plurkchat.data.Responses;
import idv.funnybrain.plurkchat.eventbus.Event_Responses_Get;
import idv.funnybrain.plurkchat.modules.Mod_Responses;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

/**
 * Created by freeman on 2014/11/15.
 */
public class Async_Responses_Get extends AsyncTaskLoader<Void> {
    String original_post_id;

    public Async_Responses_Get(Context context, String input) {
        super(context);
        original_post_id = input;
    }

    @Override
    public Void loadInBackground() {
        JSONObject result = null;
        JsonFactory factory = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        try {
            result = DataCentral.getInstance(getContext()).getPlurkOAuth().getModule(Mod_Responses.class)
                     .get(original_post_id, 0);

            JSONArray obj_responses = result.getJSONArray("responses");
            JsonParser parser_responses = factory.createParser(obj_responses.toString());
            Responses[] responses = mapper.readValue(parser_responses, Responses[].class);

            JSONObject obj_friends = result.getJSONObject("friends");
//            JsonParser parser_friends = factory.createParser(obj_friends.toString());
//            Friend[] friends = mapper.readValue(parser_friends, Friend[].class);
            HashMap<String, Friend> friends = new HashMap<String, Friend>();
            Iterator<String> iterator = obj_friends.keys();
            while (iterator.hasNext()) {
                String idx = iterator.next();
                JsonParser parser_friend = factory.createParser(obj_friends.getJSONObject(idx).toString());
                Friend obj_friend = mapper.readValue(parser_friend, Friend.class);
                friends.put(idx, obj_friend);
            }

            EventBus.getDefault().post(new Event_Responses_Get(Arrays.asList(responses), friends));
        } catch (RequestException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //return result;
        return null;
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
